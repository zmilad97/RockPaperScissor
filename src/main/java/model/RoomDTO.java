package model;

import org.apache.commons.lang3.RandomStringUtils;
import service.GameService;

import java.util.List;
import java.util.Map;

public class RoomDTO {
    private final String id;
    private final String name;
    private final PlayerDTO admin;
    private final boolean isPublic;
    private final int playerCount;
    private List<GameDTO> games;
    private List<PlayerDTO> players;
    private final Map<String, Integer> cardsCount;

    public RoomDTO(String name, boolean isPublic, int playerCount, Map<String, Integer> cardsCount, String id) {
        this.id = RandomStringUtils.random(5, true, true);
        this.name = name;
        this.isPublic = isPublic;
        this.playerCount = playerCount;
        this.cardsCount = cardsCount;
        admin = GameService.playerDTOS.get(id);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public PlayerDTO getAdmin() {
        return admin;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public int getPlayerCount() {
        return playerCount;
    }

    public List<GameDTO> getGames() {
        return games;
    }

    public void setGames(List<GameDTO> games) {
        this.games = games;
    }

    public List<PlayerDTO> getPlayers() {
        return players;
    }

    public void setPlayers(List<PlayerDTO> players) {
        this.players = players;
    }

    public Map<String, Integer> getCardsCount() {
        return cardsCount;
    }
}
