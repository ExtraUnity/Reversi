package Controller.Gui;

import java.io.InputStream;

import Model.Model;
import MsgPass.ModelMsg.RestartBtnPressedMsg;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class RestartBtn extends ImageView {
    private Image img;
    private Image imgPressed;

    public RestartBtn() {

        InputStream src = getClass().getResourceAsStream("/Assets/restartButton.png");
        this.img = new Image(src, 0, Gui.fitTileSize(), true, false);

        InputStream src2 = getClass().getResourceAsStream("/Assets/restartButtonPressed.png");
        this.imgPressed = new Image(src2, 0, Gui.fitTileSize(), true, false);

        setImage(img);
        setOnMousePressed(e -> {
            setImage(imgPressed);
            Model.sendGameMsg(new RestartBtnPressedMsg());
        });
        setOnMouseReleased(e -> {
            setImage(img);
        });
    }
}
