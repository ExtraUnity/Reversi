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
    public TilePosition[] tilePositions;
    public final LegalMove[] legalMoves;

    public final int whitePoints;
    public final int blackPoints;

    public boolean isPassing;
    public final int turns;

    // Skrevet af Christian
    public UpdateBoardMsg(TileColor color, TilePosition[] tilePositions, LegalMove[] legalMoves, int whitePoints,
            int blackPoints, boolean isPassing, int turns) {
        this(color, legalMoves, whitePoints, blackPoints, turns);
        this.tilePositions = tilePositions;
        this.isPassing = isPassing;

    }

    // Skrevet af Thor
    public UpdateBoardMsg(TileColor color, LegalMove[] legalMoves, int whitePoints, int blackPoints, int turns) {
        this.color = color;
        this.tilePositions = new TilePosition[0];
        this.legalMoves = legalMoves;
        this.whitePoints = whitePoints;
        this.blackPoints = blackPoints;
        this.isPassing = true;
        this.turns = turns;
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
