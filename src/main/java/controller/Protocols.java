package controller;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import model.Player;
import service.GameService;

import java.io.IOException;

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
                /**
                 * Client to server commands
                 */
                case "JOIN" -> joinRoom();

                case "ADMIN" -> roomAdmin();

                case "START" -> startGame();

                case "HAND" -> hand();

                case "CARDS" -> {
                    return cards();
                }
                case "LEAVE" -> leave();

                case "EXIT" -> exit();

                /**
                 * Server to client commands
                 */
                case "ENTERED" -> entered();

                case "JOINED" -> joined();

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

    private void play() {
    }

    private void rest() {
    }

    private void stat() {
    }

    private void endRoom() {
    }

    private void endRound() {
    }

    private void endGame() {
    }

    /**
     * Client to Server methods
     */
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
