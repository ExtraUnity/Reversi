package Server;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;

public class Server {
    private static final int PORT = 4000;
    private static final int POLLING_INTERVAL = 100;

    private static volatile LinkedBlockingQueue<WaitingClient> newConnections = new LinkedBlockingQueue<>();

    /**
     * Har valgt at bruge et polling interval i stedet for at lave en helt masse
     * komplikeret threading. Især fordi mutexes i java er ret wack.
     */
    public static void main(String[] args) {

        // Connection accepting thread
        new Thread(new Runnable() {
            @Override
            public void run() {
                try (var socket = new ServerSocket(PORT)) {
                    while (true) {
                        newConnections.put(new WaitingClient(socket.accept()));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }).start();

        var clients = new ArrayList<WaitingClient>();

        while (true) {
            try {
                Thread.sleep(POLLING_INTERVAL);
                var client = newConnections.poll();
                if (client != null) {
                    client.socket.getOutputStream().write(client.id);
                    clients.add(client);
                }

                for (int i = 0; i < clients.size(); i++) {
                    var poll_client = clients.get(i);
                    var inStream = poll_client.socket.getInputStream();
                    if (inStream.available() >= 6) {
                        byte[] netIdBytes = new byte[6];
                        inStream.read(netIdBytes);
                        var outStream = poll_client.socket.getOutputStream();
                        for (int j = 0; j < clients.size(); j++) {
                            var found_client = clients.get(j);
                            if (found_client.id.equals(netIdBytes)) {
                                outStream.write(1);
                                found_client.socket.getOutputStream().write(1);

                                clients.remove(poll_client);
                                clients.remove(found_client);
                                var gamehost = new GameHost(poll_client, found_client);
                                gamehost.spawn();
                                break;
                            }
                        }
                        outStream.write(0);
                        break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

    public static String getInitId(Socket socket) {
        byte[] id_temp = new byte[6];
        try {
            socket.getInputStream().read(id_temp);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return new String(id_temp, Charset.forName("UTF-8"));
    }
    @SuppressWarnings("unused")
    private static byte[] toServerMsg(Serializable msg) {
        try {
            ByteArrayOutputStream binWriter = new ByteArrayOutputStream();
            ObjectOutputStream objWriter = new ObjectOutputStream(binWriter);
            objWriter.writeObject(msg);
            byte[] msg_bin = binWriter.toByteArray();

            ByteArrayOutputStream totalOut = new ByteArrayOutputStream();
            totalOut.write(msg_bin.length);
            totalOut.write(msg_bin);
            return totalOut.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

class WaitingClient {
    final Socket socket;
    final byte[] id;

    WaitingClient(Socket socket) {
        this.socket = socket;
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 6;
        Random random = new Random();

        String generatedString = random.ints(leftLimit, rightLimit + 1)
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
        System.out.println("Generated netId " + generatedString);
        id = generatedString.getBytes();
    }
}

class GameHost {
    Socket socket1;
    Socket socket2;

    GameHost(WaitingClient player1, WaitingClient player2) {
        socket1 = player1.socket;
        socket2 = player2.socket;
    }

    void spawn() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    var in1 = socket1.getInputStream();
                    var out1 = socket1.getOutputStream();
                    var in2 = socket2.getInputStream();
                    var out2 = socket2.getOutputStream();
                    while (true) {
                        Thread.sleep(100);
                        if (in1.available() >= 4) {
                            byte[] msgLenBytes = new byte[4];
                            in1.read(msgLenBytes);
                            ByteBuffer buffer = ByteBuffer.wrap(msgLenBytes);
                            int len = buffer.getInt();
                            byte[] message = new byte[len];
                            in1.read(message);
                            out2.write(message);
                        }
                        if (in2.available() >= 4) {
                            byte[] msgLenBytes = new byte[4];
                            in2.read(msgLenBytes);
                            ByteBuffer buffer = ByteBuffer.wrap(msgLenBytes);
                            int len = buffer.getInt();
                            byte[] message = new byte[len];
                            in2.read(message);
                            out1.write(message);
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

}
