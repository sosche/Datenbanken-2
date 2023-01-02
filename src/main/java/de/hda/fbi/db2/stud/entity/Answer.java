package de.hda.fbi.db2.stud.entity;

import javax.persistence.Embeddable;
import javax.persistence.Table;

@Embeddable
public class Answer {

  Answer() {
  }

  ;

  public Answer(String answer, boolean correctAnswer) {
    this.answer = answer;
    this.isCorrect = correctAnswer;
  }

  public String getAnswer() {
    return answer;
  }

  private String answer;

  public boolean isCorrect() {
    return isCorrect;
  }

  private boolean isCorrect;

}
