package Controller.Gui;

import java.io.InputStream;
import Model.Model;
import MsgPass.ModelMsg.PassMsg;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class PassButton extends ImageView {
    private Image img;
    private InputStream src;
    private double size;
    private boolean available;

    PassButton() {
        this.src = getClass().getResourceAsStream("/Assets/notPassButton.png");
        this.size = Gui.fitTileSize() * 3;
        this.img = new Image(src, size, 0, true, false);

        setImage(img);
        setOnMouseClicked(e -> {
            Model.sendGameMsg(new PassMsg());
        });
    }

    public Image getImg() {
        return this.img;
    }

    public void setImage(String src) {
        this.src = getClass().getResourceAsStream(src);
        this.img = new Image(src, this.size, 0, true, false);
        setImage(img);
        System.out.println("Setting image as " + src);
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public boolean getAvailable() {
        return this.available;
    }
}
