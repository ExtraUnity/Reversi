package Controller.Gui;

import javafx.scene.layout.VBox;

public class RightMenu extends VBox {
    RightMenu() {
        Gui.stackRoot.widthProperty().addListener((obs, oldVal, newVal) -> {
            double windowWidth = Gui.stackRoot.getWidth();
            double width = windowWidth / 2 - 8 * Gui.fitTileSize() / 2;
            setFillWidth(true);
            setPrefWidth(width);
        });
    }

}
