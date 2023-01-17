package Controller.Gui;

import java.util.ArrayList;

import Shared.TilePosition;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

public class Board extends HBox {
    HBox backPane;
    GridPane gridpane;

    public Board() {
        backPane = new HBox();
        gridpane = new GridPane();
        //setWidth(8 * Gui.getScreenWidth() / 11);
        //setHeight(8 * Gui.getScreenHeight() / 11);
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                Tile tile = new Tile(x, y);
                gridpane.add(tile, x, y);
            }
        }
        System.out.println(gridpane.toString());
        backPane.getChildren().add(gridpane);
        //setCenter(backPane);
    }

    public Tile getTile(TilePosition position) {
        return (Tile) getChildren().get(position.x * 8 + position.y);
    }

    public HBox[] getAllTiles() {
        var allTiles = new ArrayList<HBox>();
        for (var tile : getChildren()) {
            allTiles.add((HBox)tile);
        }
        return allTiles.toArray(new HBox[0]);
    }
}
