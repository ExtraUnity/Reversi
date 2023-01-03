package MsgPass.ControllerMsg;

import Shared.TileColor;
import Shared.TilePosition;

public class UpdateBoardMsg extends ControllerMsg {
    // Indeholder koordinater på de tiles som skal ændres, og den farve det skal
    // ændres til.
    public final TileColor color;
    public final TilePosition[] tilePositions;

    public UpdateBoardMsg(TileColor color, TilePosition[] tilePositions) {
        this.color = color;
        this.tilePositions = tilePositions;
    }
    @Override
    public String toString() {
        var str =  "Color: " + color + " [ ";
        for (TilePosition tilePosition : tilePositions) {
            str += tilePosition + ", ";
        }
        return str + " ]";
    }
}
