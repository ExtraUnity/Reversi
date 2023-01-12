package Model;

import java.util.concurrent.LinkedBlockingQueue;

import MsgPass.ControllerMsg.ControllerMsg;
import MsgPass.ControllerMsg.ControllerWindowClosedMsg;
import MsgPass.ControllerMsg.StartGameMsg;
import MsgPass.ModelMsg.ModelMsg;
import Server.ServerConn;
import MsgPass.ModelMsg.GameStateMsg;

public class Model {
    private static volatile LinkedBlockingQueue<ModelMsg> gameQueue = new LinkedBlockingQueue<>();
    private static volatile LinkedBlockingQueue<ControllerMsg> controllerQueue = new LinkedBlockingQueue<>();
    private static volatile LinkedBlockingQueue<GameStateMsg> startGamequeue = new LinkedBlockingQueue<>();

    static Thread modelMainThread;

    public static void initModel() {
        modelMainThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        var msg = startGamequeue.take();
                        if (msg.exit) {
                            System.out.println("Model loop exited. Sending close controller msg");
                            sendControllerMsg(new ControllerWindowClosedMsg());
                            break;
                        }
                        System.out.println("Model received start game msg");
                        Game game;
                        switch (msg.gameMode) {
                            case CLASSIC:
                                game = new ClassicGame(msg.gameOptions);
                                break;
                            case AI_GAME:
                                game = new AIGame(msg.gameOptions);
                                break;
                            case MULTIPLAYER:
                                game = new MultiPlayerGame(msg.gameOptions);
                                break;
                            default:
                                throw new UnsupportedOperationException("Invalid gamemode");
                        }
                        sendControllerMsg(new StartGameMsg(msg.gameOptions));
                        game.startGame();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                ServerConn.shutdown();
            }
        });
        modelMainThread.start();
    }

    public static void shutdownModel() {
        try {
            startGamequeue.put(GameStateMsg.exit());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static void startGame(Game.GameMode gamemode, GameOptions options) {
        try {
            startGamequeue.put(new GameStateMsg(gamemode, options));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    public static void sendGameMsg(ModelMsg event) {
        try {
            gameQueue.put(event);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static void sendControllerMsg(ControllerMsg event) {
        try {
            controllerQueue.put(event);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static ModelMsg readGameMsg() {
        try {
            return gameQueue.take();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static ControllerMsg readControllerMsg() {
        try {
            return controllerQueue.take();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
