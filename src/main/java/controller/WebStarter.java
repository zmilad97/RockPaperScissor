package controller;

import com.google.gson.Gson;
import service.PlayerService;
import service.RoomService;

import java.util.HashMap;

import static spark.Spark.*;
import static spark.Spark.post;

public class WebStarter {
    private final RoomController roomController;

    public WebStarter(Gson gson, PlayerService playerService, RoomService roomService) {
        roomController = new RoomController(gson, roomService, playerService);

        path("/api", () -> {
            get("/hello", ((request, response) -> "hello World"));
            post("/login", (roomController::handleLogin));
            post("/room/create", (roomController::handleCreateRoom));
            post("/room/join/:roomId", (request, response) -> response.status());

        });

    }
}
