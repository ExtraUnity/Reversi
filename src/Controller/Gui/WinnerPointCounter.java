package Controller.Gui;
//Filen er skrevet af Frederik
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

public class WinnerPointCounter extends HBox {
    WinnerPointCounter(int blackPoints, int whitePoints) {
        setAlignment(Pos.TOP_CENTER);
        var blackText = new Text("Black: " + blackPoints);
        var whiteText = new Text("White: " + whitePoints);
        MenuMultiplayer.setFontStyle(blackText, 30);
        MenuMultiplayer.setFontStyle(whiteText, 30);
        setMargin(blackText, new Insets(10));
        setMargin(whiteText, new Insets(10));
        getChildren().addAll(whiteText, blackText);
    }
}
