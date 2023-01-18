package Shared;
//Filen er skrevet af Thor
public class ButtonPosition {
    public final double x;
    public final double y;

    public ButtonPosition(double x, double y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "x: " + x + " y: " + y;
    }
}
