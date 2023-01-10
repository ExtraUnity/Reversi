package Model;

import Controller.Gui.Tile;
import Shared.TileColor;
import Shared.TilePosition;

public class AIGame extends Game {
    static AIPlayer aiPlayer;

    AIGame(GameOptions options) {
        super(options);
        aiPlayer = new AIPlayer(board);
        aiPlayer.setMyTurn(options.startPlayer == TileColor.BLACK);
    }

    @Override
    void handleTileClick(TilePosition pos) {
        super.handleTileClick(pos);
        if (followRules()) {
            var legalMoves = getAllLegalMoves(getNextTurn());
            handleAITurn(legalMoves);
        }
    }

    @Override
    void handlePassClick() {
        super.handlePassClick();
        if (followRules()) {
            var legalMoves = getAllLegalMoves(getNextTurn());
            handleAITurn(legalMoves);
        }
    }

    @Override
    void handleAITurn(LegalMove[] legalMoves) {
        aiPlayer.updateBoard(board, legalMoves);
        try {
            Thread.sleep(1500);
        } catch (Exception e) {
            System.out.println(e.getStackTrace());
        }
        if (aiPlayer.isMyTurn()) {
            aiPlayer.setMyTurn(false);
            if (legalMoves.length != 0) {
                aiPlayer.setBestMove();
                System.out.println("Best Move is: " + AIGame.aiPlayer.getBestMove());

                handleTileClick(aiPlayer.getBestMove());
            } else {
                System.out.println("I should pass...");
                handlePassClick();
            }
        } else {
            aiPlayer.setMyTurn(true);
        }
    }

    /**
     * Returns a double representing the evaluation of the current game position.
     * Returns a positive value for white winning and a negative value for black
     * winning.
     * A larger absolute number means a more decisive game.
     * 
     * @return
     */
    public int evaluatePosition() {
        int whitePoints = 0;
        int blackPoints = 0;
        for (int x = 0; x < board.length; x++) {
            for (int y = 0; y < board[x].length; y++) {
                int tileValue = tileValue(x, y);
                switch (board[x][y]) {
                    case BLACK:
                        blackPoints += tileValue;
                        break;
                    case WHITE:
                        whitePoints += tileValue;
                        break;
                    default:
                        break;

                }
            }
        }

        return whitePoints - blackPoints;
    }

    /**
     * Returns the value of a tile in a given position. The tile is determined by
     * the following grid:
     * +4 -3 +2 +2 +2 +2 -3 +4
     * -3 -4 -1 -1 -1 -1 -4 -3
     * +2 -1 +1 +0 +0 +1 -1 +2
     * +2 -1 +0 +1 +1 +0 -1 +2
     * +2 -1 +0 +1 +1 +0 -1 +2
     * +2 -1 +1 +0 +0 +1 -1 +2
     * -3 -4 -1 -1 -1 -1 -4 -3
     * +4 -3 +2 +2 +2 +2 -3 +4
     * 
     * @return
     */
    private static int tileValue(int x, int y) {
        if (isCorner(x, y)) {
            return 4;
        }
        if (isMiddleEdge(x, y)) {
            return 2;
        }
        if (isMiddleDiagonal(x, y)) {
            return 1;
        }
        if (isMiddleOneFromEdge(x, y)) {
            return -1;
        }
        if (isCornerEdge(x, y)) {
            return -3;
        }
        if (isFirstCornerDiagonal(x, y)) {
            return -4;
        }

        return 0;
    }

    // These functions together handle where on the 8x8 grid a tile is positioned,
    // dividing into the appropriate catagories.
    private static boolean isCorner(int x, int y) {
        return (x == 0 && (y == 0 || y == 7)) || x == 7 && (y == 0 || y == 7);
    }

    private static boolean isMiddleEdge(int x, int y) {
        return (centerFour(x) && (y == 0 || y == 7))
                || (centerFour(y) && (x == 0 || x == 7));
    }

    private static boolean isMiddleDiagonal(int x, int y) {
        return (x == y || (x == 7 - y)) && x > 1 && x < 6;
    }

    private static boolean isMiddleOneFromEdge(int x, int y) {
        return (centerFour(x) && (y == 1 || y == 6))
                || (centerFour(y) && (x == 1 || x == 6));
    }

    private static boolean centerFour(int x) {
        return x > 1 && x < 6;
    }

    private static boolean isCornerEdge(int x, int y) {
        return ((x == 0 || x == 7) && (y == 1 || y == 6)) ||
                ((y == 0 || y == 7) && (x == 1 || x == 6));
    }

    private static boolean isFirstCornerDiagonal(int x, int y) {
        return ((x == y || x == 7 - y) && (x == 1 || x == 6));
    }

}
