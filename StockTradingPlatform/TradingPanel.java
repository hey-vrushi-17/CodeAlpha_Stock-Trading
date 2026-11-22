// ============================================================
//  TradingPanel.java  –  Phase 8: Main Trading GUI
//  Sidebar navigation + content area with:
//  Dashboard, Market, Buy, Sell, Portfolio, History panels.
// ============================================================

import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.table.*;

public class TradingPanel extends JPanel {

    private StockTradingGUI mainFrame;
    private User currentUser;
    private StockMarket stockMarket;

    private CardLayout contentCards;
    private JPanel contentPanel;

    // Dashboard labels (refreshable)
    private JLabel lblBalance, lblNetWorth, lblStockVal, lblPL, lblTrades;
    // Portfolio table
    private DefaultTableModel portfolioModel;
    // History table
    private DefaultTableModel historyModel;
    // Buy/Sell fields
    private JComboBox<String> buySymbol, sellSymbol;
    private JTextField buyQty, sellQty;
    private JLabel buyInfo, sellInfo, buyResult, sellResult;

    public TradingPanel(StockTradingGUI mainFrame, User user, StockMarket sm) {
        this.mainFrame   = mainFrame;
        this.currentUser = user;
        this.stockMarket = sm;
        setLayout(new BorderLayout());
        setBackground(AppTheme.BG_DARK);
        buildUI();
    }

    private void buildUI() {
        // ── Sidebar ───────────────────────────────────────────
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(AppTheme.BG_SIDEBAR);
        sidebar.setPreferredSize(new Dimension(230, 0));
        sidebar.setBorder(BorderFactory.createMatteBorder(0,0,0,1, AppTheme.BORDER));

        // User info at top
        JPanel userBox = new JPanel();
        userBox.setLayout(new BoxLayout(userBox, BoxLayout.Y_AXIS));
        userBox.setBackground(AppTheme.BG_SIDEBAR);
        userBox.setBorder(BorderFactory.createEmptyBorder(20,20,20,10));
        userBox.add(AppTheme.createLabel("👤 " + currentUser.getName(),
                AppTheme.FONT_HEADING, AppTheme.TEXT));
        userBox.add(Box.createVerticalStrut(4));
        userBox.add(AppTheme.createLabel(currentUser.getEmail(),
                AppTheme.FONT_SMALL, AppTheme.TEXT_DIM));
        userBox.setMaximumSize(new Dimension(230, 80));
        sidebar.add(userBox);
        sidebar.add(Box.createVerticalStrut(10));

        // Nav buttons
        String[][] navItems = {
            {"Dashboard",   "📊", "DASH"},
            {"Market Data", "📈", "MARKET"},
            {"Buy Stock",   "🟢", "BUY"},
            {"Sell Stock",  "🔴", "SELL"},
            {"Portfolio",   "📁", "PORT"},
            {"History",     "📋", "HIST"},
        };

        for (String[] item : navItems) {
            JButton btn = AppTheme.createNavButton(item[0], item[1]);
            String card = item[2];
            btn.addActionListener(e -> {
                contentCards.show(contentPanel, card);
                refreshAll();
            });
            sidebar.add(btn);
            sidebar.add(Box.createVerticalStrut(2));
        }

        sidebar.add(Box.createVerticalGlue());

        // Logout button
        JButton logout = AppTheme.createNavButton("Logout", "🚪");
        logout.setForeground(AppTheme.DANGER);
        logout.addActionListener(e -> mainFrame.onLogout());
        sidebar.add(logout);
        sidebar.add(Box.createVerticalStrut(16));

        add(sidebar, BorderLayout.WEST);

        // ── Content Area ──────────────────────────────────────
        contentCards = new CardLayout();
        contentPanel = new JPanel(contentCards);
        contentPanel.setBackground(AppTheme.BG_DARK);

        contentPanel.add(buildDashboard(),  "DASH");
        contentPanel.add(buildMarket(),     "MARKET");
        contentPanel.add(buildBuyPanel(),   "BUY");
        contentPanel.add(buildSellPanel(),  "SELL");
        contentPanel.add(buildPortfolio(),  "PORT");
        contentPanel.add(buildHistory(),    "HIST");

        add(contentPanel, BorderLayout.CENTER);
        refreshAll();
    }

    // ============================================================
    //  DASHBOARD
    // ============================================================
    private JPanel buildDashboard() {
        JPanel p = new JPanel(new BorderLayout(0, 20));
        p.setBackground(AppTheme.BG_DARK);
        p.setBorder(BorderFactory.createEmptyBorder(24, 28, 24, 28));

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(AppTheme.BG_DARK);
        header.add(AppTheme.createLabel("Dashboard",
                AppTheme.FONT_TITLE, AppTheme.TEXT), BorderLayout.WEST);
        JLabel ver = AppTheme.createLabel("v8.0  |  All Phases Active  ",
                AppTheme.FONT_SMALL, AppTheme.TEXT_DIM);
        header.add(ver, BorderLayout.EAST);
        p.add(header, BorderLayout.NORTH);

        // Cards row
        JPanel cards = new JPanel(new GridLayout(1, 5, 14, 0));
        cards.setBackground(AppTheme.BG_DARK);

        lblBalance  = new JLabel("Rs. 0");   lblBalance.setFont(AppTheme.FONT_BIG_NUM);
        lblNetWorth = new JLabel("Rs. 0");   lblNetWorth.setFont(AppTheme.FONT_BIG_NUM);
        lblStockVal = new JLabel("Rs. 0");   lblStockVal.setFont(AppTheme.FONT_BIG_NUM);
        lblPL       = new JLabel("Rs. 0");   lblPL.setFont(AppTheme.FONT_BIG_NUM);
        lblTrades   = new JLabel("0");       lblTrades.setFont(AppTheme.FONT_BIG_NUM);

        cards.add(makeCard("💰 Cash Balance", lblBalance,  AppTheme.PRIMARY));
        cards.add(makeCard("💎 Net Worth",    lblNetWorth, AppTheme.ACCENT));
        cards.add(makeCard("📈 Stock Value",  lblStockVal, AppTheme.WARNING));
        cards.add(makeCard("📉 Unrealized P/L", lblPL,    AppTheme.PRIMARY));
        cards.add(makeCard("📋 Total Trades", lblTrades,   AppTheme.TEXT));

        p.add(cards, BorderLayout.CENTER);

        // Quick actions
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        actions.setBackground(AppTheme.BG_DARK);
        JButton qBuy  = AppTheme.createButton("🟢  Buy Stock",  AppTheme.PRIMARY_DK);
        JButton qSell = AppTheme.createButton("🔴  Sell Stock", AppTheme.DANGER);
        JButton qPort = AppTheme.createButton("📁  Portfolio",  AppTheme.ACCENT);
        qBuy.addActionListener(e  -> { contentCards.show(contentPanel,"BUY");  refreshAll(); });
        qSell.addActionListener(e -> { contentCards.show(contentPanel,"SELL"); refreshAll(); });
        qPort.addActionListener(e -> { contentCards.show(contentPanel,"PORT"); refreshAll(); });
        actions.add(qBuy); actions.add(qSell); actions.add(qPort);
        p.add(actions, BorderLayout.SOUTH);

        return p;
    }

    private JPanel makeCard(String title, JLabel valueLabel, Color accent) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(AppTheme.BG_CARD);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(AppTheme.BORDER, 1),
            BorderFactory.createEmptyBorder(16, 18, 16, 18)));
        JLabel t = AppTheme.createLabel(title, AppTheme.FONT_SMALL, AppTheme.TEXT_MUTED);
        t.setAlignmentX(Component.LEFT_ALIGNMENT);
        valueLabel.setForeground(accent);
        valueLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(t); card.add(Box.createVerticalStrut(8)); card.add(valueLabel);
        return card;
    }

    // ============================================================
    //  MARKET DATA
    // ============================================================
    private JPanel buildMarket() {
        JPanel p = new JPanel(new BorderLayout(0, 14));
        p.setBackground(AppTheme.BG_DARK);
        p.setBorder(BorderFactory.createEmptyBorder(24, 28, 24, 28));
        p.add(AppTheme.createLabel("📈  Live Market Data",
                AppTheme.FONT_TITLE, AppTheme.TEXT), BorderLayout.NORTH);

        String[] cols = {"Symbol","Company","Price (Rs.)","Sector","Day High","Day Low","Change %","Volume"};
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        for (Stock s : stockMarket.getStockList()) {
            String chg = (s.getChangePercent() >= 0 ? "▲ +" : "▼ ") +
                    String.format("%.2f%%", Math.abs(s.getChangePercent()));
            model.addRow(new Object[]{
                s.getSymbol(), s.getCompanyName(),
                String.format("%.2f", s.getPrice()), s.getSector(),
                String.format("%.2f", s.getDayHigh()),
                String.format("%.2f", s.getDayLow()), chg,
                String.format("%,d", s.getVolume())
            });
        }
        JTable table = new JTable(model);
        AppTheme.styleTable(table);
        p.add(AppTheme.createScrollPane(table), BorderLayout.CENTER);
        return p;
    }

    // ============================================================
    //  BUY STOCK
    // ============================================================
    private JPanel buildBuyPanel() {
        JPanel p = new JPanel(new BorderLayout(0, 14));
        p.setBackground(AppTheme.BG_DARK);
        p.setBorder(BorderFactory.createEmptyBorder(24, 28, 24, 28));
        p.add(AppTheme.createLabel("🟢  Buy Stock",
                AppTheme.FONT_TITLE, AppTheme.PRIMARY), BorderLayout.NORTH);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(AppTheme.BG_CARD);
        form.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(AppTheme.BORDER),
            BorderFactory.createEmptyBorder(24, 28, 24, 28)));
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(6, 8, 6, 8);
        g.anchor = GridBagConstraints.WEST;

        // Symbol dropdown
        g.gridx=0; g.gridy=0;
        form.add(AppTheme.createLabel("Stock Symbol:", AppTheme.FONT_SUB, AppTheme.TEXT), g);
        g.gridx=1;
        buySymbol = new JComboBox<>();
        for (Stock s : stockMarket.getStockList())
            buySymbol.addItem(s.getSymbol() + " - " + s.getCompanyName());
        buySymbol.setFont(AppTheme.FONT_BODY);
        buySymbol.setPreferredSize(new Dimension(300, 36));
        buySymbol.addActionListener(e -> updateBuyInfo());
        form.add(buySymbol, g);

        // Quantity
        g.gridx=0; g.gridy=1;
        form.add(AppTheme.createLabel("Quantity:", AppTheme.FONT_SUB, AppTheme.TEXT), g);
        g.gridx=1;
        buyQty = AppTheme.createTextField("Enter quantity");
        buyQty.setPreferredSize(new Dimension(300, 36));
        buyQty.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent e) { updateBuyInfo(); }
        });
        form.add(buyQty, g);

        // Info label
        g.gridx=0; g.gridy=2; g.gridwidth=2;
        buyInfo = AppTheme.createLabel(" ", AppTheme.FONT_BODY, AppTheme.TEXT_MUTED);
        form.add(buyInfo, g);

        // Buy button
        g.gridy=3; g.gridwidth=2;
        JButton btn = AppTheme.createButton("Confirm Purchase", AppTheme.PRIMARY_DK);
        btn.setPreferredSize(new Dimension(300, 42));
        btn.addActionListener(e -> doBuy());
        form.add(btn, g);

        // Result
        g.gridy=4;
        buyResult = AppTheme.createLabel(" ", AppTheme.FONT_SUB, AppTheme.PRIMARY);
        form.add(buyResult, g);

        p.add(form, BorderLayout.CENTER);
        return p;
    }

    private void updateBuyInfo() {
        try {
            int idx = buySymbol.getSelectedIndex();
            Stock s = stockMarket.getStockList().get(idx);
            String qText = buyQty.getText().trim();
            if (qText.isEmpty()) {
                buyInfo.setText(String.format("Price: Rs. %.2f  |  Balance: Rs. %.2f",
                        s.getPrice(), currentUser.getBalance()));
            } else {
                int q = Integer.parseInt(qText);
                double cost = s.getPrice() * q;
                buyInfo.setText(String.format("Total: Rs. %.2f  |  Balance after: Rs. %.2f",
                        cost, currentUser.getBalance() - cost));
                buyInfo.setForeground(cost <= currentUser.getBalance() ?
                        AppTheme.TEXT_MUTED : AppTheme.DANGER);
            }
        } catch (Exception ex) {
            buyInfo.setText("Enter a valid number.");
            buyInfo.setForeground(AppTheme.DANGER);
        }
    }

    private void doBuy() {
        try {
            int idx = buySymbol.getSelectedIndex();
            Stock stock = stockMarket.getStockList().get(idx);
            int qty = Integer.parseInt(buyQty.getText().trim());
            if (qty <= 0) throw new NumberFormatException();

            double cost = stock.getPrice() * qty;
            if (cost > currentUser.getBalance())
                throw new InsufficientBalanceException(cost, currentUser.getBalance());

            int confirm = JOptionPane.showConfirmDialog(this,
                String.format("Buy %d share(s) of %s for Rs. %.2f?",
                        qty, stock.getSymbol(), cost),
                "Confirm Purchase", JOptionPane.YES_NO_OPTION);

            if (confirm != JOptionPane.YES_OPTION) return;

            // Execute via backend (use reflection-safe approach)
            currentUser.executeBuy(stock, qty);

            buyResult.setText(String.format("✔ Bought %d x %s for Rs. %.2f",
                    qty, stock.getSymbol(), cost));
            buyResult.setForeground(AppTheme.PRIMARY);
            buyQty.setText("");
            refreshAll();

        } catch (NumberFormatException ex) {
            buyResult.setText("Enter a valid positive quantity.");
            buyResult.setForeground(AppTheme.DANGER);
        } catch (InsufficientBalanceException ex) {
            buyResult.setText(ex.getMessage());
            buyResult.setForeground(AppTheme.DANGER);
        }
    }

    // ============================================================
    //  SELL STOCK
    // ============================================================
    private JPanel buildSellPanel() {
        JPanel p = new JPanel(new BorderLayout(0, 14));
        p.setBackground(AppTheme.BG_DARK);
        p.setBorder(BorderFactory.createEmptyBorder(24, 28, 24, 28));
        p.add(AppTheme.createLabel("🔴  Sell Stock",
                AppTheme.FONT_TITLE, AppTheme.DANGER), BorderLayout.NORTH);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(AppTheme.BG_CARD);
        form.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(AppTheme.BORDER),
            BorderFactory.createEmptyBorder(24, 28, 24, 28)));
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(6, 8, 6, 8);
        g.anchor = GridBagConstraints.WEST;

        g.gridx=0; g.gridy=0;
        form.add(AppTheme.createLabel("Stock Symbol:", AppTheme.FONT_SUB, AppTheme.TEXT), g);
        g.gridx=1;
        sellSymbol = new JComboBox<>();
        for (Stock s : stockMarket.getStockList())
            sellSymbol.addItem(s.getSymbol() + " - " + s.getCompanyName());
        sellSymbol.setFont(AppTheme.FONT_BODY);
        sellSymbol.setPreferredSize(new Dimension(300, 36));
        sellSymbol.addActionListener(e -> updateSellInfo());
        form.add(sellSymbol, g);

        g.gridx=0; g.gridy=1;
        form.add(AppTheme.createLabel("Quantity:", AppTheme.FONT_SUB, AppTheme.TEXT), g);
        g.gridx=1;
        sellQty = AppTheme.createTextField("Enter quantity");
        sellQty.setPreferredSize(new Dimension(300, 36));
        sellQty.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent e) { updateSellInfo(); }
        });
        form.add(sellQty, g);

        g.gridx=0; g.gridy=2; g.gridwidth=2;
        sellInfo = AppTheme.createLabel(" ", AppTheme.FONT_BODY, AppTheme.TEXT_MUTED);
        form.add(sellInfo, g);

        g.gridy=3;
        JButton btn = AppTheme.createButton("Confirm Sale", AppTheme.DANGER);
        btn.setPreferredSize(new Dimension(300, 42));
        btn.addActionListener(e -> doSell());
        form.add(btn, g);

        g.gridy=4;
        sellResult = AppTheme.createLabel(" ", AppTheme.FONT_SUB, AppTheme.PRIMARY);
        form.add(sellResult, g);

        p.add(form, BorderLayout.CENTER);
        return p;
    }

    private void updateSellInfo() {
        try {
            int idx = sellSymbol.getSelectedIndex();
            Stock s = stockMarket.getStockList().get(idx);
            int owned = currentUser.getPortfolio().getQuantity(s.getSymbol());
            double avg = currentUser.getPortfolio().getAverageBuyPrice(s.getSymbol());
            sellInfo.setText(String.format("Owned: %d  |  Avg Cost: Rs. %.2f  |  Current: Rs. %.2f",
                    owned, avg, s.getPrice()));
            sellInfo.setForeground(AppTheme.TEXT_MUTED);
        } catch (Exception ex) { /* ignore */ }
    }

    private void doSell() {
        try {
            int idx = sellSymbol.getSelectedIndex();
            Stock stock = stockMarket.getStockList().get(idx);
            int qty = Integer.parseInt(sellQty.getText().trim());
            if (qty <= 0) throw new NumberFormatException();

            int owned = currentUser.getPortfolio().getQuantity(stock.getSymbol());
            if (qty > owned)
                throw new InsufficientStocksException(stock.getSymbol(), qty, owned);

            double avg = currentUser.getPortfolio().getAverageBuyPrice(stock.getSymbol());
            double saleAmt = stock.getPrice() * qty;
            double pl = saleAmt - (avg * qty);
            String plText = pl >= 0 ? String.format("Profit: Rs. %.2f", pl)
                                    : String.format("Loss: Rs. %.2f", Math.abs(pl));

            int confirm = JOptionPane.showConfirmDialog(this,
                String.format("Sell %d x %s for Rs. %.2f?\n%s",
                        qty, stock.getSymbol(), saleAmt, plText),
                "Confirm Sale", JOptionPane.YES_NO_OPTION);

            if (confirm != JOptionPane.YES_OPTION) return;

            currentUser.executeSell(stock, qty);

            sellResult.setText(String.format("✔ Sold %d x %s  |  %s",
                    qty, stock.getSymbol(), plText));
            sellResult.setForeground(pl >= 0 ? AppTheme.PRIMARY : AppTheme.DANGER);
            sellQty.setText("");
            refreshAll();

        } catch (NumberFormatException ex) {
            sellResult.setText("Enter a valid positive quantity.");
            sellResult.setForeground(AppTheme.DANGER);
        } catch (InsufficientStocksException ex) {
            sellResult.setText(ex.getMessage());
            sellResult.setForeground(AppTheme.DANGER);
        }
    }

    // ============================================================
    //  PORTFOLIO
    // ============================================================
    private JPanel buildPortfolio() {
        JPanel p = new JPanel(new BorderLayout(0, 14));
        p.setBackground(AppTheme.BG_DARK);
        p.setBorder(BorderFactory.createEmptyBorder(24, 28, 24, 28));
        p.add(AppTheme.createLabel("📁  My Portfolio",
                AppTheme.FONT_TITLE, AppTheme.TEXT), BorderLayout.NORTH);

        String[] cols = {"Symbol","Company","Qty","Avg Price","Invested","Curr Value","P/L","Status"};
        portfolioModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = new JTable(portfolioModel);
        AppTheme.styleTable(table);
        p.add(AppTheme.createScrollPane(table), BorderLayout.CENTER);
        return p;
    }

    // ============================================================
    //  TRANSACTION HISTORY
    // ============================================================
    private JPanel buildHistory() {
        JPanel p = new JPanel(new BorderLayout(0, 14));
        p.setBackground(AppTheme.BG_DARK);
        p.setBorder(BorderFactory.createEmptyBorder(24, 28, 24, 28));
        p.add(AppTheme.createLabel("📋  Transaction History",
                AppTheme.FONT_TITLE, AppTheme.TEXT), BorderLayout.NORTH);

        String[] cols = {"TXN#","Date & Time","Type","Symbol","Company","Qty","Price","Total","P/L"};
        historyModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = new JTable(historyModel);
        AppTheme.styleTable(table);
        p.add(AppTheme.createScrollPane(table), BorderLayout.CENTER);
        return p;
    }

    // ============================================================
    //  REFRESH ALL DATA
    // ============================================================
    public void refreshAll() {
        Portfolio port = currentUser.getPortfolio();
        ArrayList<Stock> stocks = stockMarket.getStockList();
        double bal     = currentUser.getBalance();
        double stockV  = port.getTotalStockValue(stocks);
        double invest  = port.getTotalInvestment(stocks);
        double unrealPL = stockV - invest;

        // Dashboard cards
        lblBalance.setText(String.format("Rs. %.0f", bal));
        lblNetWorth.setText(String.format("Rs. %.0f", bal + stockV));
        lblStockVal.setText(String.format("Rs. %.0f", stockV));
        lblPL.setText(String.format("%s Rs. %.0f",
                unrealPL >= 0 ? "▲" : "▼", Math.abs(unrealPL)));
        lblPL.setForeground(unrealPL >= 0 ? AppTheme.PRIMARY : AppTheme.DANGER);
        lblTrades.setText(String.valueOf(currentUser.getTransactionCount()));

        // Portfolio table
        portfolioModel.setRowCount(0);
        for (Stock s : stocks) {
            int qty = port.getQuantity(s.getSymbol());
            if (qty > 0) {
                double avg  = port.getAverageBuyPrice(s.getSymbol());
                double inv  = port.getInvested(s.getSymbol());
                double curr = qty * s.getPrice();
                double pl   = curr - inv;
                portfolioModel.addRow(new Object[]{
                    s.getSymbol(), s.getCompanyName(), qty,
                    String.format("Rs. %.2f", avg),
                    String.format("Rs. %.2f", inv),
                    String.format("Rs. %.2f", curr),
                    String.format("Rs. %.2f", Math.abs(pl)),
                    pl >= 0 ? "▲ PROFIT" : "▼ LOSS"
                });
            }
        }

        // History table (latest first)
        historyModel.setRowCount(0);
        ArrayList<Transaction> hist = currentUser.getHistory();
        java.time.format.DateTimeFormatter fmt =
            java.time.format.DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        for (int i = hist.size()-1; i >= 0; i--) {
            Transaction t = hist.get(i);
            String pl = t.getType().equals("SELL") ?
                String.format("Rs. %.2f %s", Math.abs(t.getProfitLoss()),
                    t.getProfitLoss() >= 0 ? "▲" : "▼") : "—";
            historyModel.addRow(new Object[]{
                "#" + t.getTransactionId(),
                t.getTimestamp().format(fmt),
                t.getType(), t.getStockSymbol(), t.getStockName(),
                t.getQuantity(),
                String.format("Rs. %.2f", t.getPricePerShare()),
                String.format("Rs. %.2f", t.getAmount()), pl
            });
        }

        // Buy/Sell info refresh
        updateBuyInfo();
        updateSellInfo();
    }
}
