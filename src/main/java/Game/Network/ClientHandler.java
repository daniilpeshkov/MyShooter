package Game.Network;

import Game.Logic.GameWorld;
import Game.Logic.Player;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

public class ClientHandler extends Thread {
    private static Socket socket;
    private static InputStream in;
    private static OutputStream out;
    private static Player player = new Player(0, 0, 1f, 5, 2 );
    private static boolean isRunningListener = true;
    private static boolean isRunningSender = true;
    private static ArrayList<byte[]> buffer;
    private static boolean hasClientId = false;

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

                    player.move(bytes[0]);
                    player.setFi(BitsFormatHandler.readFloatBits(bytes, 1));
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
