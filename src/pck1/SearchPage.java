package pck1;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class SearchPage implements AppColors {

    public static JPanel build(AppNavigator nav) {
        JPanel page = new JPanel(new BorderLayout());
        page.setBackground(BG_PAGE);

        // Header cu buton inapoi
        JPanel header = new JPanel(new BorderLayout(10, 0));
        header.setBackground(GREEN_DARK);
        header.setBorder(new EmptyBorder(10, 16, 10, 16));

        JButton btnBack = new JButton("< Inapoi");
        btnBack.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnBack.setForeground(ALB);
        btnBack.setBackground(GREEN_PRIMARY);
        btnBack.setBorder(new EmptyBorder(6, 14, 6, 14));
        btnBack.setFocusPainted(false);
        btnBack.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnBack.addActionListener(e -> nav.showPage("home"));
        header.add(btnBack, BorderLayout.WEST);

        JLabel lblHeader = new JLabel("Rezultate cautare");
        lblHeader.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblHeader.setForeground(ALB);
        header.add(lblHeader, BorderLayout.CENTER);
        page.add(header, BorderLayout.NORTH);

        // Continut cu cele trei sectiuni
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(BG_PAGE);
        content.setBorder(new EmptyBorder(16, 20, 20, 20));

        // Sectiunea Cazari
        content.add(UIHelpers.createSectionTitle("Cazari gasite"));
        content.add(Box.createVerticalStrut(10));
        String[][] cazari = {
            {"Hotel Central",     "Centru, Iasi", "Hotel modern cu vedere panoramica.",    "4.5", "89",  "160"},
            {"Vila Panoramic",    "Tatarasi",     "Vila cu gradina si piscina privata.",   "4.2", "45",  "120"},
            {"Hostel Backpacker", "Centru, Iasi", "Cazare accesibila in centrul orasului.","3.8", "210", "35"},
        };
        for (String[] c : cazari) {
            content.add(UIHelpers.createListingCard(
                c[0], c[1], c[2], c[3], c[4], c[5], false, new Color(60, 130, 180), nav));
            content.add(Box.createVerticalStrut(10));
        }

        // Sectiunea Atractii
        content.add(Box.createVerticalStrut(16));
        content.add(UIHelpers.createSectionTitle("Atractii in zona"));
        content.add(Box.createVerticalStrut(10));
        String[][] atractii = {
            {"Palatul Culturii",      "Centru, Iasi", "4 muzee nationale sub acelasi acoperis.", "4.8", "1200", "0"},
            {"Gradina Botanica",      "Copou",         "10.000 de specii de plante.",             "4.6", "850",  "0"},
            {"Catedrala Mitropolitana","Centru",        "Cel mai mare lacas ortodox din Moldova.", "4.9", "2100", "0"},
        };
        for (String[] a : atractii) {
            content.add(UIHelpers.createListingCard(
                a[0], a[1], a[2], a[3], a[4], a[5], false, new Color(40, 140, 80), nav));
            content.add(Box.createVerticalStrut(10));
        }

        // Sectiunea Restaurante
        content.add(Box.createVerticalStrut(16));
        content.add(UIHelpers.createSectionTitle("Restaurante recomandate"));
        content.add(Box.createVerticalStrut(10));
        String[][] restaurante = {
            {"Bolta Rece", "Str. Rece 10",  "Restaurant traditional moldovenesc.", "4.7", "560", "45"},
            {"La Mama",    "Centru, Iasi",  "Bucatarie romaneasca autentica.",      "4.5", "890", "55"},
            {"Vivo Cafe",  "Copou",          "Cafenea si bistro cu terasa.",         "4.3", "320", "30"},
        };
        for (String[] r : restaurante) {
            content.add(UIHelpers.createListingCard(
                r[0], r[1], r[2], r[3], r[4], r[5], false, new Color(180, 80, 40), nav));
            content.add(Box.createVerticalStrut(10));
        }

        JScrollPane scroll = new JScrollPane(content);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(BG_PAGE);
        scroll.getVerticalScrollBar().setUnitIncrement(20);
        scroll.getVerticalScrollBar().setBlockIncrement(60);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        page.add(scroll, BorderLayout.CENTER);
        return page;
    }
}
