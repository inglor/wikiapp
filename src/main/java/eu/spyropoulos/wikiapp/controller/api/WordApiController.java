package eu.spyropoulos.wikiapp.controller.api;

import eu.spyropoulos.wikiapp.model.Word;
import eu.spyropoulos.wikiapp.service.WordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api")
public class WordApiController {
  private Logger log = LoggerFactory.getLogger(WordApiController.class);

  @Autowired
  private WordService wordService;

  @RequestMapping(value = "/word/{wordId}", method = RequestMethod.GET)
  public Word getWordById(@PathVariable("wordId") Long wordId) {
    log.debug("Fetching from the database word with id : " + wordId);
    Word word = wordService.getWordById(wordId);
    log.debug("Found in database : " + word);
    return word;
  }

  @RequestMapping(value = "/word", method = RequestMethod.POST)
  public String saveWord(@RequestBody Word word) {
    Word newWord = new Word(word.getName(), word.getDefinition());
    Word savedWord = wordService.addWord(newWord);
    log.debug("Saving new word to Database : " + savedWord);
    return "Saved word: " + savedWord.toString();
  }
}
