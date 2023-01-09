package Controller.Gui;

import Model.Model;
import MsgPass.ModelMsg.RestartBtnPressedMsg;

public class ButtonRestart extends Button {

    public ButtonRestart() {
        super(Buttons.Restart);
        setOnMousePressed(e -> {
            setImage(imgPressed);

        });
        setOnMouseReleased(e -> {
            Model.sendGameMsg(new RestartBtnPressedMsg());
            setImage(img);
        });
    }

}
