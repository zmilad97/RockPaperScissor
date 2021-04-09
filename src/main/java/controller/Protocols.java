package controller;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import model.Player;
import service.GameService;

@Slf4j
public class Protocols {
    private String[] command;
    private final Player player;

    public Protocols(Player player) {
        this.player = player;
    }


    public String parseCommand(String request) {
        if (request != null) {
            command = request.split(" ");
            switch (command[0]) {
                case "JOIN" -> {
                    return joinRoom();
                }
                case "START" -> {
                    return startGame();
                }
                case "HAND" -> {
                    return hand();
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
            log.info("player : " + player.getName() + " added to room : " + GameService.rooms.get(command[1]));
        }
        return "null";
    }

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

    private String hand() {

        return null;
    }

    private String startGame() {
        return null;
    }

}
