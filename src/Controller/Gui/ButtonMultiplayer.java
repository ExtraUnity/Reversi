package Controller.Gui;

import Server.ServerConn;

public class ButtonMultiplayer extends Button {


    public ButtonMultiplayer() {
        super(Buttons.Multiplayer, 4);

        setOnMousePressed(e -> {
            setImage(imgPressed);
        });
        setOnMouseReleased(e -> {

            ServerConn conn = new ServerConn();
            setImage(img);
            Gui.makeMultiplayerMenu(conn);

        });
    }

}
