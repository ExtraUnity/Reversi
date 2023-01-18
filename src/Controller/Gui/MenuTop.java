package Controller.Gui;

import Model.GameOptions;
import javafx.geometry.Pos;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

public class MenuTop extends BorderPane {
    HBox topMenu;

    MenuTop(GameOptions gameOptions) {
        topMenu = new HBox();
        TopTurnIndication turn = new TopTurnIndication(gameOptions.startPlayer);
        topMenu.getChildren().add(turn);
        topMenu.setAlignment(Pos.CENTER);

        setCenter(topMenu);
    }
}
