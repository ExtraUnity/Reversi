package Model;

import MsgPass.ModelMsg.PassMsg;
import MsgPass.ModelMsg.TilePressedMsg;
import Server.ServerConn;
import Shared.TilePosition;

public class MultiPlayerGame extends Game {

    MultiPlayerGame(GameOptions options) {
        super(options);
    }

    @Override
    void handlePassClick(PassMsg msg) {
        super.handlePassClick(msg);
        if (!msg.netTransfer) {
            var newmsg = new PassMsg();
            newmsg.netTransfer = true;
            ServerConn.sendModelMessage(newmsg);
        }

    }

    @Override
    void handleTileClick(TilePosition pos, TilePressedMsg msg) {
        super.handleTileClick(pos, msg);
        if (!msg.netTransfer) {
            var newmsg = new TilePressedMsg(pos);
            newmsg.netTransfer = true;
            ServerConn.sendModelMessage(newmsg);
        }

    }
}
