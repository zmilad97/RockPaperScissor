package com.github.zmilad97.rps.model;

import com.github.zmilad97.rps.controller.Protocols;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import com.github.zmilad97.rps.service.GameService;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class Game extends Thread {

    private final String gameId;
    private final String roomId;
    private final Player player1;
    private final Player player2;
    private String p1Choice;
    private String p2Choice;
    private final Map<Player, String> result = new HashMap<>();
    private final Protocols protocols;

    public Game(String roomId, Player player1, Player player2) {
        this.gameId = RandomStringUtils.random(6);
        this.roomId = roomId;
        this.player1 = player1;
        this.player2 = player2;
        protocols = new Protocols();
    }

    @SneakyThrows
    public void run() {
        protocols.parseCommand("PLAY " + player1.getId());
        protocols.parseCommand("PLAY " + player2.getId());
    }

    public void addChoice(String choice, Player player) {
        if (player1.equals(player))
            p1Choice = choice;
        else if (player2.equals(player))
            p2Choice = choice;
        if (p1Choice != null && p2Choice != null)
            checkWin(protocols.parseCommand("CARDS " + p1Choice), protocols.parseCommand("CARDS " + p2Choice));
    }

    private void checkWin(String player1Answer, String player2Answer) {


        if (!resolveSituiation(player1Answer, player2Answer)) {
            handleCards(player1Answer, player2Answer);
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
        }
        handlePlayers();

    }

    public void handleCards(String player1Answer, String player2Answer) {

        if (player1Answer != null && !player1Answer.equals(""))
            player1.cardsMinus(player1Answer);
        if (player2Answer != null && !player2Answer.equals(""))
            player2.cardsMinus(player2Answer);
    }

    @SneakyThrows
    public void handlePlayers() {
        if (!result.get(player1).equals(result.get(player2))) {
            if (result.get(player1).equals("won")) {

                player1.setLives(player1.getLives() + 1);
                player2.setLives(player2.getLives() - 1);

                GameService.rooms.get(roomId).addStatus(player1, "WON");
                GameService.rooms.get(roomId).addStatus(player2, "LOST");

            } else if (result.get(player1).equals("lost")) {
                player1.setLives(player1.getLives() - 1);
                player2.setLives(player2.getLives() + 1);


                GameService.rooms.get(roomId).addStatus(player2, "WON");
                GameService.rooms.get(roomId).addStatus(player1, "LOST");

            }
        } else {
            player1.setLives(player1.getLives() - 1);
            player2.setLives(player2.getLives() - 1);

            GameService.rooms.get(roomId).addStatus(player1, "LOST");
            GameService.rooms.get(roomId).addStatus(player2, "LOST");
        }

        GameService.playerGameMap.remove(player1);
        GameService.playerGameMap.remove(player2);

        if (result.get(player1).equals("won"))
            protocols.parseCommand("WON " + player1.getId());
        else
            protocols.parseCommand("LOST " + player1.getId());

        if (result.get(player2).equals("won"))
            protocols.parseCommand("WON " + player2.getId());
        else
            protocols.parseCommand("LOST " + player2.getId());

//        if (result.get(player1).equals("won") && result.get(player2).equals("lost")) {
//            protocols.parseCommand("WON " + player1.getId());
//            protocols.parseCommand("LOST " + player2.getId());
//
//        } else {
//            protocols.parseCommand("WON " + player2.getId());
//            protocols.parseCommand("LOST " + player1.getId());
//        }


    }

    public boolean resolveSituiation(String player1Answer, String player2Answer) {
        boolean situiation = false;
        if (player1Answer.equals("") && player2Answer.equals("")) {
            result.put(player1, "lost");
            result.put(player2, "lost");
            handleCards("", "");
            situiation = true;
        } else if (player1Answer.equals("")) {
            result.put(player1, "lost");
            result.put(player2, "won");
            handleCards("", player2Answer);
            situiation = true;
        } else if (player2Answer.equals("")) {
            result.put(player1, "won");
            result.put(player2, "lost");
            handleCards(player1Answer, "");
            situiation = true;
        } else if (player1.getCardsCount().get(player1Answer) == 0 && player2.getCardsCount().get(player2Answer) != 0) {
            result.put(player1, "lost");
            result.put(player2, "won");
            handleCards("", player2Answer);
            situiation = true;
        } else if (player2.getCardsCount().get(player2Answer) == 0 && player1.getCardsCount().get(player1Answer) != 0) {
            result.put(player2, "lost");
            result.put(player1, "won");
            handleCards(player1Answer, "");
            situiation = true;
        } else if (player2.getCardsCount().get(player2Answer) == 0 && player1.getCardsCount().get(player1Answer) == 0) {
            result.put(player1, "lost");
            result.put(player2, "lost");
            handleCards("", "");
            situiation = true;
        }

        return situiation;
    }


}
