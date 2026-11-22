// ============================================================
//  Main.java  –  Entry point. Menu-driven console application.
//  Creates the market, registers the user, and shows the menu.
// ============================================================

import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        // ── 1. Build the stock market ─────────────────────────
        ArrayList<Stock> market = new ArrayList<>();
        market.add(new Stock("TCS",   "Tata Consultancy Services", 3500.00));
        market.add(new Stock("INFY",  "Infosys Ltd.",              1800.00));
        market.add(new Stock("RELI",  "Reliance Industries",       1400.00));
        market.add(new Stock("HDFC",  "HDFC Bank Ltd.",            1700.00));
        market.add(new Stock("WIPRO", "Wipro Ltd.",                 600.00));

        // ── 2. Welcome banner ─────────────────────────────────
        System.out.println("╔══════════════════════════════════════════╗");
        System.out.println("║      STOCK TRADING PLATFORM  –  v1.0    ║");
        System.out.println("║           Phase 1 : Foundation          ║");
        System.out.println("╚══════════════════════════════════════════╝");

        System.out.print("\n  Enter your name : ");
        String name = sc.nextLine().trim();

        // ── 3. Create user (implements Trading) ───────────────
        User user = new User(name, market, sc);

        System.out.println("\n  Welcome, " + name + "!");
        System.out.printf("  Starting Balance : Rs. %.2f%n%n", user.getBalance());

        // ── 4. Main menu loop ─────────────────────────────────
        int choice;
        do {
            System.out.println("┌──────────────────────────────────────────┐");
            System.out.println("│               MAIN  MENU                 │");
            System.out.println("├──────────────────────────────────────────┤");
            System.out.println("│  1. View Market Data                     │");
            System.out.println("│  2. Buy  Stock                           │");
            System.out.println("│  3. Sell Stock                           │");
            System.out.println("│  4. View Portfolio                       │");
            System.out.println("│  5. Transaction History                  │");
            System.out.println("│  6. Check Balance                        │");
            System.out.println("│  7. Exit                                 │");
            System.out.println("└──────────────────────────────────────────┘");
            System.out.print("  Your choice : ");

            choice = sc.nextInt();

            System.out.println();

            switch (choice) {

                case 1: // ── View market ───────────────────────
                    System.out.println("  ─── LIVE MARKET DATA ───");
                    System.out.printf("  %-8s %-28s %s%n",
                            "SYMBOL","COMPANY","PRICE");
                    System.out.println("  " + "-".repeat(50));
                    for (Stock s : market) s.display();
                    System.out.println("  " + "-".repeat(50));
                    break;

                case 2: // ── Buy ───────────────────────────────
                    user.buyStock();
                    break;

                case 3: // ── Sell ──────────────────────────────
                    user.sellStock();
                    break;

                case 4: // ── Portfolio ─────────────────────────
                    user.viewPortfolio();
                    break;

                case 5: // ── History ───────────────────────────
                    user.transactionHistory();
                    break;

                case 6: // ── Balance ───────────────────────────
                    System.out.printf("  Available Balance : Rs. %.2f%n",
                            user.getBalance());
                    break;

                case 7: // ── Exit ──────────────────────────────
                    System.out.println("  Thank you for using Stock Trading Platform!");
                    System.out.println("  Goodbye, " + user.getName() + "! 👋");
                    break;

                default:
                    System.out.println("  [ERROR] Invalid choice! Enter 1–7.");
            }

            System.out.println();

        } while (choice != 7);

        sc.close();
    }
}
