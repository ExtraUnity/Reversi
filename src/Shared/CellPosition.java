package Shared;

public class CellPosition {
    public final int x;
    public final int y;

    public CellPosition(int x, int y) {
        if (x < 0 || x > 8 || y < 0 || y > 8) {
            throw new RuntimeException("Invalid cell position" + x + " " + y + ". Min 0 max 8");
        }
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "x: " + x + " y: " + y;
    }
}