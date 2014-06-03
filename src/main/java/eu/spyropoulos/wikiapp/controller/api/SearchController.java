package eu.spyropoulos.wikiapp.controller.api;

import eu.spyropoulos.wikiapp.model.Word;
import eu.spyropoulos.wikiapp.service.WordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/api/search")
public class SearchController {
  private Logger log = LoggerFactory.getLogger(SearchController.class);
  @Autowired
  private WordService wordService;

  @RequestMapping(value = "/word/{wordName}", method = RequestMethod.GET)
  public Word searchWord(@PathVariable("wordName") String wordName) {
    log.debug("Searching for Word with name {}", wordName);
    List<Word> words = wordService.search(wordName);
    log.debug("Found in database {} words", words.size());
    return words.get(0);
  }

  @RequestMapping(value = "/word/reindex", method = RequestMethod.GET)
  public void reIndex() {
    log.debug("Re-indexing initialized, will take time...");
    wordService.reIndex();
    log.debug("Re-index finished.");
  }

  @RequestMapping(value = "/word/optimize", method = RequestMethod.GET)
  public void optimize() {
    log.debug("Optimizing initialized, will take time...");
    wordService.optimize();
    log.debug("Optimizing finished.");

  }
}
