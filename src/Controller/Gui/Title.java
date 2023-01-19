package Controller.Gui;
//Filen er skrevet af Katinka
//klassen indlæser title til hovedmenuen
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Title extends ImageView {
    protected Image img;

    public Title() {
        this.img = new Image("/Assets/titleReversi.png", 0, Gui.fitTileSize()*2, true, false);
        setImage(img);
    }
}
