package Controller.Gui;
//Filen er skrevet af Katinka
public class ButtonMultiplayer extends Button {


    public ButtonMultiplayer() {
        super(Buttons.Multiplayer, 4);

        setOnMousePressed(e -> {
            setImage(imgPressed);
        });
        setOnMouseReleased(e -> {
            setImage(img);
            Gui.makeMultiplayerMenu();
        });
    }

}
