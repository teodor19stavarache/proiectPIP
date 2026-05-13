package pck1;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

public class UIHelpers implements AppColors {

    // Card listing reutilizabil - folosit in HomePage si SearchPage
    public static JPanel createListingCard(String titlu, String locatie, String descriere,
                                           String rating, String nrRec, String pret,
                                           boolean topAles, Color imgColor, AppNavigator nav) {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(BG_PAGE);
        wrapper.setMaximumSize(new Dimension(Integer.MAX_VALUE, 162));

        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(ALB);
        card.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 1));

        // Zona imagine
        JPanel imgZone = new JPanel(new BorderLayout());
        imgZone.setPreferredSize(new Dimension(160, 162));
        imgZone.setBackground(imgColor);

        JLabel imgIcon = new JLabel("🏨", SwingConstants.CENTER);
        imgIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 36));
        imgIcon.setForeground(new Color(255, 255, 255, 140));
        imgZone.add(imgIcon, BorderLayout.CENTER);

        JLabel badge = new JLabel(topAles ? "  ★ Top ales  " : "  Proprietate  ",
                                  SwingConstants.CENTER);
        badge.setFont(new Font("Segoe UI", Font.BOLD, 10));
        badge.setForeground(ALB);
        badge.setBackground(topAles ? GREEN_DARK : new Color(0, 0, 0, 80));
        badge.setOpaque(true);
        badge.setPreferredSize(new Dimension(160, 22));
        imgZone.add(badge, BorderLayout.NORTH);
        card.add(imgZone, BorderLayout.WEST);

        // Info centru
        JPanel info = new JPanel();
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
        info.setBackground(ALB);
        info.setBorder(new EmptyBorder(14, 16, 14, 10));

        JLabel lblT = new JLabel(titlu);
        lblT.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblT.setForeground(new Color(15, 15, 15));
        lblT.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblL = new JLabel("📍 " + locatie);
        lblL.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblL.setForeground(new Color(0, 100, 180));
        lblL.setAlignmentX(Component.LEFT_ALIGNMENT);

        int stele = (int) Math.round(Double.parseDouble(rating));
        JPanel ratingRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        ratingRow.setBackground(ALB);
        ratingRow.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel scoreBox = new JLabel(" " + rating + " ");
        scoreBox.setFont(new Font("Segoe UI", Font.BOLD, 12));
        scoreBox.setForeground(ALB);
        scoreBox.setBackground(GREEN_DARK);
        scoreBox.setOpaque(true);

        JLabel steleLabel = new JLabel("★".repeat(stele) + "☆".repeat(5 - stele));
        steleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        steleLabel.setForeground(ORANGE_STAR);

        JLabel recLabel = new JLabel("· " + nrRec + " recenzii");
        recLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        recLabel.setForeground(GRI_TEXT);

        ratingRow.add(scoreBox);
        ratingRow.add(steleLabel);
        ratingRow.add(recLabel);

        JPanel tagsRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        tagsRow.setBackground(ALB);
        tagsRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        String[] tags = topAles ? new String[]{"Wi-Fi", "Mic dejun", "Spa"} : new String[]{"Wi-Fi", "Parcare"};
        for (String tag : tags) {
            JLabel t = new JLabel(tag);
            t.setFont(new Font("Segoe UI", Font.PLAIN, 10));
            t.setForeground(new Color(60, 60, 60));
            t.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(210, 210, 210), 1),
                new EmptyBorder(2, 7, 2, 7)
            ));
            t.setBackground(new Color(248, 248, 248));
            t.setOpaque(true);
            tagsRow.add(t);
        }

        JLabel lblD = new JLabel(
            "<html><body style='width:300px;color:#6b6b6b'>" + descriere + "</body></html>");
        lblD.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblD.setAlignmentX(Component.LEFT_ALIGNMENT);

        info.add(lblT);
        info.add(Box.createVerticalStrut(3));
        info.add(lblL);
        info.add(Box.createVerticalStrut(5));
        info.add(ratingRow);
        info.add(Box.createVerticalStrut(5));
        info.add(tagsRow);
        info.add(Box.createVerticalStrut(6));
        info.add(lblD);
        card.add(info, BorderLayout.CENTER);

        // Pret si buton dreapta
        JPanel right = new JPanel(new BorderLayout());
        right.setBackground(new Color(250, 252, 250));
        right.setBorder(BorderFactory.createCompoundBorder(
            new MatteBorder(0, 1, 0, 0, new Color(230, 230, 230)),
            new EmptyBorder(14, 16, 14, 18)
        ));

        JLabel lblDisp = new JLabel("Disponibil");
        lblDisp.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblDisp.setForeground(GREEN_PRIMARY);
        lblDisp.setHorizontalAlignment(SwingConstants.RIGHT);

        JPanel pretPanel = new JPanel();
        pretPanel.setLayout(new BoxLayout(pretPanel, BoxLayout.Y_AXIS));
        pretPanel.setBackground(new Color(250, 252, 250));

        JLabel lblP = new JLabel(pret + "€");
        lblP.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblP.setForeground(new Color(15, 15, 15));
        lblP.setAlignmentX(Component.RIGHT_ALIGNMENT);

        JLabel lblN = new JLabel("/ noapte");
        lblN.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblN.setForeground(GRI_TEXT);
        lblN.setAlignmentX(Component.RIGHT_ALIGNMENT);

        JLabel lblTaxe = new JLabel("Taxe incluse");
        lblTaxe.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        lblTaxe.setForeground(new Color(150, 150, 150));
        lblTaxe.setAlignmentX(Component.RIGHT_ALIGNMENT);

        pretPanel.add(Box.createVerticalGlue());
        pretPanel.add(lblP);
        pretPanel.add(lblN);
        pretPanel.add(Box.createVerticalStrut(2));
        pretPanel.add(lblTaxe);
        pretPanel.add(Box.createVerticalGlue());

        JButton btnRez = new JButton("Rezerva");
        btnRez.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnRez.setBackground(GREEN_PRIMARY);
        btnRez.setForeground(ALB);
        btnRez.setBorder(new EmptyBorder(9, 0, 9, 0));
        btnRez.setFocusPainted(false);
        btnRez.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnRez.setPreferredSize(new Dimension(120, 36));
        btnRez.addActionListener(e ->
            nav.navigateToDetail(titlu, locatie, descriere, rating, nrRec, pret));

        right.add(lblDisp,   BorderLayout.NORTH);
        right.add(pretPanel, BorderLayout.CENTER);
        right.add(btnRez,    BorderLayout.SOUTH);
        card.add(right, BorderLayout.EAST);

        wrapper.add(card, BorderLayout.CENTER);
        JPanel shadow = new JPanel();
        shadow.setBackground(new Color(210, 210, 210));
        shadow.setPreferredSize(new Dimension(0, 2));
        wrapper.add(shadow, BorderLayout.SOUTH);
        return wrapper;
    }

    // Header de sectiune colorat - folosit in SearchPage
    public static JPanel createSectionTitle(String text) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(GREEN_PRIMARY);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        panel.setBorder(new EmptyBorder(0, 14, 0, 0));

        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lbl.setForeground(ALB);
        panel.add(lbl, BorderLayout.CENTER);
        return panel;
    }
}
