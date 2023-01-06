package Model;

import Shared.TileColor;

public class GameOptions {
    public final int gametime;
    public final boolean countPoints;
    public final TileColor startPlayer;

    public GameOptions(int gametime, boolean countPoints, TileColor startPlayer) {
        this.gametime = gametime;
        this.countPoints = countPoints;
        this.startPlayer = startPlayer;
    }

    @Override
    public String toString() {
        return "time: " + gametime + " points: " + countPoints;
    }
}