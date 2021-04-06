package com.github.zmilad97.rps.model;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class PlayerDTO {
  private String Id;
  private String name;
  private Map<String, Integer> cardsCount = new HashMap<>();
  private int lives  = 3;

  public String getId() {
    return Id;
  }

  public PlayerDTO setId(String id) {
    Id = id;
    return this;
  }

  public String getName() {
    return name;
  }

  public PlayerDTO setName(String name) {
    this.name = name;
    return this;
  }

  public Map<String, Integer> getCardsCount() {
    return cardsCount;
  }

  public PlayerDTO setCardsCount(Map<String, Integer> cardsCount) {
    this.cardsCount = cardsCount;
    return this;
  }

  public int getLives() {
    return lives;
  }

  public PlayerDTO setLives(int lives) {
    this.lives = lives;
    return this;
  }
}
