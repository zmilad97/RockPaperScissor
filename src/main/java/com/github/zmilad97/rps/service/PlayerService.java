package com.github.zmilad97.rps.service;

import com.google.inject.Provides;
import com.github.zmilad97.rps.model.Player;

import java.util.List;

public class PlayerService {


    @Provides
    public Player findPlayer(String id) {
        List<Player> players = GameService.players;
        return players.stream().filter(player ->
                player.getId().equals(id)).findFirst().orElse(null);
    }

    @Provides
    public String createPlayer(String name) {
        Player player = new Player();
        player.setName(name);
        GameService.players.add(player);
        return player.getId();
    }
}
