package MsgPass.ControllerMsg;

import Shared.TileColor;

public class WinnerMsg extends ControllerMsg {
    public final TileColor winner;

    public WinnerMsg(TileColor winner) {
        this.winner = winner;
    }

}
