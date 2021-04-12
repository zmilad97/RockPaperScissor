package com.github.zmilad97.rps.controller;

import com.google.gson.Gson;
import com.github.zmilad97.rps.model.RoomDTO;
import com.github.zmilad97.rps.service.PlayerService;
import com.github.zmilad97.rps.service.RoomService;
import spark.Request;
import spark.Response;

import java.util.HashMap;
import java.util.List;

public class RoomController {
    private final Gson gson;
    private final RoomService roomService;
    private final PlayerService playerService;

    public RoomController(Gson gson, RoomService roomService, PlayerService playerService) {
        this.gson = gson;
        this.roomService = roomService;
        this.playerService = playerService;
    }

    public String handleLogin(Request request, Response response) {
        String name = gson.fromJson(request.body(), HashMap.class).get("name").toString();
        String playerId = playerService.createPlayer(name);
        response.body("Hello " + name + " your id is : " + playerId + "\n Join a room with room id or\n Select from available rooms ");
        return response.body();
    }

    public String handleCreateRoom(Request request, Response response) {
        return "room id : " + roomService.createRoom(gson.fromJson(request.body(), RoomDTO.class));
    }

    public List<String> allRoom(Request request, Response response) {
        return roomService.allRooms();
    }

    public String joinRoom(Request request, Response response) {
        roomService.playerJoin(request.params("roomId"), gson.fromJson(request.body(), HashMap.class).get("playerId").toString());
        return "you joined";
    }
}
