package eu.spyropoulos.wikiapp.controller;

import eu.spyropoulos.wikiapp.model.Word;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@RestController
public class WordController {

  @RequestMapping(value = "/view/word/{wordName}", method = RequestMethod.GET)
  public String getWord(@PathVariable String wordName, Model model) {
    model.addAttribute("wordName", wordName);
    return "Hello " + wordName;
  }

  @RequestMapping(value = "/view/words", method = RequestMethod.GET)
  public String getWords() {
    return "Multiple words";
  }

  @RequestMapping(value = "/api/word/{wordName}", method = RequestMethod.GET)
  public Word getWordObject(@PathVariable("wordName") String wordName) {
    Word word = new Word(wordName);
    word.setDefinition("Random Definition");
    return word;
  }

  @RequestMapping(value = "/api/word", method = RequestMethod.POST)
  public String saveWord(@RequestBody Word word) {
    return "Saved person: " + word.toString();
  }

}
