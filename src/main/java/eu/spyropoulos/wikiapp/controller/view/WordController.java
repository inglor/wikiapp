package eu.spyropoulos.wikiapp.controller.view;

import eu.spyropoulos.wikiapp.service.WordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@Controller
@RequestMapping(value = "/view")
public class WordController {
  private Logger log = LoggerFactory.getLogger(WordController.class);

  @Autowired
  private WordService wordService;

  @RequestMapping(value = "/word/{wordName}", method = RequestMethod.GET)
  public String getWord(@PathVariable String wordName, Model model) {
    model.addAttribute("wordName", wordName);
    return "Hello " + wordName;
  }

  @RequestMapping(value = "/words", method = RequestMethod.GET)
  public String getWords() {
    return "Multiple words";
  }


}
