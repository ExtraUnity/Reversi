package Controller.Gui;

import Model.GameOptions;
import Shared.TileColor;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class Timer extends HBox {
    static Timer instance;

    Timer(GameOptions options) {
        instance = this;
        setAlignment(Pos.TOP_CENTER);
        getChildren().add(new Label(""));
        getChildren().add(new Label(""));
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

                instance.getChildren().set(index, new Label(" " + color + " " + minutes_str + ":" + seconds_str + " "));
            });

        } else {
            var stacktrace = Thread.currentThread().getStackTrace();
            System.out.println("TRIED TO SET TIME WHEN THERE IS NOT TIMER!!!!!!");
            System.out.println("THIS IS NOT A CRASH. JUST AN GUIDE TO WHERE IT WENT WRONG!!");
            System.out.println(stacktrace);
        }
    }
}
