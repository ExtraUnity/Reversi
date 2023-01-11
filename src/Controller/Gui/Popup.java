package Controller.Gui;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class Popup extends VBox {
    final int timeAlive;
    boolean removed = false;

    public Popup(String text_str, int timeAlive) {
        this.timeAlive = timeAlive;
        setAlignment(Pos.TOP_CENTER);
        var text = new Text(text_str);
        text.setFont(new Font(text.getFont().getFamily(), 30));
        getChildren().add(text);
        var button = new javafx.scene.control.Button("Ok");
        button.setOnAction((e) -> {
            close();
        });
        setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));
        setMaxSize(400, 130);
    }

    void close() {
        if (!removed) {
            Platform.runLater(() -> {
                Gui.stackRoot.getChildren().remove(this);
                removed = true;
            });

        }
    }

    public static void showPopup(Popup popup) {
        Platform.runLater(() -> {
            Gui.stackRoot.getChildren().add(popup);
            System.out.println("ADDED " + popup);
            for (var thing : Gui.stackRoot.getChildren()) {
                System.out.println(thing);
            }
            new Thread(() -> {
                try {
                    Thread.sleep(popup.timeAlive);
                    for (var thing : Gui.stackRoot.getChildren()) {
                        System.out.println(thing);
                    }
                    popup.close();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        });

    }
}
