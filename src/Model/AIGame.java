package Model;

import Shared.TileColor;

public class AIGame extends Game {
    static AIPlayer aiPlayer;

    AIGame(GameOptions options) {
        super(options);
        aiPlayer = new AIPlayer(board);
        aiPlayer.setMyTurn(options.startPlayer == TileColor.BLACK);
    }

    @Override
    void handleAITurn(LegalMove[] legalMoves) {
        aiPlayer.setLegalMoves(legalMoves);
        aiPlayer.setGameBoard(board);
        System.out.println("WOOP WOOP!");
        if (aiPlayer.isMyTurn()) {
            aiPlayer.update(board, legalMoves);
            System.out.println("Best Move is: " + AIGame.aiPlayer.getBestMove());
            aiPlayer.setMyTurn(false);
            if (legalMoves.length != 0) {
                handleTileClick(aiPlayer.getBestMove());
            } else {
                handlePassClick();
            }
        } else {
            aiPlayer.setMyTurn(true);
        }
    }
}
