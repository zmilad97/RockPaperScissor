package com.github.zmilad97.rps.model;

import com.github.zmilad97.rps.service.GameService;
import org.apache.commons.lang3.RandomStringUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class GameDTO {

    private String gameId;
    private String roomId;
    private PlayerDTO player1;
    private PlayerDTO player2;
    private Map<PlayerDTO, String> result = new HashMap<>();

    public String getGameId() {
        return gameId;
    }

    public GameDTO setGameId(String gameId) {
        this.gameId = gameId;
        return this;
    }

    public String getRoomId() {
        return roomId;
    }

    public GameDTO setRoomId(String roomId) {
        this.roomId = roomId;
        return this;
    }

    public PlayerDTO getPlayer1() {
        return player1;
    }

    public GameDTO setPlayer1(PlayerDTO player1) {
        this.player1 = player1;
        return this;
    }

    public PlayerDTO getPlayer2() {
        return player2;
    }

    public GameDTO setPlayer2(PlayerDTO player2) {
        this.player2 = player2;
        return this;
    }

    public Map<PlayerDTO, String> getResult() {
        return result;
    }

    public GameDTO setResult(Map<PlayerDTO, String> result) {
        this.result = result;
        return this;
    }
}
