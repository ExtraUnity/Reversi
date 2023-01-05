package MsgPass.ControllerMsg;

import Model.LegalMove;
import Shared.TileColor;
import Shared.TilePosition;

public class UpdateBoardMsg extends ControllerMsg {
    // Indeholder koordinater på de tiles som skal ændres, og den farve det skal
    // ændres til.
    public final TileColor color;
    public final TilePosition[] tilePositions;
    public final LegalMove[] legalMoves;

    public final int whitePoints;
    public final int blackPoints;

    public final boolean isPassing;

    public UpdateBoardMsg(TileColor color, TilePosition[] tilePositions, LegalMove[] legalMoves, int whitePoints,
            int blackPoints, boolean isPassing) {
        this.color = color;
        this.tilePositions = tilePositions;
        this.legalMoves = legalMoves;
        this.whitePoints = whitePoints;
        this.blackPoints = blackPoints;
        this.isPassing = isPassing;
    }

    public UpdateBoardMsg(TileColor color, LegalMove[] legalMoves, int whitePoints, int blackPoints) {
        this.color = color;
        this.tilePositions = new TilePosition[0];
        this.legalMoves = legalMoves;
        this.whitePoints = whitePoints;
        this.blackPoints = blackPoints;
        this.isPassing = true;
    }

    @Override
    public String toString() {
        var str = "Color: " + color + " [ ";
        for (TilePosition tilePosition : tilePositions) {
            str += tilePosition + ", ";
        }
        return str + " ]";
    }
}
