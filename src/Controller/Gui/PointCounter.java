package Controller.Gui;

import Shared.TileColor;
import javafx.geometry.Pos;
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
            setAlignment(Pos.TOP_RIGHT);
        }
        this.color = color;

    }

    public static void setWhitePoints(int points) {
        if (white != null) {
            white.setPoints(points);
        }

    }

    public static void setBlackPoints(int points) {
        if (black != null) {
            black.setPoints(points);
        }
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

    private static Image white;
    private static Image black;

    Point(TileColor color) {
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
            white = new Image("/Assets/whitePiece.png", Gui.fitTileSize() / 2, 0, true, false);
        }
        return white;
    }

    private Image getBlack() {
        if (black == null) {
            black = new Image("/Assets/blackPiece.png", Gui.fitTileSize() / 2, 0, true, false);
        }
        return black;
    }

}
