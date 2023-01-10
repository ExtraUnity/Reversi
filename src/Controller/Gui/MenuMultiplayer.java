package Controller.Gui;

import Server.ServerConn;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;


public class MenuMultiplayer extends BorderPane {

    public MenuMultiplayer(ServerConn conn){
        VBox dialogVbox = new VBox();
        Text key = new Text("Your id: " + conn.netId);
        TextField text = new TextField();

        text.setMaxWidth(Gui.fitTileSize()*2);
        text.setFont(Font.font("verdana", FontWeight.BLACK, FontPosture.REGULAR, 20));

        key.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 20));
    
        dialogVbox.getChildren().add(key);
        dialogVbox.getChildren().add(text);
        dialogVbox.getChildren().add(new ButtonJoin(conn , text));
        dialogVbox.getChildren().add(new ButtonExitGame());

        dialogVbox.setAlignment(Pos.CENTER);
        dialogVbox.setSpacing(15);
        setCenter(dialogVbox);
        
        setMargin(getCenter(), new Insets(64, 0, 0, 0));
    }
    
    
}
