package com.github.zmilad97.rps.controller;

import com.github.zmilad97.rps.service.PlayerService;
import com.github.zmilad97.rps.service.UserService;
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
    private UserService userService;

    public Protocols(Player player, UserService userService) {
        this.player = player;
        this.userService = userService;
    }

    public Protocols() {

    }


    public String parseCommand(String request) {
        if (request != null) {
            command = request.split(" ");
            switch (command[0].toUpperCase()) {

                // Client to server commands
                case "HELP" -> help();

                case "NAME" -> name();

                case "JOIN" -> joinRoom();

                case "START" -> startGame();

                case "HAND" -> hand();

                case "REMOVE" -> remove();

                case "CARDS" -> {
                    return cards();
                }

                case "LEAVE" -> leave();

                case "EXIT" -> {
                    return exit();
                }


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

    private void name() {
        if (player != null && player.getName().equals(""))
            if (command.length != 0) {
                PlayerService playerService = new PlayerService();
                String id = playerService.createPlayer(command[1]);
                Player player = new Player(GameService.playerDTOS.get(id));
                player.setSocket(this.player.getSocket());
                userService.setPlayer(player);
            }
    }

    @SneakyThrows
    private void end() {
        if (command.length >= 3) {
            if (command[1].equalsIgnoreCase("ROUND")) {
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
                    winners.append("  ");
                    losers.append("  ");
                });
                winners.append("\n");
                losers.append("\n");
                List<Player> players = room.getPlayers();
                players.forEach(p -> {
                    try {
                        if (!p.getId().equals(room.getAdmin().getId()))
                            p.getDos().writeChars("\n\n" + winners.toString() + "\n\n" + losers.toString());
                    } catch (IOException e) {
                        log.error(e.getMessage());
                    }
                });
                GameService.players.get(room.getAdmin().getId()).getDos().writeChars("\n\n" + winners.toString() + "\n\n" + losers.toString());

            } else if (command[1].equalsIgnoreCase("ROOM")) {
                List<Player> winners = GameService.rooms.get(command[2]).getRoomWinners();
                GameService.rooms.get(command[2]).getPlayers().forEach(p -> {
                    try {
                        p.getDos().writeChars("\nThe Room Ended ! You can join another room\n\n   The Winners Are : ||  ");

                        winners.forEach(w -> {
                            try {
                                p.getDos().writeChars(w.getName() + "  ||  ");
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });
                        p.getDos().writeChars(" \n");

                    } catch (IOException e) {
                        log.error(e.getMessage());
                    }
                    GameService.playerRoomMap.remove(p);
                });
                GameService.rooms.remove(command[2]);
                GameService.roomDTOs.remove(command[2]);
            }
        }
    }

    @SneakyThrows
    private void help() {
        if (command.length == 1)
            player.getDos().writeChars("""

                     Use commands without brackets\s

                     JOIN [room id]: joins a room 
                     HAND [first letter of a card]: plays a card 
                     HELP ADMIN : shows admin commands 
                     LEAVE : leaves the room 
                     EXIT : exits the game
                                                                                
                    """);
        else if (command[1].equalsIgnoreCase("ADMIN"))
            player.getDos().writeChars("""


                       START [room id] :start next round 
                       REMOVE [room id] [player id]: removes a player from room
                       REMOVE BAN [room id] [player id] : removes and ban a player\s
                       
                    """);
    }

    private void remove() {
        if (command.length >= 3) {
            if (command[1].toUpperCase().equals("BAN")) {
                GameService.rooms.get(command[2]).banPlayers(GameService.players.get(command[3]));
                GameService.rooms.get(command[2]).removePlayer(GameService.players.get(command[3]));
            } else
                GameService.rooms.get(command[1]).removePlayer(GameService.players.get(command[2]));
        }
    }

    @SneakyThrows
    private void lost() {
        if (command.length == 2) {
            Player p1 = GameService.players.get(command[1]);
            p1.getDos().writeBytes("\n you LOST this round . you have : " + p1.getLives() + " lives\n");
        } else if (command.length == 3 && command[1].equals("LIFE"))
            GameService.players.get(command[2]).getDos().writeChars("\nyou lost ! you have no more lives . \n you can now use LEAVE command\n");
        else if (command.length == 3 && command[1].equals("CARD"))
            GameService.players.get(command[2]).getDos().writeChars("\nyou lost ! you have no more cards . \n you can now use LEAVE command\n");

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
                    "\nYou Are Player Number " + command[3] + " Waiting For " + command[4] + " More Players\n");
        }
    }

    @SneakyThrows
    private void joined() {
        if (command.length >= 4) {
            Player jp = GameService.players.get(command[2]);
            Room room = GameService.rooms.get(command[1]);
            room.getPlayers().forEach(p -> {
                if (!p.equals(jp) && !p.equals(room.getAdmin())) {
                    try {
                        p.getDos().writeChars("\nPlayer " + jp.getName() + " Entered" +
                                " Waiting For " + command[3] + " More Players\n");
                    } catch (IOException e) {
                        log.error("Player " + p + "Error " + e.getMessage());
                    }
                }
            });
            if (room.getAdmin() != null)
                if (GameService.players.get(room.getAdmin().getId()).getDos() != null)
                    GameService.players.get(room.getAdmin().getId()).getDos().writeChars("\nPlayer " + jp.getName() + " With Id : " + jp.getId() + " Joined \nWaiting For " + command[3] + " More Players\n\n");
        }
    }

    @SneakyThrows
    private void full() {
        if (command.length == 3) {
            Player p = GameService.players.get(command[1]);
            p.getDos().writeChars("\n\nThe Room Is Full !!! \n");
            if (GameService.rooms.get(command[2]).getAdmin() != null)
                GameService.players.get(GameService.rooms.get(command[2]).getAdmin().getId()).getDos().writeChars("The Player " + p.getName() + " With Id : " + p.getId() + " Tried To Join But Room Is Full \n");
        }
    }

    @SneakyThrows
    private void play() {
        if (command.length >= 2) {
            Player pl = GameService.players.get(command[1]);
            pl.getDos().writeChars("\n\nThe Game Started ! Be Ready ...\n\n You have | Rock : " + pl.getCardsCount().get("Rock") +
                    " | Paper : " + pl.getCardsCount().get("Paper") + " | Scissor : " + pl.getCardsCount().get("Scissor") + " | " +
                    "\n\n[R]ock,[P]aper,[S]cissors : ");
        }
    }

    @SneakyThrows
    private void rest() {
        if (command.length >= 2)
            GameService.players.get(command[1]).getDos().writeChars("\n\nYou Are On The Rest This Round ! Please Wait For Other Players \n");
    }

    private void stat() {
        if (command.length >= 2) {
            Room room = GameService.rooms.get(command[1]);
            Map<String, Integer> cards = GameService.rooms.get(command[1]).cardsCount();
            room.getPlayers().forEach(p -> {
                try {
                    p.getDos().writeChars("\n\nTotal Cards : | Rock = " + cards.get("Rock")
                            + " | Paper = " + cards.get("Paper") + " | Scissor = " + cards.get("Scissor") + " | \n");
                } catch (IOException e) {
                    log.error(e.getMessage());
                }
            });
        }
    }


    // Client to Server methods

    @SneakyThrows
    private void joinRoom() {
        if (command.length >= 2)
            if (GameService.rooms.get(command[1]) != null)
                GameService.rooms.get(command[1]).addPlayer(player);
    }

    private void startGame() {
        if (command.length >= 2) {
            if (GameService.rooms.get(command[1]) != null)
                if (!GameService.rooms.get(command[1]).isRoundStarted() && GameService.rooms.get(command[1]).getAdmin().getId().equals(player.getId()))
                    GameService.rooms.get(command[1]).startGame();
        }
    }

    private void hand() {
        if (command.length >= 2)
            if (GameService.playerGameMap.get(player) != null)
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

    @SneakyThrows
    private void leave() {
        Room room = GameService.playerRoomMap.get(player);
        room.getPlayers().remove(player);
        player.getDos().writeChars("\nYou left the room\n");
        if (room.getAdmin() != null)
            if (room.getAdmin().getDos() != null)
                room.getAdmin().getDos().writeChars("\nPlayer " + player.getName() + " With Id : " + player.getId() + " Left The Room\n\n");
    }

    @SneakyThrows
    private String exit() {
        if (GameService.playerRoomMap.size() != 0)
            GameService.playerRoomMap.get(player).getPlayers().remove(player);
        if (GameService.playerRoomMap.size() != 0)
            GameService.playerRoomMap.remove(player);
        if (GameService.playerDTOS.size() != 0)
            GameService.playerDTOS.remove(player.getId());
        if (GameService.players.size() != 0)
            GameService.players.remove(player.getId());
        return "exit";
    }

}
