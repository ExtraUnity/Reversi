package Controller;

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

    public Tile(int x, int y) {
        position = new TilePosition(x, y);
        setOnMouseClicked(e -> {
            Model.sendModelMsg(new TilePressedMsg(position));
        });
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

    public void setTilecolor(TileColor tilecolor) {
        this.tilecolor = tilecolor;
        System.out.println("Settings tile " + position + " to " + tilecolor);
        switch (tilecolor) {
            case WHITE:
                setImage(getWhiteImage());
                break;
            case BLACK:
                setImage(getBlackImage());
                break;

        }
    }
}
