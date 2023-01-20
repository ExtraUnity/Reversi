package Controller.Gui;
//Filen er skrevet af Thor
import Model.GameOptions;
import Shared.TileColor;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class Timer extends HBox {
    static Timer instance;

    Timer(GameOptions options) {
        instance = this;
        setAlignment(Pos.TOP_CENTER);
        var text1 = new Text("OBAM;MA");
        var text2 = new Text("OMNANFJK");
        MenuMultiplayer.setFontStyle(text1, 30);
        MenuMultiplayer.setFontStyle(text2, 30);

        getChildren().add(text1);
        getChildren().add(text2);

        setTime(TileColor.WHITE, options.gametime);
        setTime(TileColor.BLACK, options.gametime);
    }

    public static void setTime(TileColor color, int time) {
        if (instance != null) {

            int index;
            if (color == TileColor.WHITE) {
                index = 0;
            } else {
                index = 1;
            }

            Platform.runLater(() -> {
                // Det her kan godt crashe, men det er rimeligt usansynligt :/
                // Hvis den bliver null i mellemtiden

                int minutes = time / 60;
                int seconds = time % 60;
                String seconds_str = "" + seconds;
                if (seconds_str.length() < 2) {
                    seconds_str = "0" + seconds_str;
                }
                String minutes_str = "" + minutes;
                if (minutes_str.length() < 2) {
                    minutes_str = "0" + minutes_str;
                }

                if (time < 20) {
                    ((Text) instance.getChildren().get(index)).setFill(Color.RED);
                }

                String finalStr = " " + minutes_str + ":" + seconds_str + " ";
                ((Text) instance.getChildren().get(index)).setText(finalStr);

            });

        } else {
            var stacktrace = Thread.currentThread().getStackTrace();
            System.out.println("TRIED TO SET TIME WHEN THERE IS NOT TIMER!!!!!!");
            System.out.println("THIS IS NOT A CRASH. JUST AN GUIDE TO WHERE IT WENT WRONG!!");
            System.out.println(stacktrace);
        }
    }
}
