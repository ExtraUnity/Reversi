package Model;

import Shared.TilePosition;

public class LegalMove implements Comparable<LegalMove> {
    public final TilePosition position;
    public final int flipped;
    public int evaluation;

    LegalMove(TilePosition position, int flipped) {
        this.position = position;
        this.flipped = flipped;
        evaluation = 0;
    }

    LegalMove(TilePosition position, int flipped, int evaluation) {
        this.position = position;
        this.flipped = flipped;
        this.evaluation = evaluation;
    }

    public void setEvaluation(int evaluation) {
        this.evaluation = evaluation;
    }

    @Override
    public int compareTo(LegalMove other) {
        return this.evaluation - other.evaluation;
        // return this.flipped - other.flipped;
    }
}
