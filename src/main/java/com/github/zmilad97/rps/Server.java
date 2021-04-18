package com.github.zmilad97.rps;

import com.github.zmilad97.rps.model.Player;
import com.github.zmilad97.rps.model.PlayerDTO;
import com.github.zmilad97.rps.service.UserService;
import com.google.gson.Gson;
import com.github.zmilad97.rps.controller.WebStarter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import com.github.zmilad97.rps.service.PlayerService;
import com.github.zmilad97.rps.service.RoomService;

import java.net.ServerSocket;
import java.net.Socket;

@Slf4j
public class Server {
    private final String ip = "localhost";
    private final int port = 6060;
    private final RoomService roomService;
    private final PlayerService playerService;
    private final Gson gson;
    private final WebStarter webStarter;
    private final ServerSocket serverSocket;

    public static void main(String[] args) {
        Server server = new Server();
    }

    @SneakyThrows
    public Server() {
        serverSocket = new ServerSocket(port);
        gson = new Gson();
        playerService = new PlayerService();
        roomService = new RoomService(playerService);
        webStarter = new WebStarter(gson, playerService, roomService);

        log.info("Server Started : " + serverSocket.getLocalSocketAddress());
        while (!serverSocket.isClosed()) {
            log.debug("server socket open");
            Socket socket = serverSocket.accept();
            Player player = new Player(new PlayerDTO(""));
            player.setSocket(socket);
            log.debug("a socket joined");
            UserService userService = new UserService(player);
            userService.start();
//            playerService.setSocket(socket);
        }
    }
}
