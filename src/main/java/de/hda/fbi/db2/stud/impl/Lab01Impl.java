package de.hda.fbi.db2.stud.impl;

import de.hda.fbi.db2.api.Lab01Data;
import de.hda.fbi.db2.stud.entity.Answer;
import de.hda.fbi.db2.stud.entity.Category;
import de.hda.fbi.db2.stud.entity.Question;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Lab01Impl extends Lab01Data {

  List<Question> questions;
  List<Category> categories;

  @Override
  public List<?> getQuestions() {
    return questions;
  }

  @Override
  public List<?> getCategories() {
    return categories;
  }

  @Override
  public void loadCsvFile(List<String[]> additionalCsvLines) {
    questions = new ArrayList<Question>();
    HashMap<String, Category> map = new HashMap<String, Category>();

    for (int i = 1; i < additionalCsvLines.size(); i++) {
      int id = Integer.parseInt(additionalCsvLines.get(i)[0]);
      String question = additionalCsvLines.get(i)[1];
      //create question object and initialize it
      Question q = new Question();
      q.setQuestion(question);
      q.setQuestionID(id);

      //get correct number of answer
      int answerNumber = Integer.parseInt(additionalCsvLines.get(i)[6]);
      //create answer object and push to list
      for (int j = 1; j <= 4; j++) {
        String answer = additionalCsvLines.get(i)[j + 1];
        boolean correctAnswer = false;
        if (j == answerNumber) {
          correctAnswer = true;
        }
        Answer a = new Answer(answer, correctAnswer);
        q.setAnswer(a);
      }
      //add questions to list of this class
      questions.add(q);

      String category = additionalCsvLines.get(i)[7];
      //check if category exists in hashmap
      if (map.containsKey(category)) {
        map.get(category).addQuestions(q);
        q.setCategory(map.get(category));
      } else {
        Category c = new Category(category);
        c.addQuestions(q);
        q.setCategory(c);
        map.put(category, c);
      }
    }
    categories = new ArrayList<Category>(map.values());

    for (Category c : categories) {

      c.getQuestionandCategorie();
    }
  }
}
