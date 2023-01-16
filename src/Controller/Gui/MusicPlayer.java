package Controller.Gui;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

public class MusicPlayer {
    final String path;

    MusicPlayer(String path) {
        this.path = path;
    }

    void play() {
        System.out.println("Loading music path " + path);

        var resource = getClass().getResource(path).toExternalForm();
        Media backgroundMusic = new Media(resource);
        Gui.musicPlayer = new MediaPlayer(backgroundMusic);
        Gui.musicPlayer.setVolume(0.2);
        Gui.musicPlayer.setOnEndOfMedia(new Runnable() {
            public void run() {
                Gui.musicPlayer.seek(Duration.ZERO);
            }
        });
    }
}