package Model;

import java.util.ArrayList;

import MsgPass.ControllerMsg.ControllerWindowClosedMsg;
import MsgPass.ControllerMsg.UpdateBoardMsg;
import MsgPass.ModelMsg.TilePressedMsg;
import MsgPass.ModelMsg.GameReadyMsg;
import MsgPass.ModelMsg.ModelWindowClosedMsg;
import Shared.TileColor;
import Shared.TilePosition;

public class Game {
    public enum GameMode {
        CLASSIC,
        AI_GAME,
        MULTIPLAYER
    }

    GameState gamestate = GameState.PLAYING;
    final Thread modelMainThread;

    TileColor[][] board = new TileColor[8][8];

    Game() {
        // Wait until GUI is ready before starting game.
        boolean ready = false;

        while (!ready) {
            var initMsg = Model.readModelMsg();
            if (initMsg instanceof GameReadyMsg) {
                ready = true;
            }
        }

        var game = this;
        modelMainThread = new Thread(new Runnable() {
            @Override
            public void run() {
                game.run_game();
            }
        });
        modelMainThread.start();

        Model.sendModelMsg(new TilePressedMsg(new TilePosition(3, 3)));
        Model.sendModelMsg(new TilePressedMsg(new TilePosition(3, 4)));
        Model.sendModelMsg(new TilePressedMsg(new TilePosition(4, 4)));
        Model.sendModelMsg(new TilePressedMsg(new TilePosition(4, 3)));
    }

    void run_game() {
        System.out.println(getClass().getSimpleName() + " loop started");
        while (gamestate == GameState.PLAYING) {
            // Game loop
            var modelMsg = Model.readModelMsg();
            System.out.println("Game Received " + modelMsg.getClass().getName());

            if (modelMsg instanceof TilePressedMsg) {
                TilePressedMsg msg = (TilePressedMsg) modelMsg;
                handleTileClick(msg.pos);

            } else if (modelMsg instanceof ModelWindowClosedMsg) {
                gamestate = GameState.EXITED;

                Model.sendControllerMsg(new ControllerWindowClosedMsg());

            }
        }
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
     * brikker der er blevet vendt
     */
    void handleTileClick(TilePosition pos) {
        if (isColor(pos.x, pos.y) && board[pos.x][pos.y] == null) {
            System.out.println("Illegal move at " + pos + ". Tile already colored");
            return;
        }

        var thiscolor = nextturn;
        var flippedTiles = flipTiles(pos, thiscolor);
        if (followRules() && flippedTiles.size() == 0) {
            System.out.println("Illegal move at " + pos + ". No flips");
            return;
        }

        flippedTiles.add(pos);

        for (var t_pos : flippedTiles) {
            board[t_pos.x][t_pos.y] = thiscolor;
        }

        turns++;
        Model.sendControllerMsg(new UpdateBoardMsg(thiscolor, flippedTiles.toArray(new TilePosition[0])));
        nextturn = nextturn.switchColor();
    }

    boolean isColor(int x, int y) {
        return x >= 0 && x < 8 && y >= 0 && y < 8 && board[x][y] != null;
    }

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

    ArrayList<TilePosition> flipTiles(TilePosition pos, TileColor placedColor) {
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

    boolean legalMove(TilePosition pos, TileColor color) {

        // check if tile is already filled
        if (board[pos.x][pos.y] != null) {
            System.out.println("Tile not empty");
            return false;
        }

        // check if any vertices comply with the rules

        boolean checkHorizontal = checkVertexLegal(pos, color, Vertex.HORIZONTAL);
        boolean checkVertical = checkVertexLegal(pos, color, Vertex.VERTICAL);
        System.out.println("Down");
        boolean checkDiagonalDown = checkVertexLegal(pos, color, Vertex.DIAGONALDOWN);
        System.out.println("Up");
        boolean checkDiagonalUp = checkVertexLegal(pos, color, Vertex.DIAGONALUP);
        if (!checkHorizontal && !checkVertical && !checkDiagonalDown && !checkDiagonalUp) {
            System.out.println("Move doesn't follow rules");
            return false;
        }

        return true;
    }

    boolean checkVertexLegal(TilePosition pos, TileColor color, Vertex direction) {
        TileColor opposingColor = (color == TileColor.WHITE) ? TileColor.BLACK : TileColor.WHITE;
        boolean foundColor = false;
        boolean foundOpposingColor = false;
        board[pos.x][pos.y] = color;

        /*
         * Start from left/top and check if there is a series of:
         * a black tile
         * x number of white tiles
         * finally a new black tile
         * and the opposite for the other players turn.
         */
        //
        for (int i = 0; i + boundsAdd(direction, pos) < board.length; i++) {

            TileColor currentColor;
            // choose which tile to look at next based on the direction
            switch (direction) {
                case DIAGONALDOWN:
                    int min = Math.min(pos.x, pos.y);
                    System.out.println(pos.x - min + i + " " + (pos.y - min + i));
                    currentColor = board[pos.x - min + i][pos.y - min + i];
                    break;

                case DIAGONALUP:
                    min = Math.min(pos.x, board.length - pos.y);
                    System.out.println(pos.x - min + i + " " + (pos.y + min - i));
                    currentColor = board[pos.x - min + i][pos.y + min - i];
                    break;

                case HORIZONTAL:
                    currentColor = board[i][pos.y];
                    break;
                case VERTICAL:
                    currentColor = board[pos.x][i];
                    break;
                default:
                    currentColor = null;
                    break;

            }

            if (currentColor == color) {
                if (foundColor && foundOpposingColor) {
                    return true;
                } else {
                    foundColor = true;
                }
            } else if (currentColor == opposingColor && foundColor) {
                foundOpposingColor = true;
            } else if (foundColor) {
                break;
            }
        }
        board[pos.x][pos.y] = null;

        return false;
    }

    private int boundsAdd(Vertex direction, TilePosition pos) {
        int num;
        switch (direction) {
            case DIAGONALDOWN:
                num = Math.max(pos.x, pos.y);
                break;
            case DIAGONALUP:
                num = Math.max(pos.x, board.length - pos.y);
                break;
            case HORIZONTAL:
            case VERTICAL:
            case default:
                num = 0;
                break;

        }

        return num;
    }
}

enum GameState {
    PLAYING,
    EXITED,
    WHITE_WINNER,
    BLACK_WINNER
}

enum Vertex {
    HORIZONTAL,
    VERTICAL,
    DIAGONALDOWN,
    DIAGONALUP;
}
