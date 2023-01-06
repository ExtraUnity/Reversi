package Controller.Gui;

import Shared.TileColor;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.paint.Color;

public class MenuSide extends BorderPane {
    MenuSide() {
        Gui.stackRoot.widthProperty().addListener((obs, oldVal, newVal) -> {
            updateSize();
        });
        setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, null)));
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
        // setFillWidth(true);
        setPrefWidth(width);
    }
}

class MenuRight extends SideMenu {
    MenuRight() {
        setTop(new PointCounter(TileColor.BLACK));
        setMargin(getTop(), new Insets(5));
        setBottom(new TurnIndication(PlayerCharacter.GenghisKhan, true));

    }
}

class MenuLeft extends SideMenu {
    MenuLeft() {
        // setAlignment(Pos.TOP_CENTER);
        setTop(new TurnIndication(PlayerCharacter.Stalin, false));
        setCenter(new PointCounter(TileColor.WHITE));
        setMargin(getCenter(), new Insets(5));
    }
}