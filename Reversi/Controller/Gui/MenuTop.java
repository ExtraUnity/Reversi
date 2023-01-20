package Controller.Gui;
//Filen er skrevet af Frederik
/* Opsætter layout for topmenuen i et igangværende spil */
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
