package Controller.Gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

public class MenuMainCenter extends BorderPane{
    //HBox menuCenter = new HBox();
    
    public MenuMainCenter(){
        /*
        var classicButton = new ButtonClassic();
        var aiButton = new ButtonAI();
        var multiplayerButton = new ButtonMultiplayer();

        menuCenter.getChildren().add(classicButton);
        menuCenter.getChildren().add(aiButton);
        menuCenter.getChildren().add(multiplayerButton);
        menuCenter.setAlignment(Pos.CENTER);
        menuCenter.setSpacing(40);
        */

        setLeft(new ButtonMultiplayer());
        setCenter(new ButtonClassic());
        setRight(new ButtonAI());

        setMargin(getLeft(), new Insets(50, 0, 0, Gui.fitTileSize()*3));
        setMargin(getCenter(), new Insets(50, 0, 0, 0));
        setMargin(getRight(), new Insets(50, Gui.fitTileSize()*3, 0, 0));

    }
}
