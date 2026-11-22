// ============================================================
//  StockMarket.java  –  NEW in Phase 3
//  Manages the full stock collection.
//  Uses HashMap<String, Stock> for O(1) symbol search.
//  Uses ArrayList<Stock>       for ordered table display.
// ============================================================

import java.util.ArrayList;
import java.util.HashMap;

public class StockMarket {

    // ── Data structures ───────────────────────────────────────
    private ArrayList<Stock>        stockList;   // ordered display
    private HashMap<String, Stock>  stockMap;    // fast symbol lookup

    // ── Constructor: seeds all 6 stocks ──────────────────────
    public StockMarket() {
        stockList = new ArrayList<>();
        stockMap  = new HashMap<>();
        seedStocks();
    }

    // ── Seed the market with 6 company stocks ─────────────────
    private void seedStocks() {
        //         Symbol  Company                    Price    Sector          High      Low     %Chg    Volume
        addStock(new Stock("TCS",   "Tata Consultancy Services", 3500.00, "IT Services",  3542.00, 3468.00, +2.15, 1250000L));
        addStock(new Stock("INFY",  "Infosys Ltd.",              1800.00, "IT Services",  1832.50, 1785.00, +1.75, 2100000L));
        addStock(new Stock("RELI",  "Reliance Industries",       1400.00, "Conglomerate", 1415.00, 1382.00, -0.85, 3400000L));
        addStock(new Stock("HDFC",  "HDFC Bank Ltd.",            1700.00, "Banking",      1718.00, 1689.00, +0.60, 1800000L));
        addStock(new Stock("WIPRO", "Wipro Ltd.",                 600.00, "IT Services",   608.50,  594.00, -1.20,  980000L));
        addStock(new Stock("ICICI", "ICICI Bank Ltd.",           1050.00, "Banking",      1065.00, 1038.00, +1.42, 2250000L));
    }

    // ── Internal add helper ───────────────────────────────────
    private void addStock(Stock s) {
        stockList.add(s);
        stockMap.put(s.getSymbol().toUpperCase(), s);
    }

    // ── 1. Display all stocks in a formatted table ────────────
    public void displayAllStocks() {
        System.out.println("\n  ╔══════════════════════════════════════════════════════════╗");
        System.out.println("  ║               📈  LIVE STOCK MARKET DATA                 ║");
        System.out.println("  ╠══════════╦══════════════════════════════╦═══════════╦═════╣");
        System.out.printf( "  ║ %-8s ║ %-28s ║ %-9s ║ %-3s ║%n",
                "SYMBOL", "COMPANY", "PRICE(Rs.)", "CHG");
        System.out.println("  ╠══════════╬══════════════════════════════╬═══════════╬═════╣");

        for (Stock s : stockList) {
            String arrow = s.getChangePercent() >= 0 ? "▲" : "▼";
            String chg   = String.format("%s%.2f%%", arrow, Math.abs(s.getChangePercent()));
            System.out.printf("  ║ %-8s ║ %-28s ║ %9.2f ║ %-3s ║%n",
                    s.getSymbol(), s.getCompanyName(), s.getPrice(), chg);
        }

        System.out.println("  ╚══════════╩══════════════════════════════╩═══════════╩═════╝");
        System.out.printf( "  Total Stocks Listed: %d%n", stockList.size());
    }

    // ── 2. Search stock by symbol (HashMap O(1) lookup) ───────
    public Stock searchBySymbol(String symbol) {
        return stockMap.get(symbol.toUpperCase());
    }

    // ── 3. Display detailed view of one stock ─────────────────
    public void displayStockDetails(String symbol) {
        Stock s = searchBySymbol(symbol);
        if (s == null) {
            System.out.println("\n  [ERROR] Stock '" + symbol + "' not found in the market.");
            System.out.println("  Tip: Use option 1 to see all valid stock symbols.");
        } else {
            s.displayDetails();
        }
    }

    // ── 4. Display search result ──────────────────────────────
    public void displaySearchResult(String symbol) {
        Stock s = searchBySymbol(symbol);
        if (s == null) {
            System.out.println("\n  [NOT FOUND] No stock with symbol '" + symbol.toUpperCase() + "'.");
        } else {
            System.out.println("\n  ✔ Stock Found:");
            System.out.println("  " + "─".repeat(52));
            System.out.printf("  %-8s %-28s Rs. %.2f%n",
                    s.getSymbol(), s.getCompanyName(), s.getPrice());
            System.out.println("  " + "─".repeat(52));
            System.out.println("  Use option 3 to view full details.");
        }
    }

    // ── Getter: list for trading (buy/sell) ───────────────────
    public ArrayList<Stock> getStockList() { return stockList; }

    // ── Getter: map (for AuthSystem / User injection) ─────────
    public HashMap<String, Stock> getStockMap() { return stockMap; }
}
