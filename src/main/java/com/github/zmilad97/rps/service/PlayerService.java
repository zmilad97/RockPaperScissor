package com.github.zmilad97.rps.service;

import com.github.zmilad97.rps.model.Player;
import com.github.zmilad97.rps.model.PlayerDTO;


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
