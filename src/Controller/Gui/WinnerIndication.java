package Controller.Gui;

import java.io.InputStream;
import Shared.TileColor;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class WinnerIndication extends ImageView {
    static private InputStream winnerSrc;
    static private Image winner;

    public WinnerIndication(TileColor color) {
        if (color == TileColor.BLACK) {
            winnerSrc = getClass().getResourceAsStream("/Assets/sortVinder.png");
            winner = new Image(winnerSrc);
            setImage(winner);
        } else {
            winnerSrc = getClass().getResourceAsStream("/Assets/hvidVinder.png");
            winner = new Image(winnerSrc);
            setImage(winner);
        }
    }
}
