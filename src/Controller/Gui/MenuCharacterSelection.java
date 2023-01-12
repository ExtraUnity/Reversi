package Controller.Gui;

import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;

import java.io.InputStream;
import java.util.ArrayList;
import Server.ServerConn;

public class MenuCharacterSelection extends BorderPane{
    FlowPane flowPane = new FlowPane();
    protected Image avatar;
    protected Image avatarSelected;
    protected ImageView image;

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
            if(character == PlayerCharacter.Black || character == PlayerCharacter.White || character == PlayerCharacter.Computer) {
                continue;
            }
            InputStream src = getClass().getResourceAsStream("/Assets/characters/" + character + ".png");
            this.avatar = new Image(src, 0, Gui.fitTileSize() * 3, true, false);
    
            InputStream src2 = getClass().getResourceAsStream("/Assets/characters/" + character + "Turn.png");
            this.avatarSelected = new Image(src2, 0, Gui.fitTileSize() * 3, true, false);
            
            if(character == Gui.yourCharacter) {
            this.image = new ImageView(avatarSelected);
            } else {
            this.image = new ImageView(avatar);
            }

            image.setOnMouseReleased(e -> {
                ServerConn.setLoadedCharacter(character);
                Gui.setYourCharacter(character);
                Gui.makeMultiplayerMenu(conn);
            });
            img.add(image);
        } 
        return img;
    }

}
