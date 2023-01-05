import Controller.Controller;
import Model.GameOptions;
import Model.Model;
import Model.Game.GameMode;

public class BarebonesMain {
    public static void main(String[] args) {
        Model.initModel();
        Controller.initController();
        Model.startGame(GameMode.CLASSIC, new GameOptions(-1));
    }
}
