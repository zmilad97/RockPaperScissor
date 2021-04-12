package com.github.zmilad97.rps.service;

import com.github.zmilad97.rps.model.*;

import java.util.HashMap;
import java.util.Map;


public class GameService {

    public static Map<String, Room> rooms = new HashMap<>();
    public static Map<String, Player> players = new HashMap<>();
    public static Map<String, RoomDTO> roomDTOs = new HashMap<>();
    public static Map<Player, Room> playerRoomMap = new HashMap<>();
    public static Map<Player, Game> playerGameMap = new HashMap<>();
    public static Map<String, PlayerDTO> playerDTOS = new HashMap<>();
    public static Map<Player, UserService> playerUserServiceMap = new HashMap<>();

}
