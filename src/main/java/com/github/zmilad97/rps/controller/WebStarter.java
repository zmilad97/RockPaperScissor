package com.github.zmilad97.rps.controller;

import com.google.gson.Gson;
import com.github.zmilad97.rps.service.PlayerService;
import com.github.zmilad97.rps.service.RoomService;


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
            post("/room/join/:roomId", roomController::joinRoom);

        });

    }
}
