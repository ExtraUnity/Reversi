package Model;

import Shared.*;
import java.util.Arrays;

public class AIPlayer {
    private TileColor[][] gameBoard;
    private LegalMove[] legalMoves;
    private boolean isMyTurn;
    private TilePosition bestMove;

    AIPlayer(TileColor[][] gameBoard) {
        this.gameBoard = gameBoard;
        bestMove = new TilePosition(0, 0);
    }

    public TileColor[][] getGameBoard() {
        return this.gameBoard;
    }

    public void setGameBoard(TileColor[][] gameBoard) {
        this.gameBoard = gameBoard;
    }

    public void setLegalMoves(LegalMove[] legalMoves) {
        this.legalMoves = legalMoves;
    }

    public boolean isMyTurn() {
        return isMyTurn;
    }

    public void setMyTurn(boolean state) {
        isMyTurn = state;
    }

    public void updateBoard(TileColor[][] gameBoard, LegalMove[] legalMoves) {
        this.gameBoard = gameBoard;
        this.legalMoves = legalMoves;
    }

    /**
     * Calculates the best move in the current position using a simple heuristic
     * algorithm
     *
     */
    public TilePosition calculateBestMove() {

        TilePosition currentBestMove = new TilePosition(0, 0);
        int bestEvaluation = 0;

        // Check for all legal moves
        for (int i = 0; i < legalMoves.length; i++) {

            TileColor[][] tempBoard = copyBoard();
            var flippedTiles = AIGame.getAllFlipped(legalMoves[i].position,
                    AIGame.getNextTurn(), tempBoard);

            flippedTiles.add(legalMoves[i].position);
            for (var t_pos : flippedTiles) {
                tempBoard[t_pos.x][t_pos.y] = AIGame.getNextTurn();
            }

            // Evaluate the position after the current move has been made
            int currentMoveEvaluation = evaluatePosition(tempBoard);
            System.out.println("Move: " + legalMoves[i].position + "    Evaluation: " + currentMoveEvaluation);
            if (i == 0) { // sets the bestEvaluation to the first one checked
                currentBestMove = legalMoves[i].position;
                bestEvaluation = currentMoveEvaluation;
                continue;
            }

            // checks whether current evaluation is better for AI than bestEvaluation
            switch (AIGame.getNextTurn()) {
                case BLACK:
                    if (currentMoveEvaluation < bestEvaluation) {
                        currentBestMove = legalMoves[i].position;
                        bestEvaluation = currentMoveEvaluation;
                    }
                    break;
                case WHITE:
                    if (currentMoveEvaluation > bestEvaluation) {
                        currentBestMove = legalMoves[i].position;
                        bestEvaluation = currentMoveEvaluation;
                    }
                    break;
                default:
                    break;

            }

        }
        System.out.println("My best move is: " + currentBestMove);
        return currentBestMove;

        /*
         * Arrays.sort(legalMoves);
         * return legalMoves[legalMoves.length - 1].position;
         */
    }

    public TilePosition getBestMove() {
        return this.bestMove;
    }

    private TileColor[][] copyBoard() {
        TileColor[][] newBoard = new TileColor[gameBoard.length][gameBoard[0].length];
        for (int x = 0; x < gameBoard.length; x++) {
            for (int y = 0; y < gameBoard[x].length; y++) {
                newBoard[x][y] = gameBoard[x][y];
            }
        }
        return newBoard;
    }

    public void setBestMove() {
        this.bestMove = calculateBestMove();
    }

    /**
     * Returns a double representing the evaluation of the current game position.
     * Returns a positive value for white winning and a negative value for black
     * winning.
     * A larger absolute number means a more decisive game.
     * 
     * @return
     */
    public int evaluatePosition(TileColor[][] board) {
        int whitePoints = 0;
        int blackPoints = 0;
        for (int x = 0; x < board.length; x++) {
            for (int y = 0; y < board[x].length; y++) {
                int tileValue = tileValue(x, y);
                if (board[x][y] != null) {
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
        }

        return whitePoints - blackPoints;
    }

    /**
     * Returns the value of a tile in a given position. The tile is determined by
     * the following grid:
     * 10 -3 +2 +2 +2 +2 -3 10
     * -3 -4 -1 -1 -1 -1 -4 -3
     * +2 -1 +1 +0 +0 +1 -1 +2
     * +2 -1 +0 +1 +1 +0 -1 +2
     * +2 -1 +0 +1 +1 +0 -1 +2
     * +2 -1 +1 +0 +0 +1 -1 +2
     * -3 -4 -1 -1 -1 -1 -4 -3
     * 10 -3 +2 +2 +2 +2 -3 10
     * 
     * @return
     */
    private static int tileValue(int x, int y) {
        if (isCorner(x, y)) {
            return 10;
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
