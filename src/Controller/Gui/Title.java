package Controller.Gui;

import java.io.InputStream;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Title extends ImageView {
    protected Image img;

    public Title() {
        var path = "/Assets/titleReversi.png";

        InputStream src = getClass().getResourceAsStream(path);
        this.img = new Image(src, 0, Gui.fitTileSize()*3, true, false);
        setImage(img);
    }
}
