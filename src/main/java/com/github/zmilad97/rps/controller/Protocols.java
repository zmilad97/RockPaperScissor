package com.github.zmilad97.rps.controller;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import com.github.zmilad97.rps.model.Player;
import com.github.zmilad97.rps.model.Room;
import com.github.zmilad97.rps.service.GameService;

import java.io.IOException;
import java.util.List;
import java.util.Map;


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

                case "START" -> startGame();

                case "HAND" -> hand();

                case "REMOVE" -> remove();

                case "CARDS" -> {
                    return cards();
                }

                case "LEAVE" -> leave();

                case "EXIT" -> exit();


                // Server to client commands

                case "ENTERED" -> entered(); //send message for player whom entered the room

                case "JOINED" -> joined(); //notify all players in room that a player joined the room

                case "FULL" -> full();

                case "REST" -> rest();

                case "STAT" -> stat();

                case "PLAY" -> play();

                case "WON" -> won();

                case "LOST" -> lost();

                case "END" -> end();


            }
        }
        return null;
    }

    //TODO : Handle END ROOM protocol
    @SneakyThrows
    private void end() {
        if (command.length >= 3) {
            if (command[1].equals("ROUND")) {
                StringBuilder losers = new StringBuilder();
                StringBuilder winners = new StringBuilder();
                winners.append("Winners Of This Round Are : ");
                losers.append("Losers Of This Round Are : ");
                Room room = GameService.rooms.get(command[2]);
                Map<Player, String> statusPlayers = room.getRoundPlayerStatus();
                statusPlayers.forEach((k, v) -> {
                    if (v.equals("WON"))
                        winners.append(k.getName());
                    else if (v.equals("LOST"))
                        losers.append(k.getName());
                    winners.append(" ");
                    losers.append(" ");
                });
                List<Player> players = room.getPlayers();
                players.forEach(p -> {
                    try {
                        p.getDos().writeChars("\n\n" + winners.toString() + "\n\n" + losers.toString());
                    } catch (IOException e) {
                        log.error(e.getMessage());
                    }
                });
                if (!players.contains(room.getAdmin()))
                    room.getAdmin().getDos().writeChars("\n\n" + winners.toString() + "\n\n" + losers.toString());

            } else if (command[1].equals("ROOM")) {
                
            }
        }
    }

    private void remove() {
        if (command.length >= 3)
            GameService.rooms.get(command[1]).removePlayer(GameService.players.get(command[2]));

    }

    @SneakyThrows
    private void lost() {
        if (command.length == 2) {
            Player p1 = GameService.players.get(command[1]);
            p1.getDos().writeBytes("\n you LOST this round . you have : " + p1.getLives() + " lives\n");
        } else if (command.length == 3 && command[1].equals("LIFE"))
            GameService.players.get(command[2]).getDos().writeChars("\nyou lost ! you have no more lives . \n you can now use LEAVE command");
        else if (command.length == 3 && command[1].equals("CARD"))
            GameService.players.get(command[2]).getDos().writeChars("\nyou lost ! you have no more cards . \n you can now use LEAVE command");

    }

    @SneakyThrows
    private void won() {
        if (command.length >= 2) {
            Player p1 = GameService.players.get(command[1]);
            p1.getDos().writeBytes("\n you  Won  you have : " + p1.getLives() + " lives\n");
        }
    }


    @SneakyThrows
    private void entered() {
        if (command.length >= 4) {
            Room room = GameService.rooms.get(command[2]);
            GameService.players.get(command[1]).getDos().writeChars("\nYou Entered The Room : " + room.getName() + " The Admin is " + room.getAdmin().getName() + "\n" +
                    "\nYou Are Player Number " + command[3] + " Waiting For " + command[4] + " More Players");
        }
    }

    //TODO : send game information to the admin
    @SneakyThrows
    private void joined() {
        if (command.length >= 4) {
            Player jp = GameService.players.get(command[2]);
            Room room = GameService.rooms.get(command[1]);
            room.getPlayers().forEach(p -> {
                if (!p.equals(jp) && !p.equals(room.getAdmin())) {
                    try {
                        p.getDos().writeChars("\nPlayer " + jp.getName() + " Entered" +
                                " Waiting For " + command[3] + " More Players");
                    } catch (IOException e) {
                        log.error(e.getMessage());
                    }
                }
            });
            room.getAdmin().getDos().writeChars("\n\nPlayer " + jp.getName() + " With Id : " + jp.getId() + " Joined \nWaiting For " + command[3] + " More Players\n\n");
        }
    }

    @SneakyThrows
    private void full() {
        if (command.length == 3) {
            Player p = GameService.players.get(command[1]);
            p.getDos().writeChars("\n\nThe Room Is Full !!! \n");
            if (GameService.rooms.get(command[2]).getAdmin() != null)
                GameService.rooms.get(command[2]).getAdmin().getDos().writeChars("The Player " + p.getName() + " With Id : " + p.getId() + " Tried To Join But Room Is Full");
        }
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


    // Client to Server methods

    @SneakyThrows
    private void joinRoom() {
        if (command.length >= 2) {
            GameService.rooms.get(command[1]).addPlayer(player);
            log.info("player : " + player.getName() + " added to room : " + GameService.rooms.get(command[1]).getName());
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
