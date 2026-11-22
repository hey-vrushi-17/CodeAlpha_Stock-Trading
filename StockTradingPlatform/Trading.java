// ============================================================
//  Trading.java  –  Interface (Contract)
//  Declares the 4 core trading operations every user must have.
// ============================================================

public interface Trading {

    void buyStock();        // Buy a stock

    void sellStock();       // Sell a stock

    void viewPortfolio();   // View current holdings

    void transactionHistory(); // View all past transactions
}
