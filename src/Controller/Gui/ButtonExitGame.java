package Controller.Gui;
//Filen er skrevet af Katinka
//Exit knap, Afslutter spillet ved at sende en luk-vinduet-besked og en afslut programmet besked
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