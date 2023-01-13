package Controller.Gui;

import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.media.AudioClip;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import Server.ServerConn;

public class MenuCharacterSelection extends BorderPane {
    FlowPane flowPane = new FlowPane();
    protected Image avatar;
    protected Image avatarSelected;
    protected ImageView image;
    private AudioClip characterSound;

    public MenuCharacterSelection() {

        ArrayList<ImageView> imgList = getCharacterAddress();

        for (int i = 0; i < imgList.size(); i++) {
            flowPane.getChildren().add(imgList.get(i));
        }
        flowPane.setAlignment(Pos.CENTER);
        flowPane.setHgap(30);
        setCenter(flowPane);

    }

    private ArrayList<ImageView> getCharacterAddress() {
        ArrayList<ImageView> img = new ArrayList<ImageView>();
        for (PlayerCharacter character : PlayerCharacter.values()) {
            if (character == PlayerCharacter.Black || character == PlayerCharacter.White
                    || character == PlayerCharacter.Computer || character == PlayerCharacter.Unknown) {
                continue;
            }
            InputStream src = getClass().getResourceAsStream("/Assets/characters/" + character + ".png");
            this.avatar = new Image(src, 0, Gui.fitTileSize() * 2.5, true, false);

            InputStream src2 = getClass().getResourceAsStream("/Assets/characters/" + character + "Turn.png");
            this.avatarSelected = new Image(src2, 0, Gui.fitTileSize() * 2.5, true, false);

            if (character == Gui.yourCharacter) {
                this.image = new ImageView(avatarSelected);
            } else {
                this.image = new ImageView(avatar);
            }

            image.setOnMouseReleased(e -> {
                playCharacterQuote(character);
                ServerConn.setLoadedCharacter(character);
                Gui.setYourCharacter(character);

                // Gui.makeMultiplayerMenu(conn);
                Gui.makeMultiplayerMenu();
            });
            img.add(image);
        }
        System.out.println("Character select er bygget");
        return img;
    }

    private void playCharacterQuote(PlayerCharacter character) {
        if (characterSound != null) {
            characterSound.stop();
        }
        String path = "./src/Assets/sounds/characterQuotes/" + character + ".mp3";
        File directory = new File(path);
        characterSound = new AudioClip(directory.toURI().toString());
        characterSound.play();
    }
}
