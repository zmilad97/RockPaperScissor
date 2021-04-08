package service;

import model.Player;
import model.PlayerDTO;

import java.net.Socket;
import java.util.List;

public class PlayerService {


    public Player findPlayer(String id) {
        List<Player> players = GameService.players;
        return players.stream().filter(player ->
                player.getId().equals(id)).findFirst().orElse(null);
    }

    public String createPlayer(String name) {
        PlayerDTO player = new PlayerDTO(name);
        player.setName(name);
        GameService.playerDTOS.add(player);
        return player.getId();
    }

    public void setSocket(Socket socket) {
        Player player = new Player();
        player.setSocket(socket);
        UserService userService = new UserService(player);
        userService.start();
    }

}
