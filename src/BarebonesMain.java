import Controller.Controller;
import Model.GameOptions;
import Model.Model;
import Model.Game.GameMode;
import MsgPass.ModelMsg.CellPressedMsg;
import Shared.CellPosition;

public class BarebonesMain {
    public static void main(String[] args) {
        Controller.initController();
        Model.startGame(GameMode.Classic, new GameOptions(-1));
        
        Model.sendModelMsg(new CellPressedMsg(new CellPosition(2, 4)));

    }
    static void testPositions() {
        
    }
}
