package Controller.Gui;

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