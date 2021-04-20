package com.github.zmilad97.rps.service;


import com.github.zmilad97.rps.model.Room;
import com.github.zmilad97.rps.model.RoomDTO;

import java.util.ArrayList;
import java.util.List;


public class RoomService {
    private final PlayerService playerService;

    public RoomService(PlayerService playerService) {
        this.playerService = playerService;
    }

    public String createRoom(RoomDTO request) {
        RoomDTO roomDTO = new RoomDTO(request.getName(), request.isPublic(), request.getPlayerCount(), request.getCardsCount(), request.getAdmin().getId(), request.isAutoAdmin());
        Room room = new Room(roomDTO);
        GameService.rooms.put(room.getId(), room);
        GameService.roomDTOs.put(roomDTO.getId(), roomDTO);
        return room.getId();
    }

    public List<String> allRooms() {
        List<String> roomList = new ArrayList<>();
        GameService.rooms.forEach((k, v) -> roomList.add(roomList.size() + 1 + " - Room : (" + v.getName() + ") Made by : (" + v.getAdmin().getName() + ")"));
        return roomList;
    }

    public void playerJoin(String roomId, String playerId) {
        GameService.rooms.get(roomId).addPlayer(playerService.findPlayer(playerId));
    }
}
