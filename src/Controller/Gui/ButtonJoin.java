package Controller.Gui;

import Server.ServerConn;
import javafx.scene.control.TextField;

public class ButtonJoin extends Button {
    
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
    
}
