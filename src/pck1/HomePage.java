package pck1;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

public class HomePage implements AppColors {

    public static JPanel build(AppNavigator nav) {
        JPanel page = new JPanel(new BorderLayout());
        page.setBackground(BG_PAGE);

        // Bara de sus cu filtre si sortare
        JPanel topSection = new JPanel(new BorderLayout());
        topSection.setBackground(ALB);

        JPanel contextBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
        contextBar.setBackground(ALB);
        contextBar.setBorder(new MatteBorder(0, 0, 1, 0, new Color(230, 230, 230)));

        JLabel lblOras = new JLabel("Iasi, Romania");
        lblOras.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblOras.setForeground(new Color(20, 20, 20));
        contextBar.add(lblOras);

        JLabel lblSep = new JLabel("·");
        lblSep.setForeground(new Color(180, 180, 180));
        contextBar.add(lblSep);

        JLabel lblNr = new JLabel("4 proprietati disponibile");
        lblNr.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblNr.setForeground(GRI_TEXT);
        contextBar.add(lblNr);

        contextBar.add(Box.createHorizontalStrut(200));
        JLabel lblSort = new JLabel("Sorteaza:");
        lblSort.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblSort.setForeground(GRI_TEXT);
        contextBar.add(lblSort);

        JComboBox<String> sortBox = new JComboBox<>(new String[]{"Recomandate", "Pret crescator", "Rating"});
        sortBox.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        sortBox.setBackground(ALB);
        contextBar.add(sortBox);

        topSection.add(buildFiltersBar(), BorderLayout.NORTH);
        topSection.add(contextBar, BorderLayout.SOUTH);
        page.add(topSection, BorderLayout.NORTH);

        // Lista carduri
        JPanel lista = new JPanel();
        lista.setLayout(new BoxLayout(lista, BoxLayout.Y_AXIS));
        lista.setBackground(BG_PAGE);
        lista.setBorder(new EmptyBorder(16, 20, 20, 20));

        lista.add(UIHelpers.createListingCard(
            "Hotel Traian", "Piata Unirii 1, Iasi",
            "Hotel emblematic cu vedere la Palatul Culturii. Spa, restaurant si bar.",
            "4.3", "128", "185", true, new Color(180, 60, 60), nav));
        lista.add(Box.createVerticalStrut(12));
        lista.add(UIHelpers.createListingCard(
            "Pensiunea La Conac", "Str. Lapusneanu 14, Iasi",
            "Pensiune boutique cu gradina interioara, mic dejun inclus si parcare.",
            "4.8", "74", "95", false, new Color(60, 110, 180), nav));
        lista.add(Box.createVerticalStrut(12));
        lista.add(UIHelpers.createListingCard(
            "Apartament Copou View", "Bd. Carol I 22, Copou",
            "Apartament modern 2 camere, la 5 minute de Gradina Copou si Universitate.",
            "4.1", "39", "60", false, new Color(40, 140, 100), nav));
        lista.add(Box.createVerticalStrut(12));
        lista.add(UIHelpers.createListingCard(
            "Casa Pogor Suites", "Str. Vasile Pogor 4, Iasi",
            "Cazare de lux in casa istorica Pogor restaurata. Mic dejun gourmet inclus.",
            "4.9", "51", "220", false, new Color(130, 60, 160), nav));

        JScrollPane scroll = new JScrollPane(lista);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(BG_PAGE);
        scroll.getVerticalScrollBar().setUnitIncrement(20);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        page.add(scroll, BorderLayout.CENTER);
        return page;
    }

    private static JPanel buildFiltersBar() {
        JPanel bar = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 10));
        bar.setBackground(ALB);
        bar.setBorder(new MatteBorder(0, 0, 1, 0, new Color(225, 225, 225)));

        String[] filtre = {"Toate", "Hoteluri", "Pensiuni", "Apartamente", "Budget", "Lux"};
        JButton[] chipuri = new JButton[filtre.length];

        for (int i = 0; i < filtre.length; i++) {
            JButton chip = new JButton(filtre[i]);
            chip.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            chip.setFocusPainted(false);
            chip.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            chip.setOpaque(true);
            if (i == 0) {
                chip.setBackground(GREEN_PRIMARY);
                chip.setForeground(ALB);
                chip.setBorder(new EmptyBorder(5, 14, 5, 14));
            } else {
                chip.setBackground(ALB);
                chip.setForeground(new Color(50, 50, 50));
                chip.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                    new EmptyBorder(4, 13, 4, 13)
                ));
            }
            chipuri[i] = chip;
            final int idx = i;
            chip.addActionListener(e -> {
                for (int j = 0; j < chipuri.length; j++) {
                    if (j == idx) {
                        chipuri[j].setBackground(GREEN_PRIMARY);
                        chipuri[j].setForeground(ALB);
                        chipuri[j].setBorder(new EmptyBorder(5, 14, 5, 14));
                    } else {
                        chipuri[j].setBackground(ALB);
                        chipuri[j].setForeground(new Color(50, 50, 50));
                        chipuri[j].setBorder(BorderFactory.createCompoundBorder(
                            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                            new EmptyBorder(4, 13, 4, 13)
                        ));
                    }
                }
            });
            bar.add(chip);
        }
        return bar;
    }
}
