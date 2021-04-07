package service;


import model.Room;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class RoomService {
    private final PlayerService playerService;
    private static final Logger LOGGER = LoggerFactory.getLogger(RoomService.class);

    public RoomService(PlayerService playerService) {
        this.playerService = playerService;
    }

    public String CreateRoom(HashMap<String, Object> body) {
        Room room = new Room();
        room.setId();
        room.setName((String) body.get("name"));
        room.setAdmin(playerService.findPlayer((body.get("admin")).toString().substring(4, 12)));
        room.setGames(new ArrayList<>());
        room.setPlayers(new ArrayList<>());
        room.setPublic((Boolean) body.get("isPublic"));
        double count = (double) body.get("playerCount");
        room.setPlayerCount((int) count);
        GameService.openRooms.put(room.getId(), room);
        LOGGER.info("The room with id : " + room.getId() + " Created by player with id : " + room.getAdmin().getId());
        return room.getId();
    }

    public List<String> allRooms() {
        List<String> roomList = new ArrayList<>();
        GameService.openRooms.forEach((k, v) -> roomList.add(roomList.size()+1 + " - Room : (" + v.getName() + ") Made by : (" + v.getAdmin().getName() + ")"));
        return roomList;
    }

    public void playerJoin(String roomId, String playerId) {
        try {
//            playerService.findPlayer(playerId).setSocket(socket);
            GameService.findRoom(roomId).addPlayer(playerService.findPlayer(playerId));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
