package Controller.Gui;

import java.io.InputStream;
import Model.Model;
import MsgPass.ModelMsg.PassMsg;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ButtonPass extends ImageView {
    private Image img;
    private InputStream src;
    private double size;
    private boolean available;
    private Image imgPressed;

    ButtonPass() {
        this.src = getClass().getResourceAsStream("/Assets/ButtonPassGrey.png");
        this.size = Gui.fitTileSize();
        this.img = new Image(src, 0, size, true, false);

        InputStream src2 = getClass().getResourceAsStream("/Assets/ButtonPassPressed.png");
        this.imgPressed = new Image(src2, 0, Gui.fitTileSize(), true, false);

        setImage(img);
    }

    public Image getImg() {
        return this.img;
    }

    public void setImage(String src) {
        this.src = getClass().getResourceAsStream(src);
        this.img = new Image(src, 0, this.size, true, false);
        setImage(img);
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public boolean getAvailable() {
        return this.available;
    }

    public void updatePressed() {
        if (available) {
            setOnMousePressed(e -> {
                setImage(imgPressed);
            });
            setOnMouseReleased(e -> {
                setImage(img);
                Model.sendGameMsg(new PassMsg());
            });
        } else {
            setOnMousePressed(e -> {
            });
            setOnMouseReleased(e -> {
            });
        }
    }
}
