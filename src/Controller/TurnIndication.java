package Controller;

import java.io.InputStream;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class TurnIndication extends ImageView {

    static private InputStream player_src;
    static private Image player;
    static private InputStream playerTurn_src;
    static private Image playerTurn;
    

    boolean isMyTurn = false;
    PlayerCharacter character;

    //get player 
    private Image getPlayer(PlayerCharacter character) {
        if (player == null) {
            player_src = getClass().getResourceAsStream(character);
            player = new Image(player_src, 100, 0, true, false);
        }
        return player;
    }

    //get player turn
    private Image getPlayerTurn(PlayerCharacter character) {
        if (playerTurn == null) {
            playerTurn_src = getClass().getResourceAsStream(character);
            playerTurn = new Image(playerTurn_src, 100, 0, true, false);
        }
        return playerTurn;
    }

}
