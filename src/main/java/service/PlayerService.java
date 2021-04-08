package service;

import model.Player;
import model.PlayerDTO;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class PlayerService {


    public Player findPlayer(String id) {
        List<Player> players = new ArrayList<>();
        GameService.players.forEach((k, v) -> players.add(v));
        return players.stream().filter(player ->
                player.getId().equals(id)).findFirst().orElse(null);
    }

    public String createPlayer(String name) {
        PlayerDTO playerDTO = new PlayerDTO(name);
        Player player = new Player(playerDTO);
        playerDTO.setName(name);
        GameService.players.put(player.getId(), player);
        GameService.playerDTOS.put(playerDTO.getId(), playerDTO);
        return playerDTO.getId();
    }

    //TODO : fix this method
    public void setSocket(Socket socket) {
        Player player = new Player();
        player.setSocket(socket);
        UserService userService = new UserService(player);
        userService.start();
    }

}
