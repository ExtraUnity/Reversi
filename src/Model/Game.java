package Model;

import java.util.ArrayList;

import MsgPass.ControllerMsg.ControllerWindowClosedMsg;
import MsgPass.ControllerMsg.ResetBoardMsg;
import MsgPass.ControllerMsg.UpdateBoardMsg;
import MsgPass.ControllerMsg.WinnerMsg;
import MsgPass.ModelMsg.TilePressedMsg;
import MsgPass.ModelMsg.GuiReadyMsg;
import MsgPass.ModelMsg.ModelWindowClosedMsg;
import MsgPass.ModelMsg.RestartBtnPressedMsg;
import MsgPass.ModelMsg.PassMsg;
import Shared.TileColor;
import Shared.TilePosition;

public class Game {
    public enum GameMode {
        CLASSIC,
        AI_GAME,
        MULTIPLAYER
    }

    final GameOptions options;
    GameState gamestate = GameState.PLAYING;

    TileColor[][] board = new TileColor[8][8];

    Game(GameOptions options) {
        this.options = options;
    }

    void startGame() {
        boolean ready = false;
        while (!ready) {
            System.out.println("Game waiting for Gui ready msg");
            var initMsg = Model.readGameMsg();
            if (initMsg instanceof GuiReadyMsg) {
                ready = true;
            }
        }

        Model.sendGameMsg(new TilePressedMsg(new TilePosition(3, 3)));
        Model.sendGameMsg(new TilePressedMsg(new TilePosition(3, 4)));
        Model.sendGameMsg(new TilePressedMsg(new TilePosition(4, 4)));
        Model.sendGameMsg(new TilePressedMsg(new TilePosition(4, 3)));

        run_game();
    }

    private void run_game() {
        System.out.println(getClass().getSimpleName() + " loop started");
        while (gamestate == GameState.PLAYING) {
            // Game loop
            var modelMsg = Model.readGameMsg();
            System.out.println("Game Received " + modelMsg.getClass().getName());

            // Håndter forskellige typer messages
            if (modelMsg instanceof TilePressedMsg) {
                TilePressedMsg msg = (TilePressedMsg) modelMsg;
                handleTileClick(msg.pos, false);

            } else if (modelMsg instanceof PassMsg) {
                PassMsg msg = (PassMsg) modelMsg;
                handleTileClick(msg.pos, true);

            } else if (modelMsg instanceof ModelWindowClosedMsg) {
                gamestate = GameState.EXITED;
                Model.sendControllerMsg(new ControllerWindowClosedMsg());
                Model.shutdownModel();

            } else if (modelMsg instanceof RestartBtnPressedMsg) {
                gamestate = GameState.EXITED;
                Model.sendControllerMsg(new ResetBoardMsg());
                Model.startGame(GameMode.CLASSIC, options);
            }
        }
        System.out.println(getClass().getSimpleName() + " loop ended");
    }

    private TileColor nextturn = TileColor.BLACK;
    private int turns = 0;

    private boolean followRules() {
        return turns > 3;
    }

    /**
     * Denne funktion bliver kaldt når der bliver sat en brik. Funktionen tjekker om
     * det er et lovligt træk og hvis det er håndterer den alt logikken som vender
     * andre brikker. Derefter sender den en besked til Controlleren om hvilke
     * brikker der er blevet vendt. Denne funktion er IKKE pure
     */
    void handleTileClick(TilePosition pos, boolean passingTurn) {

        if (isColor(pos.x, pos.y) && board[pos.x][pos.y] != null && !passingTurn) {
            System.out.println("Illegal move at " + pos + ". Tile already colored");
            return;
        }

        var thiscolor = nextturn;
        var flippedTiles = getAllFlipped(pos, thiscolor);
        if (followRules() && flippedTiles.size() == 0 && !passingTurn) {
            System.out.println("Illegal move at " + pos + ". No flips");
            return;
        }

        flippedTiles.add(pos);

        for (var t_pos : flippedTiles) {
            board[t_pos.x][t_pos.y] = thiscolor;
        }

        turns++;
        nextturn = nextturn.switchColor();
        var legalMoves = getAllLegalMoves();

        int whitePoints = getPoints(TileColor.WHITE);
        int blackPoints = getPoints(TileColor.BLACK);
        if (passingTurn) {
            flippedTiles = new ArrayList<TilePosition>();
        }
        Model.sendControllerMsg(new UpdateBoardMsg(thiscolor, flippedTiles.toArray(new TilePosition[0]), legalMoves,
                whitePoints, blackPoints));

        if (legalMoves.length == 0 || allTilesPlaced()) {
            if (noLegalsLastTurn) {
                // Send gameover beskrev hvor vinderen er den med flest point
                if (whitePoints > blackPoints) {
                    Model.sendControllerMsg(new WinnerMsg(TileColor.WHITE));
                } else {
                    Model.sendControllerMsg(new WinnerMsg(TileColor.BLACK));
                }
            }
            noLegalsLastTurn = true;
        }

    }

    boolean noLegalsLastTurn = false;

    boolean allTilesPlaced() {
        for (int x = 0; x < board.length; x++) {
            for (int y = 0; y < board[x].length; y++) {
                if (board[x][y] != null) {
                    return false;
                }
            }
        }

        return true;
    }

    boolean isColor(int x, int y) {
        return x >= 0 && x < 8 && y >= 0 && y < 8 && board[x][y] != null;
    }

    /**
     * Laver en liste af alle de tiles som skal flippes, i den retning som bliver
     * givet af dx og dy. Denne funktion er pure
     */
    ArrayList<TilePosition> flipable(TilePosition pos, int dx, int dy, TileColor placedColor) {
        var flipped = new ArrayList<TilePosition>();
        int x = pos.x + dx;
        int y = pos.y + dy;
        boolean flipValid;
        while (true) {
            if (!isColor(x, y)) {
                flipValid = false;
                break;
            }
            if (board[x][y].otherColor(placedColor)) {
                flipped.add(new TilePosition(x, y));
                x += dx;
                y += dy;
            } else {
                flipValid = true;
                break;
            }
        }
        if (flipValid) {
            for (var flip_pos : flipped) {
                System.out.println(pos + " dir " + dx + " " + dy + " flipped " + flip_pos);
            }
            return flipped;
        } else {
            return new ArrayList<>();
        }
    }

    /**
     * Finder alle de tiles som kan vendes ved et givet træk. Denne funktion er
     * pure.
     */
    ArrayList<TilePosition> getAllFlipped(TilePosition pos, TileColor placedColor) {
        // Flip all above
        var aboveFlipped = flipable(pos, 0, 1, placedColor);
        var rightFlipped = flipable(pos, 1, 0, placedColor);
        var belowFlipped = flipable(pos, 0, -1, placedColor);
        var leftFlipped = flipable(pos, -1, 0, placedColor);

        var topRightFlipped = flipable(pos, 1, -1, placedColor);
        var botRightFlipped = flipable(pos, 1, 1, placedColor);

        var topLeftFlipped = flipable(pos, -1, -1, placedColor);
        var botLeftFlipped = flipable(pos, -1, 1, placedColor);

        var allFlipped = new ArrayList<TilePosition>();
        allFlipped.addAll(aboveFlipped);
        allFlipped.addAll(rightFlipped);
        allFlipped.addAll(belowFlipped);
        allFlipped.addAll(leftFlipped);

        allFlipped.addAll(topRightFlipped);
        allFlipped.addAll(botRightFlipped);
        allFlipped.addAll(topLeftFlipped);
        allFlipped.addAll(botLeftFlipped);

        return allFlipped;
    }

    /**
     * Finds all legal moves and returns an array of them :=)
     */
    public LegalMove[] getAllLegalMoves() {
        var legalMoves = new ArrayList<LegalMove>();
        for (int x = 0; x < board.length; x++) {
            for (int y = 0; y < board[x].length; y++) {
                if (board[x][y] == null) {
                    var pos = new TilePosition(x, y);
                    int flipped = flippedFromMove(pos, nextturn);
                    if (flipped > 0) {
                        legalMoves.add(new LegalMove(pos, flipped));
                    }
                }

            }
        }
        return legalMoves.toArray(new LegalMove[0]);
    }

    int flippedFromMove(TilePosition pos, TileColor color) {
        return getAllFlipped(pos, color).size();
    }

    /**
     * finder hvor mange point en givet farve har. Dette er en pure funktion.
     */
    int getPoints(TileColor color) {
        int points = 0;
        for (int x = 0; x < board.length; x++) {
            for (int y = 0; y < board[x].length; y++) {
                if (board[x][y] != null && board[x][y].equals(color)) {
                    points += 1;
                }
            }
        }
        return points;
    }

}

enum GameState {
    PLAYING,
    EXITED,
    WHITE_WINNER,
    BLACK_WINNER
}
