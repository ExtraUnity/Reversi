package MsgPass.ControllerMsg;

import Shared.CellState;
import Shared.CellPosition;

public class UpdateBoardMsg extends ControllerMsg {
    // Indeholder koordinater på de celler som skal ændres, og den farve det skal
    // ændres til.
    public final CellState color;
    public final CellPosition[] cellPositions;

    public UpdateBoardMsg(CellState color, CellPosition[] cellPositions) {
        this.color = color;
        this.cellPositions = cellPositions;
    }
    @Override
    public String toString() {
        var str =  "Color: " + color + " [ ";
        for (CellPosition cellPosition : cellPositions) {
            str += cellPosition + ", ";
        }
        return str + " ]";
    }
}
