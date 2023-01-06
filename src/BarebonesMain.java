import java.util.Random;

import Controller.Controller;
import Model.GameOptions;
import Model.Model;
import Model.Game.GameMode;
import Shared.TileColor;

public class BarebonesMain {
    public static void main(String[] args) {

        Model.initModel();
        Controller.initController();

        TileColor startPlayer;
        if (new Random().nextBoolean()) {
            startPlayer = TileColor.BLACK;
        } else {
            startPlayer = TileColor.WHITE;
        }

        Model.startGame(GameMode.CLASSIC, new GameOptions(-1, false, startPlayer));
    }
}
