package Game.Network;

import Game.Logic.GameWorld;
import Game.Logic.Player;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

public class ClientHandler extends Thread {
    private Socket socket;
    private InputStream in;
    private OutputStream out;
    private Player player = new Player(0, 0, 1f, 5, 2 );
    private boolean isRunningListener = true;
    private boolean isRunningSender = true;
    private ArrayList<byte[]> buffer = new ArrayList<>();

    public ClientHandler(Socket socket, GameWorld gameWorld) throws IOException {
        this.socket = socket;

        in = socket.getInputStream();
        out = socket.getOutputStream();

        gameWorld.addEntity(player);

        new InputListener().start();
        new CoreSender().start();
    }

    private class InputListener extends Thread {
        @Override
        public void run() {
            byte[] bytes = new byte[5];

            while (isRunningListener) {
                try {
                    in.read(bytes);
                    player.updateDirection(bytes[0]);
                    player.setFi(BitsFormatHandler.readFloatBits(bytes, BitsFormatHandler.pFi));
                    System.out.println(player.getEntityId());

                } catch (SocketException s) {
                    System.out.println("Connection is closed");
                    isRunningListener = false;
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
                        System.out.println(buffer.size());
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
}
