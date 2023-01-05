package Controller.Gui;

import javafx.geometry.Insets;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.paint.Color;

public class BotMenu extends BorderPane {
    BotMenu() {

        setLeft(new PassButton());
        setRight(new RestartBtn());

        setMargin(getLeft(), new Insets(5,20,5,20));
        setMargin(getRight(), new Insets(5,20,5,20));
        //setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, null)));
        //setWidth(8 * Gui.fitTileSize() / 11);

    }

    public PassButton getPassButton() {
        try {
            return (PassButton) this.getLeft();
        } catch (Exception e) {
            return null;
        }
    }
}
