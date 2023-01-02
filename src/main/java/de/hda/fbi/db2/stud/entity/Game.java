package de.hda.fbi.db2.stud.entity;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapKey;
import javax.persistence.MapKeyJoinColumn;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "Game", schema = "scherazweizel")
public class Game {

  @Id
  @GeneratedValue
  private int id;

  public void setStarttime(Date starttime) {
    this.starttime = new Date(starttime.getTime());
  }

  @Temporal(TemporalType.TIMESTAMP)
  private Date starttime;


  public void setEndTime(Date endTime) {
    this.endTime = new Date(endTime.getTime());
  }

  @Temporal(TemporalType.TIMESTAMP)
  private Date endTime;

  //@Column
  //private int maxQuestion;

  public Map<Question, Boolean> getAnswer() {
    return answer;
  }

  public void setAnswer(Map<Question, Boolean> answer) {
    this.answer = answer;
  }

  @ElementCollection
  @CollectionTable(name = "game_question", joinColumns = @JoinColumn(name = "gameId"),
      schema = "scherazweizel")
  @MapKeyJoinColumn(name = "questionId")
  @Column(name = "answer")
  private Map<Question, Boolean> answer;

  public Player getPlayer() {
    return player;
  }

  public void setPlayer(Player player) {
    this.player = player;
  }

  @ManyToOne(cascade = CascadeType.PERSIST)
  private Player player;

  /**
   * Constructor for the Class Game.
   */
  public Game() {
    answer = new HashMap<>();
    starttime = null;
    endTime = null;
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (obj == this || getClass() != obj.getClass()) {
      return true;
    }

    Game game = (Game) obj;
    return id == game.id;
  }
}
