package Controller.Gui;

import java.io.InputStream;

import Model.Model;
import MsgPass.ModelMsg.TilePressedMsg;
import Shared.TileColor;
import Shared.TilePosition;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Tile extends ImageView {
    private TilePosition position;
    TileColor tilecolor = null;

    // init af tiles
    static private InputStream white_tile_src;
    static private Image white_tile;
    static private InputStream black_tile_src;
    static private Image black_tile;
    static private InputStream legal_tile_src;
    static private Image legal_tile;
    static private InputStream empty_tile_src;
    static private Image empty_tile;

    public Tile(int x, int y) {
        position = new TilePosition(x, y);
        setImage(getEmptyImage());
        setOnMouseClicked(e -> {
            Model.sendGameMsg(new TilePressedMsg(position));
        });
    }

    private Image getEmptyImage() {
        if (empty_tile == null) {
            empty_tile_src = getClass().getResourceAsStream("/Assets/stoneTileEmpty.png");
            empty_tile = new Image(empty_tile_src, Gui.fitTileSize(), 0, true, false);

        }
        return empty_tile;
    }

    private Image getWhiteImage() {
        if (white_tile == null) {
            white_tile_src = getClass().getResourceAsStream("/Assets/stoneTileWhite.png");
            white_tile = new Image(white_tile_src, Gui.fitTileSize(), 0, true, false);

        }
        return white_tile;
    }

    private Image getBlackImage() {
        if (black_tile == null) {
            black_tile_src = getClass().getResourceAsStream("/Assets/stoneTileBlack.png");
            black_tile = new Image(black_tile_src, Gui.fitTileSize(), 0, true, false);
        }
        return black_tile;
    }

    private Image getLegalImage() {
        if (legal_tile == null) {
            legal_tile_src = getClass().getResourceAsStream("/Assets/stoneTilePossibleMove.png");
            legal_tile = new Image(legal_tile_src, Gui.fitTileSize(), 0, true, false);

        }
        return legal_tile;
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

    public void setTilecolor(TileColor tilecolor) {
        isLegalMove = false;
        this.tilecolor = tilecolor;
        System.out.println("Settings tile " + position + " to " + tilecolor);
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
