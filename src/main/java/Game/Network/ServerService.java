package Game.Network;

import Game.Graphics.GameRenderer;
import Game.Logic.TexturedEntity;
import Main.Main;
import org.joml.Vector3f;

import java.io.*;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketException;

public class ServerService {
    static int port;
    static final int SERVER_PORT = 58146;
    private static BufferedReader input;
    private static InputStream in;
    private static OutputStream out;
    private static byte[] outputPack = new byte[5];
    private Socket socket;
    private String ip;

    private static boolean isRunningHandler = true;
    private static boolean isRunningReceiver = true;

    private static boolean hasId = false;
    private static int id;
    private static Vector3f playerPos = new Vector3f(0, 0, 0);

    public ServerService(String ip, int port) {
        this.ip = ip;
        this.port = port;

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

    public ServerService(String ip) {
        this(ip, SERVER_PORT);
    }

    public void moveNude(byte direction) {
        outputPack[0] = direction;
    }

    public void angleNude(float angle) {
        BitsFormatHandler.writeFloatBits(angle, outputPack, 1);
    }

    private class NudesHandler extends Thread {
        @Override
        public void run() {
            while (isRunningHandler) {
                try {
                    out.write(outputPack);
                } catch (SocketException s) {
                    System.out.println("Connection is closed");
                    isRunningHandler = false;
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
            byte[] bytes = new byte[23];

            while (isRunningReceiver) {
                try {
                    in.read(bytes);

                    Main.buffer.clear();
                    Main.buffer.add(new TexturedEntity(BitsFormatHandler.readFloatBits(bytes, BitsFormatHandler.x),
                            BitsFormatHandler.readFloatBits(bytes, BitsFormatHandler.y),
                            BitsFormatHandler.readFloatBits(bytes, BitsFormatHandler.r),
                            bytes[0]));

                } catch (SocketException s) {
                    System.out.println("Connection is closed");
                    isRunningReceiver = false;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            ServerService.this.stopService();
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