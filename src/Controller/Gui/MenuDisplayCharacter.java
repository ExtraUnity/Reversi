package Controller.Gui;

import java.io.InputStream;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;



public class MenuDisplayCharacter extends BorderPane{
    BorderPane borderPane = new BorderPane();
    InputStream character_src;
    Image character;

    public MenuDisplayCharacter(PlayerCharacter character){
        this.character_src = getClass().getResourceAsStream("/Assets/characters/" + character + ".png");
        this.character = new Image(character_src, Gui.fitTileSize() * 4, 0, true, false);

        borderPane.getChildren().add(new ImageView(this.character));
        System.out.println("displaying " + character);
        
        setLeft(borderPane);
        
    }

}
