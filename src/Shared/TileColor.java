package Shared;
//Filen er skrevet af Thor
public enum TileColor {
    WHITE,
    BLACK,
    EMPTY;

    public TileColor switchColor() {
        switch (this) {
            case WHITE:
                return BLACK;
            case BLACK:
                return WHITE;
            case default:
                break;
        }

        throw new RuntimeException("Litteraly impossible");
    }

    public boolean otherColor(TileColor other) {
        if (this == TileColor.WHITE) {
            return other == TileColor.BLACK;
        } else {
            return other == TileColor.WHITE;
        }

    }

}
