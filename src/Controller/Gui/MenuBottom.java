package Controller.Gui;

//Filen er skrevet af Christian
import Model.GameOptions;
import javafx.geometry.Insets;
import javafx.scene.layout.BorderPane;

public class MenuBottom extends BorderPane {

    MenuBottom(GameOptions gameOptions) {

        setLeft(new ButtonPass());
        setRight(new ButtonResign());
        // Only create timer if used
        if (gameOptions.gametime > 0) {
            setCenter(new Timer(gameOptions));
        }

        setMargin(getLeft(), new Insets(0, 20, 0, 20));
        setMargin(getRight(), new Insets(0, 20, 0, 20));

    }

    public ButtonPass getButtonPass() {
        try {
            return (ButtonPass) this.getLeft();
        } catch (Exception e) {
            return null;
        }
    }
}
