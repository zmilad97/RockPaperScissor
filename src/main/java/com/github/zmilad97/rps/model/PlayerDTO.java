package com.github.zmilad97.rps.model;

import org.apache.commons.lang3.RandomStringUtils;

import java.util.HashMap;
import java.util.Map;

public class PlayerDTO {
    private final String id;
    private String name;
    private final Map<String, Integer> cardsCount;
    private final int lives;

    public PlayerDTO(String name) {
        id = RandomStringUtils.random(8, true, true);
        this.name = name;
        this.cardsCount = new HashMap<>();
        this.lives = 3;
    }

    public String getId() {
        return id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, Integer> getCardsCount() {
        return cardsCount;
    }


    public int getLives() {
        return lives;
    }

}
