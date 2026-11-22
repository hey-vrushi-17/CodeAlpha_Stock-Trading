// ============================================================
//  User.java  –  UPDATED Phase 7
//  Auto-saves to files after every buy/sell operation.
//  Loads saved portfolio, balance, and history on creation.
// ============================================================

import java.util.ArrayList;
import java.util.Scanner;

public class User implements Trading {

    private String    name;
    private String    email;
    private double    balance;
    private Portfolio portfolio;
    private ArrayList<Transaction> history;

    private ArrayList<Stock> marketStocks;
    private Scanner          scanner;

    // ── Constructor (creates fresh user) ──────────────────────
    public User(String name, String email,
                ArrayList<Stock> marketStocks, Scanner scanner) {
        this.name         = name;
        this.email        = email;
        this.balance      = 100000.00;
        this.portfolio    = new Portfolio();
        this.history      = new ArrayList<>();
        this.marketStocks = marketStocks;
        this.scanner      = scanner;
    }

    // ── Phase 7: Load saved data into this user ───────────────
    public void loadSavedData() {
        // Load balance
        this.balance = DataManager.loadBalance(name);

        // Load portfolio holdings
        DataManager.loadPortfolio(name, portfolio);

        // Load transaction history
        ArrayList<Transaction> saved = DataManager.loadTransactions(name);
        this.history.addAll(saved);

        if (!saved.isEmpty()) {
            System.out.printf("  [LOADED] %d transactions, Balance: Rs. %.2f, " +
                    "%d stock type(s) restored.%n",
                    saved.size(), balance, portfolio.countDistinctHoldings());
        }
    }

    // ── Getters ──────────────────────────────────────────────
    public String    getName()      { return name; }
    public String    getEmail()     { return email; }
    public double    getBalance()   { return balance; }
    public Portfolio getPortfolio() { return portfolio; }

    private Stock findStock(String symbol) {
        for (Stock s : marketStocks)
            if (s.getSymbol().equalsIgnoreCase(symbol)) return s;
        return null;
    }

    // ── Dashboard ─────────────────────────────────────────────
    public void showDashboard() {
        double stockVal = portfolio.getTotalStockValue(marketStocks);
        double invested = portfolio.getTotalInvestment(marketStocks);
        double unrealPL = stockVal - invested;
        String arrow    = unrealPL >= 0 ? "▲" : "▼";
        System.out.println("\n╔══════════════════════════════════════════╗");
        System.out.println("║           PERSONALIZED DASHBOARD         ║");
        System.out.println("╚══════════════════════════════════════════╝");
        System.out.printf("  👤 Trader      : %s%n",   name);
        System.out.printf("  📧 Email       : %s%n",   email);
        System.out.printf("  💰 Cash Balance: Rs. %.2f%n", balance);
        System.out.printf("  📊 Holdings    : %d stock type(s)%n",
                portfolio.countDistinctHoldings());
        System.out.printf("  📈 Stock Value : Rs. %.2f%n", stockVal);
        System.out.printf("  💎 Net Worth   : Rs. %.2f%n", balance + stockVal);
        System.out.printf("  📉 Unrealized  : %s Rs. %.2f%n", arrow, Math.abs(unrealPL));
        System.out.printf("  📋 Trades      : %d transaction(s)%n", history.size());
        System.out.println("  " + "─".repeat(44));
    }

    // ============================================================
    //  1. buyStock()  –  with auto-save (Phase 7)
    // ============================================================
    @Override
    public void buyStock() {
        System.out.println("\n  ─── 📈 BUY STOCKS ───");
        System.out.printf("  %-8s %-28s %-12s%n","SYMBOL","COMPANY","PRICE(Rs.)");
        System.out.println("  " + "─".repeat(50));
        for (Stock s : marketStocks) s.display();
        System.out.println("  " + "─".repeat(50));

        System.out.print("\n  Enter Stock Symbol : ");
        String sym   = scanner.next().toUpperCase();
        Stock  stock = findStock(sym);

        if (stock == null) {
            System.out.println("  [ERROR] Stock '" + sym + "' not found.");
            return;
        }

        System.out.printf("  Current Price      : Rs. %.2f per share%n", stock.getPrice());
        System.out.print("  Enter Quantity     : ");
        int qty = readInt();

        if (qty <= 0) {
            System.out.println("  [ERROR] Quantity must be positive.");
            return;
        }

        double totalCost = stock.getPrice() * qty;

        // Confirmation
        System.out.println("\n  ┌─────────────────────────────────────┐");
        System.out.println("  │          PURCHASE CONFIRMATION       │");
        System.out.println("  ├─────────────────────────────────────┤");
        System.out.printf( "  │  Stock    : %-24s│%n", sym+" - "+stock.getCompanyName());
        System.out.printf( "  │  Quantity : %-24d│%n", qty);
        System.out.printf( "  │  Price    : Rs. %-20.2f│%n", stock.getPrice());
        System.out.printf( "  │  Total    : Rs. %-20.2f│%n", totalCost);
        System.out.printf( "  │  Balance  : Rs. %-20.2f│%n", balance);
        System.out.printf( "  │  After    : Rs. %-20.2f│%n", balance - totalCost);
        System.out.println("  └─────────────────────────────────────┘");
        System.out.print("  Confirm purchase? (Y/N) : ");
        String confirm = scanner.next();
        if (!confirm.equalsIgnoreCase("Y")) {
            System.out.println("  Purchase cancelled.");
            return;
        }

        try {
            if (totalCost > balance)
                throw new InsufficientBalanceException(totalCost, balance);

            balance -= totalCost;
            portfolio.addHolding(sym, qty, totalCost);
            Transaction txn = new Transaction("BUY", sym, stock.getCompanyName(),
                            qty, stock.getPrice(), totalCost, 0.0);
            history.add(txn);
            portfolio.takeSnapshot(balance, marketStocks, "BUY " + sym + " x" + qty);

            // ── Phase 7: Auto-save to files ───────────────────
            DataManager.saveAll(name, balance, portfolio, txn);

            System.out.println("\n  ✔ Purchase Successful!");
            System.out.printf("  Bought  : %d share(s) of %s%n", qty, sym);
            System.out.printf("  Paid    : Rs. %.2f%n", totalCost);
            System.out.printf("  Balance : Rs. %.2f%n", balance);
            System.out.println("  [SAVED] Data saved to disk automatically.");

        } catch (InsufficientBalanceException e) {
            System.out.println("\n  [ERROR] " + e.getMessage());
            System.out.printf("  Tip: You can afford up to %d share(s) of %s.%n",
                    (int)(balance / stock.getPrice()), sym);
        }
    }

    // ============================================================
    //  2. sellStock()  –  with auto-save (Phase 7)
    // ============================================================
    @Override
    public void sellStock() {
        System.out.println("\n  ─── 📉 SELL STOCKS ───");
        portfolio.displayHoldings(marketStocks);
        if (portfolio.countDistinctHoldings() == 0) return;

        System.out.print("  Enter Stock Symbol to SELL : ");
        String sym   = scanner.next().toUpperCase();
        Stock  stock = findStock(sym);

        if (stock == null) {
            System.out.println("  [ERROR] Stock '" + sym + "' not found.");
            return;
        }

        int    owned  = portfolio.getQuantity(sym);
        double avgBuy = portfolio.getAverageBuyPrice(sym);

        System.out.printf("  You own          : %d share(s) of %s%n", owned, sym);
        System.out.printf("  Avg. Buy Price   : Rs. %.2f%n", avgBuy);
        System.out.printf("  Current Price    : Rs. %.2f%n", stock.getPrice());
        System.out.print("  Enter Quantity   : ");
        int qty = readInt();

        try {
            if (qty <= 0) throw new IllegalArgumentException("Quantity must be positive.");
            if (qty > owned) throw new InsufficientStocksException(sym, qty, owned);

            double costPortion  = avgBuy * qty;
            double saleAmount   = stock.getPrice() * qty;
            double profitOrLoss = saleAmount - costPortion;
            String plArrow      = profitOrLoss >= 0 ? "▲ PROFIT" : "▼ LOSS";

            // Confirmation
            System.out.println("\n  ┌─────────────────────────────────────┐");
            System.out.println("  │            SELL CONFIRMATION         │");
            System.out.println("  ├─────────────────────────────────────┤");
            System.out.printf( "  │  Stock      : %-22s│%n", sym);
            System.out.printf( "  │  Quantity   : %-22d│%n", qty);
            System.out.printf( "  │  Avg Cost   : Rs. %-18.2f│%n", avgBuy);
            System.out.printf( "  │  Sell Price : Rs. %-18.2f│%n", stock.getPrice());
            System.out.printf( "  │  Sale Total : Rs. %-18.2f│%n", saleAmount);
            System.out.printf( "  │  P / L      : Rs. %-6.2f  %-10s│%n",
                    Math.abs(profitOrLoss), plArrow);
            System.out.println("  └─────────────────────────────────────┘");
            System.out.print("  Confirm sale? (Y/N) : ");
            String confirm = scanner.next();
            if (!confirm.equalsIgnoreCase("Y")) {
                System.out.println("  Sale cancelled.");
                return;
            }

            balance += saleAmount;
            portfolio.removeHolding(sym, qty, costPortion);
            Transaction txn = new Transaction("SELL", sym, stock.getCompanyName(),
                            qty, stock.getPrice(), saleAmount, profitOrLoss);
            history.add(txn);
            portfolio.takeSnapshot(balance, marketStocks, "SELL " + sym + " x" + qty);

            // ── Phase 7: Auto-save to files ───────────────────
            DataManager.saveAll(name, balance, portfolio, txn);

            System.out.println("\n  ✔ Sale Successful!");
            System.out.printf("  Sold    : %d share(s) of %s%n", qty, sym);
            System.out.printf("  Earned  : Rs. %.2f%n", saleAmount);
            System.out.printf("  P / L   : Rs. %.2f  %s%n",
                    Math.abs(profitOrLoss), plArrow);
            System.out.printf("  Balance : Rs. %.2f%n", balance);
            System.out.println("  [SAVED] Data saved to disk automatically.");

        } catch (InsufficientStocksException | IllegalArgumentException e) {
            System.out.println("\n  [ERROR] " + e.getMessage());
        }
    }

    // ============================================================
    //  3. viewPortfolio()
    // ============================================================
    @Override
    public void viewPortfolio() {
        double totalValue      = portfolio.getTotalStockValue(marketStocks);
        double totalInvestment = portfolio.getTotalInvestment(marketStocks);
        double unrealizedPL    = totalValue - totalInvestment;
        double netWorth        = balance + totalValue;
        String plArrow         = unrealizedPL >= 0 ? "▲ PROFIT" : "▼ LOSS";

        System.out.println("\n  ══════════════════════════════════════════════════════════════");
        System.out.println("                        📁  MY PORTFOLIO");
        System.out.println("  ══════════════════════════════════════════════════════════════");
        System.out.printf( "  Trader : %s   |   Email : %s%n", name, email);
        System.out.println("  " + "─".repeat(62));
        System.out.printf( "  %-8s %-20s %-6s %-12s %-12s %-12s %-10s%n",
                "SYMBOL","COMPANY","QTY","AVG.PRICE","INVESTED","CURR.VALUE","UNREAL.P/L");
        System.out.println("  " + "─".repeat(85));

        boolean any = false;
        for (Stock s : marketStocks) {
            int qty = portfolio.getQuantity(s.getSymbol());
            if (qty > 0) {
                any = true;
                double avgP   = portfolio.getAverageBuyPrice(s.getSymbol());
                double inv    = portfolio.getInvested(s.getSymbol());
                double curr   = qty * s.getPrice();
                double unreal = curr - inv;
                String arr    = unreal >= 0 ? "▲" : "▼";
                System.out.printf("  %-8s %-20s %-6d Rs.%-9.2f Rs.%-9.2f Rs.%-9.2f %s Rs.%.2f%n",
                        s.getSymbol(), s.getCompanyName(), qty, avgP,
                        inv, curr, arr, Math.abs(unreal));
            }
        }
        if (!any) System.out.println("  No stocks in portfolio yet.");

        System.out.println("  " + "─".repeat(85));
        System.out.printf("  Cash Balance        : Rs. %.2f%n", balance);
        System.out.printf("  Total Investment    : Rs. %.2f%n", totalInvestment);
        System.out.printf("  Current Stock Value : Rs. %.2f%n", totalValue);
        System.out.printf("  Unrealized P / L    : Rs. %.2f  %s%n",
                Math.abs(unrealizedPL), plArrow);
        System.out.printf("  Net Worth           : Rs. %.2f%n", netWorth);
        System.out.println("  ══════════════════════════════════════════════════════════════");
    }

    // ============================================================
    //  4. transactionHistory()
    // ============================================================
    @Override
    public void transactionHistory() {
        System.out.println("\n  ══════════════════════════════════════════════════════════════════════════════════════════");
        System.out.println("                              📋  TRANSACTION HISTORY (Latest First)");
        System.out.println("  ══════════════════════════════════════════════════════════════════════════════════════════");
        if (history.isEmpty()) {
            System.out.println("  No transactions yet.");
        } else {
            System.out.printf("  %-7s %-21s %-5s %-7s %-22s %-5s %-11s %-11s %-10s%n",
                    "TXN#","TIMESTAMP","TYPE","SYMBOL","COMPANY","QTY","PRICE/SHR","TOTAL","P/L");
            System.out.println("  " + "─".repeat(105));
            for (int i = history.size() - 1; i >= 0; i--) history.get(i).display();
            System.out.println("  " + "─".repeat(105));

            int buys=0, sells=0; double totalBought=0, totalSold=0, totalPL=0;
            for (Transaction t : history) {
                if (t.getType().equals("BUY")) { buys++; totalBought += t.getAmount(); }
                else { sells++; totalSold += t.getAmount(); totalPL += t.getProfitLoss(); }
            }
            System.out.printf("  Total: %d  |  Buys: %d (Rs. %.2f)  |  Sells: %d (Rs. %.2f)%n",
                    history.size(), buys, totalBought, sells, totalSold);
            if (sells > 0)
                System.out.printf("  Realized P/L: Rs. %.2f  %s%n",
                        Math.abs(totalPL), totalPL >= 0 ? "▲ PROFIT" : "▼ LOSS");
        }
        System.out.println("  ══════════════════════════════════════════════════════════════════════════════════════════");
    }

    public void viewTransactionDetail() {
        if (history.isEmpty()) { System.out.println("  No transactions."); return; }
        System.out.print("  Enter Transaction ID (e.g. 1001) : ");
        int id = readInt();
        for (Transaction t : history)
            if (t.getTransactionId() == id) { t.displayDetailed(); return; }
        System.out.println("  [ERROR] Transaction #" + id + " not found.");
    }

    public void filterHistory(String type) {
        System.out.printf("\n  ─── Showing %s transactions only ───%n", type);
        boolean found = false;
        for (int i = history.size()-1; i >= 0; i--)
            if (history.get(i).getType().equals(type)) { history.get(i).display(); found = true; }
        if (!found) System.out.println("  No " + type + " transactions yet.");
    }

    public void showPerformanceHistory() {
        portfolio.displayPerformanceHistory();
    }

    private int readInt() {
        try { return scanner.nextInt(); }
        catch (Exception e) { scanner.nextLine(); return -1; }
    }
}
