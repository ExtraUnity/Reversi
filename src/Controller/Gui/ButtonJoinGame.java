package Controller.Gui;

public class ButtonJoinGame extends Button {

    public ButtonJoinGame() {
        super(Buttons.JoinGame, 1);
        setOnMousePressed(e -> {
            setImage(imgPressed);
        });
        setOnMouseReleased(e -> {
            Gui.makeJoinMenu();
            setImage(img);
        });    }
    
}
