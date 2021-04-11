package model;

import controller.Protocols;
import lombok.SneakyThrows;
import service.GameService;

import java.io.IOException;
import java.util.*;

public class Room {
    private final String id;
    private final String name;
    private final Player admin;
    private final boolean isPublic;
    private final int playerCount;
    private final List<Game> games;
    private final List<Player> players;
    private final Map<String, Integer> cardsCount;
    //    private Map<String, Integer> cards;
    private final Protocols protocols;

    public Room(RoomDTO roomDTO) {
        this.id = roomDTO.getId();
        this.name = roomDTO.getName();
        this.admin = GameService.players.get(roomDTO.getAdmin().getId());
        this.isPublic = roomDTO.isPublic();
        this.playerCount = roomDTO.getPlayerCount();
        this.cardsCount = roomDTO.getCardsCount();
        players = new ArrayList<>();
        games = new ArrayList<>();
        protocols = new Protocols();
//        cards = new HashMap<>();
    }


    @SneakyThrows
    public void addPlayer(Player player) {
        if (this.players.size() != playerCount) {
            this.players.add(player);
            int needPlayer = playerCount - this.players.size();
            protocols.parseCommand("ENTERED " + player.getId() + " " + this.name + " " + players.size() + " " + needPlayer);

            if (player.getCardsCount().size() == 0)
                player.setCardsCount(this.cardsCount.get("Rock"), this.cardsCount.get("Paper"), this.cardsCount.get("Scissor"));

            protocols.parseCommand("JOINED " + this.id + " " + player.getId() + " " + needPlayer);

        } else {
            //TODO : use protocol
            player.getDos().writeChars("\nThe Room Is Full");
        }
    }


    public void startGame() {
        boolean win = false;
        //TODO : Use Protocol
        players.forEach(p -> {
            try {
                p.getDos().writeChars("\nThe Game Started ! Be Ready ...\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        if (players.size() == playerCount) {
//            cards = cardsCount();
            protocols.parseCommand("STAT " + this.id);
            if (players.size() > 1) {

            } else {
                win = true;
            }
            /**
             * here matched players enter the game
             */
            Map<Player, Player> matchedPlayers = matchingPlayers();
            matchedPlayers.forEach((k, v) -> {
                if (!k.equals(v)) {
                    Game game = new Game(id, k, v);
                    game.start();
                    GameService.playerGameMap.put(k, game);
                    GameService.playerGameMap.put(v, game);
                    games.add(game);
                } else {
                    try {
                        System.out.println(k.getName() + " : is on the rest this round");
                        k.getDos().writeChars("\nyou are on rest on this round");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });


            players.forEach(p -> {
                if (p.getLives() == 0) {
                    p.getCardsCount().clear();
                    try {
                        p.getDos().writeChars("\nyou lost ! you have no more lives .");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if (p.getCardsCount().get("Rock") == 0 &&
                        p.getCardsCount().get("Paper") == 0 &&
                        p.getCardsCount().get("Scissor") == 0 &&
                        p.getLives() < 3) {
                    p.getCardsCount().clear();
                    try {
                        p.getDos().writeChars("you lost ! you have no more cards .");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
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
        Random random = new Random();
        List<Player> playerList = new ArrayList<>();
        players.forEach(p -> {
            if (p.getLives() > 0 &&
                    !(p.getCardsCount().get("Rock") == 0 &&
                            p.getCardsCount().get("Paper") == 0 &&
                            p.getCardsCount().get("Scissor") == 0))
                playerList.add(p);
        });
        Map<Player, Player> playerMap = new HashMap<>();

        while (playerList.size() > 1) {
            Player randP1 = playerList.get(random.nextInt(playerList.size()));
            players.remove(randP1);
            playerList.remove(randP1);
            Player randP2 = playerList.get(random.nextInt(playerList.size()));
            players.remove(randP2);

            playerList.remove(randP2);
            playerMap.put(randP1, randP2);
        }
        if (playerList.size() == 1)
            playerMap.put(players.get(0), players.get(0));

        return playerMap;
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

}
