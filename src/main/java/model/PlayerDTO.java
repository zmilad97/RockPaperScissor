package model;

import org.apache.commons.lang3.RandomStringUtils;

import java.util.HashMap;
import java.util.Map;

public class PlayerDTO {
    private String id;
    private String name;
    private Map<String, Integer> cardsCount;
    private int lives;

    public PlayerDTO(String name) {
        id = RandomStringUtils.random(8, true, true);
        this.name = name;
        this.cardsCount = new HashMap<>();
        this.lives = 3;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public void setCardsCount(Map<String, Integer> cardsCount) {
        this.cardsCount = cardsCount;
    }

    public int getLives() {
        return lives;
    }

    public void setLives(int lives) {
        this.lives = lives;
    }
}
