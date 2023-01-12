package Model;

import java.util.ArrayList;

import Controller.Gui.Gui;
import Controller.Gui.PlayerCharacter;
import Controller.Gui.Timer;
import Controller.Gui.ButtonPass;
import MsgPass.ControllerMsg.ControllerWindowClosedMsg;
import MsgPass.ControllerMsg.UpdateBoardMsg;
import MsgPass.ControllerMsg.WinnerMsg;
import MsgPass.ModelMsg.TilePressedMsg;
import MsgPass.ModelMsg.GuiReadyMsg;
import MsgPass.ModelMsg.MainMenuMsg;
import MsgPass.ModelMsg.ModelWindowClosedMsg;
import MsgPass.ModelMsg.RestartBtnPressedMsg;
import MsgPass.ModelMsg.PassMsg;
import MsgPass.ModelMsg.ResignMsg;
import Shared.TileColor;
import Shared.TilePosition;

public abstract class Game {
    public enum GameMode {
        CLASSIC,
        AI_GAME,
        MULTIPLAYER
    }

    final GameMode gameMode;

    final GameOptions options;
    protected GameState gamestate = GameState.PLAYING;

    static TileColor[][] board = new TileColor[8][8];

    Game(GameOptions options, GameMode gameMode) {
        this.options = options;
        this.gameMode = gameMode;
        board = new TileColor[8][8];
        nextturn = options.startPlayer;
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
        var msg1 = new TilePressedMsg(new TilePosition(3, 3));
        msg1.ignoreNet = true;
        Model.sendGameMsg(msg1);
        var msg2 = new TilePressedMsg(new TilePosition(3, 4));
        msg2.ignoreNet = true;
        Model.sendGameMsg(msg2);
        var msg3 = new TilePressedMsg(new TilePosition(4, 4));
        msg3.ignoreNet = true;
        Model.sendGameMsg(msg3);
        var msg4 = new TilePressedMsg(new TilePosition(4, 3));
        msg4.ignoreNet = true;
        Model.sendGameMsg(msg4);

        if (options.gametime > 0) {
            new Thread(() -> {
                runTimer(options.gametime);
            }).start();
        }

        run_game();
    }

    protected void run_game() {
        System.out.println(getClass().getSimpleName() + " loop started");
        while (gamestate == GameState.PLAYING) {
            // Game loop
            var modelMsg = Model.readGameMsg();
            // System.out.println("Game Received " + modelMsg.getClass().getName());

            // Håndter forskellige typer messages
            if (modelMsg instanceof TilePressedMsg) {
                TilePressedMsg msg = (TilePressedMsg) modelMsg;
                handleTileClick(msg.pos, msg);

            } else if (modelMsg instanceof PassMsg) {
                handlePassClick((PassMsg) modelMsg);

            } else if (modelMsg instanceof ModelWindowClosedMsg) {
                gamestate = GameState.EXITED;
                Model.sendControllerMsg(new ControllerWindowClosedMsg());
                Model.shutdownModel();

            } else if (modelMsg instanceof RestartBtnPressedMsg) {

                handleRestartBtnPressed((RestartBtnPressedMsg) modelMsg);
            } else if (modelMsg instanceof ResignMsg) {
                handleResign((ResignMsg) modelMsg);
            } else if (modelMsg instanceof MainMenuMsg) {
                handleMainMenuPressed();
            }
        }
        System.out.println(getClass().getSimpleName() + " loop ended");
    }

    void handleResign(ResignMsg msg) {
        var winner = nextturn.switchColor();
        Model.sendControllerMsg(new WinnerMsg(winner));
    }

    void handleRestartBtnPressed(RestartBtnPressedMsg msg) {
        gamestate = GameState.EXITED;
        GameOptions newOptions = new GameOptions(options.gametime, options.countPoints,
                options.startPlayer.switchColor(), PlayerCharacter.White, PlayerCharacter.Black);
        Model.startGame(gameMode, newOptions);
    }

    void handleMainMenuPressed() {
        gamestate = GameState.EXITED;
    }

    protected static TileColor nextturn = TileColor.BLACK;
    private int turns = 0;

    public boolean followRules() {
        return turns > 3;
    }

    /**
     * Denne funktion håndterer når pass knappen bliver trykket på. Den tjekker om
     * brugeren
     * må melde pas, og hvis de må, så udregner den de mulige træk for modstanderen,
     * samt skifter farven.
     * Derefter sender den besked til controlleren om de ting, der skal ændres.
     * 
     * @return Den returner hvis der bliver passet
     */
    boolean handlePassClick(PassMsg msg) {
        ButtonPass ButtonPass = Gui.getMenuBottom().getButtonPass();
        if (!ButtonPass.getAvailable()) {
            System.out.println("YOU SHALL NOT PASS!");
            return false;
        }
        var thiscolor = nextturn;
        nextturn.switchColor();
        TilePosition noTile = new TilePosition(0, 0);

        for (var t_pos : getAllFlipped(noTile, thiscolor, board)) {
            board[t_pos.x][t_pos.y] = thiscolor;
        }
        turns++;
        nextturn = nextturn.switchColor();
        var legalMoves = getAllLegalMoves(nextturn, board);
        System.out.println("Legal moves: " + legalMoves.length);
        // flippedTiles = new ArrayList<TilePosition>();
        int whitePoints = getPoints(TileColor.WHITE);
        int blackPoints = getPoints(TileColor.BLACK);

        Model.sendControllerMsg(new UpdateBoardMsg(thiscolor, legalMoves, whitePoints, blackPoints, turns));
        checkWinner(whitePoints, blackPoints);
        noLegalsLastTurn = true;
        return true;
    }

    /**
     * Denne funktion bliver kaldt når der bliver sat en brik. Funktionen tjekker om
     * det er et lovligt træk og hvis det er håndterer den alt logikken som vender
     * andre brikker. Derefter sender den en besked til Controlleren om hvilke
     * brikker der er blevet vendt. Denne funktion er IKKE pure
     * 
     * @return Den returner true hvis brikken bliver sat. Ellers returner den false.
     */
    boolean handleTileClick(TilePosition pos, TilePressedMsg msg) {

        if (isColor(pos.x, pos.y, board) && board[pos.x][pos.y] != null) {
            System.out.println("Illegal move at " + pos + ". Tile already colored");
            return false;
        }

        var thiscolor = nextturn;
        var flippedTiles = getAllFlipped(pos, thiscolor, board);
        if (followRules() && flippedTiles.size() == 0) {
            System.out.println("Illegal move at " + pos + ". No flips");
            return false;
        }

        flippedTiles.add(pos);

        for (var t_pos : flippedTiles) {
            board[t_pos.x][t_pos.y] = thiscolor;
        }

        turns++;
        nextturn = nextturn.switchColor();
        var legalMoves = getAllLegalMoves(nextturn, board);

        int whitePoints = getPoints(TileColor.WHITE);
        int blackPoints = getPoints(TileColor.BLACK);
        System.out.println("Legal moves: " + legalMoves.length);
        System.out.println("The move " + pos + " has been played");
        Model.sendControllerMsg(new UpdateBoardMsg(thiscolor, flippedTiles.toArray(new TilePosition[0]), legalMoves,
                whitePoints, blackPoints, false, turns));
        noLegalsLastTurn = false;
        checkWinner(whitePoints, blackPoints);
        return true;
    }

    void handleAITurn(LegalMove[] legalMoves) {
    }

    boolean noLegalsLastTurn = false;

    void checkWinner(int whitePoints, int blackPoints) {
        if (noLegalsLastTurn || allTilesPlaced()) {
            // Send gameover beskrev hvor vinderen er den med flest point
            System.out.println("Game over");
            System.out.println("White has: " + whitePoints + " Black has: " + blackPoints);
            if (whitePoints > blackPoints) {
                Model.sendControllerMsg(new WinnerMsg(TileColor.WHITE));
            } else if (blackPoints > whitePoints) {
                Model.sendControllerMsg(new WinnerMsg(TileColor.BLACK));
            } else {
                Model.sendControllerMsg(new WinnerMsg(TileColor.EMPTY));
            }
        }
    }

    boolean allTilesPlaced() {
        for (int x = 0; x < board.length; x++) {
            for (int y = 0; y < board[x].length; y++) {
                if (board[x][y] == null) {
                    return false;
                }
            }
        }

        return true;
    }

    static boolean isColor(int x, int y, TileColor[][] board) {
        return x >= 0 && x < 8 && y >= 0 && y < 8 && board[x][y] != null;
    }

    /**
     * Laver en liste af alle de tiles som skal flippes, i den retning som bliver
     * givet af dx og dy. Denne funktion er pure
     */
    static ArrayList<TilePosition> flipable(TilePosition pos, int dx, int dy, TileColor placedColor,
            TileColor[][] board) {
        var flipped = new ArrayList<TilePosition>();
        int x = pos.x + dx;
        int y = pos.y + dy;
        boolean flipValid;
        while (true) {
            if (!isColor(x, y, board)) {
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
            return flipped;
        } else {
            return new ArrayList<>();
        }
    }

    /**
     * Finder alle de tiles som kan vendes ved et givet træk. Denne funktion er
     * pure.
     */
    static ArrayList<TilePosition> getAllFlipped(TilePosition pos, TileColor placedColor, TileColor[][] board) {
        // Flip all above
        var aboveFlipped = flipable(pos, 0, 1, placedColor, board);
        var rightFlipped = flipable(pos, 1, 0, placedColor, board);
        var belowFlipped = flipable(pos, 0, -1, placedColor, board);
        var leftFlipped = flipable(pos, -1, 0, placedColor, board);

        var topRightFlipped = flipable(pos, 1, -1, placedColor, board);
        var botRightFlipped = flipable(pos, 1, 1, placedColor, board);

        var topLeftFlipped = flipable(pos, -1, -1, placedColor, board);
        var botLeftFlipped = flipable(pos, -1, 1, placedColor, board);

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
    public static LegalMove[] getAllLegalMoves(TileColor nextturn, TileColor[][] gameBoard) {
        var legalMoves = new ArrayList<LegalMove>();
        for (int x = 0; x < gameBoard.length; x++) {
            for (int y = 0; y < gameBoard[x].length; y++) {
                if (gameBoard[x][y] == null) {
                    var pos = new TilePosition(x, y);
                    int flipped = flippedFromMove(pos, nextturn, gameBoard);
                    if (flipped > 0) {
                        legalMoves.add(new LegalMove(pos, flipped));
                    }
                }
            }
        }
        return legalMoves.toArray(new LegalMove[0]);
    }

    static int flippedFromMove(TilePosition pos, TileColor color, TileColor[][] gameBoard) {
        return getAllFlipped(pos, color, gameBoard).size();
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

    public static TileColor getNextTurn() {
        return nextturn;
    }

    public static void setNextTurn(TileColor color) {
        nextturn = color;
    }

    private void runTimer(int gameTime) {
        try {
            // Shared memory, dont care. Man skal være meget uheldig for at det her bliver
            // et problem
            int whiteTimer = gameTime;
            int blackTimer = gameTime;
            while (gamestate == GameState.PLAYING) {

                // 1 sekund
                Thread.sleep(1000);
                if (nextturn == TileColor.WHITE) {
                    whiteTimer -= 1;
                    if (whiteTimer <= 0) {
                        Model.sendControllerMsg(new WinnerMsg(TileColor.BLACK));
                    } else {
                        Timer.setTime(TileColor.WHITE, whiteTimer);
                    }

                } else {
                    blackTimer -= 1;
                    if (blackTimer <= 0) {
                        Model.sendControllerMsg(new WinnerMsg(TileColor.WHITE));
                    } else {
                        Timer.setTime(TileColor.BLACK, blackTimer);
                    }

                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}

enum GameState {
    PLAYING,
    EXITED,
    WHITE_WINNER,
    BLACK_WINNER
}
