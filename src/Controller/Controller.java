package Controller;

import Controller.Gui.Gui;
import Controller.Gui.PointCounter;
import Model.Game;

import java.util.concurrent.LinkedBlockingQueue;

import Controller.Gui.ButtonPass;
import Controller.Gui.Tile;
import Controller.Gui.TurnIndication;
import Model.LegalMove;
import Model.Model;
import MsgPass.ControllerMsg.ControllerMsg;
import MsgPass.ControllerMsg.UpdateBoardMsg;
import MsgPass.ControllerMsg.WinnerMsg;
import Shared.TileColor;
import Shared.TilePosition;
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
        while (controller.state == ControllerState.RUNNING) {

            ControllerMsg controllerMsg = Model.readControllerMsg();
            System.out.println("Controller Received " + controllerMsg.getClass().getName());

            if (controllerMsg instanceof UpdateBoardMsg) {
                UpdateBoardMsg msg = (UpdateBoardMsg) controllerMsg;
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        if (!msg.isPassing || Gui.getMenuBottom().getButtonPass().getAvailable()) {
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
            } else if (controllerMsg instanceof WinnerMsg) {
                WinnerMsg msg = (WinnerMsg) controllerMsg;
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        Gui.displayWinner(msg.winner);
                    }
                });
            } else if (controllerMsg instanceof StartGameMsg) {
                StartGameMsg msg = (StartGameMsg) controllerMsg;
                waitUntilGuiInitDone();
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        Gui.buildGui(msg.gameOptions);
                    }
                });

            }
        }
    }

    private void updateBoard(UpdateBoardMsg msg) {

        for (var tile : Gui.getBoard().getAllTiles()) {
            tile.resetLegalMove();
        }
        for (TilePosition position : msg.tilePositions) {
            Tile tile = Gui.getBoard().getTile(position);
            tile.setTilecolor(msg.color);
        }
        for (LegalMove move : msg.legalMoves) {
            Tile tile = Gui.getBoard().getTile(move.position);
            tile.setLegalImage();
        }
        PointCounter.setBlackPoints(msg.blackPoints);
        PointCounter.setWhitePoints(msg.whitePoints);
        System.out.println("The next player has " + msg.legalMoves.length + " moves");

        updateButtonPass(msg.legalMoves.length);
    }

    /**
     * 
     * @param legalMoves
     *                   Uses the amount of legal moves update the pass button image
     *                   using the rule:
     *                   No legal moves -> Pass available
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
        System.out.println("changing pic");
    }
}
