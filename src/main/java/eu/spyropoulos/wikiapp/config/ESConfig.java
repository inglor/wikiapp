package eu.spyropoulos.wikiapp.config;

import eu.spyropoulos.wikiapp.controller.api.SearchController;
import org.apache.commons.io.IOUtils;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.IndicesAdminClient;
import org.elasticsearch.client.Requests;
import org.elasticsearch.common.logging.ESLoggerFactory;
import org.elasticsearch.common.logging.slf4j.Slf4jESLoggerFactory;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.node.Node;
import org.elasticsearch.node.NodeBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.PropertySource;

import javax.annotation.PostConstruct;

@Configuration
@PropertySource("classpath:wikiapp.properties")
public class ESConfig {
  public static final String INDEX_NAME = "words-idx";
  public static final String WORD_TYPE = "word";

  @Value("${search.index:wikiapp-index}")
  protected String searchIndex;
  @Value("${search.http.port}")
  protected int searchPort;
  @Autowired
  private SearchController searchController;

  @Bean(destroyMethod = "close")
  @Lazy
  public Node node() throws Exception {
    ESLoggerFactory.setDefaultFactory(new Slf4jESLoggerFactory());
    ImmutableSettings.Builder settings = ImmutableSettings.settingsBuilder();
    settings.put("node.name", "wikiapp-node");
    settings.put("path.data", searchIndex);
    if (searchPort > 0)
      settings.put("http.port", searchPort);
    else
      settings.put("http.enabled", false);
    return NodeBuilder.nodeBuilder()
        .settings(settings)
        .clusterName("wikiapp-cluster")
        .data(true).local(true).node();
  }

  @Bean(destroyMethod = "close")
  @Lazy
  public Client client() throws Exception {
    return node().client();
  }

  @PostConstruct
  public void createWordIndex() throws Exception {
    String wordMapping = IOUtils.toString(getClass().getResourceAsStream("/search/word-mapping.json"));
    IndicesAdminClient adminClient = client().admin().indices();
    boolean indexExist = adminClient.exists(Requests.indicesExistsRequest(INDEX_NAME)).actionGet().isExists();
    if (!indexExist) {
      String settings = IOUtils.toString(getClass().getResourceAsStream("/search/settings.json"));
      CreateIndexRequest request = Requests.createIndexRequest(INDEX_NAME).settings(settings);
      adminClient.create(request).actionGet();

    }
    adminClient.preparePutMapping(INDEX_NAME).setType(WORD_TYPE).setSource(wordMapping).execute().actionGet();
    if (!indexExist) {
      searchController.reIndex();
    }
  }
}