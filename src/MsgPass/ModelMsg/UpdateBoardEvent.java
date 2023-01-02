package MsgPass.ModelMsg;

import Shared.CellColor;
import Shared.CellPosition;

public class UpdateBoardEvent extends ModelMsg {
    // Indeholder koordinater på de celler som skal ændres, og den farve det skal
    // ændres til.
    public final CellColor color;
    public final CellPosition[] cellPositions;

    public UpdateBoardEvent(CellColor color, CellPosition[] cellPositions) {
        this.color = color;
        this.cellPositions = cellPositions;
    }
}
