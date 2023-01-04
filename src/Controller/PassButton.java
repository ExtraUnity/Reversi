package Controller;

import java.io.InputStream;
import Model.Model;
import MsgPass.ModelMsg.PassMsg;
import Shared.ButtonPosition;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class PassButton extends ImageView {
    private ButtonPosition position;
    private ButtonPosition size;
    private Image img;

    PassButton(double x, double y, ButtonPosition size, InputStream src) {
        this.size = size;
        position = new ButtonPosition(x, y);
        this.img = new Image(src, size.x, size.y, false, false);

        setImage(img);
        setOnMouseClicked(e -> {
            Model.sendModelMsg(new PassMsg());
        });
    }

    public ButtonPosition getPosition() {
        return this.position;
    }

    public ButtonPosition getSize() {
        return this.size;
    }

    public Image getImg() {
        return this.img;
    }
}
