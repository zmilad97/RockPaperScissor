package controller;

public class Protocols {
    private String[] command;

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

    private void joinRoom() {
    }

    private void leave() {
    }

    private void exit() {
    }

    private void hand() {

    }

    private static void startGame() {

    }

}
