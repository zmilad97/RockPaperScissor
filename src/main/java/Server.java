import com.google.inject.Guice;
import com.google.inject.Injector;
import model.Player;
import service.PlayerService;
import service.RoomService;
import service.UserService;

import java.net.ServerSocket;
import java.net.Socket;

import static spark.Spark.*;

//TODO : protobuf xolstice.maven | google guice | drop wizard or SparkJava.com library for rest |

public class Server {
    private final String ip = "localhost";
    private final int port = 6060;
    private final RoomService roomService;
    private final PlayerService playerService;

    public static void main(String[] args) throws Exception {
        Server server = new Server();
    }

    public Server() throws Exception {
        Injector injector = Guice.createInjector();
        roomService = injector.getInstance(RoomService.class);
        playerService = injector.getInstance(PlayerService.class);
        path("/api", () -> {
            get("/hello", ((request, response) -> "hello World"));
            post("/login",((request, response) -> {

                return "Hello "+request.queryParams("name");
            }));
            post("/room/join/:roomId", (request, response) -> {
                return response.status();
            });
            post("/room/create", ((request, response) -> {
                return response.status();
            }));
        });

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
