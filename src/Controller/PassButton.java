package Controller;

import java.io.InputStream;

import Model.Model;
import MsgPass.ModelMsg.PassMsg;
import MsgPass.ModelMsg.TilePressedMsg;
import Shared.TileColor;
import Shared.TilePosition;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class PassButton extends ImageView {
    private TilePosition position;
    private Image img;

    PassButton(int x, int y, InputStream src) {
        position = new TilePosition(x, y);
        this.img = new Image(src, Gui.fitPassSize(), 0, true, false);
        setImage(img);
        setOnMouseClicked(e -> {
            Model.sendModelMsg(new PassMsg());
        });
    }

    public TilePosition getPosition() {
        return this.position;
    }
}
