package Controller.Gui;

import Shared.TileColor;
import Model.GameOptions;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.layout.BorderPane;

public class MenuSide extends BorderPane {
    MenuSide() {
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
        // setFillWidth(true);
        setPrefWidth(width);
    }
}

class MenuRight extends MenuSide {
    MenuRight(GameOptions gameOptions) {
        if (gameOptions.countPoints) {
            setTop(new PointCounter(TileColor.BLACK));
            setMargin(getTop(), new Insets(5));
        }

        setBottom(new TurnIndication(PlayerCharacter.GenghisKhan, gameOptions.startPlayer == TileColor.BLACK));

    }
}

class MenuLeft extends MenuSide {
    MenuLeft(GameOptions gameOptions) {
        if (gameOptions.countPoints) {
            setCenter(new PointCounter(TileColor.WHITE));
            setMargin(getCenter(), new Insets(5));
        }
        setTop(new TurnIndication(PlayerCharacter.Stalin, gameOptions.startPlayer == TileColor.WHITE));

    }
}