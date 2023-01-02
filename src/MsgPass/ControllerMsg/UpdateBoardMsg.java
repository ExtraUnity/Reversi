package MsgPass.ControllerMsg;

import Shared.CellColor;
import Shared.CellPosition;

public class UpdateBoardMsg extends ControllerMsg {
    // Indeholder koordinater på de celler som skal ændres, og den farve det skal
    // ændres til.
    public final CellColor color;
    public final CellPosition[] cellPositions;

    public UpdateBoardMsg(CellColor color, CellPosition[] cellPositions) {
        this.color = color;
        this.cellPositions = cellPositions;
    }
}
