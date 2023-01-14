package Controller.Gui;

import java.io.InputStream;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ButtonMute extends Button {
    boolean muted;

    public ButtonMute() {
        super(Buttons.Mute, 1);
        muted = false;
        setOnMousePressed(e -> {
            // setImage(imgPressed);
        });
        setOnMouseReleased(e -> {
            var path = "";
            if (muted) {
                Gui.playMusic();
                path = "/Assets/ButtonMute.png";
            } else {
                path = "/Assets/ButtonMutePressed.png";
                Gui.stopMusic();
            }
            InputStream src = getClass().getResourceAsStream(path);
            this.img = new Image(src, 0, Gui.fitTileSize() * 1, true, false);
            setImage(img);
            muted = !muted;
        });
    }
}
