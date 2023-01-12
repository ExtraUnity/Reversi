package Controller.Gui;

import java.io.InputStream;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;



public class MenuDisplayCharacter extends BorderPane{
    VBox vBox = new VBox();
    InputStream character_src;
    Image character;

    public MenuDisplayCharacter(PlayerCharacter character){
        this.character_src = getClass().getResourceAsStream("/Assets/characters/" + character + "Turn.png");
        this.character = new Image(character_src, Gui.fitTileSize() * 5, 0, true, false);

        vBox.getChildren().add(new ImageView(this.character));
        
        setBottom(vBox);
        setMargin(getBottom(), new Insets(0, 100, 0, 0));
        
        vBox.setAlignment(Pos.BOTTOM_RIGHT);
    }

}
