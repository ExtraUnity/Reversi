package Model;

import MsgPass.ControllerMsg.ControllerWindowClosedMsg;
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

    CellColor[][] board = new CellColor[8][8];

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
                handleCellClick(msg.pos);

            } else if (modelMsg instanceof ModelWindowClosedMsg) {
                gamestate = GameState.Exited;

                Model.sendControllerMsg(new ControllerWindowClosedMsg());

            }
        }
    }
    void handleCellClick(CellPosition pos) {

    }
}

enum GameState {
    Playing,
    Exited,
    WhiteWinner,
    BlackWinner
}