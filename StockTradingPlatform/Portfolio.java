// ============================================================
//  Portfolio.java  –  UPDATED Phase 5
//  Added: snapshot history to track performance over time,
//         detailed holdings display with avg price & unrealized P/L,
//         totalInvestment, totalStockValue calculations.
// ============================================================

import java.util.ArrayList;
import java.util.HashMap;

public class Portfolio {

    // ── Holdings data ────────────────────────────────────────
    private HashMap<String, Integer> holdings;
    private HashMap<String, Double>  invested;

    // ── Phase 5: Snapshot history ─────────────────────────────
    private ArrayList<PortfolioSnapshot> snapshots;

    // ── Constructor ──────────────────────────────────────────
    public Portfolio() {
        holdings  = new HashMap<>();
        invested  = new HashMap<>();
        snapshots = new ArrayList<>();
    }

    // ── Add shares (BUY) ─────────────────────────────────────
    public void addHolding(String symbol, int qty, double cost) {
        holdings.put(symbol, holdings.getOrDefault(symbol, 0) + qty);
        invested.put(symbol, invested.getOrDefault(symbol, 0.0) + cost);
    }

    // ── Remove shares (SELL) ─────────────────────────────────
    public void removeHolding(String symbol, int qty, double costPortion) {
        int current = holdings.getOrDefault(symbol, 0);
        holdings.put(symbol, current - qty);
        invested.put(symbol, invested.getOrDefault(symbol, 0.0) - costPortion);
    }

    // ── Getters ──────────────────────────────────────────────
    public int getQuantity(String symbol) {
        return holdings.getOrDefault(symbol, 0);
    }

    public double getInvested(String symbol) {
        return invested.getOrDefault(symbol, 0.0);
    }

    public double getAverageBuyPrice(String symbol) {
        int qty = getQuantity(symbol);
        return (qty == 0) ? 0 : invested.getOrDefault(symbol, 0.0) / qty;
    }

    public HashMap<String, Integer> getAllHoldings() { return holdings; }
    public HashMap<String, Double>  getAllInvested() { return invested; }

    public boolean hasStock(String symbol) {
        return holdings.getOrDefault(symbol, 0) > 0;
    }

    public int countDistinctHoldings() {
        int count = 0;
        for (int qty : holdings.values()) if (qty > 0) count++;
        return count;
    }

    // ── Phase 5: Calculate total investment across all stocks ─
    public double getTotalInvestment(ArrayList<Stock> marketStocks) {
        double total = 0;
        for (Stock s : marketStocks) {
            int qty = getQuantity(s.getSymbol());
            if (qty > 0) total += getInvested(s.getSymbol());
        }
        return total;
    }

    // ── Phase 5: Calculate total current stock value ──────────
    public double getTotalStockValue(ArrayList<Stock> marketStocks) {
        double total = 0;
        for (Stock s : marketStocks) {
            int qty = getQuantity(s.getSymbol());
            if (qty > 0) total += qty * s.getPrice();
        }
        return total;
    }

    // ── Phase 5: Take a snapshot of current portfolio state ───
    public void takeSnapshot(double cashBalance,
                              ArrayList<Stock> marketStocks, String trigger) {
        double stockVal = getTotalStockValue(marketStocks);
        double invested = getTotalInvestment(marketStocks);
        snapshots.add(new PortfolioSnapshot(cashBalance, stockVal,
                invested, trigger));
    }

    // ── Phase 5: Display snapshot history (performance) ───────
    public void displayPerformanceHistory() {
        System.out.println("\n  ════════════════════════════════════════════════════════════════════════════════════════");
        System.out.println("                        📊  PORTFOLIO PERFORMANCE HISTORY");
        System.out.println("  ════════════════════════════════════════════════════════════════════════════════════════");
        if (snapshots.isEmpty()) {
            System.out.println("  No snapshots yet. Start trading to build history!");
        } else {
            System.out.printf("  %3s  %-21s  %-14s  %-14s  %-14s  %-14s  %s%n",
                    "#","TIMESTAMP","NET WORTH","CASH","STOCKS","UNREAL. P/L","TRIGGER");
            System.out.println("  " + "─".repeat(110));
            for (int i = snapshots.size() - 1; i >= 0; i--) {
                snapshots.get(i).display(i + 1);
            }
            System.out.println("  " + "─".repeat(110));
            System.out.printf("  Total snapshots: %d%n", snapshots.size());

            // Show net worth change from first to last
            if (snapshots.size() > 1) {
                double first = snapshots.get(0).getNetWorth();
                double last  = snapshots.get(snapshots.size() - 1).getNetWorth();
                double change = last - first;
                String arrow  = change >= 0 ? "▲" : "▼";
                System.out.printf("  Net worth change: Rs. %.2f → Rs. %.2f  (%s Rs. %.2f)%n",
                        first, last, arrow, Math.abs(change));
            }
        }
        System.out.println("  ════════════════════════════════════════════════════════════════════════════════════════");
    }

    // ── Detailed holdings display ─────────────────────────────
    public void displayHoldings(ArrayList<Stock> marketStocks) {
        System.out.println("\n  ─── YOUR CURRENT HOLDINGS ───");
        System.out.printf("  %-8s %-6s %-14s %-12s %-10s%n",
                "SYMBOL","QTY","AVG.BUY PRICE","CURR.PRICE","UNREAL. P/L");
        System.out.println("  " + "─".repeat(58));
        boolean any = false;
        for (Stock s : marketStocks) {
            int qty = getQuantity(s.getSymbol());
            if (qty > 0) {
                any = true;
                double avgBuy = getAverageBuyPrice(s.getSymbol());
                double unreal = (s.getPrice() - avgBuy) * qty;
                String arrow  = unreal >= 0 ? "▲" : "▼";
                System.out.printf("  %-8s %-6d Rs.%-11.2f Rs.%-9.2f %s Rs.%.2f%n",
                        s.getSymbol(), qty, avgBuy, s.getPrice(),
                        arrow, Math.abs(unreal));
            }
        }
        if (!any) System.out.println("  No holdings yet. Buy some stocks first!");
        System.out.println("  " + "─".repeat(58));
    }
}
