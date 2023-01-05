package Controller.Gui;
import Model.Game;
import java.io.InputStream;
import Model.Model;
import MsgPass.ControllerMsg.WinnerMsg;
import MsgPass.ModelMsg.RestartBtnPressedMsg;
import Shared.TileColor;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Button extends ImageView {
    private Image img;
    private Image imgPressed;
    private Buttons type;

    public Button(Buttons button) {
        InputStream src = getClass().getResourceAsStream("/Assets/Button" + button.name().toLowerCase() + ".png");
        this.img = new Image(src, 0, Gui.fitTileSize(), true, false);
        this.type = button;
        InputStream src2 = getClass()
                .getResourceAsStream("/Assets/Button" + button.name().toLowerCase() + "Pressed.png");
        this.imgPressed = new Image(src2, 0, Gui.fitTileSize(), true, false);
        setImage(img);

        switch (type) {
            case PASS:
                break;
            case RESTART:
                setOnMousePressed(e -> {
                    setImage(imgPressed);
                    Model.sendGameMsg(new RestartBtnPressedMsg());
                });
                break;
            case RESIGN:
            setOnMousePressed(e -> {
                setImage(imgPressed);
                if(Game.getNextTurn() == TileColor.WHITE){
                    Model.sendControllerMsg(new WinnerMsg(TileColor.BLACK));
                }else{
                    Model.sendControllerMsg(new WinnerMsg(TileColor.WHITE));
                 }
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
