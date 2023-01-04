package Model;

import java.util.concurrent.LinkedBlockingQueue;

import MsgPass.ControllerMsg.ControllerMsg;
import MsgPass.ModelMsg.ModelMsg;

public class Model {
    @SuppressWarnings("unused")
    private static volatile Game game;
    private static volatile LinkedBlockingQueue<ModelMsg> modelqueue = new LinkedBlockingQueue<>();
    private static volatile LinkedBlockingQueue<ControllerMsg> controllerqueue = new LinkedBlockingQueue<>();

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
        try {
            modelqueue.put(event);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static void sendControllerMsg(ControllerMsg event) {
        try {
            controllerqueue.put(event);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
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
