package MsgPass.ModelMsg;

//Filen er skrevet af Frederik
import Shared.TilePosition;

public class TilePressedMsg extends ModelMsg {
    public final TilePosition pos;

    public TilePressedMsg(TilePosition pos) {
        this.pos = pos;
    }
}
