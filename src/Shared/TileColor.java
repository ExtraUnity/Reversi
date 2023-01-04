package Shared;

public enum TileColor {
    WHITE,
    BLACK;

    public TileColor switchColor() {
        switch (this) {
            case WHITE:
                return BLACK;
            case BLACK:
                return WHITE;
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
