package Controller.Gui;

import java.io.InputStream;
import Model.Model;
import MsgPass.ModelMsg.RestartBtnPressedMsg;
import javafx.scene.image.Image;

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
