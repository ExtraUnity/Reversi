package Controller.Gui;

import Model.Model;
import MsgPass.ModelMsg.TilePressedMsg;
import Shared.TileColor;
import Shared.TilePosition;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

public class Tile extends BorderPane {
    private StackPane stackPane;
    private ImageView imageView;
    private TilePosition position;
    TileColor tilecolor = null;

    // init af tiles
    static private Image white_tile;
    static private Image black_tile;
    static private Image legal_tile;
    static private Image empty_tile;

    public Tile(int x, int y) {
        position = new TilePosition(x, y);

        stackPane = new StackPane();
        imageView = new ImageView();

        
        imageView.setImage(getEmptyImage());
        imageView.setOnMouseClicked(e -> {
            Model.sendGameMsg(new TilePressedMsg(position));
        });

        stackPane.getChildren().add(imageView);
        stackPane.setAlignment(Pos.CENTER);

        setCenter(stackPane);
    }

    private Image getEmptyImage() {
        if (empty_tile == null) {
            var empty_tile_src = getClass().getResourceAsStream("/Assets/stoneTileEmpty.png");
            empty_tile = new Image(empty_tile_src, Gui.fitTileSize(), 0, true, false);

        }
        return empty_tile;
    }

    /*
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
    */

    private Image getLegalImage() {
        if (legal_tile == null) {
            var legal_tile_src = getClass().getResourceAsStream("/Assets/stoneTilePossibleMove.png");
            legal_tile = new Image(legal_tile_src, Gui.fitTileSize(), 0, true, false);

        }
        return legal_tile;
    }

    /*
    private static URL whiteToBlack;
    private static URL blackToWhite;

    private Image getSwitchToWhite() {
        if (blackToWhite == null) {
            blackToWhite = getClass().getResource("/Assets/stoneTileBlackToWhite.gif");
        }
        try {
            var switch_to_white = new Image(blackToWhite.openStream(), Gui.fitTileSize(), 0, true, false);
            return switch_to_white;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    private Image getSwitchToBlack() {
        if (whiteToBlack == null) {
            whiteToBlack = getClass().getResource("/Assets/stoneTileWhiteToBlack.gif");
        }
        try {
            var switch_to_black = new Image(whiteToBlack.openStream(), Gui.fitTileSize(), 0, true, false);
            return switch_to_black;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    */
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

    //private volatile int animationIndex = 0;
    /*
    public void switchTilecolor(TileColor newColor) {
        isLegalMove = false;
        this.tilecolor = newColor;
        if (newColor == TileColor.WHITE)
            imageView.setImage(getSwitchToWhite());
        else
            imageView.setImage(getSwitchToBlack());

        animationIndex += 1;
        int newAnimationIndex = animationIndex;

        new Thread(() -> {
            try {
                Thread.sleep(1000);
                Platform.runLater(() -> {
                    //For at garanterer at ting ikke fucker up
                    if (animationIndex == newAnimationIndex) {
                        setTilecolor(newColor);
                    }
                });
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

    }
    */
    public void setTilecolor(TileColor tilecolor) {
        isLegalMove = false;
        this.tilecolor = tilecolor;
        switch (tilecolor) {
            case WHITE:
                stackPane.getChildren().clear();
                stackPane.getChildren().add(TileAnimation.flipToWhite());
                break;
            case BLACK:
                stackPane.getChildren().clear();
                stackPane.getChildren().add(TileAnimation.flipToBlack());
                break;

            case default:
                break;

        }
    }

}
