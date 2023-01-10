package Controller.Gui;

import Server.ServerConn;
import javafx.scene.control.TextField;

public class ButtonJoin extends Button {
    
    //lbare indtil thor's server er oppe igen
    public ButtonJoin() {
        super(Buttons.Join, 1);
        setOnMousePressed(e -> {
            setImage(imgPressed);
        });
        setOnMouseReleased(e -> {
            setImage(img);
        });
    }
    /*
    public ButtonJoin(ServerConn conn, TextField inputfield) {
        super(Buttons.Join, 1);
        setOnMousePressed(e -> {
            setImage(imgPressed);
        });
        setOnMouseReleased(e -> {
            conn.tryJoin(inputfield.getText());
            setImage(img);
        });
    }
    */
}
