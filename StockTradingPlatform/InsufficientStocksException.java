// ============================================================
//  InsufficientStocksException.java  –  NEW Phase 4
//  Thrown when user tries to sell more shares than they own.
// ============================================================

public class InsufficientStocksException extends Exception {

    private String symbol;
    private int    requested;
    private int    available;

    public InsufficientStocksException(String symbol, int requested, int available) {
        super(String.format(
            "Cannot sell %d share(s) of %s — you only own %d share(s).",
            requested, symbol, available));
        this.symbol    = symbol;
        this.requested = requested;
        this.available = available;
    }

    public String getSymbol()    { return symbol; }
    public int    getRequested() { return requested; }
    public int    getAvailable() { return available; }
}
