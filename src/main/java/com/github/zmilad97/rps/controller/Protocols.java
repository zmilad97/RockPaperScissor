package com.github.zmilad97.rps.controller;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import com.github.zmilad97.rps.model.Player;
import com.github.zmilad97.rps.model.Room;
import com.github.zmilad97.rps.service.GameService;

import java.io.IOException;
import java.util.Map;


//TODO : fix Probable Concurrency Problem
@Slf4j
public class Protocols {
    private String[] command;
    private Player player;

    public Protocols(Player player) {
        this.player = player;
    }

    public Protocols() {

    }

    public String parseCommand(String request) {
        if (request != null) {
            command = request.split(" ");
            switch (command[0]) {

                // Client to server commands

                case "JOIN" -> joinRoom();

                case "ADMIN" -> roomAdmin();

                case "START" -> startGame();

                case "HAND" -> hand();

                case "CARDS" -> {
                    return cards();
                }

                case "LEAVE" -> leave();

                case "EXIT" -> exit();


                // com.github.zmilad97.rps.Server to client commands

                case "ENTERED" -> entered(); //send message for player whom entered the room

                case "JOINED" -> joined(); //notify all players in room that a player joined the room

                case "FULL" -> full();

                case "REST" -> rest();

                case "STAT" -> stat();

                case "PLAY" -> play();

                case "END-GAME" -> endGame();

                case "END-ROUND" -> endRound();

                case "END-ROOM" -> endRoom();

            }
        }
        return null;
    }


    @SneakyThrows
    private void entered() {
        if (command.length >= 4)
            GameService.players.get(command[1]).getDos().writeChars("\nYou Entered The Room : " + command[2] + "\n" +
                    "\nYou Are Player Number " + command[3] + " Waiting For " + command[4] + " More Players");
    }

    //TODO : send game information to the admin
    private void joined() {
        if (command.length >= 4) {
            Player jp = GameService.players.get(command[2]);
            GameService.rooms.get(command[1]).getPlayers().forEach(p -> {
                if (!p.equals(jp)) {
                    try {
                        p.getDos().writeChars("\nPlayer Number " + jp.getName() + " Entered" +
                                " Waiting For " + command[3] + " More Players");
                    } catch (IOException e) {
                        log.error(e.getMessage());
                    }
                }
            });
        }
    }

    @SneakyThrows
    private void full() {
        GameService.players.get(command[1]).getDos().writeChars("\n\nThe Room Is Full !!! \n");
    }

    @SneakyThrows
    private void play() {
        if (command.length >= 2) {
            Player pl = GameService.players.get(command[1]);
            pl.getDos().writeChars("\n\nThe Game Started ! Be Ready ...\n\n You have Rock : " + pl.getCardsCount().get("Rock") +
                    " Paper : " + pl.getCardsCount().get("Paper") + " Scissor : " + pl.getCardsCount().get("Scissor") +
                    "\n\n[R]ock,[P]aper,[S]cissors : ");
        }
    }

    @SneakyThrows
    private void rest() {
        if (command.length >= 2)
            GameService.players.get(command[1]).getDos().writeChars("\n\nYou Are On The Rest This Round ! Please Wait For Other Players ");
    }

    private void stat() {
        if (command.length >= 2) {
            Room room = GameService.rooms.get(command[1]);
            Map<String, Integer> cards = GameService.rooms.get(command[1]).cardsCount();
            room.getPlayers().forEach(p -> {
                try {
                    p.getDos().writeChars("\n\nTotal Cards : Rock = " + cards.get("Rock")
                            + " | Paper = " + cards.get("Paper") + " | Scissor = " + cards.get("Scissor"));
                } catch (IOException e) {
                    log.error(e.getMessage());
                }
            });
        }
    }

    private void endRoom() {
    }

    private void endRound() {
    }

    private void endGame() {
    }


    // Client to com.github.zmilad97.rps.Server methods

    @SneakyThrows
    private void joinRoom() {
        if (command.length >= 2) {
            GameService.rooms.get(command[1]).addPlayer(player);
            log.info("player : " + player.getName() + " added to room : " + GameService.rooms.get(command[1]).getName());
        }
    }

    private void roomAdmin() {
        if (command.length >= 2) {
            if (GameService.rooms.get(command[1]).getAdmin().equals(player))
                System.out.println("ADMIN");  //TODO : redirect to admin panel
        }
    }

    private void startGame() {
        if (command.length >= 3) {
            if (GameService.rooms.get(command[1]).getAdmin().getId().equals(command[3]))
                GameService.rooms.get(command[1]).startGame();
        }
    }

    private void hand() {
        if (command.length >= 2)
            GameService.playerGameMap.get(player).addChoice(command[1], player);
    }

    private String cards() {
        if (command[1] != null && !command[1].equals(""))
            switch (command[1]) {
                case "r" -> {
                    return "Rock";
                }
                case "p" -> {
                    return "Paper";
                }
                case "s" -> {
                    return "Scissor";
                }
            }
        return "";
    }

    private void leave() {
        GameService.playerRoomMap.get(player).getPlayers().remove(player);
    }

    @SneakyThrows
    private void exit() {
        GameService.playerRoomMap.get(player).getPlayers().remove(player);
        GameService.playerRoomMap.remove(player);
        GameService.playerDTOS.remove(player.getId());
        GameService.players.remove(player.getId());
        player.getSocket().close();
    }

}
