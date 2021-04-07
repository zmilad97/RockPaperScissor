import com.google.gson.Gson;
import com.google.inject.Guice;
import com.google.inject.Injector;
import model.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.PlayerService;
import service.RoomService;
import service.UserService;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

import static spark.Spark.*;

public class Server {
    private final String ip = "localhost";
    private final int port = 6060;
    private final RoomService roomService;
    private final PlayerService playerService;
    private final Gson gson;
    private static final Logger LOGGER = LoggerFactory.getLogger(Server.class);

    public static void main(String[] args) throws Exception {
        Server server = new Server();
    }

    public Server() throws Exception {
        /**
         * Injections
         */
        Injector injector = Guice.createInjector();
        roomService = injector.getInstance(RoomService.class);
        playerService = injector.getInstance(PlayerService.class);
        gson = injector.getInstance(Gson.class);

        /**
         * Route Mapping
         */



        ServerSocket serverSocket = new ServerSocket(port);

        while (!serverSocket.isClosed()) {
            Socket socket = serverSocket.accept();
            Player player = new Player();
            System.out.println("\nmodel.Player (" + (player.getSocket().getLocalAddress().toString()).substring(1) + ":"
                    + socket.getLocalPort() + ") has joined ...");
            UserService userService = new UserService(player);
            userService.start();
        }
    }
}
