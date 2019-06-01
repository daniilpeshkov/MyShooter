package Game.Network;

import java.io.*;
import java.net.Socket;

public class ClientService {
    private Socket socket;
    private String ip;
//    private int port;
    static final int port = 58146;

    private static BufferedReader input;
    private static InputStream in;
    private static OutputStream out;

    private static byte[] outputPack = new byte[8];

    public ClientService(String ip, int port) {
        this.ip = ip;
//        this.port = port;

        try {
            this.socket = new Socket(ip, this.port);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            input = new BufferedReader(new InputStreamReader(System.in));
            in = socket.getInputStream();
            out = socket.getOutputStream();

            new NudesHandler().start();
        } catch (IOException e) {
            ClientService.this.stopService();
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

    public void moveNude(int button, byte mode) {
        outputPack[button] = mode;
    }

//    private class SendConsoleMessage extends Thread {
//        @Override
//        public void run() {
//            while (true) {
//                String string;
//
//                try {
//                    string = input.readLine();
//
//                    if (input.equals("stop")) {
//                        out.writeUTF("stop");
//                        ClientService.this.stopService();
//                        break;
//                    } else {
//                        out.writeUTF(string);
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }

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
