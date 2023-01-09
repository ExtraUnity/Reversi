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
        if (nextturn != ServerConn.selfColor) {
            return;
        }
        super.handlePassClick(msg);
        if (!msg.ignoreNet) {
            var newmsg = new PassMsg();
            newmsg.ignoreNet = true;
            ServerConn.sendModelMessage(newmsg);
        }

    }

    @Override
    void handleTileClick(TilePosition pos, TilePressedMsg msg) {

        if (!msg.ignoreNet) {
            // Hvis den ikke ignorer net må det betyde at det er dig selv som har trykket.
            // Derfor skal den tjekke om det er din tur!
            if (nextturn != ServerConn.selfColor && followRules()) {
                System.out.println("NOT YOUR TURN CANT PLACE TILE");
                return;
            }
            super.handleTileClick(pos, msg);
            System.out.println(msg + " does not ignore net and will be sent");
            var newmsg = new TilePressedMsg(pos);
            newmsg.ignoreNet = true;
            ServerConn.sendModelMessage(newmsg);
        } else {
            super.handleTileClick(pos, msg);
            System.out.println(msg + " ignores net and is not being sent");
        }

    }
}
