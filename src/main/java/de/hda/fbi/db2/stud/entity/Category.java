package de.hda.fbi.db2.stud.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "Category", schema = "scherazweizel")
public class Category {

  @Id
  @GeneratedValue
  private int id;

  @Column(unique = true)
  private String name;

  @OneToMany(mappedBy = "category", cascade = {CascadeType.PERSIST})
  private List<Question> questions = new ArrayList<Question>();

  public void deleteQuestions(int index) {
    questions.remove(index);
  }


  public Category() {
  } //default Konstruktor

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getQuestionSize() {
    return questions.size();
  }

  public Question getQuestion(int index) {
    return questions.get(index);
  }

  public List<Question> getAllQuestions() {
    return questions;
  }

  public Category(String name) {
    this.name = name;
    //questions = new ArrayList<>();
  }

  public void addQuestions(Question q) {
    questions.add(q);
  }

  /**
   * Prints all Questions, their category and possible answers with the correct index of the
   * answer.
   */
  public void getQuestionandCategorie() {
    for (Question question : questions) {
      System.out.println("Kategorie: " + this.getName());
      System.out.println("Frage: " + question.getQuestion());
      question.printAnswer();
    }
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(getId());
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    Category category = (Category) obj;
    return getId() == category.getId();
  }

  public int getId() {
    return id;
  }
}
