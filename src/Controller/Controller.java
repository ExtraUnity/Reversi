package Controller;

import Model.Model;
import MsgPass.ControllerMsg.ControllerMsg;
import MsgPass.ControllerMsg.UpdateBoardMsg;
import MsgPass.ControllerMsg.ControllerWindowClosedMsg;

public class Controller {
    static private Controller controller;

    @SuppressWarnings("unused")
    static private Gui gui = new Gui();
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
                System.out.println(msg);

            } else if (controllerMsg instanceof ControllerWindowClosedMsg) {
                controller.state = ControllerState.Closing;
            }
        }
    }
}
