package Model;

import java.util.concurrent.LinkedTransferQueue;

import MsgPass.ControllerMsg.ControllerMsg;
import MsgPass.ModelMsg.ModelMsg;

public class Model {
    private static volatile Game game;
    private static volatile LinkedTransferQueue<ModelMsg> modelqueue = new LinkedTransferQueue<>();
    private static volatile LinkedTransferQueue<ControllerMsg> controllerqueue = new LinkedTransferQueue<>();

    public static void startGame(Game.GameMode gamemode, GameOptions options) {
        if (game == null) {
            switch (gamemode) {
                case Classic:
                    game = new ClassicGame();
                    break;
                case AIGame:
                    throw new UnsupportedOperationException("Not yet implemented");
                case Multiplayer:
                    throw new UnsupportedOperationException("Not yet implemented");
            }
        } else {
            throw new RuntimeException("CANNOT START A NEW GAME. A GAME IS ALREADY BEING PLAYED");
        }
    }

    public static synchronized void sendModelMsg(ModelMsg event) {
        modelqueue.put(event);
    }

    public static synchronized void sendControllerMsg(ControllerMsg event) {
        controllerqueue.put(event);
    }

    public static synchronized ModelMsg readModelMsg() {
        try {
            return modelqueue.take();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static synchronized ControllerMsg readControllerMsg() {
        try {
            return controllerqueue.take();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
