package Controller.Gui;

import java.io.InputStream;

import Shared.TileColor;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;

public class PointCounter extends FlowPane {
    static PointCounter white;
    static PointCounter black;

    private TileColor color;

    PointCounter(TileColor color) {
        setHgap(10);
        setVgap(10);
        if (color == TileColor.WHITE) {
            white = this;

        } else {
            black = this;
        }
        this.color = color;

    }

    public static void setWhitePoints(int points) {
        white.setPoints(points);
    }

    public static void setBlackPoints(int points) {
        black.setPoints(points);
    }

    void setPoints(int points) {
        var pointsGui = getChildren();
        while (pointsGui.size() > points) {
            pointsGui.remove(0);
        }
        while (pointsGui.size() < points) {
            pointsGui.add(new Point(color));
        }
    }
}

class Point extends ImageView {

    private static InputStream white_src;
    private static Image white;
    private static InputStream black_src;
    private static Image black;

    Point(TileColor color) {
        System.out.println("POINT COLOR " + color);
        Image image;
        if (color == TileColor.WHITE) {
            image = getWhite();
        } else {
            image = getBlack();
        }
        setImage(image);
    }

    // get player
    private Image getWhite() {
        if (white == null) {
            white_src = getClass().getResourceAsStream("/Assets/whitePiece.png");
            white = new Image(white_src, Gui.fitTileSize() / 2, 0, true, false);
        }
        return white;
    }

    private Image getBlack() {
        if (black == null) {
            black_src = getClass().getResourceAsStream("/Assets/blackPiece.png");
            black = new Image(black_src, Gui.fitTileSize() / 2, 0, true, false);
        }
        return black;
    }

}
