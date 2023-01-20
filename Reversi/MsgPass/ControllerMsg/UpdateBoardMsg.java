package MsgPass.ControllerMsg;

//Filen er skrevet af Flere
import Model.LegalMove;
import Shared.TileColor;
import Shared.TilePosition;

public class UpdateBoardMsg extends ControllerMsg {
    // Skrevet af Thor
    // Indeholder koordinater på de tiles som skal ændres, og den farve det skal
    // ændres til.
    public final TileColor color;
    public final TilePosition[] tilePositions;
    public final LegalMove[] legalMoves;

    public final int whitePoints;
    public final int blackPoints;

    public final boolean isPassing;
    public final int turns;

    // Skrevet af Christian
    public UpdateBoardMsg(TileColor color, TilePosition[] tilePositions, LegalMove[] legalMoves, int whitePoints,
            int blackPoints, boolean isPassing, int turns) {
        this.color = color;
        this.legalMoves = legalMoves;
        this.whitePoints = whitePoints;
        this.blackPoints = blackPoints;
        this.turns = turns;
        this.tilePositions = tilePositions;
        this.isPassing = isPassing;

    }

    // Skrevet af Thor
    public UpdateBoardMsg(TileColor color, LegalMove[] legalMoves, int whitePoints, int blackPoints, int turns) {
        this(color, new TilePosition[0], legalMoves, whitePoints, blackPoints, true, turns);
    }

    // Skrevet af Thor
    @Override
    public String toString() {
        var str = "Color: " + color + " [ ";
        for (TilePosition tilePosition : tilePositions) {
            str += tilePosition + ", ";
        }
        return str + " ]";
    }
}
