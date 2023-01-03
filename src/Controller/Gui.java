package Controller;

import java.io.InputStream;
import java.util.stream.IntStream;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Gui extends Application {

    static void initGui(){
        launch(new String[] {});
    }

    @Override
    public void start(Stage stage) throws Exception {

        // initialisering af grundpanel
        stage.setTitle("Reversi");
        StackPane root = new StackPane();
        stage.setScene(new Scene(root,500,500));
        stage.show();
        stage.setMinHeight(400);
        stage.setMinWidth(400);

        // init af borderpane
        BorderPane panel_manager = new BorderPane();
        root.getChildren().add(panel_manager);

        // init af gridpane
        GridPane board = new GridPane();
        panel_manager.setCenter(board);
        board.setConstraints(board, 8, 8);

        // init af imageview 
        InputStream inStream = getClass().getResourceAsStream("/Assets/stoneTileEmpty.jpg");
        Image pic = new Image(inStream);

        // init af spilbræt
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                ImageView imv = new ImageView();
                imv.setImage(pic);
                board.add(imv,i,j);
            }
            
        }
    }
    
}
