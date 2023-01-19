package Shared;

//Filen er skrevet af Thor
public enum TileColor {
    WHITE,
    BLACK,
    EMPTY;

    /**
     * Returnerer den omvendte farve af sig selv.
     */
    public TileColor switchColor() {
        switch (this) {
            case WHITE:
                return BLACK;
            case BLACK:
                return WHITE;
            case EMPTY:
                throw new RuntimeException("En farve kan ikke switch hvis den er empty");
        }

        throw new RuntimeException("Litteraly impossible");
    }

    /*
     * Det her kan gøres smartere. Men har ikke lyst til at pille ved det nu, siden
     * det virker.
     */
    public boolean otherColor(TileColor other) {
        if (this == TileColor.WHITE) {
            return other == TileColor.BLACK;
        } else {
            return other == TileColor.WHITE;
        }

    }

}
