package service;

import model.Game;
import model.Player;
import model.Room;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class GameService {

    public static Map<String, Room> openRooms = new HashMap<>();
    public static Map<Room,Player> roomPlayerMap = new HashMap<>();
    public static Map<Game, Map<Player, String>> gamesResult = new HashMap<>();


    private static Map<Socket, String> answers = new HashMap<>();
    public static List<Player> players = new ArrayList<>();
    private static ArrayList<String> res = new ArrayList();

    public static Map<Socket, Map<String, Integer>> cardsCount = new HashMap<>();

    private static DataOutputStream dos1;
    private static DataOutputStream dos2;

    public static Room findRoom(String id) {
        return GameService.openRooms.get(id);
    }


    public static void addAnswer(Socket socket, String rps) throws IOException {
        answers.put(socket, rps);
        res.add(rps);
        if (answers.size() >= 2) {
            dos1 = new DataOutputStream(players.get(0).getSocket().getOutputStream());
            dos2 = new DataOutputStream(players.get(1).getSocket().getOutputStream());
            checkWin();
            answers.clear();
            res.clear();
        }
    }

    private static void checkWin() {

        String player1 = answers.get(players.get(0).getSocket());
        Map<String, Integer> cards1 = cardsCount.get(players.get(0).getSocket());
        cards1.put(player1, cards1.get(player1) - 1);
        cardsCount.put(players.get(0).getSocket(), cards1);

        String player2 = answers.get(players.get(1).getSocket());
        Map<String, Integer> cards2 = cardsCount.get(players.get(1).getSocket());
        cards2.put(player2, cards2.get(player2) - 1);
        cardsCount.put(players.get(1).getSocket(), cards2);

        String client1_res = "";
        String client2_res = "";

        if (player1.equals(player2)) {
            client1_res = "draw";
            client2_res = "draw";
            System.out.println("It is a draw");
        }

        if (player1.equals("s") && player2.equals("p")) {
            client1_res = "you won";
            client2_res = "you lost";
            System.out.println("player 1 won");
        }

        if (player1.equals("p") && player2.equals("s")) {
            client1_res = "you lost";
            client2_res = "you won";
            System.out.println("player 2 won");
        }

        if (player1.equals("r") && player2.equals("p")) {
            client1_res = "you lost";
            client2_res = "you won";
            System.out.println("player 2 won");
        }

        if (player1.equals("p") && player2.equals("r")) {
            client1_res = "you won";
            client2_res = "you lost";
            System.out.println("player 1 won");
        }

        if (player1.equals("s") && player2.equals("r")) {
            client1_res = "you lost";
            client2_res = "you won";
            System.out.println("player 2 won");
        }

        if (player1.equals("r") && player2.equals("s")) {
            client1_res = "you won";
            client2_res = "you lost";
            System.out.println("player 1 won");
        }

        try {
            dos1.writeBytes("\n" + client1_res.toUpperCase() + "\n");
            dos2.writeBytes("\n" + client2_res.toUpperCase() + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }


        System.out.println("player " + players.get(0).getSocket().getLocalAddress().toString().substring(2) + "cards = " + cardsCount.get(players.get(0)));
        System.out.println("player " + players.get(1).getSocket().getLocalAddress().toString().substring(2) + "cards = " + cardsCount.get(players.get(1)));


    }
}
