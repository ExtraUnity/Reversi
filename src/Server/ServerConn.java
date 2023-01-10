package Server;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import Model.GameOptions;
import Model.Model;
import Model.Game.GameMode;
import MsgPass.ModelMsg.ModelMsg;
import Shared.TileColor;

public class ServerConn {
    private final Socket socket;
    public final String netId;
    private boolean joined = false;
    private Thread socketReaderThread;
    public static TileColor selfColor;

    private static ServerConn instance;

    public ServerConn() {
        instance = this;
        try {
            socket = new Socket("77.213.215.246", 80);
            netId = Server.getInitId(socket);
            System.out.println("Received netId " + netId);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        while (true) {
                            var buffer = new byte[1];
                            socket.getInputStream().read(buffer);
                            var success = buffer[0] == 1;
                            System.out.println("JOIN SUCCESS: " + success);
                            if (success) {
                                joined = true;

                                var buffer_2 = new byte[1];
                                socket.getInputStream().read(buffer_2);
                                if (buffer_2[0] == 0) {
                                    selfColor = TileColor.BLACK;
                                } else {
                                    selfColor = TileColor.WHITE;
                                }
                                System.out.println("My color is " + selfColor);
                                socketReaderThread = new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        socketReaderLoop();
                                    }
                                });
                                socketReaderThread.start();
                                Model.startGame(GameMode.MULTIPLAYER, new GameOptions(-1, true, TileColor.BLACK));
                                return;
                            } else {
                                System.out.println("JOIN FAILED");
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        return;
                    }

                }
            }).start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void sendModelMessage(ModelMsg msg) {
        System.out.println("Sending " + msg);

        try {
            ByteArrayOutputStream binWriter = new ByteArrayOutputStream();
            ObjectOutputStream objWriter = new ObjectOutputStream(binWriter);
            objWriter.writeObject(msg);
            byte[] msg_bin = binWriter.toByteArray();
            byte[] msg_len_bin = ByteBuffer.allocate(4).putInt(msg_bin.length).array();

            ByteArrayOutputStream totalOut = new ByteArrayOutputStream();
            totalOut.write(msg_len_bin);

            System.out.println("sending msg len " + msg_bin.length);
            System.out.println("sending msg len bin ");
            for (int i = 0; i < 4; i++) {
                System.out.print(totalOut.toByteArray()[i] + " ");
            }
            System.out.println();

            totalOut.write(msg_bin);
            byte[] bytes = totalOut.toByteArray();
            instance.socket.getOutputStream().write(bytes);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void tryJoin(String try_netId) {
        if (joined) {
            return;
        }
        try {
            byte[] try_netIdBytes = try_netId.getBytes();
            if (try_netIdBytes.length != 6) {
                return;
            }
            System.out.println("Trying to join " + try_netId);
            socket.getOutputStream().write(try_netIdBytes);

        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
    }

    private void socketReaderLoop() {
        try {
            while (true) {
                var msgsizebuffer = new byte[4];
                socket.getInputStream().read(msgsizebuffer);
                ByteBuffer buffer = ByteBuffer.wrap(msgsizebuffer);
                int len = buffer.getInt();
                System.out.println("Received msg len " + len);
                System.out.println("Received msg len bin ");
                for (int i = 0; i < msgsizebuffer.length; i++) {
                    System.out.print(msgsizebuffer[i] + " ");
                }
                System.out.println();

                var msg_buffer = new byte[len];
                socket.getInputStream().read(msg_buffer);
                var byteBufferInputStream = new ByteArrayInputStream(msg_buffer);
                var objectIn = new ObjectInputStream(byteBufferInputStream);
                ModelMsg msg = (ModelMsg) objectIn.readObject();
                System.out.println("Received msg " + msg);
                Model.sendGameMsg(msg);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
