package Model;
//Hele filen er skrevet af Christian

import MsgPass.ModelMsg.PassMsg;
import MsgPass.ModelMsg.TilePressedMsg;
import Shared.TileColor;
import Shared.TilePosition;
import MsgPass.ModelMsg.ResignMsg;
import MsgPass.ModelMsg.RestartBtnPressedMsg;
import Controller.Gui.PlayerCharacter;
import MsgPass.ControllerMsg.WinnerMsg;

public class AIGame extends Game {
    static AIPlayer aiPlayer;

    AIGame(GameOptions options) {
        super(options, GameMode.AI_GAME);
        aiPlayer = new AIPlayer(board);
    }

    @Override
    void handleResign(ResignMsg msg) {
        if (getNextTurn() == TileColor.WHITE) { // Prevent player resigning for AI
            var winner = nextturn.switchColor();
            Model.sendControllerMsg(new WinnerMsg(winner));
        }
    }

    @Override
    boolean handleTileClick(TilePosition pos, TilePressedMsg msg) {
        boolean superReturn = super.handleTileClick(pos, msg);

        // Handle ai's turn if white just played a move.
        if (followRules() && superReturn && !allTilesPlaced() && getNextTurn() == TileColor.BLACK) {
            var legalMoves = getAllLegalMoves(getNextTurn(), board);
            handleAITurn(legalMoves);
        }
        return superReturn;
    }

    @Override
    boolean handlePassClick(PassMsg msg) {
        boolean superReturn = super.handlePassClick(msg);

        // Handle ai's turn if white just passed.
        if (followRules() && superReturn && getNextTurn() == TileColor.BLACK) {
            var legalMoves = getAllLegalMoves(getNextTurn(), board);
            handleAITurn(legalMoves);
        }
        return superReturn;
    }

    @Override
    void handleRestartBtnPressed(RestartBtnPressedMsg msg) { // Update the correct avatars
        gamestate = GameState.EXITED;
        GameOptions newOptions = new GameOptions(options.gametime, options.countPoints,
                options.startPlayer.switchColor(), PlayerCharacter.White, PlayerCharacter.Computer);
        Model.startGame(gameMode, newOptions);
    }

    void handleAITurn(LegalMove[] legalMoves) {
        aiPlayer.updateBoard(board, legalMoves); // updates ai with the newest board

        try {
            Thread.sleep(1500); // 1.5 second buffer to give the illusion of the AI thinking
        } catch (Exception e) {
            System.out.println(e.getStackTrace());
        }

        if (legalMoves.length != 0) {
            aiPlayer.setBestMove(); // Algorithm magic
            System.out.println("Best Move is: " + aiPlayer.getBestMove());

            // we call handleTileClick directly to avoid player being able to intervene with
            // another TilePressedMsg
            handleTileClick(aiPlayer.getBestMove(), new TilePressedMsg(aiPlayer.getBestMove()));

        } else { // then pass
            System.out.println("I should pass...");
            Model.sendGameMsg(new PassMsg());
        }
    }

}
