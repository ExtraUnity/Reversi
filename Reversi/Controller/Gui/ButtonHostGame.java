package Controller.Gui;
//Filen er skrevet af Katinka
//Host et spil knap, sender en besked til Gui at den skal opdatere og bygge Host-menuen
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
