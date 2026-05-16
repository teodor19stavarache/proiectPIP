package pck1;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

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

        // Buton Profil (singura actiune din bara de sus)
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
