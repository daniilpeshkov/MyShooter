package Game.Network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientHandler extends Thread {
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;

    public ClientHandler(Socket socket) throws IOException {
        this.socket = socket;

        in = new DataInputStream (socket.getInputStream());
        out = new DataOutputStream (socket.getOutputStream());

        start();
    }

    @Override
    public void run() {
        try {
            String string;

            while (true) {
                string = in.readUTF();
                if (string.equals("stop")) {
                    this.stopService();
                    break;
                }

                System.out.println("Client " + socket.getInetAddress() + ":" + socket.getLocalPort() + " says: " + string);
            }
        } catch (IOException e) {
            e.printStackTrace();
            this.stopService();
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
