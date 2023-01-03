package Model;

import MsgPass.ControllerMsg.ControllerWindowClosedMsg;
import MsgPass.ControllerMsg.UpdateBoardMsg;
import MsgPass.ModelMsg.TilePressedMsg;
import MsgPass.ModelMsg.ModelWindowClosedMsg;
import Shared.TileColor;
import Shared.TilePosition;

public class Game {
    public enum GameMode {
        CLASSIC,
        AI_GAME,
        MULTIPLAYER
    }

    GameState gamestate = GameState.PLAYING;
    final Thread modelMainThread;

    TileColor[][] board = new TileColor[8][8];

    Game() {
        board[3][3] = TileColor.WHITE;
        board[4][4] = TileColor.WHITE;
        board[3][4] = TileColor.BLACK;
        board[4][3] = TileColor.BLACK;
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
        while (gamestate == GameState.PLAYING) {
            // Game loop
            var modelMsg = Model.readModelMsg();
            System.out.println("Game Received " + modelMsg.getClass().getName());

            if (modelMsg instanceof TilePressedMsg) {
                TilePressedMsg msg = (TilePressedMsg) modelMsg;
                handleTileClick(msg.pos);

            } else if (modelMsg instanceof ModelWindowClosedMsg) {
                gamestate = GameState.EXITED;

                Model.sendControllerMsg(new ControllerWindowClosedMsg());

            }
        }
    }

    private TileColor nextturn = TileColor.BLACK;

    /**
     * Denne funktion bliver kaldt når der bliver sat en brik. Funktionen tjekker om
     * det er et lovligt træk og hvis det er håndterer den alt logikken som vender
     * andre brikker. Derefter sender den en besked til Controlleren om hvilke
     * brikker der er blevet vendt
     */
    void handleTileClick(TilePosition pos) {
        var thiscolor = nextturn;
        if (!legalMove(pos, thiscolor)) {
            return;
        }

        board[pos.x][pos.y] = thiscolor;
        switch (thiscolor) {
            case WHITE:
                nextturn = TileColor.BLACK;
                break;
            case BLACK:
                nextturn = TileColor.WHITE;
                break;
        }

        Model.sendControllerMsg(new UpdateBoardMsg(thiscolor, new TilePosition[] { pos }));
    }

    boolean legalMove(TilePosition pos, TileColor color) {

        // check if tile is already filled
        if (board[pos.x][pos.y] != null) {
            System.out.println("Tile not empty");
            return false;
        }

        // check if any vertices comply with the rules
        boolean checkHorizontal = checkHorizontalLegal(pos, color);
        boolean checkVertical = checkVerticalLegal(pos, color);
        boolean checkDiagonal = checkDiagonalLegal(pos, color);

        if (!checkHorizontal && !checkVertical && !checkDiagonal) {
            System.out.println("Move doesn't follow rules");
            return false;
        }
        return true;
    }

    boolean checkHorizontalLegal(TilePosition pos, TileColor color) {
        TileColor opposingColor = (color == TileColor.WHITE) ? TileColor.BLACK : TileColor.WHITE;
        boolean foundColor = false;
        boolean foundOpposingColor = false;
        board[pos.x][pos.y] = color;

        // Start from left and check if there is a series of:
        // a black tile
        // x number of white tiles
        // finally a new black tile
        // and the opposite for the other players turn.
        for (int x = 0; x < board.length; x++) {

            TileColor currentColor = board[x][pos.y];
            if (currentColor == color) {
                foundColor = true;
            } else if (currentColor == opposingColor && foundColor) {
                foundOpposingColor = true;
            } else if (foundColor) {
                break;
            }

            if (foundColor && foundOpposingColor) {
                return true;
            }
        }
        board[pos.x][pos.y] = null;

        return false;
    }

    boolean checkVerticalLegal(TilePosition pos, TileColor color) {

        return false;
    }

    boolean checkDiagonalLegal(TilePosition pos, TileColor color) {

        return false;
    }

}

enum GameState {
    PLAYING,
    EXITED,
    WHITE_WINNER,
    BLACK_WINNER
}