package Controller.Gui;

import Model.GameOptions;
import Model.Model;
import Model.Game.GameMode;
import Shared.TileColor;

public class ButtonMultiplayer extends Button {


    public ButtonMultiplayer() {
        super(Buttons.Multiplayer, 4);

        setOnMousePressed(e -> {
            setImage(imgPressed);
        });
        setOnMouseReleased(e -> {
            Model.startGame(GameMode.MULTIPLAYER, new GameOptions(-1, false, TileColor.WHITE));
            setImage(img);
        });
    }
    
}
