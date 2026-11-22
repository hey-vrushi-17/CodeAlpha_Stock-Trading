// ============================================================
//  AppTheme.java  –  Phase 8: GUI Theme & Styled Components
//  Professional dark trading theme with helper methods.
// ============================================================

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;

public class AppTheme {

    // ── Colors ────────────────────────────────────────────────
    public static final Color BG_DARK     = new Color(10, 15, 30);
    public static final Color BG_CARD     = new Color(17, 24, 39);
    public static final Color BG_SIDEBAR  = new Color(13, 17, 23);
    public static final Color BG_INPUT    = new Color(30, 41, 59);
    public static final Color BG_HOVER    = new Color(30, 58, 95);

    public static final Color PRIMARY     = new Color(34, 197, 94);    // green
    public static final Color PRIMARY_DK  = new Color(22, 163, 74);
    public static final Color DANGER      = new Color(239, 68, 68);    // red
    public static final Color ACCENT      = new Color(59, 130, 246);   // blue
    public static final Color WARNING     = new Color(234, 179, 8);    // yellow

    public static final Color TEXT        = new Color(226, 232, 240);
    public static final Color TEXT_MUTED  = new Color(148, 163, 184);
    public static final Color TEXT_DIM    = new Color(100, 116, 139);
    public static final Color BORDER      = new Color(30, 41, 59);

    // ── Fonts ─────────────────────────────────────────────────
    public static final Font FONT_TITLE   = new Font("Segoe UI", Font.BOLD, 28);
    public static final Font FONT_HEADING = new Font("Segoe UI", Font.BOLD, 18);
    public static final Font FONT_SUB     = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font FONT_BODY    = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FONT_SMALL   = new Font("Segoe UI", Font.PLAIN, 11);
    public static final Font FONT_MONO    = new Font("Consolas", Font.PLAIN, 13);
    public static final Font FONT_BTN     = new Font("Segoe UI", Font.BOLD, 13);
    public static final Font FONT_NAV     = new Font("Segoe UI", Font.BOLD, 14);
    public static final Font FONT_BIG_NUM = new Font("Segoe UI", Font.BOLD, 24);

    // ── Styled JButton ────────────────────────────────────────
    public static JButton createButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setFont(FONT_BTN);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(160, 38));
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            Color orig = bg;
            public void mouseEntered(java.awt.event.MouseEvent e) {
                btn.setBackground(orig.brighter());
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                btn.setBackground(orig);
            }
        });
        return btn;
    }

    // ── Nav Button (sidebar) ──────────────────────────────────
    public static JButton createNavButton(String text, String icon) {
        JButton btn = new JButton(icon + "  " + text);
        btn.setFont(FONT_NAV);
        btn.setBackground(BG_SIDEBAR);
        btn.setForeground(TEXT_MUTED);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setMaximumSize(new Dimension(220, 44));
        btn.setPreferredSize(new Dimension(220, 44));
        btn.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 10));
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                btn.setBackground(BG_HOVER);
                btn.setForeground(PRIMARY);
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                btn.setBackground(BG_SIDEBAR);
                btn.setForeground(TEXT_MUTED);
            }
        });
        return btn;
    }

    // ── Styled JTextField ─────────────────────────────────────
    public static JTextField createTextField(String placeholder) {
        JTextField tf = new JTextField();
        tf.setFont(FONT_BODY);
        tf.setBackground(BG_INPUT);
        tf.setForeground(TEXT);
        tf.setCaretColor(PRIMARY);
        tf.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER, 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)));
        tf.setPreferredSize(new Dimension(280, 38));
        tf.setToolTipText(placeholder);
        return tf;
    }

    // ── Styled JPasswordField ─────────────────────────────────
    public static JPasswordField createPasswordField() {
        JPasswordField pf = new JPasswordField();
        pf.setFont(FONT_BODY);
        pf.setBackground(BG_INPUT);
        pf.setForeground(TEXT);
        pf.setCaretColor(PRIMARY);
        pf.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER, 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)));
        pf.setPreferredSize(new Dimension(280, 38));
        return pf;
    }

    // ── Styled JLabel ─────────────────────────────────────────
    public static JLabel createLabel(String text, Font font, Color color) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(font);
        lbl.setForeground(color);
        return lbl;
    }

    // ── Info Card Panel ───────────────────────────────────────
    public static JPanel createCard(String title, String value, Color accent) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(BG_CARD);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER, 1),
            BorderFactory.createEmptyBorder(16, 20, 16, 20)));
        card.setPreferredSize(new Dimension(200, 90));

        JLabel t = createLabel(title, FONT_SMALL, TEXT_MUTED);
        JLabel v = createLabel(value, FONT_BIG_NUM, accent);
        t.setAlignmentX(Component.LEFT_ALIGNMENT);
        v.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(t);
        card.add(Box.createVerticalStrut(6));
        card.add(v);
        return card;
    }

    // ── Styled JTable ─────────────────────────────────────────
    public static void styleTable(JTable table) {
        table.setFont(FONT_MONO);
        table.setBackground(BG_CARD);
        table.setForeground(TEXT);
        table.setGridColor(BORDER);
        table.setSelectionBackground(BG_HOVER);
        table.setSelectionForeground(PRIMARY);
        table.setRowHeight(32);
        table.setShowHorizontalLines(true);
        table.setShowVerticalLines(false);
        table.setIntercellSpacing(new Dimension(0, 1));
        table.setFillsViewportHeight(true);

        JTableHeader header = table.getTableHeader();
        header.setFont(FONT_BTN);
        header.setBackground(new Color(15, 23, 42));
        header.setForeground(PRIMARY);
        header.setBorder(BorderFactory.createMatteBorder(0,0,2,0, PRIMARY_DK));
        header.setReorderingAllowed(false);

        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++)
            table.getColumnModel().getColumn(i).setCellRenderer(center);
    }

    // ── Styled ScrollPane ─────────────────────────────────────
    public static JScrollPane createScrollPane(JTable table) {
        JScrollPane sp = new JScrollPane(table);
        sp.setBackground(BG_CARD);
        sp.getViewport().setBackground(BG_CARD);
        sp.setBorder(BorderFactory.createLineBorder(BORDER, 1));
        return sp;
    }
}
