package Controller.Gui;

import java.util.ArrayList;

import Shared.TilePosition;
import javafx.scene.layout.GridPane;

public class Board extends GridPane {

    public Board() {
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                Tile tile = new Tile(x, y);
                this.add(tile, x, y);
            }
        }
    }

    public Tile getTile(TilePosition position) {
        return (Tile) getChildren().get(position.x * 8 + position.y);
    }

    public Tile[] getAllTiles() {
        var allTiles = new ArrayList<Tile>();
        for (var tile : getChildren()) {
            allTiles.add((Tile)tile);
        }
        return allTiles.toArray(new Tile[0]);
    }
}
