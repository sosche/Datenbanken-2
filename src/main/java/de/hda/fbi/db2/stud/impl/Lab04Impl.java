package de.hda.fbi.db2.stud.impl;

import de.hda.fbi.db2.api.Lab04MassData;
import de.hda.fbi.db2.stud.entity.Category;
import de.hda.fbi.db2.stud.entity.Game;
import de.hda.fbi.db2.stud.entity.Player;
import de.hda.fbi.db2.stud.entity.Question;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class Lab04Impl extends Lab04MassData {

  EntityManagerFactory emf = Persistence.createEntityManagerFactory("default-postgresPU");
  //EntityManagerFactory emf = Persistence.createEntityManagerFactory("fbi-postgresPU");
  EntityManager em = emf.createEntityManager();
  private List<Category> categories;
  private int c = 0;
  static final long START_TIME = 1577836800000L; //01.01.2020
  static final long END_TIME = 1580515200000L; //01.02.2020
  static final int Players = 10000;
  static final int Games = 100;


  @Override
  public void createMassData() {
    categories = em.createQuery("select c from Category c").getResultList();

    final long diffTime = END_TIME - START_TIME;
    long startTime = START_TIME;
    long time = diffTime / (Players * Games);
    long x = System.currentTimeMillis();

    em.clear();
    em.getTransaction().begin();

    for (int i = 0; i < Players; i++) {
      String playerName = "Player" + (i + 1);
      Player player = new Player(playerName);

      for (int j = 0; j < Games; j++) {
        List<Object> questions = generateQuestions();
        Date date = new Date();
        date.setTime(startTime);
        Game game = (Game) lab03Game.createGame(player, questions);
        player.getGames().add(game);
        game.setStarttime(date);
        Date start = new Date();
        lab03Game.playGame(game);
        Date end = new Date();
        Date endTime = new Date();
        endTime.setTime(startTime + (end.getTime() - start.getTime()));
        game.setEndTime(endTime);
        em.persist(game);
        startTime += time;
        //startTime += (1000 * 60); //Update every minute
      }
      if ((i + 1) % 100 == 0 && i != 0) {
        em.getTransaction().commit();
        double z = (double) (i + 1) / (double) Players;
        System.out.println((z * 100) + "%");
        em.clear();
        em.getTransaction().begin();
      }
    }
    em.getTransaction().commit();
    long y = System.currentTimeMillis();
    long diff = y - x;
    String duration = String.format("%d min, %d sec",
        TimeUnit.MILLISECONDS.toMinutes(diff),
        TimeUnit.MILLISECONDS.toSeconds(diff)
            - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(diff)));
    System.out.println("Duration: " + duration);
  }

  private List<Object> generateQuestions() {
    List<Object> questions = new ArrayList<Object>();
    Random rand = new Random();
    int numberQuestions = rand.nextInt(11) + 10;
    int n = numberQuestions / 2;

    Category category = categories.get(c);
    c = (c + 1) % 51;
    List<Question> categoryQuestion = category.getAllQuestions();
    Collections.shuffle(categoryQuestion);
    if (category.getQuestionSize() <= n) {
      questions.addAll(categoryQuestion);
    } else {
      for (int i = 0; i < n; i++) {
        questions.add(categoryQuestion.get(i));
      }
    }
    while (true) {
      category = categories.get(c);
      c = (c + 1) % 51;
      categoryQuestion = category.getAllQuestions();
      Collections.shuffle(categoryQuestion);
      if (categoryQuestion.size() <= n) {
        questions.addAll(categoryQuestion);
      } else {
        for (int i = 0; i < n; i++) {
          questions.add(categoryQuestion.get(i));
        }
      }
      if (questions.size() == numberQuestions) {
        break;
      }
      n = numberQuestions - questions.size();
    }
    return questions;
  }
}
