package Controller.Gui;

//Filen er skrevet af Katinka
//Button Super-Klassen til alle vores custom buttons, her hentes de Assets der passer til hver button
//og sætter dem til henholdsvis knappen og knappen når den bliver trykket på.
import java.io.InputStream;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Button extends ImageView {
    protected Image img;
    protected Image imgPressed;

    public Button(Buttons button, double i) {
        InputStream src = getClass().getResourceAsStream("/Assets/Button" + button.name() + ".png");
        this.img = new Image(src, 0, Gui.fitTileSize() * i, true, false);

        InputStream src2 = getClass().getResourceAsStream("/Assets/Button" + button.name() + "Pressed.png");
        this.imgPressed = new Image(src2, 0, Gui.fitTileSize() * i, true, false);
        
        setImage(img);
    }
}
