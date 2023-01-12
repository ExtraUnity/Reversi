package Model;

import MsgPass.ModelMsg.PassMsg;
import MsgPass.ModelMsg.TilePressedMsg;
import Shared.TileColor;
import Shared.TilePosition;
import MsgPass.ModelMsg.ResignMsg;
import MsgPass.ControllerMsg.WinnerMsg;

public class AIGame extends Game {
    static AIPlayer aiPlayer;
    private static boolean isCalculating;

    AIGame(GameOptions options) {
        super(options, GameMode.AI_GAME);
        aiPlayer = new AIPlayer(board);
        aiPlayer.setMyTurn(options.startPlayer == TileColor.BLACK);
        isCalculating = false;
    }

    @Override
    void handleResign(ResignMsg msg) {
        if (getNextTurn() == TileColor.WHITE) {
            var winner = nextturn.switchColor();
            Model.sendControllerMsg(new WinnerMsg(winner));
        }
    }

    @Override
    boolean handleTileClick(TilePosition pos, TilePressedMsg msg) {
        if (isCalculating) {
            return false;
        }
        boolean superReturn = super.handleTileClick(pos, msg);
        if (followRules() && superReturn && !allTilesPlaced() && getNextTurn() == TileColor.BLACK) {
            var legalMoves = getAllLegalMoves(getNextTurn(), board);
            handleAITurn(legalMoves);
        }
        return superReturn;
    }

    @Override
    boolean handlePassClick(PassMsg msg) {
        if (isCalculating) {
            return false;
        }
        boolean superReturn = super.handlePassClick(msg);
        if (followRules() && superReturn && getNextTurn() == TileColor.BLACK) {
            var legalMoves = getAllLegalMoves(getNextTurn(), board);
            handleAITurn(legalMoves);
        }
        return superReturn;
    }

    @Override
    void handleAITurn(LegalMove[] legalMoves) {
        isCalculating = true;
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
            isCalculating = false;
            handleTileClick(aiPlayer.getBestMove(), new TilePressedMsg(aiPlayer.getBestMove()));
            // Model.sendGameMsg(new TilePressedMsg(aiPlayer.getBestMove()));

            //
        } else { // then pass
            System.out.println("I should pass...");
            Model.sendGameMsg(new PassMsg());
        }
    }

}
