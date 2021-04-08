package model;

import org.apache.commons.lang3.RandomStringUtils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class Player {
    private String Id;
    private String name;
    private Socket socket;
    private DataOutputStream dos;
    private BufferedReader br;
    private Map<String, Integer> cardsCount = new HashMap<>();
    private int lives = 3;

    public Player(PlayerDTO playerDTO) {
        this.Id = playerDTO.getId();
        this.name = playerDTO.getName();
        this.cardsCount = playerDTO.getCardsCount();
        this.lives = playerDTO.getLives();
    }
    public Player(){

    }

    public void cardsMinus(String card) {
        cardsCount.put(card, cardsCount.get(card) - 1);
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
        try {
            dos = new DataOutputStream(this.socket.getOutputStream());
            br = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Map<String, Integer> getCardsCount() {
        return cardsCount;
    }

    public void setCardsCount(Map<String, Integer> cardsCount) {
        this.cardsCount = cardsCount;
    }

    public DataOutputStream getDos() {
        return dos;
    }

    public void setDos(DataOutputStream dos) {
        this.dos = dos;
    }

    public BufferedReader getBr() {
        return br;
    }

    public void setBr(BufferedReader br) {
        this.br = br;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLives() {
        return lives;
    }

    public void setLives(int lives) {
        this.lives = lives;
    }
}
