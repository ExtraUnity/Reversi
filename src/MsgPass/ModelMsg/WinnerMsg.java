package MsgPass.ModelMsg;

import Shared.TileColor;

public class WinnerMsg extends ModelMsg {
    TileColor winner;

    public WinnerMsg(TileColor winner) {
        this.winner = winner;
    }

}
