package Controller.Gui;

import java.io.InputStream;

import Model.Model;
import MsgPass.ModelMsg.RestartBtnPressedMsg;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class RestartBtn extends ImageView {

    public RestartBtn() {
        InputStream btnAssetSrc = getClass().getResourceAsStream("/Assets/btnAssetSrc.png");
        double size = Gui.fitTileSize() * 1;
        Image btnAsset = new Image(btnAssetSrc, size, 0, true, false);
        setImage(btnAsset);
        setOnMouseClicked(e -> {
            Model.sendGameMsg(new RestartBtnPressedMsg());
        });
    }
}
