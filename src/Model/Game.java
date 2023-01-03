package Model;

import MsgPass.ControllerMsg.ControllerWindowClosedMsg;
import MsgPass.ControllerMsg.UpdateBoardMsg;
import MsgPass.ModelMsg.CellPressedMsg;
import MsgPass.ModelMsg.ModelWindowClosedMsg;
import Shared.CellColor;
import Shared.CellPosition;

public class Game {
    public enum GameMode {
        Classic,
        AIGame,
        Multiplayer
    }

    GameState gamestate = GameState.Playing;
    final Thread modelMainThread;

    Game() {
        var game = this;
        modelMainThread = new Thread(new Runnable() {
            @Override
            public void run() {
                game.run_game();
            }
        });
        modelMainThread.start();
    }

    void run_game() {
        System.out.println(getClass().getSimpleName() + " loop started");
        while (gamestate == GameState.Playing) {
            // Game loop
            var modelMsg = Model.readModelMsg();
            System.out.println("Game Received " + modelMsg.getClass().getName());

            if (modelMsg instanceof CellPressedMsg) {
                CellPressedMsg msg = (CellPressedMsg) modelMsg;
                System.out.println(msg.pos);
                Model.sendControllerMsg(new UpdateBoardMsg(CellColor.Black, new CellPosition[] { msg.pos }));

            } else if (modelMsg instanceof ModelWindowClosedMsg) {
                gamestate = GameState.Exited;
                
                Model.sendControllerMsg(new ControllerWindowClosedMsg());

            }
        }
    }
}

enum GameState {
    Playing,
    Exited,
    WhiteWinner,
    BlackWinner
}