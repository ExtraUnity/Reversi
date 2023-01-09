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

    public TilePosition calculateBestMove() {
        Arrays.sort(legalMoves);
        return legalMoves[legalMoves.length - 1].position;

    }

    public TilePosition getBestMove() {
        return this.bestMove;
    }

    public void setBestMove() {
        this.bestMove = calculateBestMove();
    }
}
