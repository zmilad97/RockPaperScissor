import com.google.gson.Gson;
import controller.WebStarter;
import lombok.extern.slf4j.Slf4j;
import model.Player;
import service.PlayerService;
import service.RoomService;
import service.UserService;

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

    public static void main(String[] args) throws Exception {
        Server server = new Server();
    }

    public Server() throws Exception {
        serverSocket = new ServerSocket(port);
        gson = new Gson();
        playerService = new PlayerService();
        roomService = new RoomService(playerService);
        webStarter = new WebStarter(gson, playerService, roomService);

        while (!serverSocket.isClosed()) {
            Server.log.info("Server Started : " + serverSocket.getLocalSocketAddress());
            Socket socket = serverSocket.accept();
            playerService.setSocket(socket);
        }
    }
}
