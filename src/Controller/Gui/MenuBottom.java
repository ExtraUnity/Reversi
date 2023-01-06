package Controller.Gui;

import javafx.geometry.Insets;
import javafx.scene.layout.BorderPane;

public class MenuBottom extends BorderPane {
    MenuBottom() {

        setLeft(new ButtonPass());
        setRight(new ButtonResign());

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
