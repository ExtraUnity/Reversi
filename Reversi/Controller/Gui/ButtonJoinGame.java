package Controller.Gui;
//Filen er skrevet af Katinka
//Join et spil knap, sender en besked til Gui at den skal opdatere og bygge Join-menuen
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
