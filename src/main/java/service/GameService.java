package service;

import model.Player;
import model.PlayerDTO;
import model.Room;
import model.RoomDTO;

import java.util.HashMap;
import java.util.Map;


public class GameService {

    public static Map<String, Room> rooms = new HashMap<>();
    public static Map<String, Player> players = new HashMap<>();
    public static Map<String , RoomDTO> roomDTOs = new HashMap<>();
    public static Map<String, PlayerDTO> playerDTOS = new HashMap<>();

    public static Map<Room, Player> roomPlayerMap = new HashMap<>();

    public static Room findRoom(String id) {
        return GameService.rooms.get(id);
    }


}
