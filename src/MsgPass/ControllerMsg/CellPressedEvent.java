package MsgPass.ControllerMsg;

import Shared.CellPosition;

public class CellPressedEvent extends ControllerMsg {
    CellPosition pos;

    public CellPressedEvent(CellPosition pos) {
        this.pos = pos;
    }
}
