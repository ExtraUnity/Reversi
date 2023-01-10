package Model;

import MsgPass.ModelMsg.PassMsg;
import MsgPass.ModelMsg.ResignMsg;
import MsgPass.ModelMsg.RestartBtnPressedMsg;
import MsgPass.ModelMsg.TilePressedMsg;
import Server.ServerConn;
import Shared.TilePosition;

public class MultiPlayerGame extends Game {

    MultiPlayerGame(GameOptions options) {
        super(options, GameMode.MULTIPLAYER);
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
    void handleResign(ResignMsg msg) {

        if (!msg.ignoreNet) {
            if (nextturn == ServerConn.selfColor) {
                var newmsg = new ResignMsg();
                newmsg.ignoreNet = true;
                ServerConn.sendModelMessage(newmsg);
            } else {
                return;
            }
        }
        super.handleResign(msg);

    }

    @Override
    void handleRestartBtnPressed(RestartBtnPressedMsg msg) {

        if (!msg.ignoreNet) {
            var newmsg = new RestartBtnPressedMsg();
            newmsg.ignoreNet = true;
            ServerConn.sendModelMessage(newmsg);
        }
        super.handleRestartBtnPressed(msg);
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
