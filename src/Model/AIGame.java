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
        if (followRules() && getNextTurn() == TileColor.BLACK && !allTilesPlaced()) {
            var legalMoves = getAllLegalMoves(getNextTurn(), board);
            handleAITurn(legalMoves);
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

    public LegalMove miniMax(TileColor[][] board, LegalMove[] legalMoves, int depth, boolean maximizingPlayer,
            LegalMove madeMove) {
        if (depth == 0 || legalMoves.length == 0) {
            madeMove.setEvaluation(evaluatePosition(board));
            return madeMove;
        }

        // IMPLEMENT IF LEGALMOVES == 0

        if (maximizingPlayer) { // wants to minimize evaluation - Black player
            LegalMove minEval = new LegalMove(new TilePosition(0, 0), 0, 1_000_000); // every found evaluation should
                                                                                     // be better than this
            for (LegalMove move : legalMoves) {
                TileColor[][] tempBoard = copyBoard(board);
                tempBoard[move.position.x][move.position.y] = TileColor.BLACK;
                LegalMove eval = miniMax(tempBoard, Game.getAllLegalMoves(TileColor.WHITE, tempBoard), depth - 1,
                        false, move);
                move.setEvaluation(eval.evaluation);
                if (move.compareTo(minEval) < 0) {
                    minEval = move;
                }
            }
            return minEval;
        }

        // White pieces
        LegalMove maxEval = new LegalMove(new TilePosition(0, 0), 0, -1_000_000); // every found evaluation should be
        // better than this
        for (LegalMove move : legalMoves) {
            TileColor[][] tempBoard = copyBoard(board);
            tempBoard[move.position.x][move.position.y] = TileColor.WHITE;
            LegalMove eval = miniMax(tempBoard, Game.getAllLegalMoves(TileColor.BLACK, tempBoard), depth - 1,
                    true, move);
            move.setEvaluation(eval.evaluation);
            if (move.compareTo(maxEval) > 0) {
                maxEval = move;
            }
        }
        return maxEval;

    }

}
