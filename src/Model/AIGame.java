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
        System.out.println("WOOP WOOP!");
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
}
