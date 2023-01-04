package MsgPass.ModelMsg;
import Shared.TilePosition;
public class PassMsg extends ModelMsg {
    public TilePosition pos;

    public PassMsg() {
        pos = new TilePosition(0, 0);
    }
}
