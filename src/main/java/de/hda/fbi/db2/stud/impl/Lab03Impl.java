package de.hda.fbi.db2.stud.impl;

import de.hda.fbi.db2.api.Lab03Game;
import de.hda.fbi.db2.stud.entity.Answer;
import de.hda.fbi.db2.stud.entity.Category;
import de.hda.fbi.db2.stud.entity.Game;
import de.hda.fbi.db2.stud.entity.Player;
import de.hda.fbi.db2.stud.entity.Question;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.nio.Buffer;
import java.nio.charset.StandardCharsets;
import java.rmi.server.ExportException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;

public class Lab03Impl extends Lab03Game {


  EntityManagerFactory emf = Persistence.createEntityManagerFactory("default-postgresPU");
  //EntityManagerFactory emf = Persistence.createEntityManagerFactory("fbi-postgresPU");
  EntityManager em = emf.createEntityManager();

  @Override
  public Object getOrCreatePlayer(String playerName) {
    Player player;
    //EntityManager em = lab02EntityManager.getEntityManager();
    String query = "select p from Player p where p.name =:name";
    try {
      player = (Player) em.createQuery(query).setParameter("name", playerName)
          .getSingleResult();
    } catch (NoResultException ex) {
      player = new Player(playerName);
    }
    return player;
  }

  @Override
  public Object interactiveGetOrCreatePlayer() {
    System.out.println("Namen eingeben: ");
    String name = null;
    BufferedReader reader = new BufferedReader(
        new InputStreamReader(System.in, StandardCharsets.UTF_8));
    try {
      name = reader.readLine();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return (Player) getOrCreatePlayer(name);
  }

  @Override
  public List<?> getQuestions(List<?> categories, int amountOfQuestionsForCategory) {
    List<Question> questions = new LinkedList<>();
    for (Object c : categories) {
      Category cat = (Category) c;
      if (cat.getQuestionSize() <= amountOfQuestionsForCategory) {
        questions.addAll(cat.getAllQuestions());
      } else {
        for (int i = 0; i < amountOfQuestionsForCategory; i++) {
          Random rand = new Random();
          int randNum = rand.nextInt(cat.getQuestionSize());
          questions.add(cat.getQuestion(randNum));
          cat.deleteQuestions(randNum);
        }
      }
    }
    return questions;
  }

  @Override
  public List<?> interactiveGetQuestions() {
    List<Category> categories = new LinkedList<>();
    int anzahlFragen = 0;
    List<Category> ca = em.createQuery("SELECT c FROM Category c ORDER BY c.id").getResultList();
    for (Object c : ca) {
      Category cat = (Category) c;
      System.out.print(cat.getId() + " ");
      System.out.print(cat.getName() + "\n");
    }
    BufferedReader reader = new BufferedReader(
        new InputStreamReader(System.in, StandardCharsets.UTF_8));

    System.out.println("Id der Kategorien eingeben(min. 2): ");

    System.out.println("(0) um Eingabe abzuschliessen: ");

    Set<Integer> categoriesID = new HashSet<>();

    try {
      while (true) {
        int id = Integer.parseInt(reader.readLine());
        if (id == 0 && categoriesID.size() >= 2) {
          break;
        } else {
          categoriesID.add(id);
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    for (Object c : ca) {
      Category cat = (Category) c;
      for (Integer integer : categoriesID) {
        if (cat.getId() == integer) {
          categories.add(cat);
          break;
        }
      }
    }
    System.out.println("Anzahl der Fragen pro Kategorie(min. 2) ");
    try {
      anzahlFragen = Integer.parseInt(reader.readLine());
    } catch (IOException e) {
      e.printStackTrace();
    }
    while (anzahlFragen < 2) {
      System.out.println("Min. 2 Fragen bitte! ");
      try {
        anzahlFragen = Integer.parseInt(reader.readLine());
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return getQuestions(categories, anzahlFragen);
    //return categories;
  }

  @Override
  public Object createGame(Object player, List<?> questions) {
    //player = interactiveGetOrCreatePlayer();
    Game game = new Game();
    game.setPlayer((Player) player);
    for (Object q : questions) {
      game.getAnswer().put((Question) q, null);
    }

    return game;
  }

  @Override
  public void playGame(Object game) {
    //((Game) game).setStarttime(new Date());
    Map<Question, Boolean> mapGameQuestion = ((Game) game).getAnswer();
    List<Question> gameQuestions = new ArrayList<Question>(mapGameQuestion.keySet());
    Collections.shuffle(gameQuestions);

    for (int i = 0; i < gameQuestions.size(); i++) {
      Question question = gameQuestions.get(i);
      //System.out.println((i + 1) + ". " + question.getQuestion());
      List<Answer> answers = question.getAnswers();
      //for (int j = 0; j < answers.size(); j++) {
      //System.out.println("\t[" + (j + 1) + "] " + answers.get(j));
      // }

      try {
        //BufferedReader reader = new BufferedReader(
        //    new InputStreamReader(System.in, StandardCharsets.UTF_8));
        while (true) {
          //String input = reader.readLine();
          String input = String.valueOf(getRandomNumber());
          if (input == null) {
            continue;
          }
          if (isNumber(input)) {
            int x = Integer.parseInt(input);
            if (x > 0 && x <= 4) {
              if ((x - 1) == question.getCorrectAnswert()) {
                //System.out.println("==> RIGHT ANSWER!");
                mapGameQuestion.put(question, true);
              } else {
                //System.out.println("==> WRONG ANSWER!");
                mapGameQuestion.put(question, false);
              }
              break;
            }
            //System.out.println("ERROR ==> Choose numbers between 1 and 4");
          } else {
            //System.out.println("ERROR ==> Invalid input");
          }
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    //((Game) game).setEndTime(new Date());
    ((Game) game).setAnswer(mapGameQuestion);
    //System.out.println("You finished the Game");
    int rightQuestions = 0;
    int wrongQuestions = 0;
    for (boolean x : mapGameQuestion.values()) {
      if (x) {
        ++rightQuestions;
      } else {
        ++wrongQuestions;
      }
    }
    //System.out.println("You guessed " + rightQuestions + " Questions RIGHT");
    //System.out.println("You guessed " + wrongQuestions + " Questions WRONG");
  }

  @Override
  public void interactivePlayGame(Object game) {

    int rightQuestions = 0;
    int wrongQuestions = 0;

    Game g = (Game) game;
    ((Game) game).setStarttime(new Date());
    //int questionSize = ((Game) game).getAnswer().size();
    for (Question q : ((Game) game).getAnswer().keySet()) {
      System.out.println(q.getQuestion());
      q.printAnswer();
      int givenAnswer = -1;
      BufferedReader reader = new BufferedReader(
          new InputStreamReader(System.in, StandardCharsets.UTF_8));

      try {
        while (givenAnswer < 1 || givenAnswer > 4) {
          givenAnswer = Integer.parseInt(reader.readLine());
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
      if (givenAnswer == q.getCorrectAnswert()) {
        g.getAnswer().put(q, true);
        rightQuestions++;
        System.out.println("Die Antwort war richtig");
      } else {
        g.getAnswer().put(q, false);
        wrongQuestions++;
        System.out.println("Die Antwort war falsch");
      }
    }
    ((Game) game).setEndTime(new Date());
    printResult(rightQuestions, wrongQuestions);
  }

  /**
   * This function show the result of the game.
   *
   * @param rightQuestions right guessed Answers
   * @param wrongQuestions wrong guessed Answers
   */
  public void printResult(int rightQuestions, int wrongQuestions) {
    System.out.println("Your results: ");
    System.out.println("=> You guessed " + rightQuestions + " Questions RIGHT");
    System.out.println("==>  You guessed " + wrongQuestions + " Questions WRONG");
  }

  @Override
  public void persistGame(Object game) {
    em.getTransaction().begin();
    em.persist((Game) game);
    em.persist(((Game) game).getPlayer());
    em.getTransaction().commit();
  }

  /**
   * This function checks if the string is a valid number or a char.
   *
   * @param input Parameter is the string which he readed from the function before
   * @return true if it's a valid number else false
   */
  public boolean isNumber(String input) {
    if (input == null) {
      return false;
    }
    try {
      Integer.parseInt(input);
    } catch (NumberFormatException n) {
      return false;
    }
    return true;
  }

  /**
   * This function generates an random number between 1 and 4.
   *
   * @return returns random Integer between 1 and 4
   */
  public int getRandomNumber() {
    int upperbound = 4;
    Random random = new Random();
    int rand = 0;
    while (true) {
      rand = random.nextInt(upperbound);
      if (rand != 0) {
        break;
      }
    }
    return rand;
  }
}
