//Filen er skrevet af Thor
package Server;

import MsgPass.ModelMsg.ModelMsg;

public class GameTimeOptionNetmsg extends ModelMsg {
    final int gameTime;

    GameTimeOptionNetmsg(int gameTime) {
        this.gameTime = gameTime;
    }

}
