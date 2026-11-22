// ============================================================
//  DataManager.java  –  NEW Phase 7
//  Handles all file I/O: save & load users, portfolios,
//  balances, and transaction history using text files.
//  File structure:
//    data/users.txt              → all registered users
//    data/balance_<user>.txt     → cash balance
//    data/portfolio_<user>.txt   → stock holdings
//    data/history_<user>.txt     → transaction log
// ============================================================

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;

public class DataManager {

    private static final String DATA_DIR   = "data";
    private static final String USERS_FILE = DATA_DIR + "/users.txt";
    private static final DateTimeFormatter DT_FMT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    // ── Ensure data directory exists ──────────────────────────
    public static void initDataDirectory() {
        File dir = new File(DATA_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
            System.out.println("  [INFO] Created data directory: " + DATA_DIR + "/");
        }
    }

    // ============================================================
    //  1. SAVE / LOAD USER CREDENTIALS
    //     Format: username|email|password   (one user per line)
    // ============================================================

    public static void saveUser(String username, String email, String password) {
        try (BufferedWriter bw = new BufferedWriter(
                new FileWriter(USERS_FILE, true))) {  // append mode
            bw.write(username + "|" + email + "|" + password);
            bw.newLine();
        } catch (IOException e) {
            System.out.println("  [ERROR] Could not save user data: " + e.getMessage());
        }
    }

    public static ArrayList<String[]> loadAllUsers() {
        ArrayList<String[]> users = new ArrayList<>();
        File file = new File(USERS_FILE);
        if (!file.exists()) return users;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty()) {
                    String[] parts = line.split("\\|");
                    if (parts.length == 3) {
                        users.add(parts);  // [username, email, password]
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("  [ERROR] Could not load user data: " + e.getMessage());
        }
        return users;
    }

    // ============================================================
    //  2. SAVE / LOAD BALANCE
    //     Format: single line with the balance double value
    // ============================================================

    public static void saveBalance(String username, double balance) {
        String path = DATA_DIR + "/balance_" + username.toLowerCase() + ".txt";
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(path))) {
            bw.write(String.valueOf(balance));
            bw.newLine();
        } catch (IOException e) {
            System.out.println("  [ERROR] Could not save balance: " + e.getMessage());
        }
    }

    public static double loadBalance(String username) {
        String path = DATA_DIR + "/balance_" + username.toLowerCase() + ".txt";
        File file = new File(path);
        if (!file.exists()) return 100000.00;  // default starting balance

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line = br.readLine();
            if (line != null && !line.trim().isEmpty()) {
                return Double.parseDouble(line.trim());
            }
        } catch (IOException | NumberFormatException e) {
            System.out.println("  [ERROR] Could not load balance: " + e.getMessage());
        }
        return 100000.00;
    }

    // ============================================================
    //  3. SAVE / LOAD PORTFOLIO HOLDINGS
    //     Format: symbol|quantity|investedAmount   (one per line)
    // ============================================================

    public static void savePortfolio(String username, Portfolio portfolio) {
        String path = DATA_DIR + "/portfolio_" + username.toLowerCase() + ".txt";
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(path))) {
            HashMap<String, Integer> holdings = portfolio.getAllHoldings();
            HashMap<String, Double>  invested = portfolio.getAllInvested();

            for (String symbol : holdings.keySet()) {
                int qty = holdings.get(symbol);
                if (qty > 0) {
                    double inv = invested.getOrDefault(symbol, 0.0);
                    bw.write(symbol + "|" + qty + "|" + inv);
                    bw.newLine();
                }
            }
        } catch (IOException e) {
            System.out.println("  [ERROR] Could not save portfolio: " + e.getMessage());
        }
    }

    public static void loadPortfolio(String username, Portfolio portfolio) {
        String path = DATA_DIR + "/portfolio_" + username.toLowerCase() + ".txt";
        File file = new File(path);
        if (!file.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                String[] parts = line.split("\\|");
                if (parts.length == 3) {
                    String symbol = parts[0];
                    int    qty    = Integer.parseInt(parts[1]);
                    double inv    = Double.parseDouble(parts[2]);
                    portfolio.addHolding(symbol, qty, inv);
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.out.println("  [ERROR] Could not load portfolio: " + e.getMessage());
        }
    }

    // ============================================================
    //  4. SAVE / LOAD TRANSACTION HISTORY
    //     Format: id|type|symbol|name|qty|price|amount|pl|timestamp
    // ============================================================

    public static void saveTransaction(String username, Transaction t) {
        String path = DATA_DIR + "/history_" + username.toLowerCase() + ".txt";
        try (BufferedWriter bw = new BufferedWriter(
                new FileWriter(path, true))) {  // append mode
            bw.write(t.getTransactionId() + "|"
                    + t.getType() + "|"
                    + t.getStockSymbol() + "|"
                    + t.getStockName() + "|"
                    + t.getQuantity() + "|"
                    + t.getPricePerShare() + "|"
                    + t.getAmount() + "|"
                    + t.getProfitLoss() + "|"
                    + t.getTimestamp().format(DT_FMT));
            bw.newLine();
        } catch (IOException e) {
            System.out.println("  [ERROR] Could not save transaction: " + e.getMessage());
        }
    }

    public static ArrayList<Transaction> loadTransactions(String username) {
        ArrayList<Transaction> list = new ArrayList<>();
        String path = DATA_DIR + "/history_" + username.toLowerCase() + ".txt";
        File file = new File(path);
        if (!file.exists()) return list;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                String[] p = line.split("\\|");
                if (p.length == 9) {
                    Transaction t = new Transaction(
                            p[1],                          // type
                            p[2],                          // symbol
                            p[3],                          // name
                            Integer.parseInt(p[4]),        // qty
                            Double.parseDouble(p[5]),      // price
                            Double.parseDouble(p[6]),      // amount
                            Double.parseDouble(p[7])       // pl
                    );
                    // Override auto-generated ID and timestamp with saved values
                    t.setTransactionId(Integer.parseInt(p[0]));
                    t.setTimestamp(LocalDateTime.parse(p[8], DT_FMT));
                    list.add(t);
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.out.println("  [ERROR] Could not load transactions: " + e.getMessage());
        }
        return list;
    }

    // ============================================================
    //  5. SAVE ALL DATA (convenience method after every trade)
    // ============================================================

    public static void saveAll(String username, double balance,
                                Portfolio portfolio, Transaction lastTxn) {
        saveBalance(username, balance);
        savePortfolio(username, portfolio);
        if (lastTxn != null) saveTransaction(username, lastTxn);
    }

    // ============================================================
    //  6. DELETE user data files (if needed in future)
    // ============================================================

    public static void deleteUserData(String username) {
        String key = username.toLowerCase();
        new File(DATA_DIR + "/balance_"   + key + ".txt").delete();
        new File(DATA_DIR + "/portfolio_" + key + ".txt").delete();
        new File(DATA_DIR + "/history_"   + key + ".txt").delete();
    }
}
