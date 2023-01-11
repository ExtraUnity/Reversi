package Server;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.nio.ByteBuffer;

import Controller.Gui.PlayerCharacter;
import Model.GameOptions;
import Model.Model;
import Model.Game.GameMode;
import MsgPass.ModelMsg.CharacterSelectedMsg;
import MsgPass.ModelMsg.ModelMsg;
import Shared.TileColor;

public class ServerConn {
    private final Socket socket;
    public final String netId;
    private boolean joined = false;
    private Thread socketReaderThread;
    public static TileColor selfColor;
    final private Thread connThread;
    // Hvis der ikke bliver valgt noget bliver man bare til stalin (eller noget)
    private PlayerCharacter selectedCharacter = PlayerCharacter.Stalin;

    private static ServerConn instance;

    public static void setLoadedCharacter(PlayerCharacter selectedCharacter) {
        instance.selectedCharacter = selectedCharacter;
    }

    public ServerConn() {
        instance = this;
        try {
            // mig egen ip. Ikke dox mig plz
            socket = new Socket("77.213.215.246", 4000);
            netId = Server.getInitId(socket);
            System.out.println("Received netId " + netId);
            connThread = new Thread(new Runnable() {
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

                                sendModelMessage(new CharacterSelectedMsg(selectedCharacter));
                                // Bagefter læs hvad den anden er
                                PlayerCharacter otherCharacter = readCharacterMessage();
                                var whiteCharacter = selfColor == TileColor.WHITE ? selectedCharacter : otherCharacter;
                                var blackCharacter = selfColor == TileColor.BLACK ? selectedCharacter : otherCharacter;

                                System.out.println("self  character " + selectedCharacter);
                                System.out.println("other character " + otherCharacter);

                                System.out.println("white character " + whiteCharacter);
                                System.out.println("black character " + blackCharacter);

                                socketReaderThread = new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        socketReaderLoop();
                                    }
                                });
                                socketReaderThread.start();
                                Model.startGame(GameMode.MULTIPLAYER, new GameOptions(-1, true, TileColor.BLACK,
                                        whiteCharacter, blackCharacter));
                                return;
                            } else {
                                System.out.println("JOIN FAILED");
                            }
                        }
                    } catch (SocketException e) {
                        if (e.getMessage().contains("Socket closed")) {
                            System.out.println("Serverconn socket closed");
                        } else {
                            e.printStackTrace();
                        }
                    } catch (IOException e) {

                        e.printStackTrace();
                        return;
                    } catch (ClassNotFoundException e) {
                        System.out.println(
                                "Dette sker hvis modstanderen ikke sender en characterselected besked med det samme");

                        System.out.println("Altså har i nok forskellige versioner af spillet. Opdater");
                        e.printStackTrace();
                    }

                }
            });
            connThread.start();
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

                var msg_buffer = new byte[len];
                socket.getInputStream().read(msg_buffer);
                var byteBufferInputStream = new ByteArrayInputStream(msg_buffer);
                var objectIn = new ObjectInputStream(byteBufferInputStream);
                ModelMsg msg = (ModelMsg) objectIn.readObject();
                System.out.println("Received msg " + msg);
                Model.sendGameMsg(msg);
            }
        } catch (SocketException e) {
            System.out.println("Socket closed: " + e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.out.println(
                    "Using incompatible versions of your game :(\n This is fatal for multiplayer and the socket has exited. Update your game and try again");
            e.printStackTrace();
        }
    }

    public static void shutdown() {
        if (instance != null) {
            try {
                instance.socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // her bestemmes den character man har i multiplayer
    static PlayerCharacter readCharacterMessage() throws IOException, ClassNotFoundException {
        var msgsizebuffer = new byte[4];
        instance.socket.getInputStream().read(msgsizebuffer);
        ByteBuffer buffer = ByteBuffer.wrap(msgsizebuffer);
        int len = buffer.getInt();
        System.out.println("Received msg len " + len);

        var msg_buffer = new byte[len];
        instance.socket.getInputStream().read(msg_buffer);
        var byteBufferInputStream = new ByteArrayInputStream(msg_buffer);
        var objectIn = new ObjectInputStream(byteBufferInputStream);
        CharacterSelectedMsg msg = (CharacterSelectedMsg) objectIn.readObject();
        return msg.character;
    }
}
