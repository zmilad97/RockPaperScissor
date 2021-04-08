package model;

import org.apache.commons.lang3.RandomStringUtils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class PlayerDTO {
    private String Id;
    private String name;
    private Map<String, Integer> cardsCount;
    private int lives;

    public PlayerDTO(String name) {
        Id = RandomStringUtils.random(8, true, true);
        this.name = name;
        this.cardsCount = new HashMap<>();
        this.lives = 3;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
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
