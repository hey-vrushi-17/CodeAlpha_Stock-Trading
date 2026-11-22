// ============================================================
//  StockTradingGUI.java  –  Phase 8: Main GUI Frame
//  Manages CardLayout switching between AuthPanel & TradingPanel.
//  Initializes backend: DataManager, StockMarket, AuthSystem.
// ============================================================

import java.awt.*;
import javax.swing.*;

public class StockTradingGUI extends JFrame {

    private CardLayout mainCards;
    private JPanel     mainPanel;

    private AuthSystem  authSystem;
    private StockMarket stockMarket;

    public StockTradingGUI() {
        // ── Backend init ──────────────────────────────────────
        DataManager.initDataDirectory();
        stockMarket = new StockMarket();
        authSystem  = new AuthSystem(stockMarket.getStockList());
        authSystem.loadFromFile(null);  // load saved users

        // ── Frame setup ───────────────────────────────────────
        setTitle("Stock Trading Platform – v8.0  |  CodeAlpha");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 750);
        setMinimumSize(new Dimension(1000, 650));
        setLocationRelativeTo(null);
        setBackground(AppTheme.BG_DARK);

        // ── Icon (optional) ────────────────────────────────────
        try {
            setIconImage(new ImageIcon("banner.png").getImage());
        } catch (Exception ignored) {}

        // ── Main card layout ──────────────────────────────────
        mainCards = new CardLayout();
        mainPanel = new JPanel(mainCards);
        mainPanel.setBackground(AppTheme.BG_DARK);

        mainPanel.add(new AuthPanel(this), "AUTH");
        // TradingPanel is added dynamically after login

        setContentPane(mainPanel);
        mainCards.show(mainPanel, "AUTH");
    }

    // ── Getters for panels ────────────────────────────────────
    public AuthSystem  getAuthSystem()  { return authSystem; }
    public StockMarket getStockMarket() { return stockMarket; }

    // ── Called by AuthPanel on successful login ────────────────
    public void onLoginSuccess(User user) {
        // Load saved data for this user
        user.loadSavedData();

        TradingPanel tp = new TradingPanel(this, user, stockMarket);
        mainPanel.add(tp, "TRADING");
        mainCards.show(mainPanel, "TRADING");
    }

    // ── Called by TradingPanel logout button ───────────────────
    public void onLogout() {
        // Remove trading panel and show auth
        for (Component c : mainPanel.getComponents()) {
            if (c instanceof TradingPanel) {
                mainPanel.remove(c);
                break;
            }
        }
        mainCards.show(mainPanel, "AUTH");
    }

    // ── Entry Point ───────────────────────────────────────────
    public static void main(String[] args) {
        // Set system look & feel hints
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            UIManager.put("OptionPane.background", AppTheme.BG_CARD);
            UIManager.put("Panel.background", AppTheme.BG_CARD);
            UIManager.put("OptionPane.messageForeground", AppTheme.TEXT);
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(() -> {
            StockTradingGUI gui = new StockTradingGUI();
            gui.setVisible(true);
        });
    }
}
