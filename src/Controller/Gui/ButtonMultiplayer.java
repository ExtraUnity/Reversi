package Controller.Gui;

import Server.ServerConn;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ButtonMultiplayer extends Button {


    public ButtonMultiplayer() {
        super(Buttons.Multiplayer, 4);

        setOnMousePressed(e -> {
            setImage(imgPressed);
        });
        setOnMouseReleased(e -> {

            /*
            ServerConn conn = new ServerConn();
            TextField inputfield = new TextField();
            setImage(img);
            Gui.makeMultiplayerMenu(conn , inputfield);
            */

            setImage(img);
            Gui.makeMultiplayerMenu();
            
            /*
            final Stage dialog = new Stage();
                dialog.initModality(Modality.APPLICATION_MODAL);
                dialog.initOwner(Gui.stage);
                VBox dialogVbox = new VBox(20);

                ServerConn conn = new ServerConn();

                dialogVbox.getChildren().add(new Text("Your id: " + conn.netId));
                var inputfield = new TextField();
                dialogVbox.getChildren().add(inputfield);
                var submitButton = new javafx.scene.control.Button("Join");
                submitButton.setOnMouseClicked(new EventHandler<javafx.event.Event>() {
                    @Override
                    public void handle(javafx.event.Event arg0) {
                        conn.tryJoin(inputfield.getText());
                    }
                });
                dialogVbox.getChildren().add(submitButton);
                Scene dialogScene = new Scene(dialogVbox, 300, 200);
                dialog.setScene(dialogScene);
                dialog.show();
                 */
        
        });
    }

}
