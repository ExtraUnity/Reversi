package Controller.Gui;

import Server.ServerConn;
import javafx.geometry.Insets;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.text.Text;


public class MenuMultiplayer extends BorderPane {

    public MenuMultiplayer(ServerConn conn){
        VBox dialogVbox = new VBox(20);
        TextField text = new TextField();
        ButtonJoin join = new ButtonJoin(conn , text);   

        text.setMaxWidth(Gui.fitTileSize());
        
        dialogVbox.getChildren().add(new Text("Your id: " + conn.netId));
        dialogVbox.getChildren().add(text);
        dialogVbox.getChildren().add(join);
        
        setCenter(dialogVbox);

        setMargin(getCenter(), new Insets(64, 0, 0, 0));
    }
    
    
}
