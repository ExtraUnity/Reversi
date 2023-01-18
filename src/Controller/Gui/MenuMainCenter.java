package Controller.Gui;

import javafx.geometry.Pos;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class MenuMainCenter extends BorderPane {
    HBox gameModeButtons;
    VBox menuLayout;

    public MenuMainCenter() {
        gameModeButtons = new HBox();
        menuLayout = new VBox();

        gameModeButtons.getChildren().addAll(new ButtonMultiplayer(),new ButtonClassic(),new ButtonAI());
        gameModeButtons.setAlignment(Pos.CENTER);
        gameModeButtons.setSpacing(Gui.fitTileSize());

        menuLayout.getChildren().addAll(new Title(), gameModeButtons, new ButtonExitGame());
        menuLayout.setAlignment(Pos.CENTER);
        menuLayout.setSpacing(Gui.fitTileSize()/2);

        setCenter(menuLayout);

    }
}
