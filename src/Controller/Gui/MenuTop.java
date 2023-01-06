package Controller.Gui;

import java.io.InputStream;

import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;

public class MenuTop extends Pane {
    InputStream turn_src;
    Image turn;

    MenuTop() {
        Gui.stackRoot.heightProperty().addListener((obs, oldVal, newVal) -> {
            getChildren().add(new TurnIndication());
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
