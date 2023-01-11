package Model;

import java.util.Arrays;

import Shared.*;

public class AIPlayer {
    private TileColor[][] gameBoard;
    private LegalMove[] legalMoves;
    private boolean isMyTurn;
    private TilePosition bestMove;
    private long timeUsed;

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

    public TilePosition calculateMultiLayerMove() {
        timeUsed = System.nanoTime();
        return miniMax(gameBoard, legalMoves, 5, true,
                legalMoves[0]).position;
    }

    public LegalMove miniMax(TileColor[][] board, LegalMove[] legalMoves, int depth, boolean maximizingPlayer,
            LegalMove madeMove) {
        if (depth == 0 || legalMoves.length == 0) {
            madeMove.setEvaluation(evaluatePosition(board));
            return madeMove;
        }

        if (maximizingPlayer) { // wants to minimize evaluation
            LegalMove minEval = new LegalMove(new TilePosition(0, 0), 0, 1_000_000); // every found evaluation should be
                                                                                     // better than this
            for (LegalMove move : legalMoves) {
                TileColor[][] tempBoard = copyBoard(board);
                tempBoard[move.position.x][move.position.y] = TileColor.BLACK;
                LegalMove eval = miniMax(tempBoard, Game.getAllLegalMoves(TileColor.WHITE, tempBoard), depth - 1,
                        false, move);
                if (eval.compareTo(minEval) < 0) {
                    minEval = move;
                }
            }
            return minEval;
        }

        LegalMove maxEval = new LegalMove(new TilePosition(0, 0), 0, -1_000_000); // every found evaluation should be
                                                                                  // better than this
        for (LegalMove move : legalMoves) {
            TileColor[][] tempBoard = copyBoard(board);
            tempBoard[move.position.x][move.position.y] = TileColor.WHITE;
            LegalMove eval = miniMax(tempBoard, Game.getAllLegalMoves(TileColor.BLACK, tempBoard), depth - 1,
                    true, move);
            if (eval.compareTo(maxEval) > 0) {
                maxEval = move;
            }
        }
        return maxEval;

    }

    public TilePosition calculateBestMove(TileColor[][] board) {
        TilePosition currentBestMove = new TilePosition(0, 0);
        int bestEvaluation = 0;

        // Check for all legal moves
        for (int i = 0; i < legalMoves.length; i++) {

            // Place the move on a temporary board
            TileColor[][] tempBoard = copyBoard(board);
            var flippedTiles = AIGame.getAllFlipped(legalMoves[i].position,
                    AIGame.getNextTurn(), tempBoard);

            flippedTiles.add(legalMoves[i].position);
            for (var t_pos : flippedTiles) {
                tempBoard[t_pos.x][t_pos.y] = AIGame.getNextTurn();
            }
            //////////////////////////////////////

            // Evaluate the position after the current move has been made
            int currentMoveEvaluation = evaluatePosition(tempBoard);
            System.out.println("Move: " + legalMoves[i].position + " Evaluation: " +
                    currentMoveEvaluation);
            if (i == 0) { // sets the bestEvaluation to the first one checked
                currentBestMove = legalMoves[i].position;
                bestEvaluation = currentMoveEvaluation;
                continue;
            }

            /*
             * Checks whether current evaluation is better for AI than bestEvaluation
             * AI will always be black.
             * So it's the best move for black.
             */
            if (currentMoveEvaluation < bestEvaluation) {
                currentBestMove = legalMoves[i].position;
                bestEvaluation = currentMoveEvaluation;
            }

        }
        System.out.println("My best move is: " + currentBestMove);
        return currentBestMove;

        // Arrays.sort(legalMoves);
        // return legalMoves[legalMoves.length - 1].position;

    }

    public TilePosition getBestMove() {
        return this.bestMove;
    }

    private TileColor[][] copyBoard(TileColor[][] board) {
        TileColor[][] newBoard = new TileColor[board.length][board[0].length];
        for (int x = 0; x < board.length; x++) {
            for (int y = 0; y < board[x].length; y++) {
                newBoard[x][y] = board[x][y];
            }
        }
        return newBoard;
    }

    public void setBestMove() {
        this.bestMove = calculateMultiLayerMove();
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
