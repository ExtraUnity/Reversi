import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class TicTacToe extends Application {

    static boolean turn = true;
    static String[][] state = new String[3][3];
    static boolean gameover = false;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        //var img = ClassLoader.getSystemResource("icon.png");
        //stage.getIcons().add(new Image(img.openStream()));
        var gridpane = new GridPane();

        for (int i = 0; i < 9; i++) {
            Button button = new Button("");
            button.setPrefSize(200, 200);
            int x = i % 3;
            int y = i / 3;
            gridpane.add(button, x, y);
            
            button.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent arg0) {
                    if (!gameover && button.getText() == "") {
                        var action = turn ? "X" : "O";
                        turn = !turn;
                        button.setText(action);
                        button.setFont(new Font(60));
                        state[x][y] = action;
                        var winner = checkWinner();

                        if (winner != "") {
                            mkPopup(stage, winner);
                            gameover = true;
                        }
                    }
                }
            });
        }

        stage.setScene(new Scene(gridpane));
        stage.show();
    }

    static String checkWinner() {
        for (int x = 0; x < 3; x++) {
            String value = state[x][0] + state[x][1] + state[x][2];
            if (value.equals("XXX")) {
                return "X";
            } else if (value == "OOO") {
                return "O";
            }
        }
        // Check all horizontal
        for (int y = 0; y < 3; y++) {
            String value = state[0][y] + state[1][y] + state[2][y];
            if (value.equals("XXX")) {
                return "X";
            } else if (value == "OOO") {
                return "O";
            }
        }
        // Check diagonal
        {
            String value = state[0][0] + state[1][1] + state[2][2];
            if (value.equals("XXX")) {
                return "X";
            } else if (value == "OOO") {
                return "O";
            }
        }
        {
            String value = state[2][0] + state[1][1] + state[0][2];
            if (value.equals("XXX")) {
                return "X";
            } else if (value == "OOO") {
                return "O";
            }
        }

        // Check if all filled and no winner

        for (int i = 0; i < 9; i++) {
            int x = i % 3;
            int y = i / 3;
            if (state[x][y] == null) {
                return "";
            }
        }

        return "No Winner";
    }

    static void printState() {
        for (int i = 0; i < 9; i++) {
            int x = i % 3;
            int y = i / 3;
            if (x == 0) {
                System.out.println();
            }
            System.out.print(state[x][y] + " ");
        }
        System.out.println();
    }

    static void mkPopup(Stage primaryStage, String winner) {
        final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(primaryStage);
        VBox dialogVbox = new VBox(20);
        var label = new Label(winner + " wins");
        label.setFont(new Font(30));
        dialogVbox.getChildren().add(label);
        Scene dialogScene = new Scene(dialogVbox, 300, 200);
        dialog.setScene(dialogScene);
        dialog.show();
    }
}