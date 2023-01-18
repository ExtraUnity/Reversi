package Model;

//Hele filen er skrevet af Christan

import Shared.*;

public class AIPlayer {
    private TileColor[][] gameBoard;
    private LegalMove[] legalMoves;
    private TilePosition bestMove;
    private final int DEPTH;

    AIPlayer(TileColor[][] gameBoard) {
        this.gameBoard = gameBoard;
        bestMove = new TilePosition(0, 0);
        DEPTH = 5;
    }

    public void updateBoard(TileColor[][] gameBoard, LegalMove[] legalMoves) {
        this.gameBoard = gameBoard;
        this.legalMoves = legalMoves;
    }

    public void setBestMove() {
        this.bestMove = calculateMultiLayerMove();
    }

    private TilePosition calculateMultiLayerMove() {
        return miniMax(gameBoard, legalMoves, DEPTH, TileColor.BLACK,
                legalMoves[0]).position;
    }

    /**
     * Calculates the best move in the current position using the minimax algorithm
     *
     */
    private LegalMove miniMax(TileColor[][] board, LegalMove[] legalMoves, int depth, TileColor turn,
            LegalMove madeMove) {
        if (depth == 0 || legalMoves.length == 0) { // Reached the desired level or end of game
            madeMove.setEvaluation(evaluatePosition(board));
            return madeMove;
        }
        LegalMove minEval = new LegalMove(new TilePosition(0, 0), 0, 1_000_000); // worst for black
        LegalMove maxEval = new LegalMove(new TilePosition(0, 0), 0, -1_000_000); // worst for white

        for (LegalMove move : legalMoves) {
            TileColor[][] tempBoard = copyBoard(board);
            tempBoard[move.position.x][move.position.y] = turn;

            // Get evaluation from one level deeper
            LegalMove eval = miniMax(tempBoard, Game.getAllLegalMoves(TileColor.WHITE, tempBoard), depth - 1,
                    turn.switchColor(), move);

            move.setEvaluation(eval.evaluation);

            if (turn == TileColor.BLACK && move.compareTo(minEval) < 0) { // Black wants low evaluation
                minEval = move;
            } else if (turn == TileColor.WHITE && move.compareTo(maxEval) > 0) { // White wants high evaluation
                maxEval = move;
            }
        }

        return turn == TileColor.BLACK ? minEval : maxEval;
    }

    public TilePosition getBestMove() {
        return this.bestMove;
    }

    /**
     * copies the given board to avoid having the same location in memory
     */
    private TileColor[][] copyBoard(TileColor[][] board) {
        TileColor[][] newBoard = new TileColor[board.length][board[0].length];
        for (int x = 0; x < board.length; x++) {
            for (int y = 0; y < board[x].length; y++) {
                newBoard[x][y] = board[x][y];
            }
        }
        return newBoard;
    }

    /**
     * Returns an int representing the evaluation of the current game position.
     * Returns a positive value for white winning and a negative value for black
     * winning.
     * A larger absolute number means a more decisive game.
     * 
     */
    private int evaluatePosition(TileColor[][] board) {
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
