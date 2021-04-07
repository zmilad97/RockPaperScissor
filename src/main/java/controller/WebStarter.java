package controller;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
            get("/room/all", roomController::allRoom);
            post("/room/join/:roomId", (request, response) -> response.status());

        });

    }
}
