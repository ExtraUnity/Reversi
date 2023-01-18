package Controller.Gui;
//Filen er skrevet af Katinka
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;



public class MenuDisplayCharacter extends BorderPane{
    VBox vBox = new VBox();
    Image character;

    public MenuDisplayCharacter(PlayerCharacter character){
        this.character = new Image("/Assets/characters/" + character + "Turn.png", Gui.fitTileSize() * 4.5, 0, true, false);

        vBox.getChildren().add(new ImageView(this.character));
        
        setBottom(vBox);
        setMargin(getBottom(), new Insets(0, 100, 0, 0));
        
        vBox.setAlignment(Pos.BOTTOM_RIGHT);
    }

}
