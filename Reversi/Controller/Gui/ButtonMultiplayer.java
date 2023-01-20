package Controller.Gui;
//Filen er skrevet af Katinka
//Multiplayer knappen på hovendmenuen, beder Gui om at lave multiplayer menu.
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
