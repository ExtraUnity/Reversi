package Controller.Gui;

import java.io.InputStream;
import Model.Model;
import MsgPass.ModelMsg.PassMsg;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class PassButton extends ImageView {
    private Image img;
    private Image imgPressed;

    PassButton() {
        InputStream src = getClass().getResourceAsStream("/Assets/passButton.png");
        this.img = new Image(src, 0, Gui.fitTileSize(), true, false);
        
        InputStream src2 = getClass().getResourceAsStream("/Assets/passButtonPressed.png");
        this.imgPressed = new Image(src2, 0, Gui.fitTileSize(), true, false);

        setImage(img);
        setOnMousePressed(e -> {
            setImage(imgPressed);
            Model.sendModelMsg(new PassMsg());
        });
        setOnMouseReleased(e -> {
            setImage(img);
        });
    }

    public Image getImg() {
        return this.img;
    }
}
