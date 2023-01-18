package Controller;

import Controller.Gui.Gui;
import Controller.Gui.PointCounter;

import java.util.concurrent.LinkedBlockingQueue;
import javafx.scene.media.AudioClip;

import Controller.Gui.ButtonPass;
import Controller.Gui.Tile;
import Controller.Gui.TurnIndication;
import Model.LegalMove;
import Model.Model;
import MsgPass.ControllerMsg.ControllerMsg;
import MsgPass.ControllerMsg.UpdateBoardMsg;
import MsgPass.ControllerMsg.WinnerMsg;
import javafx.application.Platform;
import MsgPass.ControllerMsg.ControllerWindowClosedMsg;
import MsgPass.ControllerMsg.StartGameMsg;

public class Controller {
    static private Controller controller;

    static private Thread controllerMainThread;
    private ControllerState state = ControllerState.RUNNING;
    private static volatile LinkedBlockingQueue<Boolean> guiReadyQueue = new LinkedBlockingQueue<>();

    private static boolean guiInitDone = false;

    private static void waitUntilGuiInitDone() {
        if (guiInitDone) {
            return;
        }
        try {
            guiReadyQueue.take();
            guiInitDone = true;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static void setGuiInitDone() {
        try {
            guiReadyQueue.put(true);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private enum ControllerState {
        RUNNING,
        CLOSING;
    }

    private Controller() {
    }

    public static void initController() {
        if (controller == null) {
            Gui.initGui();
            controller = new Controller();
            controllerMainThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    controller.run_controller();
                }
            });
            controllerMainThread.start();
        } else {
            throw new RuntimeException("Controller already initet");
        }

    }

    private void run_controller() {
        System.out.println("Controller loop started");
        int blackPoints = 0;
        int whitePoints = 0;
        while (controller.state == ControllerState.RUNNING) {

            ControllerMsg controllerMsg = Model.readControllerMsg();
            // System.out.println("Controller Received " +
            // controllerMsg.getClass().getName());

            if (controllerMsg instanceof UpdateBoardMsg) {
                UpdateBoardMsg msg = (UpdateBoardMsg) controllerMsg;
                whitePoints = msg.whitePoints;
                blackPoints = msg.blackPoints;
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        if (!msg.isPassing || Gui.getMenuBottom().getButtonPass().getAvailable()) {
                            if (!msg.isPassing && msg.turns > 4) {
                                piecePlaySound();
                            }
                            updateBoard(msg);
                            TurnIndication.switchTurns();
                        } else if (msg.isPassing) {
                            System.out.println(
                                    "not allowed to pass because you have " + msg.legalMoves.length + " moves");
                        }
                    }
                });

            } else if (controllerMsg instanceof ControllerWindowClosedMsg) {
                controller.state = ControllerState.CLOSING;
                Gui.close();
            } else if (controllerMsg instanceof WinnerMsg) {
                WinnerMsg msg = (WinnerMsg) controllerMsg;
                int blackPointsClone = blackPoints;
                int whitePointsClone = whitePoints;
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {

                        Gui.displayWinner(msg.winner, blackPointsClone, whitePointsClone);
                    }
                });
            } else if (controllerMsg instanceof StartGameMsg) {
                StartGameMsg msg = (StartGameMsg) controllerMsg;
                waitUntilGuiInitDone();
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        Gui.buildGameGui(msg.gameOptions);
                    }
                });

            }
        }
    }

    private void piecePlaySound() {
        new AudioClip(
                getClass().getResource("/Assets/sounds/DiskPlace.mp3").toExternalForm()).play();
    }

    private void updateBoard(UpdateBoardMsg msg) {

        for (var tile : Gui.getBoard().getAllTiles()) {
            tile.resetLegalMove();
        }

        // Hvis der bliver passet bliver der sendt en updateBoard besked med ingen
        // tilePositions.
        // Derfor skal der kun animeres hvis der IKKE bliver passet
        if (msg.tilePositions.length > 0) {
            // Den sidste skal ikke animeres. Men der skal i stedet bare sætted farven.
            var placed_tile_pos = msg.tilePositions[msg.tilePositions.length - 1];
            var placed_tile = Gui.getBoard().getTile(placed_tile_pos);
            placed_tile.setTilecolor(msg.color);

            // Skipper den første, da der ikke skal animeres men i stedet bare sætte farven.
            for (int i = 0; i < msg.tilePositions.length - 1; i++) {
                Tile tile = Gui.getBoard().getTile(msg.tilePositions[i]);
                tile.switchTilecolor(msg.color);
            }
        }

        for (LegalMove move : msg.legalMoves) {
            Tile tile = Gui.getBoard().getTile(move.position);
            tile.setLegalImage();
        }
        PointCounter.setBlackPoints(msg.blackPoints);
        PointCounter.setWhitePoints(msg.whitePoints);

        updateButtonPass(msg.legalMoves.length);
    }

    /**
     * Uses the amount of legal moves update the pass button image
     * using the rule:
     * No legal moves -> Pass available
     */
    void updateButtonPass(int legalMoves) {
        ButtonPass ButtonPass = Gui.getMenuBottom().getButtonPass();
        if (legalMoves == 0) {
            ButtonPass.setImage("/Assets/ButtonPass.png");
            ButtonPass.setAvailable(true);
        } else {
            ButtonPass.setImage("/Assets/ButtonPassGrey.png");
            ButtonPass.setAvailable(false);
        }
        ButtonPass.updatePressed();
    }
}
