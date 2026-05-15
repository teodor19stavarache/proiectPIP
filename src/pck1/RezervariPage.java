package pck1;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class RezervariPage implements AppColors {

    public static JPanel build() {
        JPanel page = new JPanel(new BorderLayout());
        page.setBackground(WARM_BG);

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(GREEN_PRIMARY);
        header.setBorder(new EmptyBorder(18, 22, 18, 22));
        JLabel titlu = new JLabel("Rezervarile mele");
        titlu.setFont(new Font("Segoe UI", Font.BOLD, 21));
        titlu.setForeground(ALB);
        header.add(titlu);
        page.add(header, BorderLayout.NORTH);

        JPanel lista = new JPanel();
        lista.setLayout(new BoxLayout(lista, BoxLayout.Y_AXIS));
        lista.setBackground(WARM_BG);
        lista.setBorder(new EmptyBorder(16, 20, 16, 20));

        lista.add(createRezervareCard("Hotel Traian",          "Piata Unirii 1, Iasi",
            "15 Mai 2025", "18 Mai 2025", "3 nopti", "555€", "Confirmata"));
        lista.add(Box.createVerticalStrut(10));
        lista.add(createRezervareCard("Apartament Copou View", "Bd. Carol I 22, Iasi",
            "2 Iun 2025",  "5 Iun 2025",  "3 nopti", "180€", "Asteptare"));
        lista.add(Box.createVerticalStrut(10));
        lista.add(createRezervareCard("Casa Pogor Suites",     "Str. Vasile Pogor 4, Iasi",
            "10 Iul 2025", "12 Iul 2025", "2 nopti", "440€", "Confirmata"));

        JScrollPane scroll = new JScrollPane(lista);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(WARM_BG);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        page.add(scroll, BorderLayout.CENTER);
        return page;
    }

    private static JPanel createRezervareCard(String hotel, String adresa, String checkIn,
                                               String checkOut, String durata,
                                               String total, String status) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(ALB);
        card.setBorder(BorderFactory.createLineBorder(BORDER_C, 1));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 112));

        Color sColor = status.equals("Confirmata") ? GREEN_PRIMARY : ORANGE_PRICE;
        JPanel bara = new JPanel();
        bara.setBackground(sColor);
        bara.setPreferredSize(new Dimension(5, 0));
        card.add(bara, BorderLayout.WEST);

        JPanel info = new JPanel();
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
        info.setBackground(ALB);
        info.setBorder(new EmptyBorder(12, 14, 12, 8));

        JLabel lblH = new JLabel(hotel);
        lblH.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblH.setForeground(new Color(20, 20, 20));

        JLabel lblA = new JLabel("  " + adresa);
        lblA.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblA.setForeground(new Color(0, 100, 180));

        JLabel lblD = new JLabel("  " + checkIn + "  →  " + checkOut + "  (" + durata + ")");
        lblD.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblD.setForeground(GRI_TEXT);

        info.add(lblH);
        info.add(Box.createVerticalStrut(3));
        info.add(lblA);
        info.add(Box.createVerticalStrut(4));
        info.add(lblD);
        card.add(info, BorderLayout.CENTER);

        JPanel right = new JPanel();
        right.setLayout(new BoxLayout(right, BoxLayout.Y_AXIS));
        right.setBackground(ALB);
        right.setBorder(new EmptyBorder(12, 8, 12, 16));

        JLabel lblT = new JLabel("Total: " + total);
        lblT.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblT.setForeground(ORANGE_PRICE);
        lblT.setAlignmentX(Component.RIGHT_ALIGNMENT);

        Color textS = status.equals("Confirmata") ? GREEN_DARK : new Color(190, 100, 0);
        JLabel lblS = new JLabel("● " + status);
        lblS.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lblS.setForeground(textS);
        lblS.setAlignmentX(Component.RIGHT_ALIGNMENT);

        JButton btnA = new JButton("Anuleaza");
        btnA.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        btnA.setBackground(new Color(245, 252, 245));
        btnA.setForeground(GREEN_DARK);
        btnA.setBorder(BorderFactory.createLineBorder(BORDER_C));
        btnA.setFocusPainted(false);
        btnA.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnA.setAlignmentX(Component.RIGHT_ALIGNMENT);

        right.add(lblT);
        right.add(Box.createVerticalStrut(4));
        right.add(lblS);
        right.add(Box.createVerticalGlue());
        right.add(btnA);
        card.add(right, BorderLayout.EAST);
        return card;
    }
}
