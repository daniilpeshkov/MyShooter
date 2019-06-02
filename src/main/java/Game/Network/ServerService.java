package Game.Network;

import Game.Logic.GameWorld;
import java.io.*;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketException;

public class ServerService {
    //    private int port;
    static final int port = 58146;
    private static BufferedReader input;
    private static InputStream in;
    private static OutputStream out;
    private static byte[] outputPack = new byte[4];
    private Socket socket;
    private String ip;
    private GameWorld downloadedWorld;

    private static boolean isRunning = true;

    public ServerService(String ip, int port) {
        this.ip = ip;
//        this.port = port;

        try {
            System.out.println("Trying to connect...");
            this.socket = new Socket(ip, this.port);
            System.out.println("Connection established");

            try {
                input = new BufferedReader(new InputStreamReader(System.in));
                in = socket.getInputStream();
                out = socket.getOutputStream();

                new NudesHandler().start();
                new NudesReceiver().start();
            } catch (IOException e) {
                this.stopService();
                e.printStackTrace();
            }
        } catch (ConnectException c) {
            System.out.println("Could not connect to server");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error");
        }
    }

    public void moveNude(byte direction) {
        outputPack[0] = direction;
    }

    public void angleNude(int angle) {
        outputPack[1] = (byte) (angle >> 8);
        outputPack[2] = (byte) (angle);
    }

    private class NudesHandler extends Thread {
        @Override
        public void run() {
            while (isRunning) {
                try {
                    out.write(outputPack);
                } catch (SocketException s) {
                    System.out.println("Connection is closed");
                    isRunning = false;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            ServerService.this.stopService();
        }
    }

    private class NudesReceiver extends Thread {
        @Override
        public void run() {
//            byte[] bytes = new byte[10];
//
//            while (isRunning) {
//                try {
//                    in.read(bytes);
//                    downloadedWorld.addEntity(Player());
//
//                    player.move(bytes[0]);
//                    player.setDeciFi((bytes[1] << 8) + bytes[2]);
//                } catch (SocketException s) {
//                    System.out.println("Connection is closed");
//                    isRunning = false;
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//            ServerService.this.stopService();
        }
    }

    private void stopService() {
        try {
            if (!socket.isClosed()) {
                socket.close();
                in.close();
                out.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}