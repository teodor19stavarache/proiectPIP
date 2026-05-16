package pck1;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class AtractiiPage implements AppColors {

    public static JPanel build() {
        JPanel page = new JPanel(new BorderLayout());
        page.setBackground(WARM_BG);

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(GREEN_PRIMARY);
        header.setBorder(new EmptyBorder(18, 22, 18, 22));
        JLabel titlu = new JLabel("Atractii turistice in Iasi");
        titlu.setFont(new Font("Segoe UI", Font.BOLD, 21));
        titlu.setForeground(ALB);
        JLabel subtitlu = new JLabel("Descopera cele mai frumoase locuri din oras");
        subtitlu.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtitlu.setForeground(new Color(200, 255, 200));
        header.add(titlu,    BorderLayout.NORTH);
        header.add(subtitlu, BorderLayout.SOUTH);
        page.add(header, BorderLayout.NORTH);

        JPanel grid = new JPanel(new GridLayout(0, 2, 12, 12));
        grid.setBackground(WARM_BG);
        grid.setBorder(new EmptyBorder(16, 20, 16, 20));

        grid.add(createAtractieCard("Palatul Culturii",
            "Cel mai important monument al Iasului, gazduieste 4 muzee nationale.",
            "Ma-Du: 10:00-17:00", "0.5 km", ORANGE_PRICE));
        grid.add(createAtractieCard("Gradina Botanica",
            "A doua gradina botanica din Romania, cu 10.000 de specii.",
            "Zilnic: 09:00-19:00", "2.0 km", new Color(255, 130, 0)));
        grid.add(createAtractieCard("Catedrala Mitropolitana",
            "Cea mai mare catedrala ortodoxa din Moldova, construita in 1880.",
            "Zilnic: 07:00-20:00", "0.3 km", GREEN_PRIMARY));
        grid.add(createAtractieCard("Universitatea UAIC",
            "Prima universitate din Romania, fondata in 1860.",
            "Campus deschis", "1.0 km", GREEN_DARK));
        grid.add(createAtractieCard("Gradina Copou",
            "Cea mai veche gradina publica, cu teiul lui Eminescu.",
            "Zilnic: 06:00-22:00", "1.5 km", GREEN_LIGHT));
        grid.add(createAtractieCard("Curtea Domneasca",
            "Ruinele fostei curti medievale, simbol al istoriei Moldovei.",
            "Ma-Du: 09:00-18:00", "0.8 km", ORANGE_STAR));

        JScrollPane scroll = new JScrollPane(grid);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(WARM_BG);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        page.add(scroll, BorderLayout.CENTER);
        return page;
    }

    private static JPanel createAtractieCard(String nume, String desc,
                                              String program, String distanta, Color culoare) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(ALB);
        card.setBorder(BorderFactory.createLineBorder(BORDER_C, 1));

        JPanel bara = new JPanel();
        bara.setBackground(culoare);
        bara.setPreferredSize(new Dimension(5, 0));
        card.add(bara, BorderLayout.WEST);

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(ALB);
        content.setBorder(new EmptyBorder(14, 14, 14, 14));

        JLabel lblNume = new JLabel(nume);
        lblNume.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblNume.setForeground(new Color(20, 20, 20));

        JLabel lblDesc = new JLabel("<html><body style='width:260px'>" + desc + "</body></html>");
        lblDesc.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblDesc.setForeground(GRI_TEXT);

        JLabel lblProg = new JLabel("  " + program);
        lblProg.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblProg.setForeground(GRI_TEXT);

        JLabel lblDist = new JLabel("  " + distanta + " de centru");
        lblDist.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lblDist.setForeground(culoare);

        content.add(lblNume);
        content.add(Box.createVerticalStrut(5));
        content.add(lblDesc);
        content.add(Box.createVerticalStrut(5));
        content.add(lblProg);
        content.add(Box.createVerticalStrut(2));
        content.add(lblDist);
        card.add(content, BorderLayout.CENTER);
        return card;
    }
}
