package Controller.Gui;
//Filen er skrevet af Katinka
//Klasse der opstiller avatar-selection modulet som bliver brugt i multiplayer-menuen
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.media.AudioClip;

import java.io.InputStream;
import java.util.ArrayList;
import Server.ServerConn;

public class MenuCharacterSelection extends BorderPane {
    FlowPane flowPane = new FlowPane();
    protected Image avatar;
    protected Image avatarSelected;
    protected ImageView image;
    private static AudioClip characterSound;

    //i konstruktøren bliver et array af imageViews 
    //som derefter bliver tilføjet til et flowpane som er avatar-selection menuen
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
                    || character == PlayerCharacter.Computer || character == PlayerCharacter.Unknown
                    || character == PlayerCharacter.UnknownWhite) {
                continue;
            }
            InputStream src = getClass().getResourceAsStream("/Assets/characters/" + character + ".png");
            this.avatar = new Image(src, 0, Gui.fitTileSize() * 3, true, false);

            InputStream src2 = getClass().getResourceAsStream("/Assets/characters/" + character + "Turn.png");
            this.avatarSelected = new Image(src2, 0, Gui.fitTileSize() * 3, true, false);

            if (character == Gui.yourCharacter) {
                this.image = new ImageView(avatarSelected);
            } else {
                this.image = new ImageView(avatar);
            }

            image.setOnMouseReleased(e -> {
                playCharacterQuote(character);
                ServerConn.setLoadedCharacter(character);
                Gui.setYourCharacter(character);
                Gui.updateMultiplayerMenu();

            });
            img.add(image);
        }
        return img;
    }

    private void playCharacterQuote(PlayerCharacter character) {
        System.out.println(characterSound);
        if (characterSound != null) {
            characterSound.stop();
        }
        String path = "/Assets/sounds/characterQuotes/" + character + ".mp3";

        characterSound = new AudioClip(getClass().getResource(path).toExternalForm());
        characterSound.play();
    }
}
