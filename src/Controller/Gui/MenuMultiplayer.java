package Controller.Gui;

import Server.ServerConn;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class MenuMultiplayer extends BorderPane {

    //bare indtil thors server er oppe igen.
    /* 
    public MenuMultiplayer(){
        VBox dialogVbox = new VBox(20);
        dialogVbox.getChildren().add(new Text("Your id: "));

        setCenter(new ButtonJoin());
    }
     */
    
    public MenuMultiplayer(ServerConn conn, TextField inputfield){
        //have the textfield that lets you enter in your game key
        VBox dialogVbox = new VBox(20);
        dialogVbox.getChildren().add(new Text("Your id: " + conn.netId));
        dialogVbox.getChildren().add(inputfield);
        

        setCenter(new ButtonJoin(conn , inputfield));
    }
    
    
}
