package Controller.Gui;

import java.io.InputStream;
import Model.Model;
import MsgPass.ModelMsg.ModelMsg;
import MsgPass.ModelMsg.RestartBtnPressedMsg;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Button extends ImageView {
    private Image img;
    private Image imgPressed;
    private Buttons type;

    public Button(Buttons button) {
        InputStream src = getClass().getResourceAsStream("/Assets/" + button.name().toLowerCase() + "Button.png");
        this.img = new Image(src, 0, Gui.fitTileSize(), true, false);
        this.type = button;
        InputStream src2 = getClass().getResourceAsStream("/Assets/" + button.name().toLowerCase() + "ButtonPressed.png");
        this.imgPressed = new Image(src2, 0, Gui.fitTileSize(), true, false);
        setImage(img);

        switch(type) {
            case PASS:
                break;
            case RESTART:
                setOnMousePressed(e -> {
                    setImage(imgPressed);
                    Model.sendGameMsg(new RestartBtnPressedMsg());
                });
                break;
            default:
            
                break;
            
        }
        
        setOnMouseReleased(e -> {
            setImage(img);
        });
    }

}
