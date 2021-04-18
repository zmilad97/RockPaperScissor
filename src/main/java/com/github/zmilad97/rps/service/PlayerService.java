package com.github.zmilad97.rps.service;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import com.github.zmilad97.rps.model.Player;
import com.github.zmilad97.rps.model.PlayerDTO;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;

@Slf4j
public class PlayerService {


    public Player findPlayer(String id) {
        return GameService.players.get(id);
    }

    public String createPlayer(String name) {
        PlayerDTO playerDTO = new PlayerDTO(name);
        Player player = new Player(playerDTO);
        playerDTO.setName(name);
        GameService.players.put(player.getId(), player);
        GameService.playerDTOS.put(playerDTO.getId(), playerDTO);
        return playerDTO.getId();
    }

}
