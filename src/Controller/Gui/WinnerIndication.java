package Controller.Gui;

import Model.Model;
import MsgPass.ModelMsg.WinnerMsg;
import Shared.TileColor;
import javafx.scene.image.ImageView;

public class WinnerIndication extends ImageView {

    public WinnerIndication(TileColor color) {
        if (color == TileColor.BLACK) {
            Model.sendGameMsg(new WinnerMsg(TileColor.BLACK));
        } else {
            Model.sendGameMsg(new WinnerMsg(TileColor.WHITE));
        }
    }
}
