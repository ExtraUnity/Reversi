package Controller.Gui;
//Filen er skrevet af Katinka
public class ButtonHostGame extends Button {

    public ButtonHostGame() {
        super(Buttons.HostGame, 1);
        setOnMousePressed(e -> {
            setImage(imgPressed);
        });
        setOnMouseReleased(e -> {
            Gui.makeHostMenu();
            setImage(img);
        });
    }
    
}
