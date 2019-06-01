package Game.Network;

import Game.Logic.GameWorld;
import Game.Logic.Player;

import java.io.*;
import java.net.Socket;

public class ClientHandler extends Thread {
    private Socket socket;
    private InputStream in;
    private OutputStream out;
    private static Player player = new Player(5, 1, 1f, 5, Main.Main.getPlayer_tex());

    private byte[] bytes;

    public ClientHandler(Socket socket, GameWorld gameWorld) throws IOException {
        this.socket = socket;

        in = socket.getInputStream();
        out = socket.getOutputStream();

        gameWorld.addEntity(player);

        start();
    }

    @Override
    public void run() {
        try {
            in.read(bytes);

            player.move(bytes[0]);
            player.setDeciFi(bytes[1]);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void stopService() {
        try {
            if (!socket.isClosed()) {
                socket.close();
                in.close();
                out.close();

                for (ClientHandler client : Server.clientList) {
                    if (client.equals(this))
                        client.interrupt();
                    Server.clientList.remove(this);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
