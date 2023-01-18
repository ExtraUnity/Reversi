package MsgPass.ControllerMsg;

import Shared.TileColor;

//Skrevet af Frederik
public class WinnerMsg extends ControllerMsg {
    public final TileColor winner;

    public WinnerMsg(TileColor winner) {
        this.winner = winner;
    }

}
