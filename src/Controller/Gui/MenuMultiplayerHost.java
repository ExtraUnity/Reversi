package Controller.Gui;

import Server.ServerConn;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class MenuMultiplayerHost extends BorderPane {

    public MenuMultiplayerHost() {
        StackPane MenuBox = new StackPane();
        VBox MenuBoxLayout = new VBox();
        StackPane IDBox = new StackPane();
        VBox IDBoxLayout = new VBox();
        HBox optionTimerBox = makeTimer();

        ImageView backgroundTile = new ImageView(
                new Image("/Assets/menutiles/medium2Tile.png", 0, Gui.fitTileSize() * 4.5, true, false));
        ImageView backgroundIDBoxTile = new ImageView(
                new Image("/Assets/menutiles/smallTile.png", 0, Gui.fitTileSize() * 2, true, false));
        ImageView yourIDLable = new ImageView(
                new Image("/Assets/menutiles/yourID.png", 0, Gui.fitTileSize() / 2, true, false));
        Text key = new Text(ServerConn.hostGame());
        MenuMultiplayer.setFontStyle(key, 45);

        MenuBox.getChildren().addAll(backgroundTile, MenuBoxLayout);
        MenuBox.setAlignment(Pos.CENTER);

        MenuBoxLayout.getChildren().addAll(IDBox, new ButtonMainMenu(), new ButtonExitGame());
        MenuBoxLayout.setAlignment(Pos.CENTER);
        MenuBoxLayout.setSpacing(10);

        IDBox.getChildren().addAll(backgroundIDBoxTile, IDBoxLayout);
        IDBox.setAlignment(Pos.CENTER);
        IDBoxLayout.setSpacing(5);

        IDBoxLayout.getChildren().addAll(yourIDLable, key, optionTimerBox);
        IDBoxLayout.setAlignment(Pos.CENTER);

        setCenter(MenuBox);
        System.out.println("host er bygget");
    }

    public HBox makeTimer() {
        var optionTimerBox = new HBox();
        // move the timer making maybe??
        var checkBox = new CheckBox();
        var timer = new Text("Timer");
        MenuMultiplayer.setFontStyle(timer, 18);
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
                optionTimerBox.getChildren().add(timerField);
                int newTimerVal = Integer.parseInt(timerField.getText());
                ServerConn.setLoadedGameTime(newTimerVal);
            } else {
                optionTimerBox.getChildren().remove(timerField);
                ServerConn.setLoadedGameTime(-1);
            }
        });
        optionTimerBox.getChildren().addAll(timer, checkBox);
        optionTimerBox.setAlignment(Pos.CENTER);
        optionTimerBox.setSpacing(10);

        System.out.println("timer");
        return optionTimerBox;
    }

}
