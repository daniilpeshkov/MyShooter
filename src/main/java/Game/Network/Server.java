package Game.Network;

import Game.Logic.Entity;
import Game.Logic.GameWorld;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
    private static final int SERVER_PORT = 58146;

    private static ArrayList<ClientHandler> clientList = new ArrayList<>();
    private static ServerSocket serverSocket;
    private static GameWorld gameWorld;

    private boolean isRunningSender = true;
    private boolean isRunningConnections = true;
    private boolean hasClients = false;

    public void terminate() {
        isRunningConnections = false;
        isRunningSender = false;

        clientList.forEach(clientHandler -> terminate());
    }

    public Server(GameWorld gameWorld) {
        Server.gameWorld = gameWorld;

        try {
            serverSocket = new ServerSocket(SERVER_PORT);

            System.out.println("Server Opened");
            System.out.println("Server Address: " + serverSocket.getLocalSocketAddress());

            new Connect().start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<ClientHandler> getClientList() {
        return clientList;
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

                    if (!hasClients) new MassSender().start();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    serverSocket.close();
                    isRunningConnections = false;
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

                    List<Entity> entities = gameWorld.getEntities();
                    int size = entities.size();
                    for (int i = 0; i < size; i++) {
                        Entity entity;
                        try {
                            entity = entities.get(i);
                        } catch (IndexOutOfBoundsException ex) {
                            break;
                        }
                        if (entity != null) {
                            buffer.add(entity.getCore());
                        }
                    }

                    for (ClientHandler clientHandler : clientList) {
                        clientHandler.writeCore(buffer);
                    }
                }
            }
        }
    }
}
