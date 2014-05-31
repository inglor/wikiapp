package eu.spyropoulos.wikiapp.dao;

import eu.spyropoulos.wikiapp.model.Word;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;

@Repository
public class WordDaoImpl implements WordDao {

  @Autowired
  private SessionFactory sessionFactory;

  @Override
  public Word addWord(Word word) {
    Serializable id = sessionFactory.getCurrentSession().save(word);
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
  public Word getWordByName(String wordName) {
    String queryString = "from Word w where w.name = '" + wordName + "'";
    return (Word) sessionFactory.getCurrentSession().createQuery(queryString).list().get(0);
  }
}
