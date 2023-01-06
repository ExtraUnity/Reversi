package Model;

public class GameOptions {
    public final int gametime;
    public final boolean countPoints;

    public GameOptions(int gametime, boolean countPoints) {
        this.gametime = gametime;
        this.countPoints = countPoints;
    }

    @Override
    public String toString() {
        return "time: " + gametime + " points: " + countPoints;
    }
}