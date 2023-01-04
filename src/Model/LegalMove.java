package Model;

import Shared.TilePosition;

public class LegalMove {
    public final TilePosition pos;
    public final int flipped;

    LegalMove(TilePosition pos, int flipped) {
        this.pos = pos;
        this.flipped = flipped;
    }
}
