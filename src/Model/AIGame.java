package Model;

import MsgPass.ModelMsg.PassMsg;
import MsgPass.ModelMsg.TilePressedMsg;
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
        System.out.println("IM HERE!");
        if (followRules() && getNextTurn() == TileColor.BLACK) {
            System.out.println("AI will make a move now");
            var legalMoves = getAllLegalMoves(getNextTurn(), board);
            handleAITurn(legalMoves);
            System.out.println("AI is done now!");
        }
    }

    @Override
    void handlePassClick() {
        super.handlePassClick();
        if (followRules() && getNextTurn() == TileColor.BLACK) {
            var legalMoves = getAllLegalMoves(getNextTurn(), board);
            handleAITurn(legalMoves);
        }
    }

    @Override
    void handleAITurn(LegalMove[] legalMoves) {
        System.out.println("AI's turn now!");

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
