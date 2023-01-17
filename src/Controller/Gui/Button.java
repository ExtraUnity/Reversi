package Controller.Gui;

import java.io.InputStream;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Button extends ImageView {
    protected Image img;
    protected Image imgPressed;

    public Button(Buttons button, int i) {
        var path = "/Assets/Button" + button.name() + ".png";
        System.out.println(path);
        InputStream src = getClass().getResourceAsStream(path);
        this.img = new Image(src, 0, Gui.fitTileSize() * i, true, false);


        InputStream src2 = getClass()
                .getResourceAsStream("/Assets/Button" + button.name() + "Pressed.png");
        this.imgPressed = new Image(src2, 0, Gui.fitTileSize() * i, true, false);
        setImage(img);

    }
}
