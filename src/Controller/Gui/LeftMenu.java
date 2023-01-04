package Controller.Gui;

import javafx.scene.layout.VBox;

public class LeftMenu extends VBox {
    LeftMenu() {
        Gui.stackRoot.widthProperty().addListener((obs, oldVal, newVal) -> {
            double windowWidth = Gui.stackRoot.getWidth();
            double width = windowWidth / 2 - (8 * Gui.fitTileSize() / 2);
            setFillWidth(true);
            setPrefWidth(width);
        });
    }
}
