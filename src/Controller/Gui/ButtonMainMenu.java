package Controller.Gui;

import Model.Model;
import MsgPass.ModelMsg.MainMenuMsg;
import Server.ServerConn;

public class ButtonMainMenu extends Button {

    public ButtonMainMenu() {
        super(Buttons.MainMenu, 1);
        setOnMousePressed(e -> {
            setImage(imgPressed);
        });
        setOnMouseReleased(e -> {
            setImage(img);
            Gui.stackRoot.getChildren().clear();

            // Don't know why this line is neccessary but it is currently...
            Gui.startMenuRoot.getChildren().set(2, new MenuMainBottom());

            Gui.stackRoot.getChildren().add(Gui.startMenuRoot);
            System.out.println("Back to MAINMENU");
            ServerConn.shutdown();
            Model.sendGameMsg(new MainMenuMsg());
        });
    }

}
