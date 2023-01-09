package Model;

import Shared.TilePosition;

public class LegalMove implements Comparable<LegalMove> {
    public final TilePosition position;
    public final int flipped;

    LegalMove(TilePosition position, int flipped) {
        this.position = position;
        this.flipped = flipped;
    }

    @Override
    public int compareTo(LegalMove other) {
        return this.flipped - other.flipped;
    }
}
