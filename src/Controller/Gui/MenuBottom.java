package Controller.Gui;

import Model.GameOptions;
import javafx.geometry.Insets;
import javafx.scene.layout.BorderPane;

public class MenuBottom extends BorderPane {
    MenuBottom(GameOptions gameOptions) {

        setLeft(new ButtonPass());
        setRight(new ButtonResign());
        // Lav kun timeren hvis den skal bruges
        if (gameOptions.gametime > 0) {
            setCenter(new Timer(gameOptions));
        }

        setMargin(getLeft(), new Insets(5, 20, 5, 20));
        setMargin(getRight(), new Insets(5, 20, 5, 20));

    }

    public ButtonPass getButtonPass() {
        try {
            return (ButtonPass) this.getLeft();
        } catch (Exception e) {
            return null;
        }
    }
}
