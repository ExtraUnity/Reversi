package Controller.Gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
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

    public MenuMultiplayer() {
        StackPane MenuBox = new StackPane();
        VBox menuLayout = new VBox();

        ImageView backgroundTile = new ImageView(
                new Image("/Assets/menutiles/medium2Tile.png",  Gui.fitTileSize() * 4.6, 0, true, false));

        MenuBox.getChildren().add(backgroundTile);

        menuLayout.getChildren().addAll(new ButtonHostGame(), new ButtonJoinGame(), new ButtonMainMenu(),
                new ButtonExitGame());
        menuLayout.setAlignment(Pos.CENTER);
        menuLayout.setSpacing(10);

        MenuBox.getChildren().add(menuLayout);

        setCenter(MenuBox);
        setMargin(getCenter(), new Insets(64, 0, 0, 0));

    }

    // sætter stykker tekst til at have samme stil
    public static void setFontStyle(Text text, int i) {
        text.setFont(Font.font("Calibri", FontWeight.BOLD, FontPosture.REGULAR, i));
        text.setFill(Color.GREY);
        text.setStrokeWidth(2);
        text.setStrokeType(StrokeType.OUTSIDE);
        text.setStroke(Color.BLACK);
    }
}
