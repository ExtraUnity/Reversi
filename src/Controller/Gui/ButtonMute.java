package Controller.Gui;

//Filen er skrevet af Christian
import java.io.InputStream;
import javafx.scene.image.Image;

public class ButtonMute extends Button {
    boolean muted;

    public ButtonMute() {
        super(Buttons.Mute, 0.75);
        muted = false;

        setOnMouseReleased(e -> { // switch states when pressed
            var path = "";
            if (muted) {
                Gui.playMusic();
                path = "/Assets/ButtonMute.png";
            } else {
                path = "/Assets/ButtonMutePressed.png";
                Gui.stopMusic();
            }
            InputStream src = getClass().getResourceAsStream(path);
            this.img = new Image(src, 0, Gui.fitTileSize() * 0.75, true, false);
            setImage(img);
            muted = !muted;
        });
    }
}
