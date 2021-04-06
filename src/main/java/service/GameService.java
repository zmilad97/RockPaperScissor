package service;

import model.Player;
import model.Room;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class GameService {

    public static Map<String, Room> openRooms = new HashMap<>();
    public static Map<Room, Player> roomPlayerMap = new HashMap<>();
    public static List<Player> players = new ArrayList<>();

    public static Room findRoom(String id) {
        return GameService.openRooms.get(id);
    }


}
