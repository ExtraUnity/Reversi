package Controller.Gui;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class SideMenu extends VBox {
    SideMenu() {
        Gui.stackRoot.widthProperty().addListener((obs, oldVal, newVal) -> {
            updateSize();
        });
        setBorder(new Border(new BorderStroke(Color.BLACK,BorderStrokeStyle.SOLID,null,null)));
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
        setAlignment(Pos.BOTTOM_CENTER);
        getChildren().add(new TurnIndication(PlayerCharacter.GenghisKhan, true));
    }
}

class LeftMenu extends SideMenu {
    LeftMenu() {
        setAlignment(Pos.TOP_CENTER);
        getChildren().add(new TurnIndication(PlayerCharacter.Stalin, false));
    }
}