package model;

import controller.Protocols;

import java.util.HashMap;
import java.util.Map;

public class GameDTO {
    private final String gameId;
    private final String roomId;
    private final PlayerDTO player1;
    private final PlayerDTO player2;
    private final Map<PlayerDTO, String> result = new HashMap<>();
    private final Protocols protocols;

    public GameDTO(String gameId, String roomId, PlayerDTO player1, PlayerDTO player2, Protocols protocols) {
        this.gameId = gameId;
        this.roomId = roomId;
        this.player1 = player1;
        this.player2 = player2;
        this.protocols = protocols;
    }
}
