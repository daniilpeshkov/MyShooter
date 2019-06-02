package Game.Network;

import Game.Logic.Entity;
import Game.Logic.GameWorld;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
    static final int SERVER_PORT = 58146;

    private static ArrayList<ClientHandler> clientList = new ArrayList<>();
    private static ServerSocket serverSocket;
    private static GameWorld gameWorld;
    private boolean isRunningSender = true;
    private boolean isRunningConnections = true;

    public static ArrayList<ClientHandler> getClientList() {
        return clientList;
    }

    public Server(GameWorld gameWorld) {
        this.gameWorld = gameWorld;

        try {
            serverSocket = new ServerSocket(SERVER_PORT);

            System.out.println("Server Started");
            System.out.println("Server Address: " + serverSocket.getLocalSocketAddress());

            new Connect().start();
            new MassSender().start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class Connect extends Thread {
        @Override
        public void run() {
            try {
                while (isRunningConnections) {
                    System.out.println("Waiting for connections...");
                    Socket socket = Server.serverSocket.accept();
                    System.out.println("Connection accepted");
                    clientList.add(new ClientHandler(socket, gameWorld));
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private class MassSender extends Thread {
        @Override
        public void run() {
            while (isRunningSender) {
                if (!clientList.isEmpty()) {
                    ArrayList<byte[]> buffer = new ArrayList<>();

                    System.out.println(buffer.size());

                    for (Entity entity : gameWorld.getEntities()) {
                        buffer.add(entity.getCore());
                    }

                    for (ClientHandler clientHandler : clientList) {
                        clientHandler.writeCore(buffer);
                    }
                }
            }
        }
    }
}
