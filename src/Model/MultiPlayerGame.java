package Model;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;

import Server.Server;

public class MultiPlayerGame extends Game {
    private final Socket socket;
    private final String netId;
    private static volatile LinkedBlockingQueue<String> joinGameMsg = new LinkedBlockingQueue<>();

    MultiPlayerGame(GameOptions options) {
        super(options);
        try {
            socket = new Socket("0.0.0.0", 4000);
            netId = Server.getInitId(socket);
            System.out.println("Received netId " + netId);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
