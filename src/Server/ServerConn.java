package Server;
//Filen er skrevet af Thor

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
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
    private boolean joined = false;
    public static TileColor selfColor;
    public static boolean isHost;

    // Hvis der ikke bliver valgt noget bliver man bare til stalin
    private static PlayerCharacter selectedCharacter = PlayerCharacter.Stalin;
    private static int selectedGametime = -1;

    private static ServerConn instance;

    public static void setLoadedCharacter(PlayerCharacter selectedCharacter) {
        ServerConn.selectedCharacter = selectedCharacter;
    }

    public static void setLoadedGameTime(int gameTime) {
        selectedGametime = gameTime;
    }

    public static String hostGame() {
        if (instance != null) {
            return "Server connection object already exists. This is a bug. Remember to call shutdown() after the end of a game.";
        }
        try {
            instance = new ServerConn();
            var outStream = instance.socket.getOutputStream();
            var inStream = instance.socket.getInputStream();
            // Byte 1 betyder man gerne vil hoste
            outStream.write(1);
            byte[] rawId = Server.readJoinId(inStream);

            instance.hostWaitForConnection();
            return new String(rawId);
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return e.getMessage();
        } catch (IOException e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    public static String joinGame(String id) {
        if (instance != null) {
            return "Server connection object already exists. This is a bug. Remember to call shutdown() after the end of a game.";
        }

        if (id.length() != 6) {
            shutdown();
            return "Invalid host id. The length MUST be 6";
        }
        try {
            instance = new ServerConn();
            byte[] rawId = id.getBytes();

            var outStream = instance.socket.getOutputStream();
            var inStream = instance.socket.getInputStream();
            // Byte 0 betyder man gerne vil joine
            outStream.write(0);
            outStream.write(rawId);
            int success = inStream.read();
            if (success == 1) {
                // Først læs hvad gameTime er
                int gameTime = readGametimeMessage();
                // Send character message
                sendModelMessage(new CharacterSelectedMsg(selectedCharacter));

                PlayerCharacter otherCharacter = readCharacterMessage();
                selfColor = TileColor.WHITE;

                isHost = false;
                instance.socketReaderLoop();
                Model.startGame(GameMode.MULTIPLAYER,
                        new GameOptions(gameTime, true, TileColor.BLACK, selectedCharacter, otherCharacter));
                return "Joining";
            } else {
                shutdown();
                return "Unused host id";
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
            shutdown();
            return e.getMessage();
        } catch (IOException e) {
            e.printStackTrace();
            shutdown();
            return e.getMessage();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            shutdown();
            return e.getMessage() + " this happend because of different game versions. Update now!";
        }
    }

    private void hostWaitForConnection() {
        new Thread(() -> {
            try {
                var inStream = instance.socket.getInputStream();
                // Når den modtager en byte betyder det at der er en spiller klar på den anden
                // side.
                inStream.read();
                // Det første der skal ske er at den sender gameTime.
                sendModelMessage(new GameTimeOptionNetmsg(selectedGametime));

                // Derefter sender den hvilken character der er blevet valgt
                sendModelMessage(new CharacterSelectedMsg(selectedCharacter));

                // Derefter læs hvæm den anden spiller som
                var otherCharacter = readCharacterMessage();

                selfColor = TileColor.BLACK;
                isHost = true;

                socketReaderLoop();
                Model.startGame(GameMode.MULTIPLAYER,
                        new GameOptions(selectedGametime, true, TileColor.BLACK, otherCharacter, selectedCharacter));

            } catch (IOException e) {
                if (!e.getMessage().contains("Socket closed")) {
                    e.printStackTrace();
                } else {
                    System.out.println("Socket closed");
                }

            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private ServerConn() throws UnknownHostException, IOException {
        socket = new Socket("77.213.215.246", 4000);
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
        new Thread(() -> {
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
        }).start();
    }

    public static void shutdown() {
        if (instance != null) {
            try {
                instance.socket.close();
            } catch (IOException e) {
                if (!e.getMessage().contains("Socket closed")) {
                    e.printStackTrace();
                } else {
                    System.out.println("Socket already closed");
                }
            }
            instance = null;
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

    static int readGametimeMessage() throws IOException, ClassNotFoundException {
        var msgsizebuffer = new byte[4];
        instance.socket.getInputStream().read(msgsizebuffer);
        ByteBuffer buffer = ByteBuffer.wrap(msgsizebuffer);
        int len = buffer.getInt();
        System.out.println("Received msg len " + len);

        var msg_buffer = new byte[len];
        instance.socket.getInputStream().read(msg_buffer);
        var byteBufferInputStream = new ByteArrayInputStream(msg_buffer);
        var objectIn = new ObjectInputStream(byteBufferInputStream);
        GameTimeOptionNetmsg msg = (GameTimeOptionNetmsg) objectIn.readObject();
        return msg.gameTime;
    }

    enum ConnMode {
        Host,
        Join
    }
}
