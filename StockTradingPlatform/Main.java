// ============================================================
//  Main.java  –  UPDATED Phase 7
//  • Calls DataManager.initDataDirectory() on startup
//  • Loads saved users via auth.loadFromFile()
//  • All previous menus intact (Market, Portfolio, History)
// ============================================================

import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {

        // ── Phase 7: Initialize data directory ────────────────
        DataManager.initDataDirectory();

        // ── 1. Build StockMarket ──────────────────────────────
        StockMarket stockMarket = new StockMarket();
        ArrayList<Stock> market = stockMarket.getStockList();

        // ── 2. Auth System ────────────────────────────────────
        AuthSystem auth = new AuthSystem(market);

        // ── Phase 7: Load saved users from files ──────────────
        int loaded = auth.loadFromFile(sc);

        // ── 3. Welcome ────────────────────────────────────────
        printWelcomeBanner();
        if (loaded > 0) {
            System.out.printf("  [LOADED] %d saved user account(s) restored from disk.%n%n", loaded);
        }

        // ── 4. Auth loop ──────────────────────────────────────
        User loggedInUser = null;

        while (loggedInUser == null) {
            printAuthMenu();
            int authChoice = readInt();
            switch (authChoice) {
                case 1:
                    System.out.println("\n  ─── NEW USER REGISTRATION ───");
                    System.out.print("  Enter Username  : ");
                    String regUser  = sc.next();
                    System.out.print("  Enter Email     : ");
                    String regEmail = sc.next();
                    System.out.print("  Enter Password  : ");
                    String regPass  = sc.next();
                    try {
                        auth.register(regUser, regEmail, regPass, sc);
                    } catch (DuplicateUserException | IllegalArgumentException e) {
                        System.out.println("\n  [ERROR] " + e.getMessage());
                    }
                    break;
                case 2:
                    System.out.println("\n  ─── USER LOGIN ───");
                    System.out.print("  Enter Username  : ");
                    String loginUser = sc.next();
                    System.out.print("  Enter Password  : ");
                    String loginPass = sc.next();
                    try {
                        loggedInUser = auth.login(loginUser, loginPass);
                        System.out.println("\n  ✔ Login Successful! Welcome back, "
                                + loggedInUser.getName() + "!");
                    } catch (InvalidCredentialsException e) {
                        System.out.println("\n  [ERROR] " + e.getMessage());
                    }
                    break;
                case 3:
                    System.out.println("\n  Goodbye! 👋");
                    sc.close(); return;
                default:
                    System.out.println("\n  [ERROR] Enter 1, 2 or 3.");
            }
            System.out.println();
        }

        // ── 5. Dashboard ──────────────────────────────────────
        loggedInUser.showDashboard();

        // ── 6. Main menu ──────────────────────────────────────
        int choice;
        do {
            printTradingMenu(loggedInUser.getName());
            choice = readInt();
            System.out.println();

            switch (choice) {
                case 1: marketDataMenu(stockMarket);       break;
                case 2: loggedInUser.buyStock();           break;
                case 3: loggedInUser.sellStock();          break;
                case 4: portfolioMenu(loggedInUser);       break;
                case 5: historyMenu(loggedInUser);         break;
                case 6:
                    System.out.printf("  Available Balance : Rs. %.2f%n",
                            loggedInUser.getBalance());
                    break;
                case 7: loggedInUser.showDashboard();     break;
                case 8:
                    System.out.println("  ✔ Logged out. All data is saved. See you, "
                            + loggedInUser.getName() + "!");
                    break;
                default: System.out.println("  [ERROR] Enter 1–8.");
            }
            System.out.println();
        } while (choice != 8);

        sc.close();
    }

    // ── Market Data Submenu ───────────────────────────────────
    private static void marketDataMenu(StockMarket stockMarket) {
        int sub;
        do {
            System.out.println("  ┌────────────────────────────────────────┐");
            System.out.println("  │          📊  MARKET DATA MENU          │");
            System.out.println("  ├────────────────────────────────────────┤");
            System.out.println("  │  1. View All Stocks (Table)            │");
            System.out.println("  │  2. Search Stock by Symbol             │");
            System.out.println("  │  3. View Individual Stock Details      │");
            System.out.println("  │  4. Back to Main Menu                  │");
            System.out.println("  └────────────────────────────────────────┘");
            System.out.print("  Your choice : ");
            sub = readInt(); System.out.println();
            switch (sub) {
                case 1: stockMarket.displayAllStocks(); break;
                case 2:
                    System.out.print("  Enter Stock Symbol : ");
                    stockMarket.displaySearchResult(sc.next()); break;
                case 3:
                    System.out.print("  Enter Stock Symbol : ");
                    stockMarket.displayStockDetails(sc.next()); break;
                case 4: System.out.println("  Returning..."); break;
                default: System.out.println("  [ERROR] Enter 1–4.");
            }
            System.out.println();
        } while (sub != 4);
    }

    // ── Portfolio Submenu ─────────────────────────────────────
    private static void portfolioMenu(User user) {
        int sub;
        do {
            System.out.println("  ┌────────────────────────────────────────┐");
            System.out.println("  │          📁  PORTFOLIO MENU            │");
            System.out.println("  ├────────────────────────────────────────┤");
            System.out.println("  │  1. View Full Portfolio                │");
            System.out.println("  │  2. View Performance History           │");
            System.out.println("  │  3. Back to Main Menu                  │");
            System.out.println("  └────────────────────────────────────────┘");
            System.out.print("  Your choice : ");
            sub = readInt(); System.out.println();
            switch (sub) {
                case 1: user.viewPortfolio(); break;
                case 2: user.showPerformanceHistory(); break;
                case 3: System.out.println("  Returning..."); break;
                default: System.out.println("  [ERROR] Enter 1–3.");
            }
            System.out.println();
        } while (sub != 3);
    }

    // ── Transaction History Submenu ───────────────────────────
    private static void historyMenu(User user) {
        int sub;
        do {
            System.out.println("  ┌────────────────────────────────────────┐");
            System.out.println("  │       📋  TRANSACTION HISTORY MENU     │");
            System.out.println("  ├────────────────────────────────────────┤");
            System.out.println("  │  1. View All Transactions (Latest 1st) │");
            System.out.println("  │  2. Filter: BUY Only                   │");
            System.out.println("  │  3. Filter: SELL Only                  │");
            System.out.println("  │  4. View Transaction Details (by ID)   │");
            System.out.println("  │  5. Back to Main Menu                  │");
            System.out.println("  └────────────────────────────────────────┘");
            System.out.print("  Your choice : ");
            sub = readInt(); System.out.println();
            switch (sub) {
                case 1: user.transactionHistory();    break;
                case 2: user.filterHistory("BUY");    break;
                case 3: user.filterHistory("SELL");   break;
                case 4: user.viewTransactionDetail(); break;
                case 5: System.out.println("  Returning..."); break;
                default: System.out.println("  [ERROR] Enter 1–5.");
            }
            System.out.println();
        } while (sub != 5);
    }

    // ── Menus ─────────────────────────────────────────────────

    private static void printWelcomeBanner() {
        System.out.println("╔══════════════════════════════════════════╗");
        System.out.println("║      STOCK TRADING PLATFORM  –  v7.0    ║");
        System.out.println("║     Phase 7 : Data Storage & Persistence ║");
        System.out.println("╚══════════════════════════════════════════╝");
    }

    private static void printAuthMenu() {
        System.out.println("┌──────────────────────────────────────────┐");
        System.out.println("│           AUTHENTICATION MENU            │");
        System.out.println("├──────────────────────────────────────────┤");
        System.out.println("│  1. Register New Account                 │");
        System.out.println("│  2. Login to Existing Account            │");
        System.out.println("│  3. Exit                                 │");
        System.out.println("└──────────────────────────────────────────┘");
        System.out.print("  Your choice : ");
    }

    private static void printTradingMenu(String name) {
        System.out.println("┌──────────────────────────────────────────┐");
        System.out.printf( "│  Logged in as: %-27s│%n", name);
        System.out.println("├──────────────────────────────────────────┤");
        System.out.println("│  1. Market Data  (View / Search)         │");
        System.out.println("│  2. Buy  Stock                           │");
        System.out.println("│  3. Sell Stock                           │");
        System.out.println("│  4. Portfolio    (View / Performance)    │");
        System.out.println("│  5. Transaction History                  │");
        System.out.println("│  6. Check Balance                        │");
        System.out.println("│  7. View Dashboard                       │");
        System.out.println("│  8. Logout                               │");
        System.out.println("└──────────────────────────────────────────┘");
        System.out.print("  Your choice : ");
    }

    private static int readInt() {
        try { return sc.nextInt(); }
        catch (Exception e) { sc.nextLine(); return -1; }
    }
}
