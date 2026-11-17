import java.util.*;

class Stock {
    String symbol;
    String company;
    double price;

    Stock(String symbol, String company, double price) {
        this.symbol = symbol;
        this.company = company;
        this.price = price;
    }

    void display() {
        System.out.println(symbol + "  " + company + "  Rs." + price);
    }
}

class Transaction {
    String type;
    String stock;
    int quantity;
    double amount;

    Transaction(String type, String stock, int quantity, double amount) {
        this.type = type;
        this.stock = stock;
        this.quantity = quantity;
        this.amount = amount;
    }

    void display() {
        System.out.println(type + "  " + stock + "  " +
                quantity + "  Rs." + amount);
    }
}

class User {
    String name;
    double balance = 100000;

    HashMap<String, Integer> portfolio = new HashMap<>();
    HashMap<String, Double> investment = new HashMap<>();
    ArrayList<Transaction> history = new ArrayList<>();

    User(String name) {
        this.name = name;
    }

    void buy(Stock s, int qty) {
        if (qty <= 0) {
            System.out.println("Invalid quantity!");
            return;
        }

        double cost = s.price * qty;

        if (balance >= cost) {
            balance -= cost;

            portfolio.put(s.symbol,
                    portfolio.getOrDefault(s.symbol, 0) + qty);

            investment.put(s.symbol,
                    investment.getOrDefault(s.symbol, 0.0) + cost);

            history.add(new Transaction("BUY", s.symbol, qty, cost));

            System.out.println("Stock purchased successfully!");
        } else {
            System.out.println("Insufficient balance!");
        }
    }

    void sell(Stock s, int qty) {
        int available = portfolio.getOrDefault(s.symbol, 0);

        if (qty <= 0 || qty > available) {
            System.out.println("Invalid quantity or insufficient stocks!");
            return;
        }

        double averagePrice = investment.get(s.symbol) / available;
        double cost = averagePrice * qty;
        double amount = s.price * qty;

        balance += amount;

        portfolio.put(s.symbol, available - qty);
        investment.put(s.symbol, investment.get(s.symbol) - cost);

        history.add(new Transaction("SELL", s.symbol, qty, amount));

        System.out.println("Stock sold successfully!");
        System.out.println("Profit/Loss: Rs." + (amount - cost));
    }

    void showPortfolio(ArrayList<Stock> stocks) {
        double totalValue = 0;
        double totalInvestment = 0;

        System.out.println("\n----- MY PORTFOLIO -----");

        for (Stock s : stocks) {
            int qty = portfolio.getOrDefault(s.symbol, 0);

            if (qty > 0) {
                double value = qty * s.price;
                totalValue += value;
                totalInvestment += investment.get(s.symbol);

                System.out.println(s.symbol + " Quantity: " + qty +
                        " Value: Rs." + value);
            }
        }

        System.out.println("Available Balance: Rs." + balance);
        System.out.println("Total Stock Value: Rs." + totalValue);
        System.out.println("Profit/Loss: Rs." +
                (totalValue - totalInvestment));
    }

    void showHistory() {
        System.out.println("\n----- TRANSACTION HISTORY -----");

        for (Transaction t : history) {
            t.display();
        }

        if (history.isEmpty()) {
            System.out.println("No transactions available.");
        }
    }
}

public class StockTradingPlatform {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        ArrayList<Stock> stocks = new ArrayList<>();

        stocks.add(new Stock("TCS", "TCS", 3500));
        stocks.add(new Stock("INFY", "Infosys", 1800));
        stocks.add(new Stock("RELI", "Reliance", 1400));
        stocks.add(new Stock("HDFC", "HDFC Bank", 1700));
        stocks.add(new Stock("WIPRO", "Wipro", 600));

        System.out.println("=================================");
        System.out.println("       STOCK TRADING PLATFORM");
        System.out.println("=================================");

        System.out.print("Enter User Name: ");
        String name = sc.nextLine();

        User user = new User(name);

        int choice;

        do {
            System.out.println("\n1. Display Market Data");
            System.out.println("2. Buy Stocks");
            System.out.println("3. Sell Stocks");
            System.out.println("4. View Portfolio");
            System.out.println("5. Transaction History");
            System.out.println("6. Check Balance");
            System.out.println("7. Exit");

            System.out.print("\nEnter your choice: ");
            choice = sc.nextInt();

            switch (choice) {

                case 1:
                    System.out.println("\nSYMBOL  COMPANY  PRICE");
                    for (Stock s : stocks) {
                        s.display();
                    }
                    break;

                case 2:
                    System.out.print("Enter Stock Symbol: ");
                    String buyName = sc.next().toUpperCase();

                    Stock buyStock = null;

                    for (Stock s : stocks) {
                        if (s.symbol.equals(buyName)) {
                            buyStock = s;
                            break;
                        }
                    }

                    if (buyStock != null) {
                        System.out.print("Enter Quantity: ");
                        int qty = sc.nextInt();
                        user.buy(buyStock, qty);
                    } else {
                        System.out.println("Stock not found!");
                    }
                    break;

                case 3:
                    System.out.print("Enter Stock Symbol: ");
                    String sellName = sc.next().toUpperCase();

                    Stock sellStock = null;

                    for (Stock s : stocks) {
                        if (s.symbol.equals(sellName)) {
                            sellStock = s;
                            break;
                        }
                    }

                    if (sellStock != null) {
                        System.out.print("Enter Quantity: ");
                        int qty = sc.nextInt();
                        user.sell(sellStock, qty);
                    } else {
                        System.out.println("Stock not found!");
                    }
                    break;

                case 4:
                    user.showPortfolio(stocks);
                    break;

                case 5:
                    user.showHistory();
                    break;

                case 6:
                    System.out.println("Available Balance: Rs." +
                            user.balance);
                    break;

                case 7:
                    System.out.println("Thank you for using Stock Trading Platform!");
                    break;

                default:
                    System.out.println("Invalid choice!");
            }

        } while (choice != 7);

        sc.close();
    }
}
