package eu.spyropoulos.wikiapp.service;

import eu.spyropoulos.wikiapp.dao.WordDao;
import eu.spyropoulos.wikiapp.model.Word;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class WordServiceImpl implements WordService {

  @Autowired
  private WordDao wordDao;

  @Transactional
  public Word addWord(Word word) {
    return wordDao.addWord(word);
  }

  @Transactional
  public Word updateWord(Word word) {
    return wordDao.updateWord(word);
  }

  @Transactional
  public Word getWordById(Long wordId) {
    return wordDao.getWordById(wordId);
  }

  @Transactional
  public List<Word> listWords() {
    return wordDao.listWords();
  }

  @Transactional
  public boolean removeWord(Long id) {
    return wordDao.removeWord(id);
  }
}
