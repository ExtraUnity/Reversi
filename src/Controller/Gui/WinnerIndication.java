package Controller.Gui;
//Filen er skrevet af Frederik
/* Modtager en vinder som input og viser herefter det korresponderende billede */
import java.io.InputStream;
import Shared.TileColor;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class WinnerIndication extends ImageView {
    static private InputStream winnerSrc;
    static private Image winner;

    public WinnerIndication(TileColor color) {
        switch (color) {
            case BLACK:
                winnerSrc = getClass().getResourceAsStream("/Assets/sortVinder.png");
                break;

            case WHITE:
                winnerSrc = getClass().getResourceAsStream("/Assets/hvidVinder.png");
                break;

            case EMPTY:
                winnerSrc = getClass().getResourceAsStream("/Assets/draw.png");
                break;
            default:
                winnerSrc = null;
                break;

        }
        winner = new Image(winnerSrc, 0, Gui.fitTileSize() * 3, true, false);
        setImage(winner);
    }
}
