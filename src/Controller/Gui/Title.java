package Controller.Gui;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Title extends ImageView {
    protected Image img;

    public Title() {
        this.img = new Image("/Assets/titleReversi.png", 0, Gui.fitTileSize()*3, true, false);
        setImage(img);
    }
}
