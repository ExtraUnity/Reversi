package Model;

import Shared.TilePosition;

public class LegalMove {
    public final TilePosition position;
    public final int flipped;

    LegalMove(TilePosition position, int flipped) {
        this.position = position;
        this.flipped = flipped;
    }
}
