package Controller.Gui;


import javafx.geometry.Pos;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import java.util.ArrayList;


public class MenuCharacterSelection extends BorderPane{
    GridPane gridpane = new GridPane();

    public MenuCharacterSelection(){
        ArrayList<String> imgList = getCharacterAddress();

        for(int i = 0; i < imgList.size(); i++){
        gridpane.add(new ImageView(imgList.get(i)), i, 0);
        }
        gridpane.setAlignment(Pos.CENTER);

        setCenter(gridpane);

        setOnMousePressed(e -> {
        });
        setOnMouseReleased(e -> {
            System.out.println("character pressed and released ");
            //spørg Thor hvordan man får det til at virke
            //Model.sendGameMsg(new CharacterSelectedMsg(position,character));
        });
    }


    private ArrayList<String> getCharacterAddress(){
        ArrayList<String> img = new ArrayList<String>();
        for (PlayerCharacter character : PlayerCharacter.values()) {
            String name = "/Assets/characters/" + character + ".png";
            img.add(name);
        }
        System.out.println(img.toString());
        return img;
    }
}
