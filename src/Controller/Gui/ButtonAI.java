package Controller.Gui;

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
            Model.startGame(GameMode.AI_GAME, new GameOptions(-1, false, TileColor.WHITE));
            setImage(img);
        });
    }
    
}
