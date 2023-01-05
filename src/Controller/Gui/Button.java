package Controller.Gui;

import java.io.InputStream;
import Model.Model;
import MsgPass.ModelMsg.RestartBtnPressedMsg;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Button extends ImageView {
    private Image img;
    private Image imgPressed;

    public Button(Buttons button){
        InputStream src = getClass().getResourceAsStream("/Assets/"+ button +"Button.png");
        this.img = new Image(src, 0, Gui.fitTileSize(), true, false);
        
        InputStream src2 = getClass().getResourceAsStream("/Assets/"+ button +"ButtonPressed.png");
        this.imgPressed = new Image(src2, 0, Gui.fitTileSize(), true, false);

        setImage(img);
        setOnMousePressed(e -> {
            setImage(imgPressed);
            Model.sendModelMsg(new RestartBtnPressedMsg());
        });
        setOnMouseReleased(e -> {
            setImage(img);
        });
    }

}


