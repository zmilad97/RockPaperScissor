package com.github.zmilad97.rps.service;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import com.github.zmilad97.rps.model.Player;
import com.github.zmilad97.rps.model.PlayerDTO;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

@Slf4j
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
    @SneakyThrows
    public void setSocket(Socket socket) {
        BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
        dos.writeChars("\n\nWelcome , Enter your id to continue : ");
        Player player = new Player(GameService.playerDTOS.get(br.readLine()));
        player.setSocket(socket);
        dos.writeChars("\n\nHi " + player.getName() + " if you want to see commands list use HELP command\n");
        UserService userService = new UserService(player);
        GameService.players.put(player.getId(), player);
        GameService.playerUserServiceMap.put(player, userService);
        userService.start();
    }

}
