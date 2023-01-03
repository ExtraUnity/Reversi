package Shared;

public enum CellColor {
    WHITE,
    BLACK;

    public CellColor switchColor() {
        switch (this) {
            case WHITE:
                return BLACK;
            case BLACK:
                return WHITE;
        }
        
        throw new RuntimeException("Litteraly impossible");
    }
}
