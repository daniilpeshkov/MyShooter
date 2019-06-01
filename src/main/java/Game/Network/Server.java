package Game.Network;

import Game.Logic.GameWorld;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
    static final int SERVER_PORT = 58146;

    public static ArrayList<ClientHandler> clientList = new ArrayList<>();

    public static void start(GameWorld gameWorld) throws IOException {
        ServerSocket serverSocket = new ServerSocket(SERVER_PORT);

        System.out.println("Server Started");
        System.out.println("Server LocalPort: " + serverSocket.getLocalPort());
        System.out.println("Server Address: " + serverSocket.getInetAddress());

        try {
            while (true) {
                System.out.println("Waiting for connections...");
                Socket socket = serverSocket.accept();
                System.out.println("Connection accepted");
                clientList.add(new ClientHandler(socket, gameWorld));
            }
        } finally {
            serverSocket.close();
        }
    }
}
