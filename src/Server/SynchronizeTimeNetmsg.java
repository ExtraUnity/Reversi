package Server;
//Filen er skrevet af Thor
import MsgPass.ModelMsg.ModelMsg;

public class SynchronizeTimeNetmsg extends ModelMsg {
    public final int blackTime;
    public final int whiteTime;

    public SynchronizeTimeNetmsg(int blackTime, int whiteTime) {
        this.blackTime = blackTime;
        this.whiteTime = whiteTime;
    }
}
