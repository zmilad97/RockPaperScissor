package com.github.zmilad97.rps.model;

import org.apache.commons.lang3.RandomStringUtils;
import com.github.zmilad97.rps.service.GameService;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Game extends Thread {

    private String gameId;
    private final String roomId;
    private Player player1;
    private Player player2;
    private Map<Player, String> result = new HashMap<>();

    public Game() {
        roomId="";
    }

    public Game(String roomId, Player player1, Player player2) {
        this.gameId = RandomStringUtils.random(6);
        this.roomId = roomId;
        this.player1 = player1;
        this.player2 = player2;
    }

    public void run() {
        try {
            player1.getDos().writeChars("\n\n\n[R]ock,[P]aper,[S]cissors : ");
            player2.getDos().writeChars("\n\n\n[R]ock,[P]aper,[S]cissors : ");

            checkWin(player1.getBr().readLine(), player2.getBr().readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void checkWin(String player1Answer, String player2Answer) {

//        switch (player1Answer) {
//            case "r" -> player1.cardsMinus("Rock");
//            case "p" -> player1.cardsMinus("Paper");
//            case "s" -> player1.cardsMinus("Scissor");
//        }
//        switch (player2Answer) {
//            case "r" -> player2.cardsMinus("Rock");
//            case "p" -> player2.cardsMinus("Paper");
//            case "s" -> player2.cardsMinus("Scissor");
//        }

        if (player1Answer.equals(player2Answer)) {
            result.put(player1, "draw");
            result.put(player2, "draw");
        } else if (player1Answer.equals("s") && player2Answer.equals("p")) {
            result.put(player1, "won");
            result.put(player2, "lost");
        } else if (player1Answer.equals("p") && player2Answer.equals("s")) {
            result.put(player1, "lost");
            result.put(player2, "won");
        } else if (player1Answer.equals("r") && player2Answer.equals("p")) {
            result.put(player1, "lost");
            result.put(player2, "won");
        } else if (player1Answer.equals("p") && player2Answer.equals("r")) {
            result.put(player1, "won");
            result.put(player2, "lost");
        } else if (player1Answer.equals("s") && player2Answer.equals("r")) {
            result.put(player1, "lost");
            result.put(player2, "won");
        } else if (player1Answer.equals("r") && player2Answer.equals("s")) {
            result.put(player1, "won");
            result.put(player2, "lost");
        }
        if (result.get(player1).equals("won")) {
            player1.setLives(player1.getLives() + 1);
            player2.setLives(player2.getLives() - 1);
        } else if (result.get(player1).equals("lost")) {
            player1.setLives(player1.getLives() - 1);
            player2.setLives(player2.getLives() + 1);
        }

        try {
            player1.getDos().writeBytes("\n you " + result.get(player1).toUpperCase() + " you have : " + player1.getLives() + " lives\n");
            player2.getDos().writeBytes("\n you " + result.get(player2).toUpperCase() + " you have : " + player2.getLives() + " lives\n");

            GameService.findRoom(roomId).addPlayer(player1);
            GameService.findRoom(roomId).addPlayer(player2);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public Player getPlayer1() {
        return player1;
    }

    public void setPlayer1(Player player1) {
        this.player1 = player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public void setPlayer2(Player player2) {
        this.player2 = player2;
    }

    public Map<Player, String> getResult() {
        return result;
    }

    public void setResult(Map<Player, String> result) {
        this.result = result;
    }

    public String getRoomId() {
        return roomId;
    }

}
