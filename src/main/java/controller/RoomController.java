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

    public Response handleLogin(Request request, Response response) {
        String name = gson.fromJson(request.body(), HashMap.class).get("name").toString();
        String playerId = playerService.createPlayer(name);
        response.body("Hello " + name + " your id is : " + playerId + "\n Join a room with room id or\n Select from available rooms ");
        return response;
    }

    public Response handleCreateRoom(Request request, Response response) {
        response.body("room id : " + roomService.CreateRoom(gson.fromJson(request.body(), HashMap.class)));
        return response;
    }

    public Response allRoom(Request request, Response response) {
        response.body(roomService.allRooms().toString());
        return response;
    }
}
