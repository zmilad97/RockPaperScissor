package controller;

import lombok.SneakyThrows;
import model.Player;
import service.GameService;

public class Protocols {
    private String[] command;
    private final Player player;

    public Protocols(Player player) {
        this.player = player;
    }


    public String parseCommand(String request) {
        command = request.split(" ");
        switch (command[0]) {
            case "JOIN" -> joinRoom();
            case "START" -> startGame();
            case "HAND" -> hand();
            case "LEAVE" -> leave();
            case "EXIT" -> exit();
        }
        return null;
    }

    @SneakyThrows
    private void joinRoom() {
        GameService.rooms.get(command[1]).addPlayer(player);
    }

    private void leave() {
    }

    private void exit() {
    }

    private void hand() {

    }

    private void startGame() {
    }

}
