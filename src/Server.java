import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
//TODO : protobuf | xolstice.maven | google guice | drop wizard or SparkJava.com library for rest |

public class Server {
    private final String ip = "localhost";
    private final int port = 6060;

    public static void main(String[] args) throws Exception {
        Server server = new Server();
    }

    public Server() throws Exception {
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("uuid : " + UUID.randomUUID().toString());

        System.out.println("   Server is up and running on " + serverSocket.getLocalSocketAddress());
        while (!serverSocket.isClosed()) {
            Socket socket = serverSocket.accept();
            Player player = new Player(socket);
            System.out.println("\nPlayer (" + (player.getSocket().getLocalAddress().toString()).substring(1) + ":"
                    + socket.getLocalPort() + ") has joined ...");
            UserService userService = new UserService(player);
            userService.start();
        }
    }
}
