package Shared;

//Filen er skrevet af Thor
import java.io.Serializable;

/**
 * Denne klasse er en repræsentation af en position på brættet. Den har en max
 * og min x,y værdi på 0..8
 * Den kaster en runtimeexception hvis den bliver givet en værdi som er unden for
 * det interval.
 */
public class TilePosition implements Serializable {
    public final int x;
    public final int y;

    public TilePosition(int x, int y) {
        if (x < 0 || x > 8 || y < 0 || y > 8) {
            throw new RuntimeException("Invalid tile position" + x + " " + y + ". Min 0 max 8");
        }
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "x: " + x + " y: " + y;
    }
}
