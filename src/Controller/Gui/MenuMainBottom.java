package Controller.Gui;

import javafx.geometry.Insets;
import javafx.scene.layout.BorderPane;
import javafx.geometry.Pos;

public class MenuMainBottom extends BorderPane {

    public MenuMainBottom() {
        setCenter(new ButtonExitGame());
        setMargin(getCenter(), new Insets(64, 0, 0, 0));

        setBottom(Gui.muteButton);
        setAlignment(getBottom(), Pos.BOTTOM_LEFT);
        setMargin(getBottom(), new Insets(55, 0, 0, 0));
    }
}
