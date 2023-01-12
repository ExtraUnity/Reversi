package Controller.Gui;

import Shared.TileColor;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

public class Popup extends VBox {
    final int timeAlive;
    boolean removed = false;

    public Popup(TileColor color, int timeAlive) {
        this.timeAlive = timeAlive;
        setAlignment(Pos.TOP_CENTER);
        ImageView image = new ImageView(new Image("Assets/"+ color.name() +".png", 0, Gui.fitTileSize()*3, true, false));
        image.setOnMouseClicked(e -> {
            close();
        });
        getChildren().add(image);
        setAlignment(Pos.CENTER);
        setMargin(image, new Insets(0,0,Gui.fitTileSize()*2,0));
    }

    public Popup(int timeAlive) {
        this.timeAlive = timeAlive;
        ImageView image = new ImageView(new Image("Assets/YourTurn.png", 0, Gui.fitTileSize(), true, false));
        image.setOnMouseClicked(e -> {
            close();
        });
        getChildren().add(image);
        setAlignment(Pos.CENTER);
        setMargin(image, new Insets(Gui.fitTileSize()*2,0,0,0));

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
