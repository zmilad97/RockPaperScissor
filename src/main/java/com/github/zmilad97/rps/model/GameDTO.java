package com.github.zmilad97.rps.model;

import com.github.zmilad97.rps.controller.Protocols;

import java.util.HashMap;
import java.util.Map;

public class GameDTO {
    private final String gameId;
    private final String roomId;
    private final PlayerDTO player1;
    private final PlayerDTO player2;
    private final Map<PlayerDTO, String> result = new HashMap<>();

    public GameDTO(String gameId, String roomId, PlayerDTO player1, PlayerDTO player2) {
        this.gameId = gameId;
        this.roomId = roomId;
        this.player1 = player1;
        this.player2 = player2;

    }
}
