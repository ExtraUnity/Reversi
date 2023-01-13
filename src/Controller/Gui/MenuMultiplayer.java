package Controller.Gui;


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
        StackPane MenuBox = new StackPane();
        StackPane IDbox = new StackPane();
        VBox IDboxLayout = new VBox();
        VBox menuLayout = new VBox();
        var optionBox = new HBox();
        
        ImageView backgroundButton = new ImageView(new Image("/Assets/menutiles/bigTile.png", 0, Gui.fitTileSize()*5.7, true, false));
        ImageView backgroundIDbox = new ImageView(new Image("/Assets/menutiles/smallTile.png", 0, Gui.fitTileSize()*2, true, false));   
        Background backgroundTexture = new Background(new BackgroundImage(new Image("/Assets/menutiles/textTile.png", 0, Gui.fitTileSize()/2, true, false) ,
            BackgroundRepeat.NO_REPEAT , BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT , BackgroundSize.DEFAULT));

        Text key = new Text("Your ID: " + conn.netId);
        setFontStyle(key , 30);

        TextField text = new TextField();
        text.setBackground(backgroundTexture);
        text.setMaxWidth(Gui.fitTileSize() * 2);
        text.setFont(Font.font("verdana", FontWeight.BLACK, FontPosture.REGULAR, Gui.fitTileSize()/4));
        text.setStyle("-fx-text-inner-color: black;");

        var checkBox = new CheckBox();
        var timer = new Text("Timer");
        setFontStyle(timer, 18);
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

    //her bliver hele den går menuboks i multiplayer menuen sammenat.
        optionBox.getChildren().addAll(timer , checkBox);
        optionBox.setAlignment(Pos.CENTER);
        optionBox.setSpacing(5);

        MenuBox.getChildren().add(backgroundButton);

        IDboxLayout.getChildren().addAll(key,text,optionBox);
        IDboxLayout.setAlignment(Pos.CENTER);
        IDboxLayout.setSpacing(15);

        IDbox.getChildren().addAll(backgroundIDbox , IDboxLayout);

        menuLayout.getChildren().addAll(IDbox , new ButtonJoin(conn, text) , new ButtonMainMenu() , new ButtonExitGame());
        menuLayout.setAlignment(Pos.CENTER);
        menuLayout.setSpacing(15);

        MenuBox.getChildren().add(menuLayout);

        setCenter(MenuBox);
        setMargin(getCenter(), new Insets(64, 0, 0, 0));
    }

    //sætter stykker tekst til at have samme stil
    public void setFontStyle(Text text, int i){
        text.setFont(Font.font("Tahoma", FontWeight.BOLD, FontPosture.REGULAR, i));
        text.setFill(Color.GREY);
        text.setStrokeWidth(2);
        text.setStrokeType(StrokeType.OUTSIDE);
        text.setStroke(Color.BLACK);
    }
}
