package eu.spyropoulos.wikiapp.model;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@Entity
@Table(name = "word")
@XmlRootElement
public class Word implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id")
  private Long id;

  @Column(name = "name")
  private String name;

  @Column(name = "definition")
  private String definition;

  public Word() {
  }

  public Word(String name) {
    this.name = name;
  }

  public Word(String name, String definition) {
    this.name = name;
    this.definition = definition;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDefinition() {
    return definition;
  }

  public void setDefinition(String definition) {
    this.definition = definition;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Word word = (Word) o;

    if (definition != null ? !definition.equals(word.definition) : word.definition != null) return false;
    if (id != null ? !id.equals(word.id) : word.id != null) return false;
    if (name != null ? !name.equals(word.name) : word.name != null) return false;

    return true;
  }

  @Override
  public int hashCode() {
    int result = id != null ? id.hashCode() : 0;
    result = 31 * result + (name != null ? name.hashCode() : 0);
    result = 31 * result + (definition != null ? definition.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "Word{" +
        "id=" + id +
        ", name='" + name + '\'' +
        ", definition='" + definition + '\'' +
        '}';
  }
}

