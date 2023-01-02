package de.hda.fbi.db2.stud.impl;

import de.hda.fbi.db2.api.Lab02EntityManager;
import de.hda.fbi.db2.stud.entity.Category;
import de.hda.fbi.db2.stud.entity.Category;
import de.hda.fbi.db2.stud.entity.Question;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;


public class Lab02Impl extends Lab02EntityManager {



  EntityManagerFactory emf = Persistence.createEntityManagerFactory("default-postgresPU");
  //EntityManagerFactory emf = Persistence.createEntityManagerFactory("fbi-postgresPU");
  EntityManager em = emf.createEntityManager();
  //EntityTransaction tx = null;

  @Override
  public void persistData() {

    List<?> categories = lab01Data.getCategories();
    List<?> questions = lab01Data.getQuestions();

    //tx = em.getTransaction();
    //tx.begin();
    em.getTransaction().begin();

    for (Object q : questions) {
      em.persist(q);
    }

    for (Object c : categories) {
      em.persist(c);
    }

    //tx.commit();

    em.getTransaction().commit();


  }


  @Override
  public EntityManager getEntityManager() {
    //EntityManagerFactory emf = Persistence.createEntityManagerFactory("default-postgresPU");
    //EntityManager em = emf.createEntityManager();
    return em;
  }
}
