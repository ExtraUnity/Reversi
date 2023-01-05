package Controller;

import Controller.Gui.Gui;
import Controller.Gui.Tile;
import Controller.Gui.TurnIndication;
import Model.LegalMove;
import Model.Model;
import MsgPass.ControllerMsg.ControllerMsg;
import MsgPass.ControllerMsg.UpdateBoardMsg;
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
                updateBoard(msg);
                TurnIndication.switchTurns();

            } else if (controllerMsg instanceof ControllerWindowClosedMsg) {
                controller.state = ControllerState.CLOSING;
            } else if (controllerMsg instanceof ResetBoardMsg) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        Gui.buildGui();
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
    }
}
