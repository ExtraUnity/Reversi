package Controller.Gui;

import Model.Game;
import Model.Model;
import MsgPass.ControllerMsg.WinnerMsg;
import Shared.TileColor;

public class ButtonResign extends Button {

    public ButtonResign() {
        super(Buttons.Resign,1);
        setOnMousePressed(e -> {
            setImage(imgPressed);
        });
        setOnMouseReleased(e -> {
            if (Game.getNextTurn() == TileColor.WHITE) {
                Model.sendControllerMsg(new WinnerMsg(TileColor.BLACK));

            } else {
                Model.sendControllerMsg(new WinnerMsg(TileColor.WHITE));
            }
            setImage(img);
        });
    }
    
}
