package Controller;

import Model.Model;
import MsgPass.ControllerMsg.ControllerMsg;
import MsgPass.ControllerMsg.UpdateBoardMsg;
import Shared.TilePosition;
import MsgPass.ControllerMsg.ControllerWindowClosedMsg;
import MsgPass.ControllerMsg.ResetBoardMsg;

public class Controller {
    static private Controller controller;

    static private Thread controllerMainThread;
    private ControllerState state = ControllerState.Running;

    private enum ControllerState {
        Running,
        Closing
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
        while (controller.state == ControllerState.Running) {

            ControllerMsg controllerMsg = Model.readControllerMsg();
            System.out.println("Controller Received " + controllerMsg.getClass().getName());

            if (controllerMsg instanceof UpdateBoardMsg) {
                UpdateBoardMsg msg = (UpdateBoardMsg) controllerMsg;
                updateBoard(msg);

            } else if (controllerMsg instanceof ControllerWindowClosedMsg) {
                controller.state = ControllerState.Closing;
            } else if (controllerMsg instanceof ResetBoardMsg) {
                Gui.initBoard();
            }
        }
    }

    private void updateBoard(UpdateBoardMsg msg) {
        for (TilePosition position : msg.tilePositions) {
            Tile tile = (Tile) Gui.board.getChildren().get(position.x * 8 + position.y);
            tile.setTilecolor(msg.color);
        }
    }
}
