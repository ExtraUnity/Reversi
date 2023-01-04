package Model;

import java.util.concurrent.LinkedTransferQueue;

import MsgPass.ControllerMsg.ControllerMsg;
import MsgPass.ModelMsg.ModelMsg;

public class Model {
    @SuppressWarnings("unused")
    private static volatile Game game;
    private static volatile LinkedTransferQueue<ModelMsg> modelqueue = new LinkedTransferQueue<>();
    private static volatile LinkedTransferQueue<ControllerMsg> controllerqueue = new LinkedTransferQueue<>();

    public static void startGame(Game.GameMode gamemode, GameOptions options) {
        switch (gamemode) {
            case CLASSIC:
                game = new ClassicGame(options);
                break;
            case AI_GAME:
                throw new UnsupportedOperationException("Not yet implemented");
            case MULTIPLAYER:
                throw new UnsupportedOperationException("Not yet implemented");
        }

    }

    public static void sendModelMsg(ModelMsg event) {
        modelqueue.put(event);
    }

    public static void sendControllerMsg(ControllerMsg event) {
        controllerqueue.put(event);
    }

    public static ModelMsg readModelMsg() {
        try {
            return modelqueue.take();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static ControllerMsg readControllerMsg() {
        try {
            return controllerqueue.take();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
