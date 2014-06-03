package eu.spyropoulos.wikiapp.dao;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.spyropoulos.wikiapp.model.Word;
import org.apache.commons.lang3.time.DateUtils;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.Requests;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static eu.spyropoulos.wikiapp.config.ESConfig.INDEX_NAME;
import static eu.spyropoulos.wikiapp.config.ESConfig.WORD_TYPE;

@Repository
public class WordDaoImpl implements WordDao {
  private final static Long ttl = 30 * DateUtils.MILLIS_PER_DAY;
  private final Logger log = LoggerFactory.getLogger(WordDao.class);
  @Autowired
  private SessionFactory sessionFactory;
  @Autowired
  private Client client;
  private ObjectMapper mapper = new ObjectMapper();


  @Override
  public Word addWord(Word word) {
    Serializable id = sessionFactory.getCurrentSession().save(word);
    word.setId((long) id);
    IndexRequest request;
    try {
      byte[] s = mapper.writeValueAsBytes(word);
      request = Requests.indexRequest(INDEX_NAME).type(WORD_TYPE).refresh(true).source(s).ttl(ttl);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
    client.index(request).actionGet();
    return (Word) sessionFactory.getCurrentSession().load(Word.class, id);
  }

  @Override
  public Word updateWord(Word word) {
    Long id = word.getId();
    sessionFactory.getCurrentSession().update(word);
    return getWordById(id);
  }

  @Override
  @SuppressWarnings("unchecked")
  public List<Word> listWords() {
    String queryString = "select w from Word w";
    Query query = sessionFactory.getCurrentSession().createQuery(queryString);
    return query.list();
  }

  @Override
  public Word getWordById(Long wordId) {
    return (Word) sessionFactory.getCurrentSession().get(Word.class, wordId);
  }

  @Override
  public boolean removeWord(Long id) {
    Word word = (Word) sessionFactory.getCurrentSession().load(Word.class, id);
    if (null != word) {
      sessionFactory.getCurrentSession().delete(word);
      return true;
    }
    return false;
  }

  @Override
  public List<Word> search(String wordName) {
    QueryBuilder qb = QueryBuilders.termQuery("name", wordName);
    List<Word> result = new ArrayList<>();
    SearchResponse scrollResp = client.prepareSearch(INDEX_NAME)
        .setTypes(WORD_TYPE)
        .setSearchType(SearchType.SCAN)
        .setScroll(new TimeValue(60000))
        .setQuery(qb)
        .setSize(100).execute().actionGet();
    while (true) {
      scrollResp = client.prepareSearchScroll(scrollResp.getScrollId())
          .setScroll(new TimeValue(600000)).execute().actionGet();
      for (SearchHit hit : scrollResp.getHits()) {
        try {
          Word word = mapper.readValue(hit.getSourceAsString(), Word.class);
          result.add(word);
        } catch (IOException e) {
          throw new RuntimeException(e);
        }
      }
      //Break condition: No hits are returned
      if (scrollResp.getHits().getHits().length == 0) {
        break;
      }
    }
    return result;
  }

  @Override
  public void reIndexElasticSearch() {
    List<Word> words = listWords();
    log.info("Indexing {} words", words.size());
    BulkRequest bulkRequest = Requests.bulkRequest();
    int count = 0;
    if (words.size() > 0) {
      try {
        for (Word word : words) {
          bulkRequest.add(Requests.indexRequest(INDEX_NAME).type(WORD_TYPE).source(mapper.writeValueAsBytes(word)).ttl(ttl));
          if (++count % 100 == 0) {
            log.info("Parsed {} words, remaining {}", count, words.size() - count);
            client.bulk(bulkRequest).actionGet();
            bulkRequest = Requests.bulkRequest();
          }
        }
        client.bulk(bulkRequest).actionGet();
      } catch (JsonProcessingException e) {
        throw new RuntimeException(e);
      }
    }
    optimizeElasticSearch();
  }

  @Override
  public void optimizeElasticSearch() {
    client.admin().indices().optimize(Requests.optimizeRequest(INDEX_NAME)).actionGet();
  }
}
