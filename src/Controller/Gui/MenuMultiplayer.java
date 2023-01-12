package Controller.Gui;

import java.io.InputStream;

import Server.ServerConn;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class MenuMultiplayer extends BorderPane {

    public MenuMultiplayer(ServerConn conn) {
        VBox menuLayout = new VBox();
        StackPane IDbox = new StackPane();
        Image button = new Image("/Assets/Button.png", 0, Gui.fitTileSize(), true, false);
        ImageView backgroundButton = new ImageView(button);
        Background backgroundTexture = new Background(new BackgroundImage(button,
            BackgroundRepeat.NO_REPEAT,BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,BackgroundSize.DEFAULT));
        Text key = new Text("Your id: " + conn.netId);
        TextField text = new TextField();

        text.setBackground(backgroundTexture);
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
        key.setFill(Color.GREY);
        key.setStrokeWidth(2);
        key.setStrokeType(StrokeType.OUTSIDE);
        key.setStroke(Color.BLACK);

        IDbox.getChildren().add(backgroundButton);
        IDbox.getChildren().add(key);

        menuLayout.getChildren().add(IDbox);
        menuLayout.getChildren().add(text);
        menuLayout.getChildren().add(optionBox);
        menuLayout.getChildren().add(new ButtonJoin(conn, text));
        menuLayout.getChildren().add(new ButtonMainMenu());
        menuLayout.getChildren().add(new ButtonExitGame());


        menuLayout.setAlignment(Pos.CENTER);
        menuLayout.setSpacing(15);
        setCenter(menuLayout);

        setMargin(getCenter(), new Insets(64, 0, 0, 0));
    }

}
