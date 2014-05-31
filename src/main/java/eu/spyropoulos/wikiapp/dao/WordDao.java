package eu.spyropoulos.wikiapp.dao;

import eu.spyropoulos.wikiapp.model.Word;

import java.util.List;

public interface WordDao {
  public Word addWord(Word word);

  public Word updateWord(Word word);

  public List<Word> listWords();

  public Word getWordById(Long wordId);

  public boolean removeWord(Long id);
}
