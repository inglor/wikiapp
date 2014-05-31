package eu.spyropoulos.wikiapp.controller.view;

import eu.spyropoulos.wikiapp.model.Word;
import eu.spyropoulos.wikiapp.service.WordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Controller
@RequestMapping(value = "/view")
public class WordController {
  private Logger log = LoggerFactory.getLogger(WordController.class);

  @Autowired
  private WordService wordService;

//  @RequestMapping(value = "/word/{wordName}", method = RequestMethod.GET)
//  public String getWordByName(@PathVariable String wordName, Model model) {
//    Word foundWord = wordService.getWordByName(wordName);
//    log.debug("Found word in database : " + foundWord);
//    model.addAttribute("word", foundWord);
//    return "details";
//  }

  @RequestMapping(value = "/words", method = RequestMethod.GET)
  public String getWords(Model model) {
    List<Word> words = wordService.listWords();
    log.debug("Found {} words in the database", words.size());
    model.addAttribute("words", words);
    return "list";
  }

  @RequestMapping(value = "/word/{wordId}", method = RequestMethod.GET)
  public String getWordById(@PathVariable Long wordId, Model model) {
    Word foundWord = wordService.getWordById(wordId);
    log.debug("Found word in database : " + foundWord);
    model.addAttribute("word", foundWord);
    return "details";
  }

}
