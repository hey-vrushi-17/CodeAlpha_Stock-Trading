// ============================================================
//  AuthPanel.java  –  Phase 8: Login & Registration GUI
//  Tabbed panel with Login and Register forms.
// ============================================================

import java.awt.*;
import javax.swing.*;

public class AuthPanel extends JPanel {

    private StockTradingGUI mainFrame;

    // Login fields
    private JTextField     loginUser;
    private JPasswordField loginPass;
    private JLabel         loginMsg;

    // Register fields
    private JTextField     regUser, regEmail;
    private JPasswordField regPass;
    private JLabel         regMsg;

    public AuthPanel(StockTradingGUI mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout());
        setBackground(AppTheme.BG_DARK);
        buildUI();
    }

    private void buildUI() {
        // ── Main centered container ───────────────────────────
        JPanel center = new JPanel(new GridBagLayout());
        center.setBackground(AppTheme.BG_DARK);

        JPanel box = new JPanel();
        box.setLayout(new BoxLayout(box, BoxLayout.Y_AXIS));
        box.setBackground(AppTheme.BG_CARD);
        box.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(AppTheme.BORDER, 1),
            BorderFactory.createEmptyBorder(30, 40, 30, 40)));
        box.setPreferredSize(new Dimension(420, 520));

        // ── Title ─────────────────────────────────────────────
        JLabel title = AppTheme.createLabel("📈 Stock Trading Platform",
                AppTheme.FONT_TITLE, AppTheme.PRIMARY);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        box.add(title);
        box.add(Box.createVerticalStrut(6));

        JLabel sub = AppTheme.createLabel("Phase 8 — Final GUI Edition",
                AppTheme.FONT_SMALL, AppTheme.TEXT_MUTED);
        sub.setAlignmentX(Component.CENTER_ALIGNMENT);
        box.add(sub);
        box.add(Box.createVerticalStrut(20));

        // ── Tabs: Login / Register ────────────────────────────
        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(AppTheme.FONT_BTN);
        tabs.setBackground(AppTheme.BG_CARD);
        tabs.setForeground(AppTheme.TEXT);
        tabs.addTab("Login", buildLoginTab());
        tabs.addTab("Register", buildRegisterTab());
        box.add(tabs);

        center.add(box);
        add(center, BorderLayout.CENTER);

        // ── Bottom credit ─────────────────────────────────────
        JLabel credit = AppTheme.createLabel("  CodeAlpha Java Internship  |  github.com/hey-vrushrab",
                AppTheme.FONT_SMALL, AppTheme.TEXT_DIM);
        credit.setBorder(BorderFactory.createEmptyBorder(10,0,10,0));
        credit.setHorizontalAlignment(SwingConstants.CENTER);
        credit.setOpaque(true);
        credit.setBackground(AppTheme.BG_DARK);
        add(credit, BorderLayout.SOUTH);
    }

    // ── Login Tab ─────────────────────────────────────────────
    private JPanel buildLoginTab() {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBackground(AppTheme.BG_CARD);
        p.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));

        p.add(AppTheme.createLabel("Username", AppTheme.FONT_SMALL, AppTheme.TEXT_MUTED));
        p.add(Box.createVerticalStrut(4));
        loginUser = AppTheme.createTextField("Enter username");
        loginUser.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        p.add(loginUser);
        p.add(Box.createVerticalStrut(12));

        p.add(AppTheme.createLabel("Password", AppTheme.FONT_SMALL, AppTheme.TEXT_MUTED));
        p.add(Box.createVerticalStrut(4));
        loginPass = AppTheme.createPasswordField();
        loginPass.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        p.add(loginPass);
        p.add(Box.createVerticalStrut(16));

        JButton loginBtn = AppTheme.createButton("Login", AppTheme.PRIMARY);
        loginBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        loginBtn.addActionListener(e -> doLogin());
        p.add(loginBtn);
        p.add(Box.createVerticalStrut(10));

        loginMsg = AppTheme.createLabel(" ", AppTheme.FONT_SMALL, AppTheme.DANGER);
        loginMsg.setAlignmentX(Component.CENTER_ALIGNMENT);
        p.add(loginMsg);

        // Enter key triggers login
        loginPass.addActionListener(e -> doLogin());

        return p;
    }

    // ── Register Tab ──────────────────────────────────────────
    private JPanel buildRegisterTab() {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBackground(AppTheme.BG_CARD);
        p.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));

        p.add(AppTheme.createLabel("Username", AppTheme.FONT_SMALL, AppTheme.TEXT_MUTED));
        p.add(Box.createVerticalStrut(4));
        regUser = AppTheme.createTextField("3–20 chars");
        regUser.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        p.add(regUser);
        p.add(Box.createVerticalStrut(10));

        p.add(AppTheme.createLabel("Email", AppTheme.FONT_SMALL, AppTheme.TEXT_MUTED));
        p.add(Box.createVerticalStrut(4));
        regEmail = AppTheme.createTextField("you@email.com");
        regEmail.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        p.add(regEmail);
        p.add(Box.createVerticalStrut(10));

        p.add(AppTheme.createLabel("Password", AppTheme.FONT_SMALL, AppTheme.TEXT_MUTED));
        p.add(Box.createVerticalStrut(4));
        regPass = AppTheme.createPasswordField();
        regPass.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        p.add(regPass);
        p.add(Box.createVerticalStrut(14));

        JButton regBtn = AppTheme.createButton("Create Account", AppTheme.ACCENT);
        regBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        regBtn.addActionListener(e -> doRegister());
        p.add(regBtn);
        p.add(Box.createVerticalStrut(10));

        regMsg = AppTheme.createLabel(" ", AppTheme.FONT_SMALL, AppTheme.DANGER);
        regMsg.setAlignmentX(Component.CENTER_ALIGNMENT);
        p.add(regMsg);

        return p;
    }

    // ── Actions ───────────────────────────────────────────────
    private void doLogin() {
        String user = loginUser.getText().trim();
        String pass = new String(loginPass.getPassword());
        if (user.isEmpty() || pass.isEmpty()) {
            loginMsg.setText("Please fill in all fields.");
            loginMsg.setForeground(AppTheme.DANGER);
            return;
        }
        try {
            User u = mainFrame.getAuthSystem().login(user, pass);
            loginMsg.setText("✔ Login successful!");
            loginMsg.setForeground(AppTheme.PRIMARY);
            mainFrame.onLoginSuccess(u);
        } catch (InvalidCredentialsException ex) {
            loginMsg.setText("Invalid username or password.");
            loginMsg.setForeground(AppTheme.DANGER);
        }
    }

    private void doRegister() {
        String user  = regUser.getText().trim();
        String email = regEmail.getText().trim();
        String pass  = new String(regPass.getPassword());
        if (user.isEmpty() || email.isEmpty() || pass.isEmpty()) {
            regMsg.setText("Please fill in all fields.");
            regMsg.setForeground(AppTheme.DANGER);
            return;
        }
        try {
            mainFrame.getAuthSystem().register(user, email, pass, null);
            regMsg.setText("✔ Account created! Switch to Login tab.");
            regMsg.setForeground(AppTheme.PRIMARY);
            regUser.setText(""); regEmail.setText(""); regPass.setText("");
        } catch (DuplicateUserException | IllegalArgumentException ex) {
            regMsg.setText(ex.getMessage());
            regMsg.setForeground(AppTheme.DANGER);
        }
    }
}
