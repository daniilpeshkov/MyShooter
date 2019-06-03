package Game.Network;

import Game.Logic.Bullet;
import Game.Logic.GameWorld;
import Game.Logic.Player;
import Game.Logic.RangedWeapon;
import org.joml.Vector2f;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

public class ClientHandler {
    private Socket socket;

    private InputStream in;
    private OutputStream out;

    private boolean isRunningReceiver = true;
    private boolean isRunningSender = true;

    private ArrayList<byte[]> buffer = new ArrayList<>();
    private GameWorld gameWorld;
    private Player player = new Player(0, 0, 1f, 5, 2);

    public void terminate() {
        isRunningReceiver = false;
        isRunningSender = false;
    }

    public ClientHandler(Socket socket, GameWorld gameWorld) throws IOException {
        this.socket = socket;
        this.gameWorld = gameWorld;

        in = socket.getInputStream();
        out = socket.getOutputStream();

        player.equipWeapon(new RangedWeapon(3, 0, 4, 400,
                new Bullet(0, 0, Bullet.RADIUS, 1, 4000, new Vector2f(), 1)));

        gameWorld.addEntity(player);

        new InputReceiver().start();
        new CoreSender().start();
    }

    public byte[] serviceCore(int mode) {
        byte[] mas = new byte[23];
        mas[22] = (byte) mode;
        return mas;
    }

    public void writeCore(ArrayList<byte[]> cores) {
        buffer = cores;
    }

    private void stopService() {
        try {
            if (!socket.isClosed()) {
                in.close();
                out.close();
                socket.close();
                Server.getClientList().remove(this);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class InputReceiver extends Thread {
        @Override
        public void run() {
            while (isRunningReceiver) {
                try {
                    byte[] bytes = new byte[6];

                    if (in.available() > 5) {
                        in.read(bytes);

                        player.updateDirection(bytes[0]);
                        player.setFi(BitsFormatHandler.readFloatBits(bytes, BitsFormatHandler.pFi));

                        if (bytes[5] == 1) player.shot(gameWorld);
                    }
                } catch (SocketException s) {
                    System.out.println("Connection is closed");
                    isRunningReceiver = false;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            ClientHandler.this.stopService();
        }
    }

    private class CoreSender extends Thread {
        @Override
        public void run() {
            while (isRunningSender) {
                try {
                    if (!buffer.isEmpty()) {
                        out.write(player.getCore());
                        for (byte[] i : buffer) {
                            out.write(i);
                        }
                        out.write(serviceCore(2));
                    }
                } catch (SocketException s) {
                    System.out.println("Connection is closed");
                    isRunningSender = false;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            ClientHandler.this.stopService();
        }
    }
}
