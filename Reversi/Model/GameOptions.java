package Model;

import Controller.Gui.PlayerCharacter;
import Shared.TileColor;

//Filen er skrevet af Thor

public class GameOptions {
    public final int gametime;
    public final boolean countPoints;
    public final TileColor startPlayer;

    public final PlayerCharacter playerWhite;
    public final PlayerCharacter playerBlack;

    public GameOptions(int gametime, boolean countPoints, TileColor startPlayer, PlayerCharacter playerWhite,
            PlayerCharacter playerBlack) {
        this.gametime = gametime;
        this.countPoints = countPoints;
        this.startPlayer = startPlayer;
        this.playerWhite = playerWhite;
        this.playerBlack = playerBlack;
    }

    @Override
    public String toString() {
        return "time: " + gametime + " points: " + countPoints;
    }
}