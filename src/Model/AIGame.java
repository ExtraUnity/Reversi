package Model;

import MsgPass.ModelMsg.PassMsg;
import MsgPass.ModelMsg.TilePressedMsg;
import Shared.TileColor;
import Shared.TilePosition;

public class AIGame extends Game {
    static AIPlayer aiPlayer;

    AIGame(GameOptions options) {
        super(options, GameMode.AI_GAME);
        aiPlayer = new AIPlayer(board);
        aiPlayer.setMyTurn(options.startPlayer == TileColor.BLACK);
    }

    @Override
    boolean handleTileClick(TilePosition pos, TilePressedMsg msg) {
        boolean superReturn = super.handleTileClick(pos, msg);
        if (followRules() && getNextTurn() == TileColor.BLACK && !allTilesPlaced()) {
            var legalMoves = getAllLegalMoves(getNextTurn(), board);
            handleAITurn(legalMoves);
        }
        return superReturn;
    }

    @Override
    boolean handlePassClick(PassMsg msg) {
        boolean superReturn = super.handlePassClick(msg);
        if (followRules() && getNextTurn() == TileColor.BLACK) {
            var legalMoves = getAllLegalMoves(getNextTurn(), board);
            handleAITurn(legalMoves);
        }
        return superReturn;
    }

    @Override
    void handleAITurn(LegalMove[] legalMoves) {

        aiPlayer.updateBoard(board, legalMoves); // updates with the newest board

        // TEMPORARY
        try {
            Thread.sleep(1500);
        } catch (Exception e) {
            System.out.println(e.getStackTrace());
        }
        //

        if (legalMoves.length != 0) {
            aiPlayer.setBestMove(); // Algorithm magic
            System.out.println("Best Move is: " + aiPlayer.getBestMove());

            Model.sendGameMsg(new TilePressedMsg(aiPlayer.getBestMove()));
            // handleTileClick(aiPlayer.getBestMove());
        } else { // then pass
            System.out.println("I should pass...");
            Model.sendGameMsg(new PassMsg());
        }
    }

}
