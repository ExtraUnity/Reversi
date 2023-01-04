package Controller.Gui;

import javafx.application.Platform;
import javafx.scene.layout.Pane;

public class TopMenu extends Pane {
    TopMenu() {
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
