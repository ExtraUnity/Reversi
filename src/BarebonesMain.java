import java.time.Instant;
import Controller.Controller;
import Controller.Gui.PlayerCharacter;
import Model.GameOptions;
import Model.Model;
import Model.Game.GameMode;
import Shared.TileColor;

public class BarebonesMain {
    public static void main(String[] args) {

        Model.initModel();
        Controller.initController();

        TileColor startPlayer;
        if (Instant.now().getEpochSecond() % 2 == 0) {
            startPlayer = TileColor.BLACK;
        } else {
            startPlayer = TileColor.WHITE;
        }

        Model.startGame(GameMode.CLASSIC,
                new GameOptions(100, true, startPlayer, PlayerCharacter.White, PlayerCharacter.Black));
    }
}
