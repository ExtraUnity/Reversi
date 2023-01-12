package Controller.Gui;

import javafx.geometry.Insets;
import javafx.scene.layout.BorderPane;

public class MenuMainBottom extends BorderPane{
    
    public MenuMainBottom(){
        setCenter(new ButtonExitGame());

        setMargin(getCenter(), new Insets(64, 0, 0, 0));
    }
}
