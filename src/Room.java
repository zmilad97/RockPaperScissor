import java.io.IOException;
import java.util.*;

public class Room {
    private final String id;
    private String name;
    private boolean isPublic;
    private final int playerCount;
    private List<Player> players;
    private List<Game> games;


    public Room(boolean isPublic, int playerCount, String name) {
        this.isPublic = isPublic;
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.playerCount = playerCount;
        players = new ArrayList<>();
        games = new ArrayList<>();
    }

    public String getId() {
        return id;
    }


    public void addPlayer(Player player) throws IOException {
        if (this.players.size() != playerCount) {
            this.players.add(player);
            int needPlayer = playerCount - this.players.size();
            player.getDos().writeChars("\nYou Entered The Room : " + this.name + " Created By : " + this.getPlayers().get(0).getName() + "\n" +
                    "\nYou Are Player Number " + players.size() + " Waiting For " + needPlayer + " More Players");
            this.players.forEach(p -> {
                try {
                    if (!p.equals(player))
                        p.getDos().writeChars("\nPlayer Number " + this.players.size() + " Entered" +
                                " Waiting For " + needPlayer + " More Players");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            if (this.players.size() == playerCount)
                startGame();

        } else {
            player.getDos().writeChars("\nThe Room Is Full");
        }
    }


    private void startGame() {
        boolean win = false;
        players.forEach(p -> {
            try {
                p.getDos().writeChars("\nThe Game Started ! Be Ready ...\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        while (!win) {
            Map<String, Integer> cards = cardsCount();
            if (players.size() > 1) {
                players.forEach(p -> {
                    try {
                        p.getDos().writeChars("Total Cards : Rock = " + cards.get("Rock")
                                + " | Paper = " + cards.get("Paper") + " | Scissor = " + cards.get("Scissor"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            } else {
                win = true;
            }
            /**
             * here matched players enter for the first game
             */
            Map<Player, Player> matchedPlayers = matchingPlayers();
            matchedPlayers.forEach((k, v) -> {
                if (!k.equals(v)) {
                    Game game = new Game(id, k, v);
                    game.start();
                    games.add(game);
                } else {
                    try {
                        System.out.println(k.getName() + " : is on the rest this round");
                        k.getDos().writeChars("you are on rest on this round");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });


            players.forEach(p -> {
                if (p.getLives() == 0) {
                    players.remove(p);
                    try {
                        p.getDos().writeChars("you lost ! you have no more lives .");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if (p.getCardsCount().get("Rock") == 0 &&
                        p.getCardsCount().get("Paper") == 0 &&
                        p.getCardsCount().get("Scissor") == 0 &&
                        p.getLives() < 3) {
                    players.remove(p);
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
        List<Player> playerList = players;
        Map<Player, Player> playerMap = new HashMap<>();

        while (players.size() > 1) {
            Player randP1 = playerList.get(random.nextInt(playerList.size()));
            playerList.remove(randP1);
            Player randP2 = playerList.get(random.nextInt(playerList.size()));
            playerList.remove(randP2);
            playerMap.put(randP1, randP2);
        }
        if (players.size() == 1)
            playerMap.put(players.get(0), players.get(0));

        return playerMap;
    }


    public int getPlayerCount() {
        return playerCount;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public List<Game> getGames() {
        return games;
    }

    public void setGames(List<Game> games) {
        this.games = games;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }


}
