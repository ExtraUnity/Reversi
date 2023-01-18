package Controller.Gui;

import Model.Model;
import MsgPass.ModelMsg.PassMsg;
import javafx.scene.image.Image;

public class ButtonPass extends Button {
    private Image buttonGrey;
    private boolean available = false;

    public ButtonPass() {
        super(Buttons.Pass, 1);
        this.buttonGrey = new Image("/Assets/ButtonPassGrey.png", 0, Gui.fitTileSize(), true, false);
        updatePressed();
        setImage(buttonGrey);
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public boolean getAvailable() {
        return this.available;
    }

    public void setImage(String src) {
        this.img = new Image(src, 0, Gui.fitTileSize(), true, false);
        setImage(img);
    }

    public void updatePressed() {
        if (this.available) {
            setOnMousePressed(e -> {
                setImage(imgPressed);
            });
            setOnMouseReleased(e -> {
                setImage(img);
                Model.sendGameMsg(new PassMsg());
            });
        } else { // Make the button non-interactive
            setOnMousePressed(e -> {
            });
            setOnMouseReleased(e -> {
            });
        }
    }
}