// ============================================================
//  Transaction.java  –  UPDATED Phase 7
//  Added setters for transactionId and timestamp so
//  DataManager can restore saved values when loading from file.
// ============================================================

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Transaction {

    // ── Static counter for unique IDs ─────────────────────────
    private static int idCounter = 1000;

    // ── Private fields ───────────────────────────────────────
    private int           transactionId;
    private String        type;
    private String        stockSymbol;
    private String        stockName;
    private int           quantity;
    private double        pricePerShare;
    private double        amount;
    private double        profitLoss;
    private LocalDateTime timestamp;

    private static final DateTimeFormatter FMT =
            DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    // ── Constructor ──────────────────────────────────────────
    public Transaction(String type, String stockSymbol, String stockName,
                       int quantity, double pricePerShare,
                       double amount, double profitLoss) {
        this.transactionId = ++idCounter;
        this.type          = type;
        this.stockSymbol   = stockSymbol;
        this.stockName     = stockName;
        this.quantity      = quantity;
        this.pricePerShare = pricePerShare;
        this.amount        = amount;
        this.profitLoss    = profitLoss;
        this.timestamp     = LocalDateTime.now();
    }

    // ── Getters ──────────────────────────────────────────────
    public int           getTransactionId() { return transactionId; }
    public String        getType()          { return type; }
    public String        getStockSymbol()   { return stockSymbol; }
    public String        getStockName()     { return stockName; }
    public int           getQuantity()      { return quantity; }
    public double        getPricePerShare() { return pricePerShare; }
    public double        getAmount()        { return amount; }
    public double        getProfitLoss()    { return profitLoss; }
    public LocalDateTime getTimestamp()     { return timestamp; }

    // ── Phase 7: Setters for restoring saved data ─────────────
    public void setTransactionId(int id) {
        this.transactionId = id;
        // Keep idCounter ahead of loaded IDs
        if (id > idCounter) idCounter = id;
    }

    public void setTimestamp(LocalDateTime ts) {
        this.timestamp = ts;
    }

    // ── Static: get current counter (for ID continuity) ───────
    public static int getIdCounter() { return idCounter; }

    // ── Short one-line display ────────────────────────────────
    public void display() {
        String pl = type.equals("SELL")
                ? String.format("  P/L: Rs.%.2f %s",
                    Math.abs(profitLoss), profitLoss >= 0 ? "(▲)" : "(▼)")
                : "";
        System.out.printf("  #%-5d [%s] %-5s %-7s (%-20s) Qty:%-4d @ Rs.%-9.2f Total: Rs.%-10.2f%s%n",
                transactionId, timestamp.format(FMT), type, stockSymbol,
                stockName, quantity, pricePerShare, amount, pl);
    }

    // ── Detailed card display ─────────────────────────────────
    public void displayDetailed() {
        String arrow = profitLoss >= 0 ? "▲ PROFIT" : "▼ LOSS";
        System.out.println("  ┌─────────────────────────────────────────────┐");
        System.out.printf( "  │  TXN #%-5d                     %s  │%n",
                transactionId, type.equals("BUY") ? "🟢 BUY " : "🔴 SELL");
        System.out.println("  ├─────────────────────────────────────────────┤");
        System.out.printf( "  │  Date     : %-32s│%n", timestamp.format(FMT));
        System.out.printf( "  │  Stock    : %-7s %-24s│%n", stockSymbol, stockName);
        System.out.printf( "  │  Quantity : %-32d│%n", quantity);
        System.out.printf( "  │  Price    : Rs. %-28.2f│%n", pricePerShare);
        System.out.printf( "  │  Total    : Rs. %-28.2f│%n", amount);
        if (type.equals("SELL")) {
            System.out.printf("  │  P / L    : Rs. %-9.2f  %-18s│%n",
                    Math.abs(profitLoss), arrow);
        }
        System.out.println("  └─────────────────────────────────────────────┘");
    }
}
