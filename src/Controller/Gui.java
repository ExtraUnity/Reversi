package Controller;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Gui extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        System.out.println("peter pedal");
        // initialisering af grundpanel
        stage.setTitle("Reversi");
        BorderPane root = new BorderPane();
        stage.setScene(new Scene(root,500,500));
        stage.show();
        stage.setMinHeight(400);
        stage.setMinWidth(400);
    }
    
}
