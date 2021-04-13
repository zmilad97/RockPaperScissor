package com.github.zmilad97.rps.model;


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

    public String getGameId() {
        return gameId;
    }

    public String getRoomId() {
        return roomId;
    }

    public PlayerDTO getPlayer1() {
        return player1;
    }

    public PlayerDTO getPlayer2() {
        return player2;
    }

    public Map<PlayerDTO, String> getResult() {
        return result;
    }
}
