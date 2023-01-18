package Controller.Gui;
//Filen er skrevet af Katinka
import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class MenuMultiplayerJoin extends BorderPane {

        public MenuMultiplayerJoin() {
                StackPane MenuBox = new StackPane();
                VBox MenuBoxLayout = new VBox();
                StackPane IDBox = new StackPane();
                VBox IDBoxLayout = new VBox();

                ImageView backgroundTile = new ImageView(
                                new Image("/Assets/menutiles/bigTile.png", Gui.fitTileSize() * 4.6, 0, true, false));
                ImageView backgroundIDBoxTile = new ImageView(
                                new Image("/Assets/menutiles/smallTile.png", 0, Gui.fitTileSize() * 2, true, false));
                ImageView yourIDLable = new ImageView(
                                new Image("/Assets/menutiles/enterID.png", 0, Gui.fitTileSize() / 2, true, false));
                Background backgroundTexture = new Background(
                                new BackgroundImage(
                                        new Image("/Assets/menutiles/textTile.png", 0, Gui.fitTileSize() / 2, true, false),
                                        BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                                        BackgroundPosition.DEFAULT,
                                        BackgroundSize.DEFAULT));

                TextField text = new TextField();
                text.setBackground(backgroundTexture);
                text.setMaxWidth(Gui.fitTileSize() * 2);
                text.setFont(Font.font("verdana", FontWeight.BLACK, FontPosture.REGULAR, Gui.fitTileSize() / 4));
                text.setStyle("-fx-text-inner-color: white;");

                Text errorLabel = new Text("");
                MenuMultiplayer.setFontStyle(errorLabel, 15);

                MenuBox.getChildren().addAll(backgroundTile, MenuBoxLayout);
                MenuBox.setAlignment(Pos.CENTER);

                MenuBoxLayout.getChildren().addAll(IDBox, new ButtonJoin(text, errorLabel), new ButtonMainMenu(),
                                new ButtonExitGame());
                MenuBoxLayout.setAlignment(Pos.CENTER);
                MenuBoxLayout.setSpacing(10);

                IDBox.getChildren().addAll(backgroundIDBoxTile, IDBoxLayout);
                IDBox.setAlignment(Pos.CENTER);
                IDBoxLayout.setSpacing(5);

                IDBoxLayout.getChildren().addAll(yourIDLable, text, errorLabel);
                IDBoxLayout.setAlignment(Pos.CENTER);
                IDBoxLayout.setSpacing(5);

                setCenter(MenuBox);
        }
}
