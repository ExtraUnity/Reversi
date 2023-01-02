package Model;

import java.util.concurrent.LinkedTransferQueue;

import MsgPass.ControllerMsg.ControllerMsg;

public class Model {
    private static volatile Game game;
    private static volatile LinkedTransferQueue<ControllerMsg> modelqueue;
    private static volatile LinkedTransferQueue<ControllerMsg> controllerqueue;

    public static void startGame(Game.GameMode gamemode, Game.GameOptions options) {
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

    public static synchronized void sendModelMsg(ControllerMsg event) {
        modelqueue.put(event);
    }

    public static synchronized void sendControllerMsg(ControllerMsg event) {
        controllerqueue.put(event);
    }

    public static synchronized ControllerMsg readModelMsg() {
        return modelqueue.poll();
    }

    public static synchronized ControllerMsg readControllerMsg() {
        return controllerqueue.poll();
    }
}
