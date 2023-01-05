package Controller.Gui;

import java.io.InputStream;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class TurnIndication extends ImageView {

    private InputStream player_src;
    private Image player;
    private InputStream playerTurn_src;
    private Image playerTurn;

    private static TurnIndication whitePlayer;
    private static TurnIndication blackPlayer;

    public static void switchTurns() {
        whitePlayer.switchTurn();
        blackPlayer.switchTurn();
    }

    boolean isMyTurn;
    PlayerCharacter character;

    TurnIndication(PlayerCharacter character, boolean isMyTurn) {
        this.character = character;
        this.isMyTurn = !isMyTurn;
        if (isMyTurn) {
            blackPlayer = this;
        } else {
            whitePlayer = this;
        }

        switchTurn();
    }

    // get player
    private Image getPlayer() {
        if (player == null) {
            player_src = getClass().getResourceAsStream("/Assets/characters/" + character + ".png");
            player = new Image(player_src, 300, 0, true, false);
        }
        return player;
    }

    // get player turn
    private Image getPlayerTurn() {
        if (playerTurn == null) {
            playerTurn_src = getClass().getResourceAsStream("/Assets/characters/" + character + "Turn.png");
            playerTurn = new Image(playerTurn_src, 300, 0, true, false);
        }
        return playerTurn;
    }

    public void switchTurn() {
        isMyTurn = !isMyTurn;
        if (isMyTurn) {
            setImage(getPlayerTurn());
        } else {
            setImage(getPlayer());
        }
    }
}
