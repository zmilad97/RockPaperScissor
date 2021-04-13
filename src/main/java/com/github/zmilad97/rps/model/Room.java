package com.github.zmilad97.rps.model;

import com.github.zmilad97.rps.controller.Protocols;
import lombok.SneakyThrows;
import com.github.zmilad97.rps.service.GameService;

import java.util.*;

public class Room {
    private final String id;
    private final String name;
    private final Player admin;
    private int playerCount;
    private final Protocols protocols;
    private final List<Game> games;
    private final List<Player> players;
    private final List<Player> bannedPlayers;
    private final Map<String, Integer> cardsCount;
    private final Map<Player, String> roundPlayerStatus;
    private final List<Player> winners;
    private boolean isEnded = false;
    private final boolean isPublic;
    private final boolean autoAdmin;
    int roundPlayerStatusSize = 0;

    public Room(RoomDTO roomDTO) {
        id = roomDTO.getId();
        name = roomDTO.getName();
        if (roomDTO.getAdmin() != null)
            admin = GameService.players.get(roomDTO.getAdmin().getId());
        else admin = null;
        isPublic = roomDTO.isPublic();
        playerCount = roomDTO.getPlayerCount();
        cardsCount = roomDTO.getCardsCount();
        autoAdmin = roomDTO.isAutoAdmin();
        winners = new ArrayList<>();
        players = new ArrayList<>();
        bannedPlayers = new ArrayList<>();
        games = new ArrayList<>();
        protocols = new Protocols();
        roundPlayerStatus = new HashMap<>();
    }


    @SneakyThrows
    public void addPlayer(Player player) {
        if (roundPlayerStatusSize == 0 && this.players.size() != playerCount && !bannedPlayers.contains(player)) {
            this.players.add(player);
            int needPlayer = playerCount - this.players.size();
            protocols.parseCommand("ENTERED " + player.getId() + " " + this.getId() + " " + players.size() + " " + needPlayer);
            GameService.playerRoomMap.put(player, GameService.rooms.get(id));
            player.setCardsCount(this.cardsCount.get("Rock"), this.cardsCount.get("Paper"), this.cardsCount.get("Scissor"));
            player.setLives(3);
            protocols.parseCommand("JOINED " + this.id + " " + player.getId() + " " + needPlayer);

        } else
            protocols.parseCommand("FULL " + player.getId() + " " + this.id);

    }

    //TODO : check the room ended or not before START
    public void startGame() {
        if (!isEnded) {
            protocols.parseCommand("STAT " + this.id);
            Map<Player, Player> matchedPlayers = matchingPlayers();
            matchedPlayers.forEach((k, v) -> {
                if (!k.equals(v)) {
                    Game game = new Game(id, k, v);
                    game.start();
                    GameService.playerGameMap.put(k, game);
                    GameService.playerGameMap.put(v, game);
                    games.add(game);
                } else
                    protocols.parseCommand("REST " + k.getId());
            });
            checkLosers();
        }
    }

    public Map<String, Integer> cardsCount() {
        Map<String, Integer> cards = new HashMap<>();
        cards.put("Rock", 0);
        cards.put("Paper", 0);
        cards.put("Scissor", 0);

        players.forEach(p -> {
            cards.put("Rock", cards.get("Rock") + p.getCardsCount().get("Rock"));
            cards.put("Paper", cards.get("Paper") + p.getCardsCount().get("Paper"));
            cards.put("Scissor", cards.get("Scissor") + p.getCardsCount().get("Scissor"));
        });
        return cards;
    }

    public Map<Player, Player> matchingPlayers() {
        roundPlayerStatusSize = 0;
        Random random = new Random();
        List<Player> playerList = new ArrayList<>();
        players.forEach(p -> {
            if (p.getLives() > 0 &&
                    !(p.getCardsCount().get("Rock") == 0 &&
                            p.getCardsCount().get("Paper") == 0 &&
                            p.getCardsCount().get("Scissor") == 0)) {
                playerList.add(p);
                roundPlayerStatusSize++;
            }
        });
        Map<Player, Player> playerMap = new HashMap<>();

        while (playerList.size() > 1) {
            Player randP1 = playerList.get(random.nextInt(playerList.size()));
            playerList.remove(randP1);
            Player randP2 = playerList.get(random.nextInt(playerList.size()));

            playerList.remove(randP2);
            playerMap.put(randP1, randP2);
        }
        if (playerList.size() == 1) {
            playerMap.put(playerList.get(0), playerList.get(0));
            roundPlayerStatusSize++;
        }
        return playerMap;
    }

    public void checkLosers() {
        players.forEach(p -> {
            if (p.getLives() == 0) {
                p.getCardsCount().clear();
                protocols.parseCommand("LOST LIFE " + p.getId());

            } else if (p.getCardsCount().get("Rock") == 0 &&
                    p.getCardsCount().get("Paper") == 0 &&
                    p.getCardsCount().get("Scissor") == 0 &&
                    p.getLives() < 3) {
                p.getCardsCount().clear();
                p.setLives(0);
                protocols.parseCommand("LOST CARD " + p.getId());
            }
        });

    }


    public String getId() {
        return id;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public String getName() {
        return name;
    }

    public Player getAdmin() {
        return admin;
    }

    public void removePlayer(Player player) {
        players.remove(player);
    }

    public boolean isAutoAdmin() {
        return autoAdmin;
    }

    public void addStatus(Player player, String status) {
        roundPlayerStatus.put(player, status);
        if (roundPlayerStatus.size() == roundPlayerStatusSize) {
            protocols.parseCommand("END ROUND " + id);
            players.forEach(p -> {
                if (p.getLives() >= 3 && p.getCardsCount().get("Rock") == 0 &&
                        p.getCardsCount().get("Paper") == 0 &&
                        p.getCardsCount().get("Scissor") == 0)
                    isEnded = true;
            });
            if (isEnded)
                protocols.parseCommand("END ROOM " + id);
        }
    }

    public Map<Player, String> getRoundPlayerStatus() {
        return roundPlayerStatus;
    }

    public void banPlayers(Player player) {
        bannedPlayers.add(player);
    }

    public int getRoundPlayerStatusSize() {
        return roundPlayerStatusSize;
    }

    public int getPlayerCount() {
        return playerCount;
    }

    public void setPlayerCount(int playerCount) {
        this.playerCount = playerCount;
    }

    public List<Player> getWinners() {
        return winners;
    }
}
