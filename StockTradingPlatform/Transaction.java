// ============================================================
//  Transaction.java  –  Records one BUY or SELL event.
//  Encapsulation: all fields private; accessed via getters.
// ============================================================

public class Transaction {

    // ── Private fields ───────────────────────────────────────
    private String type;        // "BUY" or "SELL"
    private String stockSymbol; // Which stock
    private int    quantity;    // How many shares
    private double amount;      // Total money involved

    // ── Constructor ──────────────────────────────────────────
    public Transaction(String type, String stockSymbol,
                        int quantity, double amount) {
        this.type        = type;
        this.stockSymbol = stockSymbol;
        this.quantity    = quantity;
        this.amount      = amount;
    }

    // ── Getters ──────────────────────────────────────────────
    public String getType()        { return type; }
    public String getStockSymbol() { return stockSymbol; }
    public int    getQuantity()    { return quantity; }
    public double getAmount()      { return amount; }

    // ── Display ──────────────────────────────────────────────
    public void display() {
        System.out.printf("  %-6s %-8s Qty: %-5d Amount: Rs. %.2f%n",
                type, stockSymbol, quantity, amount);
    }
}
