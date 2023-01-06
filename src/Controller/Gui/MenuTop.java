package Controller.Gui;

import java.io.InputStream;

import Model.GameOptions;
import Shared.TileColor;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;

public class MenuTop extends BorderPane {
    InputStream turn_src;
    Image turn;

    MenuTop(GameOptions gameOptions) {
        setCenter(new TopTurnIndication(gameOptions.startPlayer));
        Gui.stackRoot.heightProperty().addListener((obs, oldVal, newVal) -> {
           
            updateSize();
        });
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                updateSize();
            }
        });
    }

    void updateSize() {
        double windowHeight = Gui.stackRoot.getHeight();
        double height = windowHeight / 2 - (8 * Gui.fitTileSize() / 2);
        // setFillHeight(true);
        setPrefHeight(height);
    }

}
