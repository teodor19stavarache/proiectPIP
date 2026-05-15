package pck1;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

public class ProfilPage implements AppColors {

    public static JPanel build() {
        JPanel page = new JPanel(new BorderLayout());
        page.setBackground(BG_PAGE);

        // Header verde cu avatar si info
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(GREEN_PRIMARY);
        header.setBorder(new EmptyBorder(20, 24, 20, 24));

        JPanel headerLeft = new JPanel(new FlowLayout(FlowLayout.LEFT, 18, 0));
        headerLeft.setBackground(GREEN_PRIMARY);

        // Avatar cu initiale
        JPanel avatarBox = new JPanel(new GridBagLayout());
        avatarBox.setBackground(GREEN_DARK);
        avatarBox.setPreferredSize(new Dimension(70, 70));
        avatarBox.setBorder(BorderFactory.createLineBorder(new Color(255, 255, 255, 60), 2));
        JLabel initiale = new JLabel("AI");
        initiale.setFont(new Font("Segoe UI", Font.BOLD, 26));
        initiale.setForeground(ALB);
        avatarBox.add(initiale);
        headerLeft.add(avatarBox);

        JPanel namePanel = new JPanel();
        namePanel.setLayout(new BoxLayout(namePanel, BoxLayout.Y_AXIS));
        namePanel.setBackground(GREEN_PRIMARY);

        JLabel lblNume = new JLabel("Alexandru Ionescu");
        lblNume.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblNume.setForeground(ALB);

        JLabel lblEmail = new JLabel("alex.ionescu@email.ro");
        lblEmail.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblEmail.setForeground(new Color(200, 255, 200));

        JLabel lblNivel = new JLabel("  Calator Frecvent  ");
        lblNivel.setFont(new Font("Segoe UI", Font.BOLD, 10));
        lblNivel.setForeground(GREEN_DARK);
        lblNivel.setBackground(new Color(255, 204, 0));
        lblNivel.setOpaque(true);
        lblNivel.setBorder(new EmptyBorder(2, 0, 2, 0));

        JLabel lblMembru = new JLabel("Membru din Ianuarie 2024  ·  Iasi, Romania");
        lblMembru.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblMembru.setForeground(new Color(170, 240, 170));

        namePanel.add(lblNume);
        namePanel.add(Box.createVerticalStrut(3));
        namePanel.add(lblEmail);
        namePanel.add(Box.createVerticalStrut(5));
        namePanel.add(lblNivel);
        namePanel.add(Box.createVerticalStrut(4));
        namePanel.add(lblMembru);
        headerLeft.add(namePanel);

        JButton btnEdit = new JButton("Editeaza profilul");
        btnEdit.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnEdit.setForeground(GREEN_PRIMARY);
        btnEdit.setBackground(ALB);
        btnEdit.setBorder(new EmptyBorder(8, 16, 8, 16));
        btnEdit.setFocusPainted(false);
        btnEdit.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        header.add(headerLeft, BorderLayout.CENTER);
        header.add(btnEdit,    BorderLayout.EAST);
        page.add(header, BorderLayout.NORTH);

        // Continut scrollabil
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(BG_PAGE);
        content.setBorder(new EmptyBorder(18, 22, 22, 22));

        // 4 carduri statistici
        JPanel statsRow = new JPanel(new GridLayout(1, 4, 12, 0));
        statsRow.setBackground(BG_PAGE);
        statsRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));
        statsRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        statsRow.add(createStatCard("3",   "Rezervari active", GREEN_PRIMARY));
        statsRow.add(createStatCard("7",   "Calatorii totale", GREEN_DARK));
        statsRow.add(createStatCard("4.8", "Rating mediu",     ORANGE_PRICE));
        statsRow.add(createStatCard("12",  "Orase vizitate",   ORANGE_STAR));
        content.add(statsRow);
        content.add(Box.createVerticalStrut(18));

        content.add(buildSection("Informatii cont", new String[][]{
            {"Nume complet",   "Alexandru Ionescu"},
            {"Email",          "alex.ionescu@email.ro"},
            {"Telefon",        "+40 712 345 678"},
            {"Oras resedinta", "Iasi, Romania"},
            {"Limba",          "Romana"},
        }));
        content.add(Box.createVerticalStrut(14));

        content.add(buildSection("Preferinte de calatorie", new String[][]{
            {"Tip cazare preferat",  "Hotel (4-5 stele)"},
            {"Buget mediu / noapte", "100 - 200 EUR"},
            {"Destinatii favorite",  "Europa, Asia"},
            {"Durata medie sejur",   "3-5 nopti"},
        }));
        content.add(Box.createVerticalStrut(14));

        // Calatorii recente
        JPanel sectCalatorii = new JPanel(new BorderLayout());
        sectCalatorii.setBackground(ALB);
        sectCalatorii.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
            new EmptyBorder(14, 16, 8, 16)
        ));
        sectCalatorii.setAlignmentX(Component.LEFT_ALIGNMENT);
        sectCalatorii.setMaximumSize(new Dimension(Integer.MAX_VALUE, 999));

        JLabel sectTitlu = new JLabel("Calatorii recente");
        sectTitlu.setFont(new Font("Segoe UI", Font.BOLD, 14));
        sectTitlu.setForeground(new Color(20, 20, 20));
        sectTitlu.setBorder(new EmptyBorder(0, 0, 8, 0));
        sectCalatorii.add(sectTitlu, BorderLayout.NORTH);

        JPanel calList = new JPanel();
        calList.setLayout(new BoxLayout(calList, BoxLayout.Y_AXIS));
        calList.setBackground(ALB);

        String[][] calatorii = {
            {"Paris, Franta",     "Hotel Grand Opera",  "Mar 2025", "4.7"},
            {"Roma, Italia",      "Colosseum Suites",   "Ian 2025", "4.5"},
            {"Praga, Cehia",      "Prague City Center", "Oct 2024", "4.8"},
            {"Barcelona, Spania", "Barceloneta Beach",  "Iun 2024", "4.3"},
        };

        for (String[] c : calatorii) {
            JPanel row = new JPanel(new BorderLayout(10, 0));
            row.setBackground(ALB);
            row.setBorder(new MatteBorder(0, 0, 1, 0, new Color(240, 240, 240)));

            JPanel rowLeft = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
            rowLeft.setBackground(ALB);
            JLabel dot = new JLabel("●");
            dot.setForeground(GREEN_PRIMARY);
            JLabel dest = new JLabel(c[0]);
            dest.setFont(new Font("Segoe UI", Font.BOLD, 13));
            dest.setForeground(new Color(15, 15, 15));
            JLabel hotel = new JLabel("· " + c[1]);
            hotel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            hotel.setForeground(GRI_TEXT);
            rowLeft.add(dot); rowLeft.add(dest); rowLeft.add(hotel);
            row.add(rowLeft, BorderLayout.WEST);

            JPanel rowRight = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 8));
            rowRight.setBackground(ALB);
            JLabel data = new JLabel(c[2]);
            data.setFont(new Font("Segoe UI", Font.PLAIN, 11));
            data.setForeground(GRI_TEXT);
            JLabel scoreTag = new JLabel(" " + c[3] + " ");
            scoreTag.setFont(new Font("Segoe UI", Font.BOLD, 11));
            scoreTag.setForeground(ALB);
            scoreTag.setBackground(GREEN_DARK);
            scoreTag.setOpaque(true);
            rowRight.add(data); rowRight.add(scoreTag);
            row.add(rowRight, BorderLayout.EAST);

            calList.add(row);
        }

        sectCalatorii.add(calList, BorderLayout.CENTER);
        content.add(sectCalatorii);
        content.add(Box.createVerticalStrut(22));

        JScrollPane scroll = new JScrollPane(content);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(BG_PAGE);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        page.add(scroll, BorderLayout.CENTER);
        return page;
    }

    private static JPanel buildSection(String titluSectiune, String[][] randuri) {
        JPanel sect = new JPanel(new BorderLayout());
        sect.setBackground(ALB);
        sect.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
            new EmptyBorder(14, 16, 6, 16)
        ));
        sect.setAlignmentX(Component.LEFT_ALIGNMENT);
        sect.setMaximumSize(new Dimension(Integer.MAX_VALUE, 999));

        JLabel titlu = new JLabel(titluSectiune);
        titlu.setFont(new Font("Segoe UI", Font.BOLD, 14));
        titlu.setForeground(new Color(15, 15, 15));
        titlu.setBorder(new EmptyBorder(0, 0, 8, 0));
        sect.add(titlu, BorderLayout.NORTH);

        JPanel rows = new JPanel();
        rows.setLayout(new BoxLayout(rows, BoxLayout.Y_AXIS));
        rows.setBackground(ALB);

        for (String[] r : randuri) {
            JPanel row = new JPanel(new BorderLayout());
            row.setBackground(ALB);
            row.setBorder(new MatteBorder(0, 0, 1, 0, new Color(245, 245, 245)));

            JLabel lblKey = new JLabel(r[0]);
            lblKey.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            lblKey.setForeground(GRI_TEXT);
            lblKey.setBorder(new EmptyBorder(9, 0, 9, 0));
            lblKey.setPreferredSize(new Dimension(180, 36));

            JLabel lblVal = new JLabel(r[1]);
            lblVal.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            lblVal.setForeground(new Color(15, 15, 15));

            row.add(lblKey, BorderLayout.WEST);
            row.add(lblVal, BorderLayout.CENTER);
            rows.add(row);
        }

        sect.add(rows, BorderLayout.CENTER);
        return sect;
    }

    private static JPanel createStatCard(String valoare, String eticheta, Color accentColor) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(ALB);
        card.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 1));

        JPanel accentLine = new JPanel();
        accentLine.setBackground(accentColor);
        accentLine.setPreferredSize(new Dimension(0, 4));
        card.add(accentLine, BorderLayout.NORTH);

        JPanel inner = new JPanel();
        inner.setLayout(new BoxLayout(inner, BoxLayout.Y_AXIS));
        inner.setBackground(ALB);
        inner.setBorder(new EmptyBorder(12, 14, 12, 14));

        JLabel lblV = new JLabel(valoare);
        lblV.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblV.setForeground(accentColor);
        lblV.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblE = new JLabel(eticheta);
        lblE.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblE.setForeground(GRI_TEXT);
        lblE.setAlignmentX(Component.LEFT_ALIGNMENT);

        inner.add(lblV);
        inner.add(Box.createVerticalStrut(2));
        inner.add(lblE);
        card.add(inner, BorderLayout.CENTER);
        return card;
    }
}
