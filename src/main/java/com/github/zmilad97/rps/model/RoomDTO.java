package com.github.zmilad97.rps.model;

import org.apache.commons.lang3.RandomStringUtils;

import java.io.IOException;
import java.util.*;

public class RoomDTO {
    private String id;
    private String name;
    private PlayerDTO admin;
    private boolean isPublic;
    private int playerCount;
    private List<PlayerDTO> players;
    private List<GameDTO> games;

    public String getId() {
        return id;
    }

    public RoomDTO setId(String id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public RoomDTO setName(String name) {
        this.name = name;
        return this;
    }

    public PlayerDTO getAdmin() {
        return admin;
    }

    public RoomDTO setAdmin(PlayerDTO admin) {
        this.admin = admin;
        return this;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public RoomDTO setPublic(boolean aPublic) {
        isPublic = aPublic;
        return this;
    }

    public int getPlayerCount() {
        return playerCount;
    }

    public RoomDTO setPlayerCount(int playerCount) {
        this.playerCount = playerCount;
        return this;
    }

    public List<PlayerDTO> getPlayers() {
        return players;
    }

    public RoomDTO setPlayers(List<PlayerDTO> players) {
        this.players = players;
        return this;
    }

    public List<GameDTO> getGames() {
        return games;
    }

    public RoomDTO setGames(List<GameDTO> games) {
        this.games = games;
        return this;
    }
}
