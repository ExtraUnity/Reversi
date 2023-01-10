package Model;

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
                System.out.println("Best Move is: " + aiPlayer.getBestMove());
                handleTileClick(aiPlayer.getBestMove());
            } else {
                System.out.println("I should pass...");
                handlePassClick();
            }
        } else {
            aiPlayer.setMyTurn(true);
        }
    }

}
