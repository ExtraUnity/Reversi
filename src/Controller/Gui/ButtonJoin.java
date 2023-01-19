package Controller.Gui;
//Filen er skrevet af Katinka
//Join game knap. Beder ServerConn om at tjekke at det korrekte id er intastet i text feltet på Join-menuen
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
