package de.hda.fbi.db2.stud.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "Question", schema = "scherazweizel")
public class Question {

  @Id
  private int questionID;

  @Column
  private String question;

  public List<Answer> getAnswers() {
    return answers;
  }

  @ElementCollection
  @CollectionTable(name = "Answer", schema = "scherazweizel")
  private List<Answer> answers = new ArrayList<Answer>();

  //public Question(){} // default Konstruktor

  @ManyToOne
  private Category category;

  public Category getCategory() {
    return category;
  }

  public void setCategory(Category category) {
    this.category = category;
  }


  public Question() {
  }

  public String getQuestion() {
    return question;
  }

  public void setQuestion(String question) {
    this.question = question;
  }

  public int getQuestionID() {
    return questionID;
  }

  public void setQuestionID(int questionID) {
    this.questionID = questionID;
  }

  public void setAnswer(Answer a) {
    answers.add(a);
  }

  /**
   * Methode zur Ausgabe der Dateien die eingelesen werden.
   */
  public void printAnswer() {
    int antwortIndex = 0;
    for (int i = 0; i < answers.size(); i++) {
      System.out.println(i + 1 + ": " + answers.get(i).getAnswer());
      if (answers.get(i).isCorrect()) {
        antwortIndex = i + 1;
      }
    }
    //System.out.println("Lösung: " + antwortIndex + "\n");
  }

  /**
   * This function returns the index of the Correct Answer inside the list.
   * @return
   */
  public int getCorrectAnswert() {
    int antwortIndex = 0;
    for (int i = 0; i < answers.size(); i++) {
      if (answers.get(i).isCorrect()) {
        antwortIndex = i + 1;
      }
      //System.out.println("Lösung: " + antwortIndex + "\n");
    }
    return antwortIndex;
  }

  @Override
  public int hashCode() {
    return Objects.hash(questionID);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    Question question = (Question) obj;
    return questionID == question.questionID;
  }
}
