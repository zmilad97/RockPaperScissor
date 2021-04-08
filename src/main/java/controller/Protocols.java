package controller;

public class Protocols {
    public static String parseCommand(String request) {
        String[] command = request.split(" ");
        switch (command[0]) {
            case "JOIN" -> joinRoom();
            case "START" -> startGame();
            case "HAND" -> hand();
            case "EXIT" -> exit();
        }
        return null;
    }

    private static void exit() {
    }

    private static void hand() {

    }

    private static void startGame() {

    }

    private static void joinRoom() {

    }

}
