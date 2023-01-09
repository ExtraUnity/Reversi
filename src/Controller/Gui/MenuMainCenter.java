package Controller.Gui;

import javafx.geometry.Insets;
import javafx.scene.layout.BorderPane;

public class MenuMainCenter extends BorderPane{
    //HBox menuCenter = new HBox();
    
    public MenuMainCenter(){

        setLeft(new ButtonMultiplayer());
        setCenter(new ButtonClassic());
        setRight(new ButtonAI());

        setMargin(getLeft(), new Insets(0, 0, 0, Gui.fitTileSize()*3));
        setMargin(getCenter(), new Insets(0, 0, 0, 0));
        setMargin(getRight(), new Insets(0, Gui.fitTileSize()*3, 0, 0));

    }
}
