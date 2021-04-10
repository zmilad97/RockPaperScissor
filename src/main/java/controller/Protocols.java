package controller;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import model.Player;
import service.GameService;

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
                case "JOIN" -> {
                    return joinRoom();
                }
                case "ADMIN" -> {
                    return roomAdmin();
                }
                case "START" -> {
                    return startGame();
                }
                case "HAND" -> {
                    return hand();
                }
                case "CARDS" -> {
                    return cards();
                }
                case "LEAVE" -> {
                    return leave();
                }
                case "EXIT" -> {
                    return exit();
                }

            }
        }
        return null;
    }

    @SneakyThrows
    private String joinRoom() {
        if (command.length >= 2) {
            GameService.rooms.get(command[1]).addPlayer(player);
            log.info("player : " + player.getName() + " added to room : " + GameService.rooms.get(command[1]).getName());
        }
        return "null";
    }

    private String roomAdmin() {
        if (command.length >= 2) {
            if (GameService.rooms.get(command[1]).getAdmin().equals(player))
                return "ADMIN"; //TODO : redirect to admin panel
        }
        return null;
    }

    private String startGame() {
        if (command.length >= 3) {
            if (GameService.rooms.get(command[1]).getAdmin().getId().equals(command[3]))
                GameService.rooms.get(command[1]).startGame();
            return command[0];
        }
        return null;
    }

    private String hand() {
        if (command.length >= 2) {
            return command[1];
        }
        return null;
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

    //TODO : fix here

    private String leave() {
        GameService.playerRoomMap.get(player).getPlayers().remove(player);
        return null;
    }

    @SneakyThrows
    private String exit() {
        GameService.playerRoomMap.get(player).getPlayers().remove(player);
        GameService.playerRoomMap.remove(player);
        GameService.playerDTOS.remove(player.getId());
        GameService.players.remove(player.getId());
        player.getSocket().close();
        return null;
    }

}
