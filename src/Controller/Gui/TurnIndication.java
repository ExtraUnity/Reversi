package Controller.Gui;
//Filen er skrevet af Katinka
//klasseen der viser en playercharacters på gameScreen 
//og holder styr på at vise det korrekte billede for nåt det er din tur eller modstanderens tur
import java.io.InputStream;

import Shared.TileColor;
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
        TopTurnIndication.switchTurn();
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

    // get player (billeder uden outline)
    private Image getPlayer() {
        if (player == null) {
            player_src = getClass().getResourceAsStream("/Assets/characters/" + character + ".png");
            player = new Image(player_src, Gui.fitTileSize() * 4, 0, true, false);
        }
        return player;
    }

    // get player turn (billeder med hvid outline)
    private Image getPlayerTurn() {
        if (playerTurn == null) {
            playerTurn_src = getClass().getResourceAsStream("/Assets/characters/" + character + "Turn.png");
            playerTurn = new Image(playerTurn_src, Gui.fitTileSize() * 4, 0, true, false);
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
//klasse der henter og viser det ImageView i toppen af spillet hvor der står "White Turn" eller "Black Turn"
class TopTurnIndication extends ImageView{
    private Image white_turn;
    private Image black_turn;

    private static TopTurnIndication instance;
    TileColor currentTurn;

    static void switchTurn() {
        switch (instance.currentTurn) {
            case WHITE:
            instance.setImage(instance.turnWhite()); 
                break;
            case BLACK:
            instance.setImage(instance.turnBlack());
                break;
            default:
                break;
        }
        instance.currentTurn = instance.currentTurn.switchColor();
    }

    TopTurnIndication(TileColor startTurn) {
        instance = this;
        currentTurn = startTurn;
        switchTurn();
    }

    public Image turnWhite(){
        if (white_turn == null) {
            white_turn = new Image("/Assets/TurnWhite.png", 0, Gui.fitTileSize(), true, false);
         } 
     return white_turn;
    }
    public Image turnBlack(){
        if (black_turn == null) {
            black_turn = new Image("/Assets/TurnBlack.png", 0, Gui.fitTileSize(), true, false);
         } 
     return black_turn;
    }

}