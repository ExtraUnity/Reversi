package Controller.Gui;
//Filen er skrevet af Katinka
//giv op knap i spillet, sender en Game besked om at man har givet op.
import Model.Model;
import MsgPass.ModelMsg.ResignMsg;

public class ButtonResign extends Button {

    public ButtonResign() {
        super(Buttons.Resign,1);
        setOnMousePressed(e -> {
            setImage(imgPressed);
        });
        setOnMouseReleased(e -> {
            Model.sendGameMsg(new ResignMsg());
            setImage(img);
        });
    }

}
