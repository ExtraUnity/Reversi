package Controller.Gui;

import Model.Model;
import MsgPass.ModelMsg.ModelWindowClosedMsg;

public class ButtonExitGame extends Button {

    public ButtonExitGame() {
        super(Buttons.ExitGame, 1);

        setOnMousePressed(e -> {
            setImage(imgPressed);
        });
        setOnMouseReleased(e -> {
            Model.sendGameMsg(new ModelWindowClosedMsg());
            Model.shutdownModel();
            setImage(img);
        });
    }
    
}