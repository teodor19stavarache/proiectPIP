package pck1;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class DetailPage implements AppColors {

    public static JPanel build(String titlu, String locatie, String descriere,
                                String rating, String nrRec, String pret,
                                AppNavigator nav) {
        JPanel page = new JPanel(new BorderLayout());
        page.setBackground(WARM_BG);

        // Header cu buton Inapoi
        JPanel header = new JPanel(new BorderLayout(12, 0));
        header.setBackground(GREEN_PRIMARY);
        header.setBorder(new EmptyBorder(10, 16, 10, 16));

        JButton btnBack = new JButton("< Inapoi");
        btnBack.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnBack.setForeground(ALB);
        btnBack.setBackground(GREEN_DARK);
        btnBack.setBorder(new EmptyBorder(6, 14, 6, 14));
        btnBack.setFocusPainted(false);
        btnBack.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnBack.addActionListener(e -> nav.showPage("home"));
        header.add(btnBack, BorderLayout.WEST);

        JLabel lblH = new JLabel(titlu);
        lblH.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblH.setForeground(ALB);
        header.add(lblH, BorderLayout.CENTER);
        page.add(header, BorderLayout.NORTH);

        // Continut
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(WARM_BG);
        content.setBorder(new EmptyBorder(22, 32, 22, 32));

        // Imagine placeholder
        JPanel imgMare = new JPanel(new GridBagLayout());
        imgMare.setBackground(new Color(180, 230, 180));
        imgMare.setMaximumSize(new Dimension(Integer.MAX_VALUE, 240));
        imgMare.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel imgL = new JLabel("Galerie foto - " + titlu, SwingConstants.CENTER);
        imgL.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        imgL.setForeground(GREEN_DARK);
        imgMare.add(imgL);
        content.add(imgMare);
        content.add(Box.createVerticalStrut(22));

        JLabel lblT = new JLabel(titlu);
        lblT.setFont(new Font("Segoe UI", Font.BOLD, 23));
        lblT.setForeground(new Color(20, 20, 20));
        lblT.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(lblT);
        content.add(Box.createVerticalStrut(4));

        JLabel lblL = new JLabel("  " + locatie);
        lblL.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblL.setForeground(new Color(0, 100, 180));
        lblL.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(lblL);
        content.add(Box.createVerticalStrut(4));

        int stele = (int) Math.round(Double.parseDouble(rating));
        JLabel lblR = new JLabel("★".repeat(stele) + "☆".repeat(5 - stele)
            + "  " + rating + " din 5  ·  " + nrRec + " recenzii");
        lblR.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblR.setForeground(ORANGE_STAR);
        lblR.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(lblR);
        content.add(Box.createVerticalStrut(22));

        JLabel secDesc = new JLabel("Despre proprietate");
        secDesc.setFont(new Font("Segoe UI", Font.BOLD, 15));
        secDesc.setForeground(new Color(20, 20, 20));
        secDesc.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(secDesc);
        content.add(Box.createVerticalStrut(8));

        JLabel lblDesc = new JLabel(
            "<html><body style='width:680px;font-size:12pt;color:#3a3a3a'>"
            + descriere
            + " Camerele sunt dotate cu aer conditionat, TV si Wi-Fi gratuit."
            + " Personalul nostru este disponibil non-stop.</body></html>");
        lblDesc.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(lblDesc);
        content.add(Box.createVerticalStrut(22));

        JLabel secFac = new JLabel("Facilitati incluse");
        secFac.setFont(new Font("Segoe UI", Font.BOLD, 15));
        secFac.setForeground(new Color(20, 20, 20));
        secFac.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(secFac);
        content.add(Box.createVerticalStrut(10));

        JPanel facPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        facPanel.setBackground(WARM_BG);
        facPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        for (String f : new String[]{"Wi-Fi gratuit", "Mic dejun", "Parcare", "AC", "Room service", "Receptie 24h"}) {
            JLabel c = new JLabel("  " + f + "  ");
            c.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            c.setForeground(GREEN_DARK);
            c.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_C, 1),
                new EmptyBorder(4, 6, 4, 6)
            ));
            c.setBackground(new Color(240, 255, 240));
            c.setOpaque(true);
            facPanel.add(c);
        }
        content.add(facPanel);
        content.add(Box.createVerticalStrut(26));

        JSeparator sep = new JSeparator();
        sep.setForeground(BORDER_C);
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        sep.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(sep);
        content.add(Box.createVerticalStrut(22));

        // Sectiunea de rezervare
        JLabel secRez = new JLabel("Rezerva acum");
        secRez.setFont(new Font("Segoe UI", Font.BOLD, 15));
        secRez.setForeground(new Color(20, 20, 20));
        secRez.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(secRez);
        content.add(Box.createVerticalStrut(14));

        JPanel rezervRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 14, 0));
        rezervRow.setBackground(WARM_BG);
        rezervRow.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel pretCard = new JPanel(new BorderLayout(0, 4));
        pretCard.setBackground(ALB);
        pretCard.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_C, 1),
            new EmptyBorder(14, 20, 14, 20)
        ));
        JLabel lblPretMare = new JLabel(pret + "€");
        lblPretMare.setFont(new Font("Segoe UI", Font.BOLD, 30));
        lblPretMare.setForeground(ORANGE_PRICE);
        JLabel lblPN = new JLabel("per noapte, taxe incluse");
        lblPN.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblPN.setForeground(GRI_TEXT);
        pretCard.add(lblPretMare, BorderLayout.CENTER);
        pretCard.add(lblPN,       BorderLayout.SOUTH);
        rezervRow.add(pretCard);

        JButton btnRezMare = new JButton("  Rezerva acum  >");
        btnRezMare.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnRezMare.setBackground(GREEN_PRIMARY);
        btnRezMare.setForeground(ALB);
        btnRezMare.setBorder(new EmptyBorder(16, 30, 16, 30));
        btnRezMare.setFocusPainted(false);
        btnRezMare.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnRezMare.addActionListener(e ->
            JOptionPane.showMessageDialog(page,
                "Rezervare initiata!\n\n" + titlu + "\n" + locatie + "\nPret: " + pret + "€ / noapte",
                "TravaleRo - Rezervare", JOptionPane.INFORMATION_MESSAGE));
        rezervRow.add(btnRezMare);
        content.add(rezervRow);
        content.add(Box.createVerticalStrut(36));

        JScrollPane scroll = new JScrollPane(content);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(WARM_BG);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        page.add(scroll, BorderLayout.CENTER);
        return page;
    }
}
