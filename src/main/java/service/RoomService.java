package service;


import model.Room;
import model.RoomDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class RoomService {
    private final PlayerService playerService;
    private static final Logger LOGGER = LoggerFactory.getLogger(RoomService.class);

    public RoomService(PlayerService playerService) {
        this.playerService = playerService;
    }

    public String createRoom(RoomDTO request) {
        RoomDTO roomDTO = new RoomDTO(request.getName(), request.isPublic(), request.getPlayerCount(), request.getCardsCount(), request.getAdmin().getId());
        Room room = new Room(roomDTO);
        GameService.rooms.put(room.getId(), room);
        GameService.roomDTOs.put(roomDTO.getId(), roomDTO);
        LOGGER.info("The room with id : " + room.getId() + " Created by player with id : " + room.getAdmin().getId());
        return room.getId();
    }

    public List<String> allRooms() {
        List<String> roomList = new ArrayList<>();
        GameService.rooms.forEach((k, v) -> roomList.add(roomList.size() + 1 + " - Room : (" + v.getName() + ") Made by : (" + v.getAdmin().getName() + ")"));
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
