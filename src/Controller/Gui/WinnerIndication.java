package Controller.Gui;

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
        winner = new Image(winnerSrc,0, Gui.fitTileSize(), true, false);
        setImage(winner);
    }
}
