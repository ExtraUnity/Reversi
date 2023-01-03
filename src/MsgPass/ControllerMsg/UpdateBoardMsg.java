package MsgPass.ControllerMsg;

import Shared.TileColor;
import Shared.TilePosition;

public class UpdateBoardMsg extends ControllerMsg {
    // Indeholder koordinater på de celler som skal ændres, og den farve det skal
    // ændres til.
    public final TileColor color;
    public final TilePosition[] cellPositions;

    public UpdateBoardMsg(TileColor color, TilePosition[] cellPositions) {
        this.color = color;
        this.cellPositions = cellPositions;
    }
    @Override
    public String toString() {
        var str =  "Color: " + color + " [ ";
        for (TilePosition cellPosition : cellPositions) {
            str += cellPosition + ", ";
        }
        return str + " ]";
    }
}
