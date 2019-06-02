package Game.Network;

import Game.Graphics.Texture;
import Game.Logic.TexturedEntity;
import Main.Main;

import java.io.*;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

public class Client {
    static int port = 58146;

    private static BufferedReader input;
    private static InputStream in;
    private static OutputStream out;
    private static byte[] outputPack = new byte[6];

    private static Socket socket;
    private static String ip;

    private static boolean isRunningHandler = true;
    private static boolean isRunningReceiver = true;

    public static void init(String ip, int port) {
        Client.ip = ip;
        Client.port = port;

        try {
            System.out.println("Trying to connect...");
            Client.socket = new Socket(ip, Client.port);
            System.out.println("Connection established");

            try {
//                input = new BufferedReader(new InputStreamReader(System.in));
                in = socket.getInputStream();
                out = socket.getOutputStream();

                new PackageSender().start();
                new NudesReceiver().start();
            } catch (IOException e) {
                Client.stopService();
                e.printStackTrace();
            }
        } catch (ConnectException c) {
            System.out.println("Could not connect to server");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error");
        }
    }

    public static void initByIp(String ip) {
        init(ip, port);
    }

    public static void updateDirection(byte direction) {
        outputPack[0] = direction;
    }

    public static void angleNude(float angle) {
        BitsFormatHandler.writeFloatBits(angle, outputPack, BitsFormatHandler.pFi);
    }

    public static void fireBullets() {
        outputPack[5] = 1;
    }

    private static class PackageSender extends Thread {
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
            Client.stopService();
        }
    }

    private static class NudesReceiver extends Thread {

        ArrayList<TexturedEntity> a;

        @Override
        public void run() {
            while (isRunningReceiver) {
                try {
                    byte[] bytes = new byte[23];
                    ArrayList<TexturedEntity> buf = new ArrayList<>();

                    while (bytes[22] != 2) {
                        if (in.available() > 22) {
                            in.read(bytes);

                            TexturedEntity entity = new TexturedEntity(BitsFormatHandler.readFloatBits(bytes, BitsFormatHandler.x),
                                    BitsFormatHandler.readFloatBits(bytes, BitsFormatHandler.y),
                                    BitsFormatHandler.readFloatBits(bytes, BitsFormatHandler.r),
                                    bytes[0]);

                            entity.setFi(BitsFormatHandler.readFloatBits(bytes, BitsFormatHandler.fi));
                            entity.setHP(bytes[17]);

                            buf.add(entity);
                        }
                    }

                    Main.buffer = buf;
                } catch (SocketException s) {
                    System.out.println("Connection is closed");
                    isRunningReceiver = false;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            Client.stopService();
        }
    }

    private static void stopService() {
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