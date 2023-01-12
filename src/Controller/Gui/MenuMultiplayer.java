package Controller.Gui;

import Server.ServerConn;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class MenuMultiplayer extends BorderPane {

    public MenuMultiplayer(ServerConn conn) {
        VBox dialogVbox = new VBox();
        Text key = new Text("Your id: " + conn.netId);
        TextField text = new TextField();

        text.setMaxWidth(Gui.fitTileSize() * 2);
        text.setFont(Font.font("verdana", FontWeight.BLACK, FontPosture.REGULAR, 20));

        var optionBox = new HBox();
        optionBox.setAlignment(Pos.TOP_CENTER);
        var checkBox = new CheckBox("Timer");
        var timerField = new TextField("90");

        timerField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> ubrugtObservable, String ubrugtGammelText,
                    String newText) {
                if (!newText.matches("\\d*")) {
                    timerField.setText(newText.replaceAll("[^\\d]", ""));
                } else {
                    // Hvis det kun er tal
                    try {
                        int newTimerVal = Integer.parseInt(newText);
                        ServerConn.setLoadedGameTime(newTimerVal);
                    } catch (Exception ex) {
                        System.out.println("Failed to parse timer to a number :(" + newText);
                    }
                }

            }
        });
        checkBox.setOnAction((event) -> {
            if (checkBox.isSelected()) {
                try {
                    optionBox.getChildren().add(timerField);
                } catch (Exception e) {
                    // Fanger exception hvis den allerede er der og der er gået noget galt. Kommer
                    // nok ikke til at ske men jeg kan ikke garanterer det.

                    e.printStackTrace();
                }
            } else {
                optionBox.getChildren().remove(timerField);
                ServerConn.setLoadedGameTime(-1);
            }
        });
        optionBox.getChildren().add(checkBox);

        key.setFont(Font.font("Tahoma", FontWeight.BOLD, FontPosture.REGULAR, 30));
        key.setStrokeWidth(2);
        key.setStrokeType(StrokeType.OUTSIDE);
        key.setStroke(Color.GREY);

        dialogVbox.getChildren().add(key);
        dialogVbox.getChildren().add(text);
        dialogVbox.getChildren().add(optionBox);
        dialogVbox.getChildren().add(new ButtonJoin(conn, text));
        dialogVbox.getChildren().add(new ButtonMainMenu());
        dialogVbox.getChildren().add(new ButtonExitGame());

        dialogVbox.setAlignment(Pos.CENTER);
        dialogVbox.setSpacing(15);
        setCenter(dialogVbox);

        setMargin(getCenter(), new Insets(64, 0, 0, 0));
    }

}
