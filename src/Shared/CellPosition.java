package Shared;

public class CellPosition {
    public final int x;
    public final int y;

    public CellPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "x: " + x + " y: " + y;
    }
}
