package pck1;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

/*
 * Gui_Aplicatie - fereastra principala a aplicatiei VoyaRo.
 *
 * Structura generala a ferestrei:
 *
 *  +--------------------------------------------------+
 *  |  TOPBAR (logo, cautare, buton profil)            |
 *  +--------+-----------------------------------------+
 *  |        |                                         |
 *  | SIDEBAR|  CONTINUT PRINCIPAL (CardLayout)        |
 *  | (stanga|    - pagina Home (listinguri)           |
 *  |  fixa) |    - pagina Atractii                    |
 *  |        |    - pagina Rezervari                   |
 *  |        |    - pagina Detalii anunt               |
 *  |        |    - pagina Profil                      |
 *  +--------+-----------------------------------------+
 *
 * Navigarea intre pagini se face cu CardLayout:
 * fiecare pagina are un nume unic (ex: "home", "atractii")
 * si o schimbam cu cardLayout.show(mainPanel, "numePagina").
 */
public class Gui_Aplicatie extends JFrame {

    // Culorile aplicatiei
    private static final Color ROS_INCHIS    = new Color(200, 23,  23);
    private static final Color ROS_PRINCIPAL = Color.RED;
    private static final Color ROS_MEDIU     = new Color(163, 45,  45);
    private static final Color ROS_DESCHIS   = new Color(240, 149, 123);
    private static final Color ROS_PASTEL    = new Color(253, 245, 245);
    private static final Color ROS_FILTRU    = new Color(247, 237, 237);
    private static final Color GRI_TEXT      = new Color(136, 135, 128);
    private static final Color SIDEBAR_BG    = new Color(80, 18, 18);
    private static final Color ALB           = Color.WHITE;

    // CardLayout permite schimbarea paginii active fara a deschide ferestre noi
    private CardLayout cardLayout;

    // Panelul care contine toate paginile suprapuse
    private JPanel mainPanel;

    // Panelul de detalii - il pastram ca field ca sa il putem goli si reumple
    private JPanel detailPanel;

    // Butoanele din sidebar - le pastram pentru a le putea evidentia pe cel activ
    private JButton btnHome, btnAtractii, btnRezervari;

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
        setTitle("VoyaRo - Travel Planner");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 700);
        setLocationRelativeTo(null);

        // Panelul radacina al ferestrei cu BorderLayout
        JPanel root = new JPanel(new BorderLayout());

        // Topbar-ul e fixat sus (NORTH) - nu dispare la schimbarea paginii
        root.add(buildTopBar(), BorderLayout.NORTH);

        // Sidebar-ul e fixat in stanga (WEST) - mereu vizibil
        root.add(buildSidebar(), BorderLayout.WEST);

        // Zona centrala cu CardLayout - se schimba la navigare
        cardLayout = new CardLayout();
        mainPanel  = new JPanel(cardLayout);
        mainPanel.add(buildHomePage(),       "home");
        mainPanel.add(buildAtractiiPage(),   "atractii");
        mainPanel.add(buildRezervariPage(),"rezervari");
        mainPanel.add(buildProfilPage(),"profil");

        // Panelul de detalii e gol la inceput, se umple cand apesi pe un listing
        detailPanel = new JPanel(new BorderLayout());
        mainPanel.add(detailPanel, "detail");

        root.add(mainPanel, BorderLayout.CENTER);
        setContentPane(root);

        // Afisam pagina home la pornire
        cardLayout.show(mainPanel, "home");
    }

    /*
     * buildTopBar() - bara de sus, mereu vizibila.
     * Contine: logo stanga, bara de cautare centru, buton profil dreapta.
     */
    private JPanel buildTopBar() {
        JPanel top = new JPanel(new BorderLayout(16, 0));
        top.setBackground(ROS_INCHIS);
        top.setBorder(new EmptyBorder(10, 16, 10, 16));

        // Logo
        JLabel logo = new JLabel(
            "<html><span style='color:white;font-size:17pt;font-weight:bold;'>Voya</span>"
            + "<span style='color:#f09595;font-size:17pt;font-weight:bold;'>Ro</span>"
            + "<span style='color:#f09595;font-size:13pt;'> ✈</span></html>"
        );
        logo.setPreferredSize(new Dimension(120, 36));
        top.add(logo, BorderLayout.WEST);

        // Bara de cautare
        JPanel searchRow = new JPanel(new BorderLayout());
        searchRow.setBackground(ALB);
        searchRow.setBorder(BorderFactory.createLineBorder(ROS_DESCHIS, 1));

        JButton mapBtn = new JButton("   Pe harta");
        mapBtn.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        mapBtn.setForeground(ROS_MEDIU);
        mapBtn.setBackground(new Color(255, 245, 245));
        mapBtn.setBorder(new MatteBorder(0, 0, 0, 1, new Color(247, 193, 193)));
        mapBtn.setFocusPainted(false);
        mapBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        mapBtn.setPreferredSize(new Dimension(110, 34));
        searchRow.add(mapBtn, BorderLayout.WEST);

        JTextField searchField = new JTextField("Cauta destinatie, hotel, atractie...");
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        searchField.setForeground(GRI_TEXT);
        searchField.setBorder(new EmptyBorder(0, 10, 0, 10));
        searchField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent e) {
                if (searchField.getText().startsWith("Cauta")) {
                    searchField.setText("");
                    searchField.setForeground(new Color(80, 19, 19));
                }
            }
            public void focusLost(java.awt.event.FocusEvent e) {
                if (searchField.getText().isEmpty()) {
                    searchField.setText("Cauta destinatie, hotel, atractie...");
                    searchField.setForeground(GRI_TEXT);
                }
            }
        });
        searchRow.add(searchField, BorderLayout.CENTER);

        JButton cautaBtn = new JButton("Cauta");
        cautaBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        cautaBtn.setBackground(ROS_PRINCIPAL);
        cautaBtn.setForeground(ALB);
        cautaBtn.setBorder(new EmptyBorder(0, 16, 0, 16));
        cautaBtn.setFocusPainted(false);
        cautaBtn.setPreferredSize(new Dimension(80, 34));
        searchRow.add(cautaBtn, BorderLayout.EAST);
        top.add(searchRow, BorderLayout.CENTER);

        // Buton profil in dreapta topbar-ului
        JButton btnProfil = new JButton("👤  Profil");
        btnProfil.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btnProfil.setForeground(ALB);
        btnProfil.setBackground(ROS_MEDIU);
        btnProfil.setBorder(new EmptyBorder(6, 14, 6, 14));
        btnProfil.setFocusPainted(false);
        btnProfil.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        // La click navigam la pagina de profil
        btnProfil.addActionListener(e -> {
            clearSidebarSelection();
            cardLayout.show(mainPanel, "profil");
        });
        top.add(btnProfil, BorderLayout.EAST);

        return top;
    }

    /*
     * buildSidebar() - panelul lateral stang, mereu vizibil.
     *
     * Contine butoane de navigare catre paginile principale:
     *   - Home (listinguri de hoteluri)
     *   - Atractii turistice
     *   - Rezervarile mele
     *
     * Si jos, o lista cu rezervarile recente pentru acces rapid.
     */
    private JPanel buildSidebar() {
        JPanel sidebar = new JPanel(new BorderLayout());
        sidebar.setBackground(SIDEBAR_BG);
        sidebar.setPreferredSize(new Dimension(200, 0));

        // Zona butoanelor de navigare (sus in sidebar)
        JPanel navSection = new JPanel();
        navSection.setLayout(new BoxLayout(navSection, BoxLayout.Y_AXIS));
        navSection.setBackground(SIDEBAR_BG);
        navSection.setBorder(new EmptyBorder(16, 0, 8, 0));

        JLabel navLabel = new JLabel("  Navigare");
        navLabel.setFont(new Font("Segoe UI", Font.BOLD, 10));
        navLabel.setForeground(new Color(200, 150, 150));
        navLabel.setBorder(new EmptyBorder(0, 16, 8, 0));
        navSection.add(navLabel);

        // Cream butoanele principale de navigare
        btnHome      = createNavButton("🏠  Hoteluri",     true);
        btnAtractii  = createNavButton("🗺  Atractii",     false);
        btnRezervari = createNavButton("📋  Rezervarile mele", false);

        // La click pe fiecare buton: evidentiaza butonul si schimba pagina
        btnHome.addActionListener(e -> {
            setActiveNavButton(btnHome);
            cardLayout.show(mainPanel, "home");
        });
        btnAtractii.addActionListener(e -> {
            setActiveNavButton(btnAtractii);
            cardLayout.show(mainPanel, "atractii");
        });
        btnRezervari.addActionListener(e -> {
            setActiveNavButton(btnRezervari);
            cardLayout.show(mainPanel, "rezervari");
        });

        navSection.add(btnHome);
        navSection.add(btnAtractii);
        navSection.add(btnRezervari);

        sidebar.add(navSection, BorderLayout.NORTH);

        // Zona rezervarilor recente (jos in sidebar)
        JPanel rezervSection = new JPanel();
        rezervSection.setLayout(new BoxLayout(rezervSection, BoxLayout.Y_AXIS));
        rezervSection.setBackground(SIDEBAR_BG);
        rezervSection.setBorder(new EmptyBorder(0, 0, 16, 0));

        // Linie separatoare
        JSeparator sep = new JSeparator();
        sep.setForeground(new Color(100, 40, 40));
        sep.setMaximumSize(new Dimension(200, 1));
        rezervSection.add(sep);
        rezervSection.add(Box.createVerticalStrut(12));

        JLabel secLabel = new JLabel("  Rezervari recente");
        secLabel.setFont(new Font("Segoe UI", Font.BOLD, 10));
        secLabel.setForeground(new Color(200, 150, 150));
        secLabel.setBorder(new EmptyBorder(0, 16, 8, 0));
        rezervSection.add(secLabel);

        // Adaugam carduri mici pentru fiecare rezervare recenta
        rezervSection.add(createSidebarRezervareCard("Hotel Traian", "15-18 Mai 2025", "Confirmata"));
        rezervSection.add(Box.createVerticalStrut(6));
        rezervSection.add(createSidebarRezervareCard("Copou View", "2-5 Iun 2025", "In asteptare"));
        rezervSection.add(Box.createVerticalStrut(6));
        rezervSection.add(createSidebarRezervareCard("Casa Pogor", "10 Iul 2025", "Confirmata"));

        sidebar.add(rezervSection, BorderLayout.SOUTH);
        return sidebar;
    }

    /*
     * createNavButton() - creaza un buton de navigare pentru sidebar.
     * Parametrul 'activ' determina daca butonul e evidentiat la start.
     */
    private JButton createNavButton(String text, boolean activ) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btn.setForeground(ALB);
        btn.setBackground(activ ? ROS_MEDIU : SIDEBAR_BG);
        btn.setBorder(new EmptyBorder(10, 16, 10, 16));
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setMaximumSize(new Dimension(200, 40));
        return btn;
    }

    /*
     * setActiveNavButton() - evidentiaza butonul selectat si le reseteaza pe celelalte.
     * Apelata la fiecare click de navigare.
     */
    private void setActiveNavButton(JButton active) {
        for (JButton b : new JButton[]{btnHome, btnAtractii, btnRezervari}) {
            b.setBackground(b == active ? ROS_MEDIU : SIDEBAR_BG);
        }
    }

    // Reseteaza evidentierea tuturor butoanelor (ex: cand mergem la Profil)
    private void clearSidebarSelection() {
        for (JButton b : new JButton[]{btnHome, btnAtractii, btnRezervari}) {
            b.setBackground(SIDEBAR_BG);
        }
    }

    /*
     * createSidebarRezervareCard() - card mic in sidebar pentru o rezervare.
     * Afiseaza numele, datele si statusul rezervarii.
     */
    private JPanel createSidebarRezervareCard(String hotel, String date, String status) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(new Color(100, 30, 30));
        card.setBorder(new EmptyBorder(8, 16, 8, 16));
        card.setMaximumSize(new Dimension(200, 70));

        JLabel lblHotel = new JLabel(hotel);
        lblHotel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblHotel.setForeground(ALB);

        JLabel lblDate = new JLabel(date);
        lblDate.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblDate.setForeground(new Color(200, 170, 170));

        // Culoarea statusului: verde pentru confirmata, galben pentru in asteptare
        Color statusColor = status.equals("Confirmata") ? new Color(120, 210, 120) : new Color(240, 190, 80);
        JLabel lblStatus = new JLabel("● " + status);
        lblStatus.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        lblStatus.setForeground(statusColor);

        card.add(lblHotel);
        card.add(Box.createVerticalStrut(2));
        card.add(lblDate);
        card.add(Box.createVerticalStrut(2));
        card.add(lblStatus);
        return card;
    }

    /*
     * buildHomePage() - pagina principala cu lista de hoteluri.
     */
    private JPanel buildHomePage() {
        JPanel page = new JPanel(new BorderLayout());
        page.setBackground(ROS_PASTEL);

        // Bara de filtre sus
        page.add(buildFiltersBar(), BorderLayout.NORTH);

        // Lista de carduri de listing
        JPanel lista = new JPanel();
        lista.setLayout(new BoxLayout(lista, BoxLayout.Y_AXIS));
        lista.setBackground(ROS_PASTEL);
        lista.setBorder(new EmptyBorder(12, 20, 12, 20));

        JLabel lbl = new JLabel("4 hoteluri gasite in Iasi");
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lbl.setForeground(ROS_MEDIU);
        lbl.setBorder(new EmptyBorder(0, 0, 8, 0));
        lista.add(lbl);

        lista.add(createListingCard("Hotel Traian",
            "Piata Unirii 1, Iasi", "Hotel emblematic cu vedere la Palatul Culturii. Spa si restaurant.",
            "4.3", "128", "185", true));
        lista.add(Box.createVerticalStrut(10));
        lista.add(createListingCard("Pensiunea La Conac",
            "Str. Lapusneanu 14, Iasi", "Pensiune boutique cu gradina interioara si mic dejun inclus.",
            "4.8", "74", "95", false));
        lista.add(Box.createVerticalStrut(10));
        lista.add(createListingCard("Apartament Copou View",
            "Bd. Carol I 22, Iasi", "Apartament modern 2 camere, 5 min de Gradina Copou. Parcare gratuita.",
            "4.1", "39", "60", false));
        lista.add(Box.createVerticalStrut(10));
        lista.add(createListingCard("Casa Pogor Suites",
            "Str. Vasile Pogor 4, Iasi", "Cazare de lux in casa istorica Pogor restaurata. Mic dejun gourmet.",
            "4.9", "51", "220", false));

        JScrollPane scroll = new JScrollPane(lista);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(ROS_PASTEL);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        page.add(scroll, BorderLayout.CENTER);
        return page;
    }

    /*
     * buildAtractiiPage() - pagina cu atractii turistice din Iasi.
     * Afiseaza carduri cu obiective de vizitat, program si distanta.
     */
    private JPanel buildAtractiiPage() {
        JPanel page = new JPanel(new BorderLayout());
        page.setBackground(ROS_PASTEL);

        // Header pagina
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(100, 20, 20));
        header.setBorder(new EmptyBorder(16, 20, 16, 20));
        JLabel titlu = new JLabel("Atractii turistice in Iasi");
        titlu.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titlu.setForeground(ALB);
        JLabel subtitlu = new JLabel("Descopera cele mai frumoase locuri din oras");
        subtitlu.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtitlu.setForeground(new Color(220, 180, 180));
        header.add(titlu, BorderLayout.NORTH);
        header.add(subtitlu, BorderLayout.SOUTH);
        page.add(header, BorderLayout.NORTH);

        // Grid 2 coloane cu carduri de atractii
        JPanel grid = new JPanel(new GridLayout(0, 2, 12, 12));
        grid.setBackground(ROS_PASTEL);
        grid.setBorder(new EmptyBorder(16, 20, 16, 20));

        // Fiecare atractie are: emoji, nume, descriere, program, distanta
        grid.add(createAtractieCard("🏛", "Palatul Culturii",
            "Cel mai important monument al Iasului, gazduieste 4 muzee nationale.",
            "Ma-Du: 10:00-17:00", "0.5 km de centru"));
        grid.add(createAtractieCard("🌿", "Gradina Botanica",
            "A doua gradina botanica din Romania ca suprafata, cu 10.000 de specii.",
            "Zi: 09:00-19:00", "2.0 km de centru"));
        grid.add(createAtractieCard("⛪", "Catedrala Mitropolitana",
            "Cea mai mare catedrala ortodoxa din Moldova, construita in 1880.",
            "Zilnic: 07:00-20:00", "0.3 km de centru"));
        grid.add(createAtractieCard("📚", "Universitatea Al. I. Cuza",
            "Prima universitate din Romania, fondata in 1860. Arhitectura impresionanta.",
            "Campus deschis", "1.0 km de centru"));
        grid.add(createAtractieCard("🌳", "Gradina Copou",
            "Cea mai veche gradina publica din Iasi, cu teiul lui Eminescu.",
            "Zilnic: 06:00-22:00", "1.5 km de centru"));
        grid.add(createAtractieCard("🏰", "Curtea Domneasca",
            "Ruinele fostei curti domnesti medievale, simbol al istoriei Moldovei.",
            "Ma-Du: 09:00-18:00", "0.8 km de centru"));

        JScrollPane scroll = new JScrollPane(grid);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(ROS_PASTEL);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        page.add(scroll, BorderLayout.CENTER);
        return page;
    }

    /*
     * createAtractieCard() - card vizual pentru o atractie turistica.
     */
    private JPanel createAtractieCard(String emoji, String nume, String desc,
                                       String program, String distanta) {
        JPanel card = new JPanel(new BorderLayout(10, 0));
        card.setBackground(ALB);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(247, 193, 193), 1),
            new EmptyBorder(14, 14, 14, 14)
        ));

        // Emoji mare in stanga
        JLabel icon = new JLabel(emoji, SwingConstants.CENTER);
        icon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 34));
        icon.setPreferredSize(new Dimension(54, 54));
        card.add(icon, BorderLayout.WEST);

        // Info text in centru
        JPanel info = new JPanel();
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
        info.setBackground(ALB);

        JLabel lblNume = new JLabel(nume);
        lblNume.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblNume.setForeground(new Color(80, 19, 19));

        JLabel lblDesc = new JLabel("<html><body style='width:220px'>" + desc + "</body></html>");
        lblDesc.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblDesc.setForeground(GRI_TEXT);

        JLabel lblProgram = new JLabel("🕐 " + program);
        lblProgram.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblProgram.setForeground(GRI_TEXT);

        JLabel lblDist = new JLabel("📍 " + distanta);
        lblDist.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblDist.setForeground(ROS_MEDIU);

        info.add(lblNume);
        info.add(Box.createVerticalStrut(4));
        info.add(lblDesc);
        info.add(Box.createVerticalStrut(4));
        info.add(lblProgram);
        info.add(Box.createVerticalStrut(2));
        info.add(lblDist);
        card.add(info, BorderLayout.CENTER);

        return card;
    }

    /*
     * buildRezervariPage() - pagina cu toate rezervarile utilizatorului.
     * Afiseaza statusul fiecarei rezervari si detaliile.
     */
    private JPanel buildRezervariPage() {
        JPanel page = new JPanel(new BorderLayout());
        page.setBackground(ROS_PASTEL);

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(100, 20, 20));
        header.setBorder(new EmptyBorder(16, 20, 16, 20));
        JLabel titlu = new JLabel("Rezervarile mele");
        titlu.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titlu.setForeground(ALB);
        header.add(titlu);
        page.add(header, BorderLayout.NORTH);

        // Lista de rezervari
        JPanel lista = new JPanel();
        lista.setLayout(new BoxLayout(lista, BoxLayout.Y_AXIS));
        lista.setBackground(ROS_PASTEL);
        lista.setBorder(new EmptyBorder(16, 20, 16, 20));

        lista.add(createRezervareCard("Hotel Traian", "Piata Unirii 1, Iasi",
            "15 Mai 2025", "18 Mai 2025", "3 nopti", "555€", "Confirmata"));
        lista.add(Box.createVerticalStrut(10));
        lista.add(createRezervareCard("Apartament Copou View", "Bd. Carol I 22, Iasi",
            "2 Iun 2025", "5 Iun 2025", "3 nopti", "180€", "In asteptare"));
        lista.add(Box.createVerticalStrut(10));
        lista.add(createRezervareCard("Casa Pogor Suites", "Str. Vasile Pogor 4, Iasi",
            "10 Iul 2025", "12 Iul 2025", "2 nopti", "440€", "Confirmata"));

        JScrollPane scroll = new JScrollPane(lista);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(ROS_PASTEL);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        page.add(scroll, BorderLayout.CENTER);
        return page;
    }

    /*
     * createRezervareCard() - card detaliat pentru o rezervare.
     * Afiseaza check-in, check-out, pret total si status.
     */
    private JPanel createRezervareCard(String hotel, String adresa, String checkIn,
                                        String checkOut, String durata, String total,
                                        String status) {
        JPanel card = new JPanel(new BorderLayout(0, 0));
        card.setBackground(ALB);
        card.setBorder(BorderFactory.createLineBorder(new Color(247, 193, 193), 1));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 110));

        // Bara colorata de status in stanga cardului
        Color statusColor = status.equals("Confirmata") ? new Color(226, 75, 74) : new Color(240, 160, 50);
        JPanel statusBar = new JPanel();
        statusBar.setBackground(statusColor);
        statusBar.setPreferredSize(new Dimension(6, 0));
        card.add(statusBar, BorderLayout.WEST);

        // Informatii centrale
        JPanel info = new JPanel();
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
        info.setBackground(ALB);
        info.setBorder(new EmptyBorder(12, 14, 12, 8));

        JLabel lblHotel = new JLabel(hotel);
        lblHotel.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblHotel.setForeground(new Color(80, 19, 19));

        JLabel lblAdresa = new JLabel("📍 " + adresa);
        lblAdresa.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblAdresa.setForeground(GRI_TEXT);

        JLabel lblDate = new JLabel("📅 " + checkIn + "  →  " + checkOut + "  (" + durata + ")");
        lblDate.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblDate.setForeground(new Color(80, 80, 80));

        info.add(lblHotel);
        info.add(Box.createVerticalStrut(3));
        info.add(lblAdresa);
        info.add(Box.createVerticalStrut(5));
        info.add(lblDate);
        card.add(info, BorderLayout.CENTER);

        // Total si status in dreapta
        JPanel right = new JPanel();
        right.setLayout(new BoxLayout(right, BoxLayout.Y_AXIS));
        right.setBackground(ALB);
        right.setBorder(new EmptyBorder(12, 8, 12, 16));

        JLabel lblTotal = new JLabel("Total: " + total);
        lblTotal.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTotal.setForeground(ROS_MEDIU);
        lblTotal.setAlignmentX(Component.RIGHT_ALIGNMENT);

        Color sColor = status.equals("Confirmata") ? new Color(34, 139, 34) : new Color(200, 130, 0);
        JLabel lblStatus = new JLabel("● " + status);
        lblStatus.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lblStatus.setForeground(sColor);
        lblStatus.setAlignmentX(Component.RIGHT_ALIGNMENT);

        JButton btnAnula = new JButton("Anuleaza");
        btnAnula.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        btnAnula.setBackground(new Color(247, 237, 237));
        btnAnula.setForeground(ROS_MEDIU);
        btnAnula.setBorder(BorderFactory.createLineBorder(ROS_DESCHIS));
        btnAnula.setFocusPainted(false);
        btnAnula.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnAnula.setAlignmentX(Component.RIGHT_ALIGNMENT);

        right.add(lblTotal);
        right.add(Box.createVerticalStrut(4));
        right.add(lblStatus);
        right.add(Box.createVerticalGlue());
        right.add(btnAnula);
        card.add(right, BorderLayout.EAST);

        return card;
    }

    /*
     * buildProfilPage() - pagina de profil a utilizatorului.
     * Afiseaza datele personale si statistici.
     */
    private JPanel buildProfilPage() {
        JPanel page = new JPanel(new BorderLayout());
        page.setBackground(ROS_PASTEL);

        // Header cu avatar
        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 16));
        header.setBackground(new Color(100, 20, 20));

        JLabel avatar = new JLabel("👤", SwingConstants.CENTER);
        avatar.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 40));
        avatar.setBackground(ROS_MEDIU);
        avatar.setOpaque(true);
        avatar.setBorder(new EmptyBorder(8, 14, 8, 14));
        header.add(avatar);

        JPanel namePanel = new JPanel();
        namePanel.setLayout(new BoxLayout(namePanel, BoxLayout.Y_AXIS));
        namePanel.setBackground(new Color(100, 20, 20));
        JLabel lblNume = new JLabel("Alexandru Ionescu");
        lblNume.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblNume.setForeground(ALB);
        JLabel lblEmail = new JLabel("alex.ionescu@email.ro");
        lblEmail.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblEmail.setForeground(new Color(220, 180, 180));
        JLabel lblMembru = new JLabel("Membru din Ianuarie 2024");
        lblMembru.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblMembru.setForeground(new Color(200, 160, 160));
        namePanel.add(lblNume);
        namePanel.add(Box.createVerticalStrut(4));
        namePanel.add(lblEmail);
        namePanel.add(Box.createVerticalStrut(2));
        namePanel.add(lblMembru);
        header.add(namePanel);
        page.add(header, BorderLayout.NORTH);

        // Continut profil
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(ROS_PASTEL);
        content.setBorder(new EmptyBorder(20, 30, 20, 30));

        // Statistici rapide
        JPanel stats = new JPanel(new GridLayout(1, 3, 12, 0));
        stats.setBackground(ROS_PASTEL);
        stats.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));
        stats.add(createStatCard("3", "Rezervari active"));
        stats.add(createStatCard("7", "Calatorii totale"));
        stats.add(createStatCard("4.8", "Rating dat"));
        stats.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(stats);
        content.add(Box.createVerticalStrut(20));

        // Buton de editare profil
        JButton btnEdit = new JButton("  Editeaza profilul");
        btnEdit.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnEdit.setBackground(ROS_PRINCIPAL);
        btnEdit.setForeground(ALB);
        btnEdit.setBorder(new EmptyBorder(10, 20, 10, 20));
        btnEdit.setFocusPainted(false);
        btnEdit.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnEdit.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(btnEdit);

        JScrollPane scroll = new JScrollPane(content);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(ROS_PASTEL);
        page.add(scroll, BorderLayout.CENTER);
        return page;
    }

    // Card mic cu o statistica (numar + eticheta)
    private JPanel createStatCard(String valoare, String eticheta) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(ALB);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(247, 193, 193), 1),
            new EmptyBorder(14, 14, 14, 14)
        ));
        JLabel lblVal = new JLabel(valoare, SwingConstants.CENTER);
        lblVal.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblVal.setForeground(ROS_PRINCIPAL);
        lblVal.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel lblEt = new JLabel(eticheta, SwingConstants.CENTER);
        lblEt.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblEt.setForeground(GRI_TEXT);
        lblEt.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(lblVal);
        card.add(Box.createVerticalStrut(4));
        card.add(lblEt);
        return card;
    }

    // Bara de filtre pentru pagina home
    private JPanel buildFiltersBar() {
        JPanel bar = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        bar.setBackground(ROS_FILTRU);
        bar.setBorder(new MatteBorder(0, 0, 1, 0, new Color(247, 193, 193)));
        JLabel lbl = new JLabel("Filtre:");
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lbl.setForeground(ROS_MEDIU);
        bar.add(lbl);
        String[] filtre = {"Toate", "Hoteluri", "Pensiuni", "Apartamente", "Budget", "Lux"};
        for (int i = 0; i < filtre.length; i++) {
            JButton chip = new JButton(filtre[i]);
            chip.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            chip.setFocusPainted(false);
            chip.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            if (i == 0) {
                chip.setBackground(ROS_PRINCIPAL);
                chip.setForeground(ALB);
                chip.setBorder(BorderFactory.createLineBorder(ROS_PRINCIPAL));
            } else {
                chip.setBackground(ALB);
                chip.setForeground(new Color(121, 31, 31));
                chip.setBorder(BorderFactory.createLineBorder(ROS_DESCHIS));
            }
            bar.add(chip);
        }
        return bar;
    }

    /*
     * createListingCard() - card de hotel pentru pagina home.
     * La click pe "Vezi anunt" navigam la pagina de detalii.
     */
    private JPanel createListingCard(String titlu, String locatie, String descriere,
                                      String rating, String nrRec, String pret, boolean topAles) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(ALB);
        card.setBorder(BorderFactory.createLineBorder(new Color(247, 193, 193), 1));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 130));

        // Imagine placeholder
        JPanel imgPanel = new JPanel(new BorderLayout());
        imgPanel.setPreferredSize(new Dimension(140, 130));
        imgPanel.setBackground(new Color(253, 240, 232));
        JLabel imgIcon = new JLabel("🏨", SwingConstants.CENTER);
        imgIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 30));
        imgPanel.add(imgIcon, BorderLayout.CENTER);
        if (topAles) {
            JLabel badge = new JLabel("  Top ales  ", SwingConstants.CENTER);
            badge.setFont(new Font("Segoe UI", Font.BOLD, 10));
            badge.setForeground(ALB);
            badge.setBackground(ROS_PRINCIPAL);
            badge.setOpaque(true);
            badge.setPreferredSize(new Dimension(140, 20));
            imgPanel.add(badge, BorderLayout.NORTH);
        }
        card.add(imgPanel, BorderLayout.WEST);

        // Info
        JPanel info = new JPanel();
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
        info.setBackground(ALB);
        info.setBorder(new EmptyBorder(10, 14, 10, 8));

        JLabel lblT = new JLabel(titlu);
        lblT.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblT.setForeground(new Color(80, 19, 19));

        JLabel lblL = new JLabel("📍 " + locatie);
        lblL.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblL.setForeground(GRI_TEXT);

        int stele = (int) Math.round(Double.parseDouble(rating));
        JLabel lblR = new JLabel("★".repeat(stele) + "☆".repeat(5 - stele)
            + "  " + rating + " (" + nrRec + " rec.)");
        lblR.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblR.setForeground(new Color(186, 117, 23));

        JLabel lblD = new JLabel("<html><body style='width:300px'>" + descriere + "</body></html>");
        lblD.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblD.setForeground(GRI_TEXT);

        info.add(lblT);
        info.add(Box.createVerticalStrut(2));
        info.add(lblL);
        info.add(Box.createVerticalStrut(2));
        info.add(lblR);
        info.add(Box.createVerticalStrut(4));
        info.add(lblD);
        card.add(info, BorderLayout.CENTER);

        // Pret + butoane dreapta
        JPanel right = new JPanel();
        right.setLayout(new BoxLayout(right, BoxLayout.Y_AXIS));
        right.setBackground(ALB);
        right.setBorder(new EmptyBorder(12, 8, 12, 16));

        JLabel lblP = new JLabel(pret + "€");
        lblP.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblP.setForeground(ROS_MEDIU);
        lblP.setAlignmentX(Component.RIGHT_ALIGNMENT);

        JLabel lblN = new JLabel("/ noapte");
        lblN.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblN.setForeground(GRI_TEXT);
        lblN.setAlignmentX(Component.RIGHT_ALIGNMENT);

        JButton btnVezi = new JButton("Vezi anunt");
        btnVezi.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnVezi.setBackground(ROS_MEDIU);
        btnVezi.setForeground(ALB);
        btnVezi.setBorder(new EmptyBorder(5, 12, 5, 12));
        btnVezi.setFocusPainted(false);
        btnVezi.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnVezi.setAlignmentX(Component.RIGHT_ALIGNMENT);

        JButton btnRez = new JButton("Rezerva");
        btnRez.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnRez.setBackground(ROS_PRINCIPAL);
        btnRez.setForeground(ALB);
        btnRez.setBorder(new EmptyBorder(5, 12, 5, 12));
        btnRez.setFocusPainted(false);
        btnRez.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnRez.setAlignmentX(Component.RIGHT_ALIGNMENT);

        // Ambele butoane duc la pagina de detalii
        btnVezi.addActionListener(e -> navigateToDetail(titlu, locatie, descriere, rating, nrRec, pret));
        btnRez.addActionListener(e -> navigateToDetail(titlu, locatie, descriere, rating, nrRec, pret));

        right.add(lblP);
        right.add(lblN);
        right.add(Box.createVerticalStrut(6));
        right.add(btnVezi);
        right.add(Box.createVerticalStrut(4));
        right.add(btnRez);
        card.add(right, BorderLayout.EAST);

        return card;
    }

    /*
     * navigateToDetail() - umple panelul de detalii si il afiseaza.
     *
     * De fiecare data cand apesi pe un listing diferit:
     *   1. Stergem continutul vechi din detailPanel
     *   2. Construim continut nou cu datele anuntului selectat
     *   3. Cerem Swing sa redeseneze (revalidate + repaint)
     *   4. Aratam pagina "detail" prin CardLayout
     */
    private void navigateToDetail(String titlu, String locatie, String descriere,
                                   String rating, String nrRec, String pret) {
        detailPanel.removeAll();
        detailPanel.add(buildDetailPage(titlu, locatie, descriere, rating, nrRec, pret));
        detailPanel.revalidate();
        detailPanel.repaint();
        clearSidebarSelection();
        cardLayout.show(mainPanel, "detail");
    }

    /*
     * buildDetailPage() - pagina completa de detalii a unui anunt.
     */
    private JPanel buildDetailPage(String titlu, String locatie, String descriere,
                                    String rating, String nrRec, String pret) {
        JPanel page = new JPanel(new BorderLayout());
        page.setBackground(ROS_PASTEL);

        // Header cu buton inapoi
        JPanel header = new JPanel(new BorderLayout(10, 0));
        header.setBackground(new Color(100, 20, 20));
        header.setBorder(new EmptyBorder(10, 16, 10, 16));

        JButton btnBack = new JButton("< Inapoi");
        btnBack.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnBack.setForeground(ALB);
        btnBack.setBackground(ROS_MEDIU);
        btnBack.setBorder(new EmptyBorder(6, 14, 6, 14));
        btnBack.setFocusPainted(false);
        btnBack.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        // La click mergem inapoi la home si reactivam butonul Home din sidebar
        btnBack.addActionListener(e -> {
            setActiveNavButton(btnHome);
            cardLayout.show(mainPanel, "home");
        });
        header.add(btnBack, BorderLayout.WEST);

        JLabel lblTitluHeader = new JLabel(titlu);
        lblTitluHeader.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTitluHeader.setForeground(ALB);
        header.add(lblTitluHeader, BorderLayout.CENTER);
        page.add(header, BorderLayout.NORTH);

        // Continut scrollabil
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(ROS_PASTEL);
        content.setBorder(new EmptyBorder(20, 30, 20, 30));

        // Imagine mare placeholder
        JPanel imgMare = new JPanel(new GridBagLayout());
        imgMare.setBackground(new Color(240, 200, 190));
        imgMare.setMaximumSize(new Dimension(Integer.MAX_VALUE, 240));
        imgMare.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel imgLbl = new JLabel("Galerie foto - " + titlu, SwingConstants.CENTER);
        imgLbl.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        imgLbl.setForeground(new Color(100, 40, 40));
        imgMare.add(imgLbl);
        content.add(imgMare);
        content.add(Box.createVerticalStrut(20));

        // Titlu + locatie + rating
        JLabel lblT = new JLabel(titlu);
        lblT.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblT.setForeground(new Color(80, 19, 19));
        lblT.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(lblT);
        content.add(Box.createVerticalStrut(4));

        JLabel lblL = new JLabel("📍 " + locatie);
        lblL.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblL.setForeground(GRI_TEXT);
        lblL.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(lblL);
        content.add(Box.createVerticalStrut(4));

        int stele = (int) Math.round(Double.parseDouble(rating));
        JLabel lblR = new JLabel("★".repeat(stele) + "☆".repeat(5 - stele)
            + "  " + rating + " din 5  ·  " + nrRec + " recenzii");
        lblR.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblR.setForeground(new Color(186, 117, 23));
        lblR.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(lblR);
        content.add(Box.createVerticalStrut(20));

        // Descriere
        JLabel secDesc = new JLabel("Despre proprietate");
        secDesc.setFont(new Font("Segoe UI", Font.BOLD, 15));
        secDesc.setForeground(new Color(80, 19, 19));
        secDesc.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(secDesc);
        content.add(Box.createVerticalStrut(8));

        JLabel lblDesc = new JLabel(
            "<html><body style='width:650px;font-size:12pt;'>" + descriere
            + " Camerele sunt dotate cu aer conditionat, TV si Wi-Fi gratuit."
            + " Receptia este deschisa non-stop.</body></html>");
        lblDesc.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblDesc.setForeground(new Color(80, 80, 80));
        lblDesc.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(lblDesc);
        content.add(Box.createVerticalStrut(20));

        // Facilitati
        JLabel secFac = new JLabel("Facilitati");
        secFac.setFont(new Font("Segoe UI", Font.BOLD, 15));
        secFac.setForeground(new Color(80, 19, 19));
        secFac.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(secFac);
        content.add(Box.createVerticalStrut(8));

        JPanel facPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        facPanel.setBackground(ROS_PASTEL);
        facPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        for (String f : new String[]{"Wi-Fi gratuit", "Mic dejun", "Parcare", "AC", "Room service", "Receptie 24h"}) {
            JLabel c = new JLabel("  " + f + "  ");
            c.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            c.setForeground(ROS_MEDIU);
            c.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ROS_DESCHIS, 1),
                new EmptyBorder(4, 6, 4, 6)));
            c.setBackground(new Color(255, 245, 245));
            c.setOpaque(true);
            facPanel.add(c);
        }
        content.add(facPanel);
        content.add(Box.createVerticalStrut(24));

        JSeparator sep = new JSeparator();
        sep.setForeground(new Color(247, 193, 193));
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        sep.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(sep);
        content.add(Box.createVerticalStrut(20));

        // Sectiunea rezervare
        JLabel secRez = new JLabel("Rezerva acum");
        secRez.setFont(new Font("Segoe UI", Font.BOLD, 15));
        secRez.setForeground(new Color(80, 19, 19));
        secRez.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(secRez);
        content.add(Box.createVerticalStrut(12));

        JPanel rezervRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        rezervRow.setBackground(ROS_PASTEL);
        rezervRow.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel pretCard = new JPanel(new BorderLayout(0, 4));
        pretCard.setBackground(ALB);
        pretCard.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(247, 193, 193), 1),
            new EmptyBorder(12, 18, 12, 18)));
        JLabel lblPretMare = new JLabel(pret + "€");
        lblPretMare.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblPretMare.setForeground(ROS_MEDIU);
        JLabel lblPN = new JLabel("per noapte");
        lblPN.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblPN.setForeground(GRI_TEXT);
        pretCard.add(lblPretMare, BorderLayout.CENTER);
        pretCard.add(lblPN, BorderLayout.SOUTH);
        rezervRow.add(pretCard);

        JButton btnRezMare = new JButton("  Rezerva acum  >");
        btnRezMare.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnRezMare.setBackground(ROS_PRINCIPAL);
        btnRezMare.setForeground(ALB);
        btnRezMare.setBorder(new EmptyBorder(14, 26, 14, 26));
        btnRezMare.setFocusPainted(false);
        btnRezMare.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnRezMare.addActionListener(e ->
            JOptionPane.showMessageDialog(this,
                "Rezervare initiata pentru:\n" + titlu + "\n" + locatie + "\nPret: " + pret + "€ / noapte",
                "VoyaRo - Rezervare", JOptionPane.INFORMATION_MESSAGE));
        rezervRow.add(btnRezMare);
        content.add(rezervRow);
        content.add(Box.createVerticalStrut(30));

        JScrollPane scroll = new JScrollPane(content);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(ROS_PASTEL);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        page.add(scroll, BorderLayout.CENTER);
        return page;
    }
}