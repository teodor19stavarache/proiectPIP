package pck1;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

public class Gui_Aplicatie extends JFrame {

    // ===== PALETA DE CULORI =====
    private static final Color ROȘ_ÎNCHIS   = new Color(122, 26,  26);  // #7a1a1a — topbar
    private static final Color ROȘ_PRINCIPAL= new Color(226, 75,  74);  // #e24b4a — butoane
    private static final Color ROȘ_MEDIU    = new Color(163, 45,  45);  // #a32d2d — preț
    private static final Color ROȘ_DESCHIS  = new Color(240, 149, 123); // #f09595 — borduri
    private static final Color ROȘ_PASTEL   = new Color(253, 245, 245); // #fdf5f5 — fundal
    private static final Color ROȘ_FILTRU   = new Color(247, 237, 237); // #f7eded — bara filtre
    private static final Color GRI_TEXT     = new Color(136, 135, 128); // subtitlu
    private static final Color ALB          = Color.WHITE;

    private JPanel contentPane;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                Gui_Aplicatie frame = new Gui_Aplicatie();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public Gui_Aplicatie() {
        setTitle("VoyaRo — Travel Planner");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(960, 680);
        setLocationRelativeTo(null);

        contentPane = new JPanel(new BorderLayout(0, 0));
        contentPane.setBackground(ROȘ_PASTEL);
        setContentPane(contentPane);

        contentPane.add(buildTopBar(),     BorderLayout.NORTH);
        contentPane.add(buildCenter(),     BorderLayout.CENTER);
    }


    private JPanel buildTopBar() {
        JPanel top = new JPanel(new BorderLayout(16, 0));
        top.setBackground(ROȘ_ÎNCHIS);
        top.setBorder(new EmptyBorder(12, 16, 12, 16));

        // --- Logo ---
        JLabel logo = new JLabel("Voya") {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
            }
        };
        logo.setText("<html><span style='color:white;font-size:18pt;font-weight:bold;'>Voya</span>"
                   + "<span style='color:#f09595;font-size:18pt;font-weight:bold;'>Ro</span>"
                   + "<span style='color:#f09595;font-size:14pt;'> ✈</span></html>");
        logo.setPreferredSize(new Dimension(130, 40));
        top.add(logo, BorderLayout.WEST);

        // --- Bara de căutare (centru) ---
        JPanel searchRow = new JPanel(new BorderLayout(0, 0));
        searchRow.setBackground(ALB);
        searchRow.setBorder(BorderFactory.createLineBorder(ROȘ_DESCHIS, 1));

        // Buton „Pe hartă" (stânga barei)
        JButton mapBtn = new JButton("📍 Pe hartă");
        mapBtn.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        mapBtn.setForeground(ROȘ_MEDIU);
        mapBtn.setBackground(new Color(255, 245, 245));
        mapBtn.setBorder(new MatteBorder(0, 0, 0, 1, new Color(247, 193, 193)));
        mapBtn.setFocusPainted(false);
        mapBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        mapBtn.setPreferredSize(new Dimension(120, 38));
        mapBtn.setToolTipText("Selectează locația pe hartă");
        searchRow.add(mapBtn, BorderLayout.WEST);

        // Câmp text căutare
        JTextField searchField = new JTextField("Caută destinație, hotel, atracție...");
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        searchField.setForeground(GRI_TEXT);
        searchField.setBorder(new EmptyBorder(0, 10, 0, 10));
        searchField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent e) {
                if (searchField.getText().equals("Caută destinație, hotel, atracție...")) {
                    searchField.setText("");
                    searchField.setForeground(new Color(80, 19, 19));
                }
            }
            public void focusLost(java.awt.event.FocusEvent e) {
                if (searchField.getText().isEmpty()) {
                    searchField.setText("Caută destinație, hotel, atracție...");
                    searchField.setForeground(GRI_TEXT);
                }
            }
        });
        searchRow.add(searchField, BorderLayout.CENTER);

        // Buton Caută (dreapta barei)
        JButton cautaBtn = new JButton("Caută");
        cautaBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        cautaBtn.setBackground(ROȘ_PRINCIPAL);
        cautaBtn.setForeground(ALB);
        cautaBtn.setBorder(new EmptyBorder(0, 18, 0, 18));
        cautaBtn.setFocusPainted(false);
        cautaBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        cautaBtn.setPreferredSize(new Dimension(90, 38));
        cautaBtn.addActionListener(e ->
            JOptionPane.showMessageDialog(this,
                "Căutare pentru: " + searchField.getText(),
                "VoyaRo", JOptionPane.INFORMATION_MESSAGE));
        searchRow.add(cautaBtn, BorderLayout.EAST);

        top.add(searchRow, BorderLayout.CENTER);
        return top;
    }

    private JPanel buildCenter() {
        JPanel center = new JPanel(new BorderLayout(0, 0));
        center.setBackground(ROȘ_PASTEL);

        // Bara de filtre
        center.add(buildFilters(), BorderLayout.NORTH);

        // Lista carduri
        JPanel lista = new JPanel();
        lista.setLayout(new BoxLayout(lista, BoxLayout.Y_AXIS));
        lista.setBackground(ROȘ_PASTEL);
        lista.setBorder(new EmptyBorder(12, 16, 12, 16));

        JLabel rezultate = new JLabel("12 rezultate găsite");
        rezultate.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        rezultate.setForeground(ROȘ_MEDIU);
        rezultate.setBorder(new EmptyBorder(0, 0, 8, 0));
        lista.add(rezultate);

        lista.add(createCard("Hotel Traian",
                "Piața Unirii 1, Iași, România",
                "Hotel emblematic în inima Iașului, cu vedere la Palatul Culturii. Camere elegante și spa.",
                "★★★★☆  4.3 (128 recenzii)", "185€ / noapte", true));
        lista.add(Box.createVerticalStrut(10));
        lista.add(createCard("Pensiunea La Conac",
                "Str. Lăpușneanu 14, Iași, România",
                "Pensiune boutique în centru vechi, grădină interioară și mic dejun inclus.",
                "★★★★★  4.8 (74 recenzii)", "95€ / noapte", false));
        lista.add(Box.createVerticalStrut(10));
        lista.add(createCard("Apartament Copou View",
                "Bd. Carol I 22, Copou, Iași",
                "Apartament modern cu 2 camere, la 5 min de Grădina Copou. Parcare gratuită.",
                "★★★★☆  4.1 (39 recenzii)", "60€ / noapte", false));
        lista.add(Box.createVerticalStrut(10));
        lista.add(createCard("Casa Pogor Suites",
                "Str. Vasile Pogor 4, Iași, România",
                "Cazare de lux în casa istorică Pogor, restaurată. Mic dejun gourmet inclus.",
                "★★★★★  4.9 (51 recenzii)", "220€ / noapte", false));

        JScrollPane scroll = new JScrollPane(lista);
        scroll.setBorder(null);
        scroll.setBackground(ROȘ_PASTEL);
        scroll.getViewport().setBackground(ROȘ_PASTEL);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        center.add(scroll, BorderLayout.CENTER);

        return center;
    }

    private JPanel buildFilters() {
        JPanel bar = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        bar.setBackground(ROȘ_FILTRU);
        bar.setBorder(new MatteBorder(0, 0, 1, 0, new Color(247, 193, 193)));

        JLabel lbl = new JLabel("Filtre:");
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lbl.setForeground(ROȘ_MEDIU);
        bar.add(lbl);

        String[] filtre = {"Toate", "Hoteluri", "Pensiuni", "Apartamente", "Budget", "Lux"};
        for (int i = 0; i < filtre.length; i++) {
            JButton chip = new JButton(filtre[i]);
            chip.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            chip.setFocusPainted(false);
            chip.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            if (i == 0) {
                chip.setBackground(ROȘ_PRINCIPAL);
                chip.setForeground(ALB);
                chip.setBorder(BorderFactory.createLineBorder(ROȘ_PRINCIPAL));
            } else {
                chip.setBackground(ALB);
                chip.setForeground(new Color(121, 31, 31));
                chip.setBorder(BorderFactory.createLineBorder(ROȘ_DESCHIS));
            }
            bar.add(chip);
        }
        return bar;
    }

    // ================================================================
    //  CARD DE LISTING
    // ================================================================
    private JPanel createCard(String titlu, String locatie, String descriere,
                               String rating, String pret, boolean topAles) {
        JPanel card = new JPanel(new BorderLayout(0, 0));
        card.setBackground(ALB);
        card.setBorder(BorderFactory.createLineBorder(new Color(247, 193, 193), 1));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 130));

        // --- Imagine placeholder ---
        JPanel img = new JPanel(new GridBagLayout());
        img.setPreferredSize(new Dimension(155, 130));
        img.setBackground(new Color(253, 240, 232));
        JLabel imgIcon = new JLabel("🏨", SwingConstants.CENTER);
        imgIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 32));
        img.add(imgIcon);

        if (topAles) {
            // Badge „Top ales"
            JPanel badgePanel = new JPanel(new BorderLayout());
            badgePanel.setBackground(new Color(253, 240, 232));
            JLabel badge = new JLabel("  Top ales  ");
            badge.setFont(new Font("Segoe UI", Font.BOLD, 10));
            badge.setForeground(ALB);
            badge.setBackground(ROȘ_PRINCIPAL);
            badge.setOpaque(true);
            badgePanel.add(imgIcon, BorderLayout.CENTER);
            badgePanel.add(badge, BorderLayout.NORTH);
            card.add(badgePanel, BorderLayout.WEST);
        } else {
            card.add(img, BorderLayout.WEST);
        }

        // --- Info central ---
        JPanel info = new JPanel();
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
        info.setBackground(ALB);
        info.setBorder(new EmptyBorder(12, 14, 12, 8));

        JLabel lblTitlu = new JLabel(titlu);
        lblTitlu.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblTitlu.setForeground(new Color(80, 19, 19));

        JLabel lblLocatie = new JLabel("📍 " + locatie);
        lblLocatie.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblLocatie.setForeground(GRI_TEXT);

        JLabel lblRating = new JLabel(rating);
        lblRating.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblRating.setForeground(new Color(186, 117, 23));

        JLabel lblDesc = new JLabel("<html><body style='width:340px'>" + descriere + "</body></html>");
        lblDesc.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblDesc.setForeground(GRI_TEXT);

        info.add(lblTitlu);
        info.add(Box.createVerticalStrut(3));
        info.add(lblLocatie);
        info.add(Box.createVerticalStrut(2));
        info.add(lblRating);
        info.add(Box.createVerticalStrut(5));
        info.add(lblDesc);
        card.add(info, BorderLayout.CENTER);

        // --- Preț + buton (dreapta) ---
        JPanel right = new JPanel();
        right.setLayout(new BoxLayout(right, BoxLayout.Y_AXIS));
        right.setBackground(ALB);
        right.setBorder(new EmptyBorder(14, 8, 14, 16));

        String[] pretParts = pret.split(" / ");
        JLabel lblPret = new JLabel(pretParts[0]);
        lblPret.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblPret.setForeground(ROȘ_MEDIU);
        lblPret.setAlignmentX(Component.RIGHT_ALIGNMENT);

        JLabel lblNoapte = new JLabel("/ " + (pretParts.length > 1 ? pretParts[1] : "noapte"));
        lblNoapte.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblNoapte.setForeground(GRI_TEXT);
        lblNoapte.setAlignmentX(Component.RIGHT_ALIGNMENT);

        JButton btnRez = new JButton("Rezervă");
        btnRez.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnRez.setBackground(ROȘ_PRINCIPAL);
        btnRez.setForeground(ALB);
        btnRez.setBorder(new EmptyBorder(7, 14, 7, 14));
        btnRez.setFocusPainted(false);
        btnRez.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnRez.setAlignmentX(Component.RIGHT_ALIGNMENT);
        btnRez.addActionListener(e ->
            JOptionPane.showMessageDialog(this,
                "Rezervare inițiată pentru:\n" + titlu + "\n" + locatie,
                "VoyaRo — Rezervare", JOptionPane.INFORMATION_MESSAGE));

        right.add(lblPret);
        right.add(lblNoapte);
        right.add(Box.createVerticalGlue());
        right.add(btnRez);
        card.add(right, BorderLayout.EAST);

        return card;
    }
}