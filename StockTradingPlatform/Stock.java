// ============================================================
//  Stock.java  –  Represents a single stock in the market.
//  Encapsulation: all fields are private; accessed via getters.
// ============================================================

public class Stock {

    // ── Private fields (Encapsulation) ───────────────────────
    private String symbol;      // e.g. "TCS"
    private String companyName; // e.g. "Tata Consultancy Services"
    private double price;       // Current market price

    // ── Constructor ──────────────────────────────────────────
    public Stock(String symbol, String companyName, double price) {
        this.symbol      = symbol;
        this.companyName = companyName;
        this.price       = price;
    }

    // ── Getters ──────────────────────────────────────────────
    public String getSymbol()      { return symbol; }
    public String getCompanyName() { return companyName; }
    public double getPrice()       { return price; }

    // ── Display ──────────────────────────────────────────────
    public void display() {
        System.out.printf("  %-8s %-28s Rs. %.2f%n",
                symbol, companyName, price);
    }
}
