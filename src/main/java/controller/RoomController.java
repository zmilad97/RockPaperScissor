package controller;

import com.google.gson.Gson;
import service.PlayerService;
import service.RoomService;
import spark.Request;
import spark.Response;

import java.util.HashMap;

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
        return "Hello " + name + " your id is : " + playerId + "\n Join a room with room id or\n Select from available rooms ";
    }

    public String handleCreateRoom(Request request, Response response) {
        return "if you want to play too use join link and this id :" + roomService.CreateRoom(gson.fromJson(request.body(), HashMap.class));
    }
}
