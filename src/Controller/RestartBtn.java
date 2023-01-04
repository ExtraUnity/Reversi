package Controller;

import java.io.InputStream;

import org.xml.sax.InputSource;

import Model.Model;
import MsgPass.ModelMsg.RestartBtnPressedMsg;
import MsgPass.ModelMsg.TilePressedMsg;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class RestartBtn extends ImageView{

    InputStream btnAssetSrc = getClass().getResourceAsStream("/Assets/btnAssetSrc.png");
    Image btnAsset = new Image(btnAssetSrc);
    
    public RestartBtn() {
        setImage(btnAsset);
        setOnMouseClicked(e -> {
            Model.sendModelMsg(new RestartBtnPressedMsg());
        });
    }
}
