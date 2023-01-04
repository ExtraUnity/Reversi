package Controller.Gui;

import java.io.InputStream;
import Model.Model;
import MsgPass.ModelMsg.PassMsg;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class PassButton extends ImageView {
    private Image img;

    PassButton() {
        InputStream src = getClass().getResourceAsStream("/Assets/passButton.png");
        double size = Gui.fitTileSize() * 3;
        this.img = new Image(src, size, 0, true, false);

        
        setImage(img);
        setOnMouseClicked(e -> {
            Model.sendModelMsg(new PassMsg());
        });
    }

    public Image getImg() {
        return this.img;
    }
}
