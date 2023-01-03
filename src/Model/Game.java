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

    private CellColor nextturn = CellColor.BLACK;

    void handleCellClick(CellPosition pos) {
        /*
         * Denne funktion bliver kaldt når der bliver sat en brik. Funktionen tjekker om
         * det er et lovligt træk og hvis det er håndterer den alt logikken som vender
         * andre brikker. Derefter sender den en besked til Controlleren om hvilke
         * brikker der er blevet vendt
         */
        var thiscolor = nextturn;
        board[pos.x][pos.y] = thiscolor;
        nextturn = thiscolor.switchColor();

        Model.sendControllerMsg(new UpdateBoardMsg(thiscolor, new CellPosition[] { pos }));
    }
}

enum GameState {
    Playing,
    Exited,
    WhiteWinner,
    BlackWinner
}