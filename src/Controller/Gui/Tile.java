package Controller.Gui;

import Model.Model;
import MsgPass.ModelMsg.TilePressedMsg;
import Shared.TileColor;
import Shared.TilePosition;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

public class Tile extends StackPane {
    private ImageView imageView;
    private TilePosition position;
    TileColor tilecolor = null;

    // init af tiles
    static private Image legal_tile;
    static private Image empty_tile;
    static private Image white_tile;
    static private Image black_tile;

    public Tile(int x, int y) {
        position = new TilePosition(x, y);
        imageView = new ImageView();
        
        imageView.setImage(getEmptyImage());
        imageView.setOnMouseClicked(e -> {
            Model.sendGameMsg(new TilePressedMsg(position));
        });

        getChildren().add(imageView);
        setAlignment(Pos.CENTER);
    }

    private Image getEmptyImage() {
        if (empty_tile == null) {
            empty_tile = new Image("/Assets/stoneTileEmpty.png", Gui.fitTileSize(), 0, true, false);
        }
        return empty_tile;
    }

    private Image getLegalImage() {
        if (legal_tile == null) {
            legal_tile = new Image("/Assets/stoneTilePossibleMove.png", Gui.fitTileSize(), 0, true, false);
        }
        return legal_tile;
    }

    private Image getWhiteImage() {
        if (white_tile == null) {
            white_tile = new Image("/Assets/stoneTileWhite.png", Gui.fitTileSize(), 0, true, false);
        }
        return white_tile;
    }

    private Image getBlackImage() {
        if (black_tile == null) {
            black_tile = new Image("/Assets/stoneTileBlack.png", Gui.fitTileSize(), 0, true, false);
        }
        return black_tile;
    }

    private boolean isLegalMove = false;

    public void setLegalImage() {
        isLegalMove = true;
        imageView.setImage(getLegalImage());
    }

    public void resetLegalMove() {
        if (isLegalMove) {
            isLegalMove = false;
            imageView.setImage(getEmptyImage());
        }
    }

    public void setTilecolor(TileColor tilecolor) {
        isLegalMove = false;
        this.tilecolor = tilecolor;
        switch (tilecolor) {
            case WHITE:
                imageView.setImage(getWhiteImage());
                break;
            case BLACK:
                imageView.setImage(getBlackImage());
                break;
            case default:
                break;
        }
    }

    public void switchTilecolor(TileColor tilecolor) {
        isLegalMove = false;
        this.tilecolor = tilecolor;
        switch (tilecolor) {
            case WHITE:
                this.getChildren().clear();
                this.getChildren().add(TileAnimation.flipToWhite());
                break;
            case BLACK:
                this.getChildren().clear();
                this.getChildren().add(TileAnimation.flipToBlack());
                break;
            case default:
                break;

        }
    }
}
