import Controller.Controller;
import Model.GameOptions;
import Model.Model;
import Model.Game.GameMode;

public class BarebonesMain {
    public static void main(String[] args) {
        Controller.initController();
        Model.startGame(GameMode.Classic, new GameOptions(-1));
    }
}
