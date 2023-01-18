package Controller.Gui;
//Filen er skrevet af Katinka
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
            Gui.stackRoot.getChildren().add(Gui.startMenuRoot);
            Gui.addMuteButton();
            ServerConn.shutdown();
            Model.sendGameMsg(new MainMenuMsg());
        });
    }

}
