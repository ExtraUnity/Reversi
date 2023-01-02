package MsgPass.ModelMsg;

import Shared.CellPosition;

public class CellPressedMsg extends ModelMsg {
    public final CellPosition pos;

    public CellPressedMsg(CellPosition pos) {
        this.pos = pos;
    }
}
