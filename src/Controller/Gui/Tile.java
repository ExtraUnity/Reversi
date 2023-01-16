package Controller.Gui;

import Model.Model;
import MsgPass.ModelMsg.TilePressedMsg;
import Shared.TileColor;
import Shared.TilePosition;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Tile extends ImageView {
    private TilePosition position;
    TileColor tilecolor = null;

    // init af tiles
    static private Image white_tile;
    static private Image black_tile;
    static private Image legal_tile;
    static private Image empty_tile;
    static private Image switch_to_white;
    static private Image switch_to_black;
    // static private InputStream empty_tile_src;
    // static private Image empty_tile;

    public Tile(int x, int y) {
        position = new TilePosition(x, y);
        setImage(getEmptyImage());
        setOnMouseClicked(e -> {
            Model.sendGameMsg(new TilePressedMsg(position));
        });
    }

    private Image getEmptyImage() {
        if (empty_tile == null) {
            var empty_tile_src = getClass().getResourceAsStream("/Assets/stoneTileEmpty.png");
            empty_tile = new Image(empty_tile_src, Gui.fitTileSize(), 0, true, false);

        }
        return empty_tile;
    }

    private Image getWhiteImage() {
        if (white_tile == null) {
            var white_tile_src = getClass().getResourceAsStream("/Assets/stoneTileWhite.png");
            white_tile = new Image(white_tile_src, Gui.fitTileSize(), 0, true, false);

        }
        return white_tile;
    }

    private Image getBlackImage() {
        if (black_tile == null) {
            var black_tile_src = getClass().getResourceAsStream("/Assets/stoneTileBlack.png");
            black_tile = new Image(black_tile_src, Gui.fitTileSize(), 0, true, false);
        }
        return black_tile;
    }

    private Image getLegalImage() {
        if (legal_tile == null) {
            var legal_tile_src = getClass().getResourceAsStream("/Assets/stoneTilePossibleMove.png");
            legal_tile = new Image(legal_tile_src, Gui.fitTileSize(), 0, true, false);

        }
        return legal_tile;
    }

    private Image getSwitchToWhite() {
        if (switch_to_white == null) {
            var switch_to_white_src = getClass().getResourceAsStream("/Assets/stoneTileWhiteToBlack.gif");
            switch_to_white = new Image(switch_to_white_src, Gui.fitTileSize(), 0, true, false);
        }
        return switch_to_white;
    }

    private Image getSwitchToBlack() {
        if (switch_to_black == null) {
            var switch_to_black_src = getClass().getResourceAsStream("/Assets/stoneTileBlackToWhite.gif");
            switch_to_black = new Image(switch_to_black_src, Gui.fitTileSize(), 0, true, false);
        }
        return switch_to_black;
    }

    private boolean isLegalMove = false;

    public void setLegalImage() {
        isLegalMove = true;
        setImage(getLegalImage());
    }

    public void resetLegalMove() {
        if (isLegalMove) {
            isLegalMove = false;
            setImage(getEmptyImage());
        }
    }

    public void switchTilecolor(TileColor newColor) {
        if (newColor == TileColor.WHITE)
            setImage(getSwitchToWhite());
        else
            setImage(getSwitchToBlack());

        new Thread(() -> {
            try {
                Thread.sleep(1000);
                Platform.runLater(() -> {
                    setTilecolor(newColor);
                });
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

    }

    public void setTilecolor(TileColor tilecolor) {
        isLegalMove = false;
        this.tilecolor = tilecolor;
        switch (tilecolor) {
            case WHITE:
                setImage(getWhiteImage());
                break;
            case BLACK:
                setImage(getBlackImage());
                break;

            case default:
                break;

        }
    }

}
