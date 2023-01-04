package Controller.Gui;

import javafx.geometry.Pos;
import javafx.scene.layout.VBox;

public class SideMenu extends VBox {
    SideMenu() {
        Gui.stackRoot.widthProperty().addListener((obs, oldVal, newVal) -> {
            double windowWidth = Gui.stackRoot.getWidth();
            double width = windowWidth / 2 - 8 * Gui.fitTileSize() / 2;
            setFillWidth(true);
            setPrefWidth(width);
        });
    }
}

class RightMenu extends SideMenu {
    RightMenu() {
        setAlignment(Pos.TOP_CENTER);
    }
}

class LeftMenu extends SideMenu {
    LeftMenu() {
        setAlignment(Pos.BOTTOM_CENTER);
    }
}