package Controller.Gui;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;

public class TileAnimation extends ImageView{

    public static ImageView makeTimeline(){
        Image image1 = new Image("/Assets/stoneTileWhiteToBlack.gif", Gui.fitTileSize(), 0, true, false);
        Image image2 = new Image("/Assets/stoneTileBlack.png", Gui.fitTileSize(), 0, true, false);
        ImageView imageView = new ImageView();
        Timeline timeline = new Timeline(
            new KeyFrame(Duration.ZERO, new KeyValue(imageView.imageProperty(), image1)),
            new KeyFrame(Duration.seconds(1), new KeyValue(imageView.imageProperty(), image2))
        );
        timeline.setCycleCount(1);
        timeline.play(); 
        
        return imageView;
    }

}