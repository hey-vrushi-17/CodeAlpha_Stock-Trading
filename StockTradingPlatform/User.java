// ============================================================
//  User.java  –  Represents the logged-in trader.
//  implements Trading → must override all 4 interface methods.
//  Encapsulation: private fields + getters/setters.
// ============================================================

import java.util.ArrayList;
import java.util.Scanner;

public class User implements Trading {

    // ── Private fields (Encapsulation) ───────────────────────
    private String    name;
    private double    balance;
    private Portfolio portfolio;
    private ArrayList<Transaction> history;

    // ── Shared market stock list (injected from Main) ─────────
    private ArrayList<Stock> marketStocks;
    private Scanner          scanner;

    // ── Constructor ──────────────────────────────────────────
    public User(String name, ArrayList<Stock> marketStocks, Scanner scanner) {
        this.name         = name;
        this.balance      = 100000.00;   // Starting balance Rs. 1,00,000
        this.portfolio    = new Portfolio();
        this.history      = new ArrayList<>();
        this.marketStocks = marketStocks;
        this.scanner      = scanner;
    }

    // ── Getters ──────────────────────────────────────────────
    public String getName()    { return name; }
    public double getBalance() { return balance; }

    // ── Helper: find stock by symbol ─────────────────────────
    private Stock findStock(String symbol) {
        for (Stock s : marketStocks) {
            if (s.getSymbol().equalsIgnoreCase(symbol)) return s;
        }
        return null;
    }

    // ── Helper: print market table ───────────────────────────
    private void showMarket() {
        System.out.println("\n  SYMBOL   COMPANY                      PRICE");
        System.out.println("  " + "-".repeat(50));
        for (Stock s : marketStocks) s.display();
        System.out.println("  " + "-".repeat(50));
    }

    // ============================================================
    //  1. buyStock()  –  implements Trading
    // ============================================================
    @Override
    public void buyStock() {
        showMarket();
        System.out.print("\n  Enter Stock Symbol to BUY : ");
        String sym = scanner.next().toUpperCase();

        Stock stock = findStock(sym);
        if (stock == null) {
            System.out.println("  [ERROR] Stock not found!");
            return;
        }

        System.out.print("  Enter Quantity             : ");
        int qty = scanner.nextInt();

        if (qty <= 0) {
            System.out.println("  [ERROR] Quantity must be greater than 0!");
            return;
        }

        double totalCost = stock.getPrice() * qty;

        if (totalCost > balance) {
            System.out.printf("  [ERROR] Insufficient balance! Need Rs. %.2f, Have Rs. %.2f%n",
                    totalCost, balance);
            return;
        }

        // Deduct balance, update portfolio, log transaction
        balance -= totalCost;
        portfolio.addHolding(sym, qty, totalCost);
        history.add(new Transaction("BUY", sym, qty, totalCost));

        System.out.println("\n  ✔ Purchase Successful!");
        System.out.printf("  Bought %d share(s) of %s @ Rs. %.2f each%n",
                qty, sym, stock.getPrice());
        System.out.printf("  Total Cost      : Rs. %.2f%n", totalCost);
        System.out.printf("  Remaining Balance: Rs. %.2f%n", balance);
    }

    // ============================================================
    //  2. sellStock()  –  implements Trading
    // ============================================================
    @Override
    public void sellStock() {
        System.out.print("\n  Enter Stock Symbol to SELL : ");
        String sym = scanner.next().toUpperCase();

        Stock stock = findStock(sym);
        if (stock == null) {
            System.out.println("  [ERROR] Stock not found!");
            return;
        }

        int owned = portfolio.getQuantity(sym);
        if (owned == 0) {
            System.out.println("  [ERROR] You don't own any shares of " + sym);
            return;
        }

        System.out.printf("  You own %d share(s) of %s%n", owned, sym);
        System.out.print("  Enter Quantity to sell     : ");
        int qty = scanner.nextInt();

        if (qty <= 0 || qty > owned) {
            System.out.println("  [ERROR] Invalid quantity!");
            return;
        }

        // Average buy price for P&L calculation
        double avgBuyPrice   = portfolio.getInvested(sym) / owned;
        double costPortion   = avgBuyPrice * qty;
        double saleAmount    = stock.getPrice() * qty;
        double profitOrLoss  = saleAmount - costPortion;

        // Update balance, portfolio, log transaction
        balance += saleAmount;
        portfolio.removeHolding(sym, qty, costPortion);
        history.add(new Transaction("SELL", sym, qty, saleAmount));

        System.out.println("\n  ✔ Sale Successful!");
        System.out.printf("  Sold %d share(s) of %s @ Rs. %.2f each%n",
                qty, sym, stock.getPrice());
        System.out.printf("  Sale Amount     : Rs. %.2f%n", saleAmount);
        System.out.printf("  Profit / Loss   : Rs. %.2f %s%n",
                Math.abs(profitOrLoss),
                profitOrLoss >= 0 ? "(PROFIT ▲)" : "(LOSS ▼)");
        System.out.printf("  New Balance     : Rs. %.2f%n", balance);
    }

    // ============================================================
    //  3. viewPortfolio()  –  implements Trading
    // ============================================================
    @Override
    public void viewPortfolio() {
        System.out.println("\n  ════════════ MY PORTFOLIO ════════════");
        System.out.printf("  Trader         : %s%n", name);
        System.out.printf("  Cash Balance   : Rs. %.2f%n", balance);
        System.out.println("  " + "-".repeat(42));

        double totalValue      = 0;
        double totalInvestment = 0;
        boolean hasHoldings    = false;

        System.out.printf("  %-8s %-6s %-12s %-12s%n",
                "SYMBOL","QTY","INVESTED","CURR.VALUE");
        System.out.println("  " + "-".repeat(42));

        for (Stock s : marketStocks) {
            int qty = portfolio.getQuantity(s.getSymbol());
            if (qty > 0) {
                hasHoldings = true;
                double invested = portfolio.getInvested(s.getSymbol());
                double currVal  = qty * s.getPrice();
                totalValue      += currVal;
                totalInvestment += invested;

                System.out.printf("  %-8s %-6d Rs.%-9.2f Rs.%-9.2f%n",
                        s.getSymbol(), qty, invested, currVal);
            }
        }

        if (!hasHoldings) {
            System.out.println("  No stocks in portfolio yet.");
        }

        System.out.println("  " + "-".repeat(42));
        System.out.printf("  Total Investment : Rs. %.2f%n", totalInvestment);
        System.out.printf("  Total Value Now  : Rs. %.2f%n", totalValue);
        System.out.printf("  Overall P / L    : Rs. %.2f %s%n",
                Math.abs(totalValue - totalInvestment),
                totalValue >= totalInvestment ? "(PROFIT ▲)" : "(LOSS ▼)");
        System.out.println("  ══════════════════════════════════════");
    }

    // ============================================================
    //  4. transactionHistory()  –  implements Trading
    // ============================================================
    @Override
    public void transactionHistory() {
        System.out.println("\n  ════════ TRANSACTION HISTORY ════════");
        if (history.isEmpty()) {
            System.out.println("  No transactions yet.");
        } else {
            System.out.printf("  %-6s %-8s %-6s %s%n",
                    "TYPE","SYMBOL","QTY","AMOUNT");
            System.out.println("  " + "-".repeat(40));
            for (Transaction t : history) {
                t.display();
            }
        }
        System.out.println("  ════════════════════════════════════");
    }
}
