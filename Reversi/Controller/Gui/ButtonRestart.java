package Controller.Gui;
//Filen er skrevet af Katinka
//Spil igen Knap på win-screen, sender en Game besked om at spillet skal starte igen.
import Model.Model;
import MsgPass.ModelMsg.RestartBtnPressedMsg;

public class ButtonRestart extends Button {

    public ButtonRestart() {
        super(Buttons.Restart,1);
        setOnMousePressed(e -> {
            setImage(imgPressed);
        });
        setOnMouseReleased(e -> {
            Model.sendGameMsg(new RestartBtnPressedMsg());
            setImage(img);
        });
    }

}
