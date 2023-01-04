package Controller.Gui;

import javafx.scene.layout.Pane;

public class TopMenu extends Pane {
    TopMenu() {
        Gui.stackRoot.heightProperty().addListener((obs, oldVal, newVal) -> {
            double windowHeight = Gui.stackRoot.getHeight();
            double height = windowHeight / 2 - (8 * Gui.fitTileSize() / 2);
            //setFillHeight(true);
            setPrefHeight(height);
        });
    }
}
