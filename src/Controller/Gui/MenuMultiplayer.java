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
        VBox menuLayout = new VBox();

        
        ImageView backgroundTile = new ImageView(new Image("/Assets/menutiles/medium2Tile.png", 0, Gui.fitTileSize()*5.7, true, false));

        MenuBox.getChildren().add(backgroundTile);

        menuLayout.getChildren().addAll( new ButtonMainMenu() , new ButtonExitGame());
        menuLayout.setAlignment(Pos.CENTER);
        menuLayout.setSpacing(15);

        MenuBox.getChildren().add(menuLayout);

        setCenter(MenuBox);
        setMargin(getCenter(), new Insets(64, 0, 0, 0));
    }



    //sætter stykker tekst til at have samme stil
    public static void setFontStyle(Text text, int i){
        text.setFont(Font.font("Lucida Fax", FontWeight.BOLD, FontPosture.REGULAR, i));
        text.setFill(Color.GREY);
        text.setStrokeWidth(2);
        text.setStrokeType(StrokeType.OUTSIDE);
        text.setStroke(Color.BLACK);
    }
}
