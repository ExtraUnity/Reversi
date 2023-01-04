package Controller.Gui;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.layout.VBox;

public class SideMenu extends VBox {
    SideMenu() {
        Gui.stackRoot.widthProperty().addListener((obs, oldVal, newVal) -> {
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
        double windowWidth = Gui.stackRoot.getWidth();
            double width = windowWidth / 2 - 8 * Gui.fitTileSize() / 2;
            setFillWidth(true);
            setPrefWidth(width);
    }
}

class RightMenu extends SideMenu {
    RightMenu() {
        setAlignment(Pos.TOP_CENTER);
        getChildren().add(new TurnIndication(PlayerCharacter.GenghisKhan, true));
    }
}

class LeftMenu extends SideMenu {
    LeftMenu() {
        setAlignment(Pos.BOTTOM_CENTER);
        getChildren().add(new TurnIndication(PlayerCharacter.Stalin, false));
    }
}