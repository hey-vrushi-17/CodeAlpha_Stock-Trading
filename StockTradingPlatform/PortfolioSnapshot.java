// ============================================================
//  PortfolioSnapshot.java  –  NEW Phase 5
//  Records the portfolio's total value at a point in time
//  to track performance over time.
// ============================================================

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PortfolioSnapshot {

    // ── Private fields ───────────────────────────────────────
    private LocalDateTime timestamp;
    private double        cashBalance;
    private double        stockValue;      // current market value of holdings
    private double        totalInvestment; // total amount paid for holdings
    private double        netWorth;        // cash + stock value
    private double        unrealizedPL;    // stock value - investment
    private String        trigger;         // what caused this snapshot: "BUY TCS x5", etc.

    private static final DateTimeFormatter FMT =
            DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    // ── Constructor ──────────────────────────────────────────
    public PortfolioSnapshot(double cashBalance, double stockValue,
                              double totalInvestment, String trigger) {
        this.timestamp       = LocalDateTime.now();
        this.cashBalance     = cashBalance;
        this.stockValue      = stockValue;
        this.totalInvestment = totalInvestment;
        this.netWorth        = cashBalance + stockValue;
        this.unrealizedPL    = stockValue - totalInvestment;
        this.trigger         = trigger;
    }

    // ── Getters ──────────────────────────────────────────────
    public LocalDateTime getTimestamp()       { return timestamp; }
    public double        getCashBalance()     { return cashBalance; }
    public double        getStockValue()      { return stockValue; }
    public double        getTotalInvestment() { return totalInvestment; }
    public double        getNetWorth()        { return netWorth; }
    public double        getUnrealizedPL()    { return unrealizedPL; }
    public String        getTrigger()         { return trigger; }

    // ── Display ──────────────────────────────────────────────
    public void display(int index) {
        String arrow = unrealizedPL >= 0 ? "▲" : "▼";
        System.out.printf("  %3d  [%s]  Net: Rs.%-10.2f  Cash: Rs.%-10.2f  Stocks: Rs.%-10.2f  P/L: %s Rs.%-8.2f  %s%n",
                index, timestamp.format(FMT), netWorth, cashBalance,
                stockValue, arrow, Math.abs(unrealizedPL), trigger);
    }
}
