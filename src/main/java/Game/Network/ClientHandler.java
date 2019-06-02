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
            while (isRunningListener) {
                try {
                    byte[] bytes = new byte[5];

                    if (in.available() > 4) {
                        in.read(bytes);

                        player.updateDirection(bytes[0]);
                        player.setFi(BitsFormatHandler.readFloatBits(bytes, BitsFormatHandler.pFi));
                    }
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
}
