package eu.spyropoulos.wikiapp.service;

import eu.spyropoulos.wikiapp.model.Word;

import java.util.List;

public interface WordService {

  public Word addWord(Word word);

  public Word updateWord(Word word);

  public Word getWordById(Long wordId);

  public List<Word> listWords();

  public boolean removeWord(Long id);

}
