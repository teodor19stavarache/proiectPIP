package pck1;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

public class TopBar implements AppColors {

    public static JPanel build(AppNavigator nav) {
        JPanel top = new JPanel(new BorderLayout(16, 0));
        top.setBackground(GREEN_PRIMARY);
        top.setBorder(new EmptyBorder(10, 18, 10, 18));

        // Logo
        JLabel logo = new JLabel(
            "<html><span style='color:white;font-size:17pt;font-weight:bold;'>" +
            "Travale</span><span style='color:#FFCC00;font-size:17pt;font-weight:bold;'>" +
            "Ro</span><span style='color:#FFCC00;font-size:12pt;'> ✈</span></html>"
        );
        logo.setPreferredSize(new Dimension(175, 36));
        top.add(logo, BorderLayout.WEST);

        // Bara de cautare
        JPanel searchRow = new JPanel(new BorderLayout());
        searchRow.setBackground(ALB);
        searchRow.setBorder(BorderFactory.createLineBorder(BORDER_C, 1));

        JButton mapBtn = new JButton("   Pe harta");
        mapBtn.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        mapBtn.setForeground(GREEN_DARK);
        mapBtn.setBackground(new Color(245, 255, 245));
        mapBtn.setBorder(new MatteBorder(0, 0, 0, 1, BORDER_C));
        mapBtn.setFocusPainted(false);
        mapBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        mapBtn.setPreferredSize(new Dimension(115, 36));
        searchRow.add(mapBtn, BorderLayout.WEST);

        JTextField searchField = new JTextField("Cauta destinatie, hotel, atractie...");
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        searchField.setForeground(new Color(160, 160, 155));
        searchField.setBorder(new EmptyBorder(0, 12, 0, 12));
        searchField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent e) {
                if (searchField.getText().startsWith("Cauta")) {
                    searchField.setText("");
                    searchField.setForeground(new Color(20, 20, 20));
                }
            }
            public void focusLost(java.awt.event.FocusEvent e) {
                if (searchField.getText().isEmpty()) {
                    searchField.setText("Cauta destinatie, hotel, atractie...");
                    searchField.setForeground(new Color(160, 160, 155));
                }
            }
        });
        searchRow.add(searchField, BorderLayout.CENTER);

        JButton cautaBtn = new JButton("Cauta");
        cautaBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        cautaBtn.setBackground(GREEN_PRIMARY);
        cautaBtn.setForeground(ALB);
        cautaBtn.setBorder(new EmptyBorder(0, 18, 0, 18));
        cautaBtn.setFocusPainted(false);
        cautaBtn.setPreferredSize(new Dimension(85, 36));
        cautaBtn.addActionListener(e -> {
            nav.clearSidebarSelection();
            nav.showPage("search");
        });
        searchRow.add(cautaBtn, BorderLayout.EAST);
        top.add(searchRow, BorderLayout.CENTER);

        // Buton Profil
        JButton btnProfil = new JButton("  Profil");
        btnProfil.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnProfil.setForeground(new Color(20, 20, 20));
        btnProfil.setBackground(new Color(255, 204, 0));
        btnProfil.setBorder(new EmptyBorder(7, 16, 7, 16));
        btnProfil.setFocusPainted(false);
        btnProfil.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnProfil.addActionListener(e -> {
            nav.clearSidebarSelection();
            nav.showPage("profil");
        });
        top.add(btnProfil, BorderLayout.EAST);

        return top;
    }
}
