package Model;
//Filen er skrevet af Christian

import Shared.TilePosition;

public class LegalMove implements Comparable<LegalMove> {
    public final TilePosition position;
    public int evaluation; // field used for AI board evaluation

    LegalMove(TilePosition position) {
        this.position = position;
        evaluation = 0;
    }

    LegalMove(TilePosition position, int flipped, int evaluation) {
        this.position = position;
        this.evaluation = evaluation;
    }

    public void setEvaluation(int evaluation) {
        this.evaluation = evaluation;
    }

    @Override
    public int compareTo(LegalMove other) {
        return this.evaluation - other.evaluation;
    }

    @Override
    public String toString() {
        return position.toString() + "  EVAL: " + evaluation;
    }
}
