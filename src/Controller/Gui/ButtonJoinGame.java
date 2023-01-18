package Controller.Gui;
//Filen er skrevet af Katinka
public class ButtonJoinGame extends Button {

    public ButtonJoinGame() {
        super(Buttons.JoinGame, 1);
        setOnMousePressed(e -> {
            setImage(imgPressed);
        });
        setOnMouseReleased(e -> {
            Gui.makeJoinMenu();
            setImage(img);
        });    
    }
    
}
