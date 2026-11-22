// ============================================================
//  Stock.java  –  UPDATED for Phase 3
//  Added: sector, dayHigh, dayLow, changePercent, volume.
//  Encapsulation: all fields private with getters only.
// ============================================================

public class Stock {

    // ── Private fields (Encapsulation) ───────────────────────
    private String symbol;
    private String companyName;
    private double price;

    // ── Phase 3 additions ─────────────────────────────────────
    private String sector;
    private double dayHigh;
    private double dayLow;
    private double changePercent;   // e.g. +2.35 or -1.10
    private long   volume;          // shares traded today

    // ── Constructor (Phase 3) ─────────────────────────────────
    public Stock(String symbol, String companyName, double price,
                 String sector, double dayHigh, double dayLow,
                 double changePercent, long volume) {
        this.symbol        = symbol;
        this.companyName   = companyName;
        this.price         = price;
        this.sector        = sector;
        this.dayHigh       = dayHigh;
        this.dayLow        = dayLow;
        this.changePercent = changePercent;
        this.volume        = volume;
    }

    // ── Getters ───────────────────────────────────────────────
    public String getSymbol()        { return symbol; }
    public String getCompanyName()   { return companyName; }
    public double getPrice()         { return price; }
    public String getSector()        { return sector; }
    public double getDayHigh()       { return dayHigh; }
    public double getDayLow()        { return dayLow; }
    public double getChangePercent() { return changePercent; }
    public long   getVolume()        { return volume; }

    // ── Short one-line display (used in market table) ─────────
    public void display() {
        String arrow = changePercent >= 0 ? "▲" : "▼";
        System.out.printf("  %-7s %-28s Rs.%8.2f   %s %.2f%%%n",
                symbol, companyName, price, arrow, Math.abs(changePercent));
    }

    // ── Detailed single-stock view ────────────────────────────
    public void displayDetails() {
        String arrow = changePercent >= 0 ? "▲  +" : "▼  -";
        System.out.println("\n  ╔══════════════════════════════════════════╗");
        System.out.printf( "  ║  %-42s║%n", symbol + " – " + companyName);
        System.out.println("  ╠══════════════════════════════════════════╣");
        System.out.printf( "  ║  Current Price  : Rs. %-18.2f  ║%n", price);
        System.out.printf( "  ║  Change         : %s%.2f%%%-15s  ║%n",
                arrow, Math.abs(changePercent), "");
        System.out.printf( "  ║  Day High       : Rs. %-18.2f  ║%n", dayHigh);
        System.out.printf( "  ║  Day Low        : Rs. %-18.2f  ║%n", dayLow);
        System.out.printf( "  ║  Volume         : %-21d  ║%n", volume);
        System.out.printf( "  ║  Sector         : %-21s  ║%n", sector);
        System.out.println("  ╚══════════════════════════════════════════╝");
    }
}
