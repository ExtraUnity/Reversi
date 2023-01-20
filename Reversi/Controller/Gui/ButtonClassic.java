package Controller.Gui;
//Filen er skrevet af Katinka
//Main Menu Classic-gamemode Button, sender en GameMode besked
//med de korrekte gameoptions og playerCharacters når knappen slippes, så spillet kan starte.
import Model.GameOptions;
import Model.Model;
import Model.Game.GameMode;
import Shared.TileColor;

public class ButtonClassic extends Button {

    public ButtonClassic() {
        super(Buttons.Classic, 4);

        setOnMousePressed(e -> {
            setImage(imgPressed);
        });
        setOnMouseReleased(e -> {
            Model.startGame(GameMode.CLASSIC,
                    new GameOptions(-1, true, TileColor.WHITE, PlayerCharacter.White, PlayerCharacter.Black));
            setImage(img);
        });
    }

}