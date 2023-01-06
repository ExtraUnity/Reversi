package Controller.Gui;

import java.io.InputStream;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class TurnIndication extends ImageView {

    private InputStream player_src;
    private Image player;
    private InputStream playerTurn_src;
    private Image playerTurn;

    private InputStream turn_src;
    private Image turn;

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
    TurnIndication() {
        switchColorTurn();
    }

    // get player
    private Image getPlayer() {
        if (player == null) {
            player_src = getClass().getResourceAsStream("/Assets/characters/" + character + ".png");
            player = new Image(player_src, Gui.fitTileSize() * 4, 0, true, false);
        }
        return player;
    }

    // get player turn
    private Image getPlayerTurn() {
        if (playerTurn == null) {
            playerTurn_src = getClass().getResourceAsStream("/Assets/characters/" + character + "Turn.png");
            playerTurn = new Image(playerTurn_src, Gui.fitTileSize() * 4, 0, true, false);
        }
        return playerTurn;
    }




    public Image turnWhite(){
        if (turn == null) {
            turn_src = getClass().getResourceAsStream("/Assets/characters/TurnWhite.png");
            turn = new Image(turn_src, Gui.fitTileSize() * 4, 0, true, false);
         } 
     return turn;
    }
    public Image turnBlack(){
        if (turn == null) {
            turn_src = getClass().getResourceAsStream("/Assets/characters/TurnBlack.png");
            turn = new Image(turn_src, Gui.fitTileSize() * 4, 0, true, false);
         } 
     return turn;
    }
    
    public void switchColorTurn(){
        if (isMyTurn) {
            setImage(turnBlack());
        } else {
            setImage(turnWhite());
        }
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
