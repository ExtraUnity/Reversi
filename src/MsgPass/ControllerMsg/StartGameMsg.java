package MsgPass.ControllerMsg;
//Filen er skrevet af Thor
import Model.GameOptions;

public class StartGameMsg extends ControllerMsg {
    public final GameOptions gameOptions;

    public StartGameMsg(GameOptions gameOptions) {
        this.gameOptions = gameOptions;
    }

}
