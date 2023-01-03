package Model;

import MsgPass.ControllerMsg.ControllerWindowClosedMsg;
import MsgPass.ControllerMsg.UpdateBoardMsg;
import MsgPass.ModelMsg.CellPressedMsg;
import MsgPass.ModelMsg.ModelWindowClosedMsg;
import Shared.CellState;
import Shared.CellPosition;

public class Game {
    public enum GameMode {
        Classic,
        AIGame,
        Multiplayer
    }

    GameState gamestate = GameState.Playing;
    final Thread modelMainThread;

    CellState[][] board = new CellState[8][8];

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

    private CellState nextturn = CellState.Black;

    void handleCellClick(CellPosition pos) {
        var thiscolor = nextturn;
        board[pos.x][pos.y] = thiscolor;
        if (nextturn == CellState.Black) {
            nextturn = CellState.White;
        } else {
            nextturn = CellState.Black;
        }

        Model.sendControllerMsg(new UpdateBoardMsg(thiscolor, new CellPosition[] { pos }));
    }
}

enum GameState {
    Playing,
    Exited,
    WhiteWinner,
    BlackWinner
}