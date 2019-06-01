package Game.Network;

import java.io.*;
import java.net.ConnectException;
import java.net.Socket;

public class ClientService {
    //    private int port;
    static final int port = 58146;
    private static BufferedReader input;
    private static InputStream in;
    private static OutputStream out;
    private static byte[] outputPack = new byte[4];
    private Socket socket;
    private String ip;

    public ClientService(String ip, int port) {
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
            } catch (IOException e) {
                ClientService.this.stopService();
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

    private class NudesHandler extends Thread {
        @Override
        public void run() {
            while (true) {
                try {
                    out.write(outputPack);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}