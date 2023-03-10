package Controller.Gui;
//Filen er skrevet af Katinka
//Main Menu AI-gamemode Button, sender en GameMode besked med de korrekte gameoptions og playerCharacters når knappen slippes.

import Model.GameOptions;
import Model.Model;
import Model.Game.GameMode;
import Shared.TileColor;

public class ButtonAI extends Button {

    public ButtonAI() {
        super(Buttons.AI, 4);

        setOnMousePressed(e -> {
            setImage(imgPressed);
        });
        setOnMouseReleased(e -> {
            Model.startGame(GameMode.AI_GAME,
                    new GameOptions(-1, true, TileColor.WHITE, PlayerCharacter.White, PlayerCharacter.Computer));
            setImage(img);
        });
    }

}
