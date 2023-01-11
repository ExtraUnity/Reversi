package Controller.Gui;


import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import java.util.ArrayList;
import Model.Model;
import MsgPass.ModelMsg.CharacterSelectedMsg;
import Server.ServerConn;


public class MenuCharacterSelection extends BorderPane{
    FlowPane flowPane = new FlowPane();

    public MenuCharacterSelection(ServerConn conn){

        ArrayList<ImageView> imgList =  getCharacterAddress(conn);
        for(int i = 0; i < imgList.size(); i++){
            flowPane.getChildren().add(imgList.get(i));
            }
            flowPane.setAlignment(Pos.CENTER);
            setCenter(flowPane);

    }

    private ArrayList<ImageView> getCharacterAddress(ServerConn conn){
        ArrayList<ImageView> img = new ArrayList<ImageView>();
        for (PlayerCharacter character : PlayerCharacter.values()) {
            String name = "/Assets/characters/" + character + ".png";
            ImageView image = new ImageView(new Image(name, 0, Gui.fitTileSize()*3, true, false));
            image.setOnMouseReleased(e -> {
                System.out.println("pressed " + character);
                Gui.setSelectedCharacter(character);
                Gui.makeMultiplayerMenu(conn);
                Model.sendGameMsg(new CharacterSelectedMsg(character));
            });
            img.add(image);
        }
        System.out.println(img.toString());
        return img;
    }

}
