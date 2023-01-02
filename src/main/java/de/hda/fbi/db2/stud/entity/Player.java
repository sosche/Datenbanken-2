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
@Table(name = "Player", schema = "scherazweizel")
public class Player {

  @Id
  @GeneratedValue
  private int id;

  @Column
  private String name;

  public List<Game> getGames() {
    return games;
  }

  @OneToMany(mappedBy = "player", cascade = CascadeType.PERSIST)
  private List<Game> games = new ArrayList<>();

  public Player() {
  }

  public Player(String name) {
    this.name = name;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }

    Player player = (Player) obj;
    return id == player.id;
  }
}
