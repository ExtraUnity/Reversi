package Controller;

import Controller.Gui.Gui;
import Controller.Gui.PassButton;
import Controller.Gui.Tile;
import Controller.Gui.TurnIndication;
import Model.LegalMove;
import Model.Model;
import MsgPass.ControllerMsg.ControllerMsg;
import MsgPass.ControllerMsg.UpdateBoardMsg;
import MsgPass.ControllerMsg.WinnerMsg;
import Shared.TilePosition;
import javafx.application.Platform;
import MsgPass.ControllerMsg.ControllerWindowClosedMsg;
import MsgPass.ControllerMsg.ResetBoardMsg;

public class Controller {
    static private Controller controller;

    static private Thread controllerMainThread;
    private ControllerState state = ControllerState.RUNNING;

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
                if (!msg.isPassing || Gui.getBotMenu().getPassButton().getAvailable()) {
                    updateBoard(msg);
                    TurnIndication.switchTurns();
                } else if (msg.isPassing) {
                    System.out.println("not allowed to pass because you have " + msg.legalMoves.length + " moves");
                }

            } else if (controllerMsg instanceof ControllerWindowClosedMsg) {
                controller.state = ControllerState.CLOSING;
            } else if (controllerMsg instanceof ResetBoardMsg) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        Gui.buildGui();
                    }
                });
            } else if (controllerMsg instanceof WinnerMsg) {
                WinnerMsg msg = (WinnerMsg) controllerMsg;
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        Gui.displayWinner(msg.winner);
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
        System.out.println("The next player has " + msg.legalMoves.length + " moves");

        updatePassButton(msg.legalMoves.length);
    }

    /**
     * 
     * @param legalMoves
     *                   Uses the amount of legal moves update the pass button image
     *                   using the rule:
     *                   No legal moves -> Pass available
     */
    void updatePassButton(int legalMoves) {
        PassButton passButton = Gui.getBotMenu().getPassButton();
        if (legalMoves == 0) {
            passButton.setImage("/Assets/passButton.png");
            passButton.setAvailable(true);
        } else {
            passButton.setImage("/Assets/notPassButton.png");
            passButton.setAvailable(false);
        }
        passButton.updatePressed();
        System.out.println("changing pic");
    }
}
