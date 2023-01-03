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
                case CLASSIC:
                    game = new ClassicGame();
                    break;
                case AI_GAME:
                    throw new UnsupportedOperationException("Not yet implemented");
                case MULTIPLAYER:
                    throw new UnsupportedOperationException("Not yet implemented");
            }
        } else {
            throw new RuntimeException("CANNOT START A NEW GAME. A GAME IS ALREADY BEING PLAYED");
        }
    }

    public static void sendModelMsg(ModelMsg event) {
        modelqueue.add(event);
    }

    public static void sendControllerMsg(ControllerMsg event) {
        controllerqueue.add(event);
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
