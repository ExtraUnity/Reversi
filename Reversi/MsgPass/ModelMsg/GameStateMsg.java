package MsgPass.ModelMsg;
//Filen er skrevet af Thor
import Model.GameOptions;
import Model.Game.GameMode;

public class GameStateMsg {
    public final GameMode gameMode;
    public final GameOptions gameOptions;
    public final boolean exit;

    public GameStateMsg(GameMode gameMode, GameOptions gameOptions) {
        this.gameMode = gameMode;
        this.gameOptions = gameOptions;
        if (gameMode == null && gameOptions == null) {
            exit = true;
        } else {
            exit = false;
        }
    }

    public static GameStateMsg exit() {
        return new GameStateMsg(null, null);
    }
}
