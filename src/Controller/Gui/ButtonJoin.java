package Controller.Gui;

import Server.ServerConn;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class ButtonJoin extends Button {
    
    public ButtonJoin(TextField inputfield, Text errorLabel) {
        super(Buttons.Join, 1);
        setOnMousePressed(e -> {
            setImage(imgPressed);
        });
        setOnMouseReleased(e -> {
            var error = ServerConn.joinGame(inputfield.getText());
            errorLabel.setText(error);
            setImage(img);
        });
    }
    
}
