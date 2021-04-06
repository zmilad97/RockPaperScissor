package com.github.zmilad97.rps;

import com.github.zmilad97.rps.controller.RoomController;
import com.google.gson.Gson;
import com.github.zmilad97.rps.model.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.github.zmilad97.rps.service.PlayerService;
import com.github.zmilad97.rps.service.RoomService;
import com.github.zmilad97.rps.service.UserService;

import java.net.ServerSocket;
import java.net.Socket;

import static spark.Spark.*;

public class Server {
    private final String ip = "localhost";
    private final int port = 6060;
    private final RoomService roomService;
    private final RoomController roomController;
    private static final Logger LOGGER = LoggerFactory.getLogger(Server.class);

    public static void main(String[] args) throws Exception {
        Server server = new Server();
    }

    public Server() throws Exception {
        LOGGER.debug("alaki");
        /**
         * Injections
         */
        PlayerService playerService = new PlayerService();
        Gson gson = new Gson();
        roomService = new RoomService(playerService);
        roomController = new RoomController(playerService, roomService, gson);

        /**
         * Route Mapping
         */
        path("/api", () -> {
            get("/hello", ((request, response) -> "hello World"));
            post("/login", (roomController::handleLogin));
            post("/room/create", (roomController::handleCreateRoom));
            post("/room/join/:roomId", (request, response) -> response.status());
        });


        ServerSocket serverSocket = new ServerSocket(port);

        while (!serverSocket.isClosed()) {
            Socket socket = serverSocket.accept();
            Player player = new Player();
            System.out.println("\ncom.github.zmilad97.rps.model.Player (" + (player.getSocket().getLocalAddress().toString()).substring(1) + ":"
                    + socket.getLocalPort() + ") has joined ...");
            UserService userService = new UserService(player);
            userService.start();
        }
    }


}
