package Model;

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
        aiPlayer.setMyTurn(options.startPlayer == TileColor.BLACK);
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
        boolean superReturn = super.handleTileClick(pos, msg);
        if (followRules() && superReturn && !allTilesPlaced() && getNextTurn() == TileColor.BLACK) {
            var legalMoves = getAllLegalMoves(getNextTurn(), board);
            handleAITurn(legalMoves);
        }
        return superReturn;
    }

    @Override
    boolean handlePassClick(PassMsg msg) {
        boolean superReturn = super.handlePassClick(msg);
        if (followRules() && superReturn && getNextTurn() == TileColor.BLACK) {
            var legalMoves = getAllLegalMoves(getNextTurn(), board);
            handleAITurn(legalMoves);
        }
        return superReturn;
    }

    @Override
    void handleRestartBtnPressed(RestartBtnPressedMsg msg) {
         gamestate = GameState.EXITED;
        GameOptions newOptions = new GameOptions(options.gametime, options.countPoints,
                options.startPlayer.switchColor(), PlayerCharacter.White, PlayerCharacter.Computer);
        Model.startGame(gameMode, newOptions);
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

            // we call handleTileClick directly to avoid player being able to intervene with
            // another TilePressedMsg
            handleTileClick(aiPlayer.getBestMove(), new TilePressedMsg(aiPlayer.getBestMove()));

        } else { // then pass
            System.out.println("I should pass...");
            Model.sendGameMsg(new PassMsg());
        }
    }

}
