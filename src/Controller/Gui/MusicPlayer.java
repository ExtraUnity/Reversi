package Controller.Gui;

//Filen er skrevet af Christian
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

public class MusicPlayer {
    final String PATH;

    MusicPlayer(String path) {
        this.PATH = path;
    }

    /**
     * Loads mp3 from directory and plays on repeat until updated
     */
    public void play() {
        System.out.println("Loading music path " + PATH);

        var resource = getClass().getResource(PATH).toExternalForm();
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