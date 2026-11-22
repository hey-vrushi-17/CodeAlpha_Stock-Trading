// ============================================================
//  Portfolio.java  –  Tracks a user's stock holdings.
//  Uses a HashMap: symbol → quantity held.
//  Uses a second HashMap: symbol → total amount invested.
// ============================================================

import java.util.HashMap;

public class Portfolio {

    // ── Private fields (Encapsulation) ───────────────────────
    private HashMap<String, Integer> holdings;    // symbol → qty
    private HashMap<String, Double>  invested;    // symbol → Rs. invested

    // ── Constructor ──────────────────────────────────────────
    public Portfolio() {
        holdings = new HashMap<>();
        invested  = new HashMap<>();
    }

    // ── Add shares when buying ────────────────────────────────
    public void addHolding(String symbol, int qty, double cost) {
        holdings.put(symbol, holdings.getOrDefault(symbol, 0) + qty);
        invested.put(symbol,  invested.getOrDefault(symbol, 0.0) + cost);
    }

    // ── Remove shares when selling ────────────────────────────
    public void removeHolding(String symbol, int qty, double costPortion) {
        int current = holdings.getOrDefault(symbol, 0);
        holdings.put(symbol, current - qty);
        invested.put(symbol,  invested.getOrDefault(symbol, 0.0) - costPortion);
    }

    // ── Getters ──────────────────────────────────────────────
    public int    getQuantity(String symbol) {
        return holdings.getOrDefault(symbol, 0);
    }

    public double getInvested(String symbol) {
        return invested.getOrDefault(symbol, 0.0);
    }

    public HashMap<String, Integer> getAllHoldings() {
        return holdings;
    }

    public HashMap<String, Double> getAllInvested() {
        return invested;
    }
}
