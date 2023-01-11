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
            var legalMoves = getAllLegalMoves(getNextTurn(), board);
            handleAITurn(legalMoves);
        }
    }

    @Override
    void handlePassClick() {
        super.handlePassClick();
        if (followRules()) {
            var legalMoves = getAllLegalMoves(getNextTurn(), board);
            handleAITurn(legalMoves);
        }
    }

    @Override
    void handleAITurn(LegalMove[] legalMoves) {
        if (!aiPlayer.isMyTurn()) {
            aiPlayer.setMyTurn(true);
            return;
        }

        aiPlayer.updateBoard(board, legalMoves); // updates with the newest board

        // TEMPORARY
        try {
            // Thread.sleep(1500);
        } catch (Exception e) {
            System.out.println(e.getStackTrace());
        }
        //

        aiPlayer.setMyTurn(false);

        if (legalMoves.length != 0) {
            aiPlayer.setBestMove(); // Algorithm magic
            System.out.println("Best Move is: " + aiPlayer.getBestMove());

            handleTileClick(aiPlayer.getBestMove());
        } else { // then pass
            System.out.println("I should pass...");
            handlePassClick();
        }
    }

}
