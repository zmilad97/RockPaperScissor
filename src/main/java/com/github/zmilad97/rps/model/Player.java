package com.github.zmilad97.rps.model;

import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Map;

public class Player {
    private String id;
    private String name;
    private Socket socket;
    private DataOutputStream dos;
    private BufferedReader br;
    private Map<String, Integer> cardsCount;
    private int lives;

    public Player(PlayerDTO playerDTO) {
        this.id = playerDTO.getId();
        this.name = playerDTO.getName();
        this.cardsCount = playerDTO.getCardsCount();
        this.lives = playerDTO.getLives();
    }


    public void cardsMinus(String card) {
        if (card != null && !card.equals(""))
            cardsCount.put(card, cardsCount.get(card) - 1);
    }

    public String getId() {
        return id;
    }


    public Socket getSocket() {
        return socket;
    }

    @SneakyThrows
    public void setSocket(Socket socket) {
        this.socket = socket;
        dos = new DataOutputStream(socket.getOutputStream());
        br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    public Map<String, Integer> getCardsCount() {
        return cardsCount;
    }

    public void setCardsCount(int r, int p, int s) {
        this.cardsCount.put("Rock", r);
        this.cardsCount.put("Paper", p);
        this.cardsCount.put("Scissor", s);
    }

    public DataOutputStream getDos() {
        return dos;
    }

    public BufferedReader getBr() {
        return br;
    }

    public String getName() {
        return name;
    }

    public int getLives() {
        return lives;
    }

    public void setLives(int lives) {
        this.lives = lives;
    }

    public void setPlayer(PlayerDTO playerDTO) {
        this.id = playerDTO.getId();
        this.name = playerDTO.getName();
        this.cardsCount = playerDTO.getCardsCount();
        this.lives = playerDTO.getLives();
    }
}
