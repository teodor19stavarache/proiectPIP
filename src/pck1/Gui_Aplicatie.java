package pck1;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

/*
 * Gui_Aplicatie - fereastra principala a aplicatiei TravaleRo.
 *
 * Aplicatia se porneste din clasa Main (nu mai exista metoda main aici).
 *
 * Paleta verde aprins:
 *   GREEN_PRIMARY = #009933  verde electric - culoarea principala
 *   GREEN_DARK    = #006622  verde inchis pentru hover si elemente secundare
 *   ORANGE_PRICE  = #FF6600  portocaliu pentru preturi - contrast maxim pe alb
 *   ORANGE_STAR   = #FF9900  portocaliu deschis pentru stele si rating
 *   BG_PAGE       = #F3F6F3  fundal gri cu nuanta verde foarte subtila
 *   SIDEBAR_BG    = #0A1A0A  aproape negru cu nuanta verde - contrast maxim
 *
 * Structura navigare:
 *   rootLayout (JFrame)
 *     "login"    -> buildLoginPage()
 *     "register" -> buildRegisterPage()
 *     "app"      -> buildAppPanel()
 *                     mainPanel (CardLayout)
 *                       "home"     -> buildHomePage()
 *                       "search"   -> buildSearchPage()
 *                       "atractii" -> buildAtractiiPage()
 *                       "rezervari"-> buildRezervariPage()
 *                       "profil"   -> buildProfilPage()
 *                       "detail"   -> detailPanel (populat dinamic)
 */
public class Gui_Aplicatie extends JFrame {

    // Paleta verde aprins
    private static final Color GREEN_PRIMARY = new Color(  0, 153,  51);
    private static final Color GREEN_DARK    = new Color(  0, 102,  34);
    private static final Color GREEN_LIGHT   = new Color( 51, 204,  51);
    private static final Color ORANGE_PRICE  = new Color(255, 102,   0);
    private static final Color ORANGE_STAR   = new Color(255, 153,   0);
    private static final Color BG_PAGE       = new Color(243, 246, 243);
    private static final Color SIDEBAR_BG    = new Color( 10,  26,  10);
    private static final Color SIDEBAR_SEC   = new Color( 18,  42,  18);
    private static final Color GRI_TEXT      = new Color(107, 107, 107);
    private static final Color BORDER_C      = new Color(220, 220, 220);
    private static final Color ALB           = Color.WHITE;

    // Alias-uri pentru compatibilitate interna
    private static final Color ROS_VIU   = GREEN_PRIMARY;
    private static final Color ROS_HOVER = GREEN_DARK;
    private static final Color PORTOCALIU   = ORANGE_PRICE;
    private static final Color PORTOCALIU_2 = ORANGE_STAR;
    private static final Color WARM_BG      = BG_PAGE;

    // CardLayout radacina - comuta intre login, register si aplicatie
    private CardLayout rootLayout;
    private JPanel     rootPanel;

    // CardLayout interior - comuta intre paginile aplicatiei
    private CardLayout cardLayout;
    private JPanel     mainPanel;
    private JPanel     detailPanel;

    // Buton Home pentru sidebar (folosit la resetare selectie)
    private JButton btnHome;

    // Filtre cautare sidebar
    private JTextField sidebarLocatieField;
    private JTextField sidebarDataInceput;
    private JTextField sidebarDataFinal;
    private JSpinner   sidebarAdulti;
    private JSpinner   sidebarCopii;
    private JPanel     sidebarVarstePanel;

    public Gui_Aplicatie() {
        setTitle("TravaleRo - Travel Planner");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 700);
        setLocationRelativeTo(null);

        rootLayout = new CardLayout();
        rootPanel  = new JPanel(rootLayout);

        rootPanel.add(buildLoginPage(),    "login");
        rootPanel.add(buildRegisterPage(), "register");
        rootPanel.add(buildAppPanel(),     "app");

        setContentPane(rootPanel);
        showRoot("app");
    }

    // Navigare centralizata - previne glitch-urile vizuale cu revalidate+repaint
    private void showPage(String name) {
        cardLayout.show(mainPanel, name);
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    private void showRoot(String name) {
        rootLayout.show(rootPanel, name);
        rootPanel.revalidate();
        rootPanel.repaint();
    }

    // ================================================================
    //  LOGIN
    // ================================================================
    /*
     * buildLoginPage() - ecranul de autentificare.
     * Stanga: branding cu logo mare si slogan.
     * Dreapta: formular cu email, parola, buton login, link register.
     */
    private JPanel buildLoginPage() {
        JPanel page = new JPanel(new GridLayout(1, 2, 0, 0));

        // Stanga - branding verde
        JPanel leftSide = new JPanel(new GridBagLayout());
        leftSide.setBackground(GREEN_PRIMARY);

        JPanel brand = new JPanel();
        brand.setLayout(new BoxLayout(brand, BoxLayout.Y_AXIS));
        brand.setBackground(GREEN_PRIMARY);

        JLabel logoMare = new JLabel(
            "<html><div style='text-align:center;'>" +
            "<span style='color:white;font-size:42pt;font-weight:bold;'>Travale</span>" +
            "<span style='color:#FFCC00;font-size:42pt;font-weight:bold;'>Ro</span>" +
            "</div></html>"
        );
        logoMare.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel avion = new JLabel("\u2708", SwingConstants.CENTER);
        avion.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 48));
        avion.setForeground(new Color(200, 255, 200));
        avion.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel slogan = new JLabel(
            "<html><div style='text-align:center;color:#CCFFCC;font-size:13pt;'>" +
            "Planifica-ti urmatoarea aventura<br>din inima Iasului spre lume." +
            "</div></html>"
        );
        slogan.setAlignmentX(Component.CENTER_ALIGNMENT);

        brand.add(Box.createVerticalGlue());
        brand.add(logoMare);
        brand.add(Box.createVerticalStrut(8));
        brand.add(avion);
        brand.add(Box.createVerticalStrut(24));
        brand.add(slogan);
        brand.add(Box.createVerticalGlue());
        leftSide.add(brand);
        page.add(leftSide);

        // Dreapta - formular
        JPanel rightSide = new JPanel(new GridBagLayout());
        rightSide.setBackground(BG_PAGE);

        JPanel formCard = new JPanel();
        formCard.setLayout(new BoxLayout(formCard, BoxLayout.Y_AXIS));
        formCard.setBackground(ALB);
        formCard.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_C, 1),
            new EmptyBorder(40, 44, 32, 44)
        ));

        JLabel titluForm = new JLabel("Bine ai revenit");
        titluForm.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titluForm.setForeground(new Color(20, 20, 20));
        titluForm.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subtitluForm = new JLabel("Conecteaza-te la contul tau TravaleRo");
        subtitluForm.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtitluForm.setForeground(GRI_TEXT);
        subtitluForm.setAlignmentX(Component.LEFT_ALIGNMENT);

        formCard.add(titluForm);
        formCard.add(Box.createVerticalStrut(4));
        formCard.add(subtitluForm);
        formCard.add(Box.createVerticalStrut(32));

        // Camp email
        JLabel lblEmail = new JLabel("Adresa de email");
        lblEmail.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblEmail.setForeground(GRI_TEXT);
        lblEmail.setAlignmentX(Component.LEFT_ALIGNMENT);
        formCard.add(lblEmail);
        formCard.add(Box.createVerticalStrut(5));

        JTextField campEmail = new JTextField();
        campEmail.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        campEmail.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_C, 1),
            new EmptyBorder(10, 12, 10, 12)
        ));
        campEmail.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        campEmail.setAlignmentX(Component.LEFT_ALIGNMENT);
        formCard.add(campEmail);
        formCard.add(Box.createVerticalStrut(18));

        // Camp parola
        JLabel lblParola = new JLabel("Parola");
        lblParola.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblParola.setForeground(GRI_TEXT);
        lblParola.setAlignmentX(Component.LEFT_ALIGNMENT);
        formCard.add(lblParola);
        formCard.add(Box.createVerticalStrut(5));

        JPasswordField campParola = new JPasswordField();
        campParola.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        campParola.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_C, 1),
            new EmptyBorder(10, 12, 10, 12)
        ));
        campParola.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        campParola.setAlignmentX(Component.LEFT_ALIGNMENT);
        formCard.add(campParola);
        formCard.add(Box.createVerticalStrut(28));

        // Buton login - verde aprins
        JButton btnLogin = new JButton("Intra in cont");
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnLogin.setBackground(GREEN_PRIMARY);
        btnLogin.setForeground(ALB);
        btnLogin.setBorder(new EmptyBorder(12, 0, 12, 0));
        btnLogin.setFocusPainted(false);
        btnLogin.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnLogin.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
        btnLogin.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnLogin.addActionListener(e -> showRoot("app"));
        campEmail.addActionListener(e -> showRoot("app"));
        campParola.addActionListener(e -> showRoot("app"));
        formCard.add(btnLogin);
        formCard.add(Box.createVerticalStrut(16));

        // Rand de jos: register stanga, forgot password dreapta
        JPanel bottomRow = new JPanel(new BorderLayout());
        bottomRow.setBackground(ALB);
        bottomRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 28));
        bottomRow.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton btnRegister = new JButton("Creeaza cont");
        btnRegister.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnRegister.setForeground(GREEN_PRIMARY);
        btnRegister.setBackground(ALB);
        btnRegister.setBorder(BorderFactory.createEmptyBorder());
        btnRegister.setFocusPainted(false);
        btnRegister.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnRegister.addActionListener(e -> showRoot("register"));

        JButton btnForgot = new JButton("Ai uitat parola?");
        btnForgot.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnForgot.setForeground(GRI_TEXT);
        btnForgot.setBackground(ALB);
        btnForgot.setBorder(BorderFactory.createEmptyBorder());
        btnForgot.setFocusPainted(false);
        btnForgot.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnForgot.addActionListener(e ->
            JOptionPane.showMessageDialog(this, "Resetare parola - in curand!",
                "TravaleRo", JOptionPane.INFORMATION_MESSAGE));

        bottomRow.add(btnRegister, BorderLayout.WEST);
        bottomRow.add(btnForgot,   BorderLayout.EAST);
        formCard.add(bottomRow);

        formCard.setPreferredSize(new Dimension(380, 420));
        rightSide.add(formCard);
        page.add(rightSide);
        return page;
    }

    // ================================================================
    //  REGISTER
    // ================================================================
    /*
     * buildRegisterPage() - ecranul de creare cont.
     * Stanga: branding galben-verde.
     * Dreapta: formular cu Nume, Prenume, Email, Parola, Oras.
     * "Autentifica-te" duce inapoi la login.
     * TODO: conectare la baza de date - inlocuieste JOptionPane cu apel JDBC.
     */
    private JPanel buildRegisterPage() {
        JPanel page = new JPanel(new GridLayout(1, 2, 0, 0));

        // Stanga branding - galben-verde
        JPanel leftSide = new JPanel(new GridBagLayout());
        leftSide.setBackground(GREEN_DARK);

        JPanel brand = new JPanel();
        brand.setLayout(new BoxLayout(brand, BoxLayout.Y_AXIS));
        brand.setBackground(GREEN_DARK);

        JLabel logoMare = new JLabel(
            "<html><div style='text-align:center;'>" +
            "<span style='color:white;font-size:40pt;font-weight:bold;'>Travale</span>" +
            "<span style='color:#FFCC00;font-size:40pt;font-weight:bold;'>Ro</span>" +
            "</div></html>"
        );
        logoMare.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel avion = new JLabel("\u2708", SwingConstants.CENTER);
        avion.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 46));
        avion.setForeground(new Color(180, 255, 180));
        avion.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel slogan = new JLabel(
            "<html><div style='text-align:center;color:#AAFFAA;font-size:13pt;'>" +
            "Alatura-te comunitatii TravaleRo<br>si descopera lumea." +
            "</div></html>"
        );
        slogan.setAlignmentX(Component.CENTER_ALIGNMENT);

        brand.add(Box.createVerticalGlue());
        brand.add(logoMare);
        brand.add(Box.createVerticalStrut(8));
        brand.add(avion);
        brand.add(Box.createVerticalStrut(24));
        brand.add(slogan);
        brand.add(Box.createVerticalGlue());
        leftSide.add(brand);
        page.add(leftSide);

        // Dreapta - formular
        JPanel rightSide = new JPanel(new GridBagLayout());
        rightSide.setBackground(BG_PAGE);

        JPanel formCard = new JPanel();
        formCard.setLayout(new BoxLayout(formCard, BoxLayout.Y_AXIS));
        formCard.setBackground(ALB);
        formCard.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_C, 1),
            new EmptyBorder(32, 44, 28, 44)
        ));

        JLabel titluForm = new JLabel("Creeaza cont nou");
        titluForm.setFont(new Font("Segoe UI", Font.BOLD, 21));
        titluForm.setForeground(new Color(20, 20, 20));
        titluForm.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subtitluForm = new JLabel("Completeaza datele de mai jos");
        subtitluForm.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtitluForm.setForeground(GRI_TEXT);
        subtitluForm.setAlignmentX(Component.LEFT_ALIGNMENT);

        formCard.add(titluForm);
        formCard.add(Box.createVerticalStrut(4));
        formCard.add(subtitluForm);
        formCard.add(Box.createVerticalStrut(24));

        // Rand Nume + Prenume pe acelasi rand
        JPanel numeRow = new JPanel(new GridLayout(1, 2, 10, 0));
        numeRow.setBackground(ALB);
        numeRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 68));
        numeRow.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel numePanel = new JPanel();
        numePanel.setLayout(new BoxLayout(numePanel, BoxLayout.Y_AXIS));
        numePanel.setBackground(ALB);
        JLabel lblNume = new JLabel("Nume");
        lblNume.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblNume.setForeground(GRI_TEXT);
        JTextField campNume = new JTextField();
        campNume.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        campNume.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_C, 1),
            new EmptyBorder(8, 10, 8, 10)
        ));
        numePanel.add(lblNume);
        numePanel.add(Box.createVerticalStrut(4));
        numePanel.add(campNume);
        numeRow.add(numePanel);

        JPanel prenumePanel = new JPanel();
        prenumePanel.setLayout(new BoxLayout(prenumePanel, BoxLayout.Y_AXIS));
        prenumePanel.setBackground(ALB);
        JLabel lblPrenume = new JLabel("Prenume");
        lblPrenume.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblPrenume.setForeground(GRI_TEXT);
        JTextField campPrenume = new JTextField();
        campPrenume.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        campPrenume.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_C, 1),
            new EmptyBorder(8, 10, 8, 10)
        ));
        prenumePanel.add(lblPrenume);
        prenumePanel.add(Box.createVerticalStrut(4));
        prenumePanel.add(campPrenume);
        numeRow.add(prenumePanel);

        formCard.add(numeRow);
        formCard.add(Box.createVerticalStrut(14));
        formCard.add(buildFormField("Adresa de email", false));
        formCard.add(Box.createVerticalStrut(14));
        formCard.add(buildFormField("Parola", true));
        formCard.add(Box.createVerticalStrut(14));
        formCard.add(buildFormField("Oras resedinta", false));
        formCard.add(Box.createVerticalStrut(26));

        JButton btnCreaza = new JButton("Creeaza cont");
        btnCreaza.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnCreaza.setBackground(GREEN_PRIMARY);
        btnCreaza.setForeground(ALB);
        btnCreaza.setBorder(new EmptyBorder(12, 0, 12, 0));
        btnCreaza.setFocusPainted(false);
        btnCreaza.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnCreaza.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
        btnCreaza.setAlignmentX(Component.LEFT_ALIGNMENT);
        // TODO: inlocuieste cu apel JDBC folosind campNume, campPrenume si celelalte
        btnCreaza.addActionListener(e ->
            JOptionPane.showMessageDialog(this,
                "Cont creat! Te poti autentifica acum.",
                "TravaleRo - Bun venit!", JOptionPane.INFORMATION_MESSAGE));
        formCard.add(btnCreaza);
        formCard.add(Box.createVerticalStrut(14));

        JPanel bottomRow = new JPanel(new BorderLayout());
        bottomRow.setBackground(ALB);
        bottomRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 26));
        bottomRow.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblAiCont = new JLabel("Ai deja un cont?");
        lblAiCont.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblAiCont.setForeground(GRI_TEXT);

        JButton btnSpreLogin = new JButton("Autentifica-te");
        btnSpreLogin.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnSpreLogin.setForeground(GREEN_PRIMARY);
        btnSpreLogin.setBackground(ALB);
        btnSpreLogin.setBorder(BorderFactory.createEmptyBorder());
        btnSpreLogin.setFocusPainted(false);
        btnSpreLogin.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnSpreLogin.addActionListener(e -> showRoot("login"));

        bottomRow.add(lblAiCont,    BorderLayout.WEST);
        bottomRow.add(btnSpreLogin, BorderLayout.EAST);
        formCard.add(bottomRow);

        formCard.setPreferredSize(new Dimension(390, 510));
        rightSide.add(formCard);
        page.add(rightSide);
        return page;
    }

    /*
     * buildFormField() - helper reutilizabil: label + camp text sau parola.
     */
    private JPanel buildFormField(String labelText, boolean esteParola) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(ALB);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lbl = new JLabel(labelText);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lbl.setForeground(GRI_TEXT);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(lbl);
        panel.add(Box.createVerticalStrut(5));

        JTextField camp = esteParola ? new JPasswordField() : new JTextField();
        camp.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        camp.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_C, 1),
            new EmptyBorder(9, 11, 9, 11)
        ));
        camp.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        camp.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(camp);
        return panel;
    }

    // ================================================================
    //  APP PANEL - continut principal dupa autentificare
    // ================================================================
    /*
     * buildAppPanel() - construieste aplicatia completa cu topbar + sidebar + pagini.
     * Topbar si sidebar sunt fixe (BorderLayout NORTH si WEST).
     * Zona centrala e un CardLayout cu toate paginile.
     */
    private JPanel buildAppPanel() {
        JPanel app = new JPanel(new BorderLayout());
        app.add(buildTopBar(),  BorderLayout.NORTH);
        app.add(buildSidebar(), BorderLayout.WEST);

        cardLayout = new CardLayout();
        mainPanel  = new JPanel(cardLayout);

        mainPanel.add(buildHomePage(),      "home");
        mainPanel.add(buildSearchPage(),    "search");
        mainPanel.add(buildAtractiiPage(),  "atractii");
        mainPanel.add(buildRezervariPage(), "rezervari");
        mainPanel.add(buildProfilPage(),    "profil");

        // detailPanel e gol - se populeaza la click pe un listing
        detailPanel = new JPanel(new BorderLayout());
        mainPanel.add(detailPanel, "detail");

        app.add(mainPanel, BorderLayout.CENTER);
        showPage("home");
        return app;
    }

    // ================================================================
    //  TOPBAR
    // ================================================================
    /*
     * buildTopBar() - bara de sus fixa.
     * Logo stanga, bara de cautare centru, buton Profil dreapta.
     * Butonul Cauta navigheaza la pagina "search".
     */
    private JPanel buildTopBar() {
        JPanel top = new JPanel(new BorderLayout(16, 0));
        top.setBackground(GREEN_PRIMARY);
        top.setBorder(new EmptyBorder(10, 18, 10, 18));

        // Logo - "Travale" alb, "Ro" galben
        JLabel logo = new JLabel(
            "<html><span style='color:white;font-size:17pt;font-weight:bold;'>" +
            "Travale</span><span style='color:#FFCC00;font-size:17pt;font-weight:bold;'>" +
            "Ro</span><span style='color:#FFCC00;font-size:12pt;'> \u2708</span></html>"
        );
        logo.setPreferredSize(new Dimension(175, 36));
        top.add(logo, BorderLayout.WEST);

        // Bara de cautare
        JPanel searchRow = new JPanel(new BorderLayout());
        searchRow.setBackground(ALB);
        searchRow.setBorder(BorderFactory.createLineBorder(BORDER_C, 1));

        JButton mapBtn = new JButton("   Pe harta");
        mapBtn.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        mapBtn.setForeground(GREEN_DARK);
        mapBtn.setBackground(new Color(245, 255, 245));
        mapBtn.setBorder(new MatteBorder(0, 0, 0, 1, BORDER_C));
        mapBtn.setFocusPainted(false);
        mapBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        mapBtn.setPreferredSize(new Dimension(115, 36));
        searchRow.add(mapBtn, BorderLayout.WEST);

        JTextField searchField = new JTextField("Cauta destinatie, hotel, atractie...");
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        searchField.setForeground(new Color(160, 160, 155));
        searchField.setBorder(new EmptyBorder(0, 12, 0, 12));
        searchField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent e) {
                if (searchField.getText().startsWith("Cauta")) {
                    searchField.setText("");
                    searchField.setForeground(new Color(20, 20, 20));
                }
            }
            public void focusLost(java.awt.event.FocusEvent e) {
                if (searchField.getText().isEmpty()) {
                    searchField.setText("Cauta destinatie, hotel, atractie...");
                    searchField.setForeground(new Color(160, 160, 155));
                }
            }
        });
        searchRow.add(searchField, BorderLayout.CENTER);

        // Butonul Cauta deschide pagina de rezultate
        JButton cautaBtn = new JButton("Cauta");
        cautaBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        cautaBtn.setBackground(GREEN_PRIMARY);
        cautaBtn.setForeground(ALB);
        cautaBtn.setBorder(new EmptyBorder(0, 18, 0, 18));
        cautaBtn.setFocusPainted(false);
        cautaBtn.setPreferredSize(new Dimension(85, 36));
        cautaBtn.addActionListener(e -> {
            clearSidebarSelection();
            showPage("search");
        });
        searchRow.add(cautaBtn, BorderLayout.EAST);
        top.add(searchRow, BorderLayout.CENTER);

        // Buton Profil - galben ca accent diferit de verde
        JButton btnProfil = new JButton("  Profil");
        btnProfil.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnProfil.setForeground(new Color(20, 20, 20));
        btnProfil.setBackground(new Color(255, 204, 0));
        btnProfil.setBorder(new EmptyBorder(7, 16, 7, 16));
        btnProfil.setFocusPainted(false);
        btnProfil.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnProfil.addActionListener(e -> {
            clearSidebarSelection();
            showPage("profil");
        });
        top.add(btnProfil, BorderLayout.EAST);

        return top;
    }

    // ================================================================
    //  SIDEBAR
    // ================================================================
    /*
     * buildSidebar() - panelul lateral stang fix.
     * Sus: filtru de cautare cu locatie, date, persoane, copii, varste copii.
     * Jos: carduri mici cu rezervarile recente.
     */
    private JPanel buildSidebar() {
        JPanel sidebar = new JPanel(new BorderLayout());
        sidebar.setBackground(SIDEBAR_BG);
        sidebar.setPreferredSize(new Dimension(222, 0));

        // ---- Sectiunea de cautare ----
        JPanel searchSection = new JPanel();
        searchSection.setLayout(new BoxLayout(searchSection, BoxLayout.Y_AXIS));
        searchSection.setBackground(SIDEBAR_BG);
        searchSection.setBorder(new EmptyBorder(14, 12, 10, 12));

        // Titlu sectiune
        JLabel searchLabel = new JLabel("CAUTA SEJUR");
        searchLabel.setFont(new Font("Segoe UI", Font.BOLD, 9));
        searchLabel.setForeground(new Color(100, 160, 100));
        searchLabel.setBorder(new EmptyBorder(0, 4, 10, 0));
        searchLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        searchSection.add(searchLabel);

        // --- Locatie ---
        searchSection.add(buildSidebarFieldLabel("\u25BE  Destinatie / Locatie"));
        sidebarLocatieField = buildSidebarTextField("ex: Paris, Iasi...");
        searchSection.add(sidebarLocatieField);
        searchSection.add(Box.createVerticalStrut(8));

        // --- Data inceput ---
        searchSection.add(buildSidebarFieldLabel("\u25BE  Data inceput"));
        sidebarDataInceput = buildSidebarTextField("zz/ll/aaaa");
        searchSection.add(sidebarDataInceput);
        searchSection.add(Box.createVerticalStrut(8));

        // --- Data final ---
        searchSection.add(buildSidebarFieldLabel("\u25BE  Data final"));
        sidebarDataFinal = buildSidebarTextField("zz/ll/aaaa");
        searchSection.add(sidebarDataFinal);
        searchSection.add(Box.createVerticalStrut(8));

        // --- Adulti ---
        searchSection.add(buildSidebarFieldLabel("\u25BE  Adulti"));
        JPanel adultiRow = new JPanel(new BorderLayout(6, 0));
        adultiRow.setBackground(new Color(18, 42, 18));
        adultiRow.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(40, 80, 40), 1),
            new EmptyBorder(4, 8, 4, 8)
        ));
        adultiRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        adultiRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel adultiIcon = new JLabel("\uD83D\uDC64");
        adultiIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 13));
        adultiRow.add(adultiIcon, BorderLayout.WEST);
        sidebarAdulti = new JSpinner(new SpinnerNumberModel(2, 1, 20, 1));
        styleSpinner(sidebarAdulti);
        adultiRow.add(sidebarAdulti, BorderLayout.CENTER);
        searchSection.add(adultiRow);
        searchSection.add(Box.createVerticalStrut(8));

        // --- Copii ---
        searchSection.add(buildSidebarFieldLabel("\u25BE  Copii (sub 18 ani)"));
        JPanel copiiRow = new JPanel(new BorderLayout(6, 0));
        copiiRow.setBackground(new Color(18, 42, 18));
        copiiRow.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(40, 80, 40), 1),
            new EmptyBorder(4, 8, 4, 8)
        ));
        copiiRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        copiiRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel copiiIcon = new JLabel("\uD83D\uDC66");
        copiiIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 13));
        copiiRow.add(copiiIcon, BorderLayout.WEST);
        sidebarCopii = new JSpinner(new SpinnerNumberModel(0, 0, 10, 1));
        styleSpinner(sidebarCopii);
        copiiRow.add(sidebarCopii, BorderLayout.CENTER);
        searchSection.add(copiiRow);
        searchSection.add(Box.createVerticalStrut(8));

        // --- Varste copii (panel dinamic) ---
        JLabel varsteLabel = buildSidebarFieldLabel("\u25BE  Varste copii");
        varsteLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        searchSection.add(varsteLabel);

        sidebarVarstePanel = new JPanel();
        sidebarVarstePanel.setLayout(new BoxLayout(sidebarVarstePanel, BoxLayout.Y_AXIS));
        sidebarVarstePanel.setBackground(SIDEBAR_BG);
        sidebarVarstePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        searchSection.add(sidebarVarstePanel);
        searchSection.add(Box.createVerticalStrut(4));

        // Listener: la schimbarea numarului de copii, actualizez campurile de varsta
        sidebarCopii.addChangeListener(e -> updateVarsteCopiiForms());
        updateVarsteCopiiForms(); // initializare

        searchSection.add(Box.createVerticalStrut(12));

        // --- Buton Cauta ---
        JButton btnCautaSidebar = new JButton("  Cauta sejur  >");
        btnCautaSidebar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnCautaSidebar.setBackground(GREEN_PRIMARY);
        btnCautaSidebar.setForeground(ALB);
        btnCautaSidebar.setBorder(new EmptyBorder(10, 0, 10, 0));
        btnCautaSidebar.setFocusPainted(false);
        btnCautaSidebar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnCautaSidebar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        btnCautaSidebar.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnCautaSidebar.setOpaque(true);
        btnCautaSidebar.addActionListener(e -> {
            clearSidebarSelection();
            showPage("search");
        });
        searchSection.add(btnCautaSidebar);

        sidebar.add(searchSection, BorderLayout.CENTER);

        // ---- Rezervari recente - jos in sidebar ----
        JPanel rezSection = new JPanel();
        rezSection.setLayout(new BoxLayout(rezSection, BoxLayout.Y_AXIS));
        rezSection.setBackground(SIDEBAR_BG);
        rezSection.setBorder(new EmptyBorder(0, 0, 12, 0));

        JSeparator sep = new JSeparator();
        sep.setForeground(new Color(20, 60, 20));
        sep.setMaximumSize(new Dimension(222, 1));
        rezSection.add(sep);
        rezSection.add(Box.createVerticalStrut(10));

        JLabel secLabel = new JLabel("  REZERVARI RECENTE");
        secLabel.setFont(new Font("Segoe UI", Font.BOLD, 9));
        secLabel.setForeground(new Color(100, 160, 100));
        secLabel.setBorder(new EmptyBorder(0, 12, 6, 0));
        rezSection.add(secLabel);

        rezSection.add(createSidebarCard("Hotel Traian", "15-18 Mai", "Confirmata"));
        rezSection.add(Box.createVerticalStrut(4));
        rezSection.add(createSidebarCard("Copou View",   "2-5 Iun",  "Asteptare"));
        rezSection.add(Box.createVerticalStrut(4));
        rezSection.add(createSidebarCard("Casa Pogor",   "10 Iul",   "Confirmata"));

        sidebar.add(rezSection, BorderLayout.SOUTH);
        return sidebar;
    }

    /*
     * buildSidebarFieldLabel() - label mic verde deschis pentru fiecare camp din sidebar.
     */
    private JLabel buildSidebarFieldLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lbl.setForeground(new Color(140, 190, 140));
        lbl.setBorder(new EmptyBorder(0, 2, 3, 0));
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        return lbl;
    }

    /*
     * buildSidebarTextField() - camp text stilizat pentru sidebar inchis la culoare.
     */
    private JTextField buildSidebarTextField(String placeholder) {
        JTextField field = new JTextField(placeholder);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        field.setForeground(new Color(120, 160, 120));
        field.setBackground(new Color(18, 42, 18));
        field.setCaretColor(GREEN_LIGHT);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(40, 80, 40), 1),
            new EmptyBorder(6, 8, 6, 8)
        ));
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        field.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent e) {
                if (field.getText().equals(placeholder)) {
                    field.setText("");
                    field.setForeground(ALB);
                }
            }
            public void focusLost(java.awt.event.FocusEvent e) {
                if (field.getText().isEmpty()) {
                    field.setText(placeholder);
                    field.setForeground(new Color(120, 160, 120));
                }
            }
        });
        return field;
    }

    /*
     * styleSpinner() - aplica stilul inchis pe un JSpinner pentru sidebar.
     */
    private void styleSpinner(JSpinner spinner) {
        spinner.setFont(new Font("Segoe UI", Font.BOLD, 13));
        spinner.setBackground(new Color(18, 42, 18));
        spinner.setForeground(ALB);
        spinner.setBorder(BorderFactory.createEmptyBorder());
        JComponent editor = spinner.getEditor();
        if (editor instanceof JSpinner.DefaultEditor) {
            JTextField tf = ((JSpinner.DefaultEditor) editor).getTextField();
            tf.setBackground(new Color(18, 42, 18));
            tf.setForeground(ALB);
            tf.setFont(new Font("Segoe UI", Font.BOLD, 13));
            tf.setBorder(new EmptyBorder(0, 4, 0, 0));
        }
    }

    /*
     * updateVarsteCopiiForms() - genereaza dinamic cate un spinner de varsta
     * pentru fiecare copil selectat. Daca 0 copii, afiseaza un mesaj discret.
     */
    private void updateVarsteCopiiForms() {
        sidebarVarstePanel.removeAll();
        int nrCopii = (Integer) sidebarCopii.getValue();
        if (nrCopii == 0) {
            JLabel lblNone = new JLabel("Niciun copil selectat");
            lblNone.setFont(new Font("Segoe UI", Font.ITALIC, 10));
            lblNone.setForeground(new Color(80, 120, 80));
            lblNone.setBorder(new EmptyBorder(2, 4, 4, 0));
            sidebarVarstePanel.add(lblNone);
        } else {
            for (int i = 1; i <= nrCopii; i++) {
                JPanel row = new JPanel(new BorderLayout(6, 0));
                row.setBackground(new Color(14, 34, 14));
                row.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(35, 70, 35), 1),
                    new EmptyBorder(3, 8, 3, 8)
                ));
                row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
                row.setAlignmentX(Component.LEFT_ALIGNMENT);
                JLabel lblCopil = new JLabel("Copil " + i + ":");
                lblCopil.setFont(new Font("Segoe UI", Font.PLAIN, 11));
                lblCopil.setForeground(new Color(140, 190, 140));
                row.add(lblCopil, BorderLayout.WEST);
                JSpinner spVarsta = new JSpinner(new SpinnerNumberModel(5, 0, 17, 1));
                styleSpinner(spVarsta);
                JLabel lblAni = new JLabel(" ani");
                lblAni.setFont(new Font("Segoe UI", Font.PLAIN, 11));
                lblAni.setForeground(new Color(120, 160, 120));
                JPanel rightPart = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
                rightPart.setBackground(new Color(14, 34, 14));
                rightPart.add(spVarsta);
                rightPart.add(lblAni);
                row.add(rightPart, BorderLayout.EAST);
                sidebarVarstePanel.add(row);
                if (i < nrCopii) sidebarVarstePanel.add(Box.createVerticalStrut(3));
            }
        }
        sidebarVarstePanel.revalidate();
        sidebarVarstePanel.repaint();
    }

    // Buton de navigare in sidebar (pastrat pentru compatibilitate interna - butonul Home)
    private JButton createNavButton(String text, boolean activ) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btn.setForeground(activ ? ALB : new Color(140, 180, 140));
        btn.setBackground(activ ? new Color(0, 100, 0, 80) : SIDEBAR_BG);
        btn.setBorder(new MatteBorder(0, activ ? 3 : 0, 0, 0, GREEN_PRIMARY));
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setMaximumSize(new Dimension(222, 44));
        btn.setOpaque(true);
        return btn;
    }

    private void setActiveNavButton(JButton active) {
        if (btnHome != null) {
            btnHome.setForeground(ALB);
            btnHome.setBackground(new Color(0, 100, 0, 80));
            btnHome.setBorder(new MatteBorder(0, 3, 0, 0, GREEN_PRIMARY));
        }
    }

    private void clearSidebarSelection() {
        // sidebar-ul nu mai are butoane de navigare activa - metoda pastrata pentru compatibilitate
    }

    // Card mic de rezervare in sidebar
    private JPanel createSidebarCard(String hotel, String date, String status) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(SIDEBAR_SEC);
        card.setBorder(new EmptyBorder(8, 16, 8, 16));
        card.setMaximumSize(new Dimension(205, 72));

        JLabel lblHotel = new JLabel(hotel);
        lblHotel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblHotel.setForeground(ALB);

        JLabel lblDate = new JLabel(date);
        lblDate.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblDate.setForeground(new Color(150, 200, 150));

        Color sColor = status.equals("Confirmata")
            ? new Color(80, 220, 100)
            : new Color(255, 180, 40);
        JLabel lblStatus = new JLabel("\u25CF " + status);
        lblStatus.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        lblStatus.setForeground(sColor);

        card.add(lblHotel);
        card.add(Box.createVerticalStrut(2));
        card.add(lblDate);
        card.add(Box.createVerticalStrut(2));
        card.add(lblStatus);
        return card;
    }

    // ================================================================
    //  HOME PAGE
    // ================================================================
    /*
     * buildHomePage() - pagina cu listinguri de hoteluri.
     * Sus: bara de filtre interactiva + bara de context cu sortare.
     * Centru: lista de carduri scrollabila.
     */
    private JPanel buildHomePage() {
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

        JLabel lblSep = new JLabel("\u00B7");
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

        lista.add(createListingCard("Hotel Traian",       "Piata Unirii 1, Iasi",
            "Hotel emblematic cu vedere la Palatul Culturii. Spa, restaurant si bar.",
            "4.3", "128", "185", true,  new Color(180,  60,  60)));
        lista.add(Box.createVerticalStrut(12));
        lista.add(createListingCard("Pensiunea La Conac", "Str. Lapusneanu 14, Iasi",
            "Pensiune boutique cu gradina interioara, mic dejun inclus si parcare.",
            "4.8", "74",  "95",  false, new Color( 60, 110, 180)));
        lista.add(Box.createVerticalStrut(12));
        lista.add(createListingCard("Apartament Copou View", "Bd. Carol I 22, Copou",
            "Apartament modern 2 camere, la 5 minute de Gradina Copou si Universitate.",
            "4.1", "39",  "60",  false, new Color( 40, 140, 100)));
        lista.add(Box.createVerticalStrut(12));
        lista.add(createListingCard("Casa Pogor Suites",  "Str. Vasile Pogor 4, Iasi",
            "Cazare de lux in casa istorica Pogor restaurata. Mic dejun gourmet inclus.",
            "4.9", "51",  "220", false, new Color(130,  60, 160)));

        JScrollPane scroll = new JScrollPane(lista);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(BG_PAGE);
        scroll.getVerticalScrollBar().setUnitIncrement(20);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        page.add(scroll, BorderLayout.CENTER);
        return page;
    }

    /*
     * buildFiltersBar() - bara de filtre interactiva.
     * Click pe un chip il activeaza si il dezactiveaza pe cel anterior.
     */
    private JPanel buildFiltersBar() {
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

    /*
     * createListingCard() - card polish pentru un listing.
     * WEST:   imagine colorata unica + badge
     * CENTER: titlu, locatie, rating cu scor, tag-uri facilitati, descriere
     * EAST:   pret + buton Rezerva (navigheaza la detail)
     */
    private JPanel createListingCard(String titlu, String locatie, String descriere,
                                      String rating, String nrRec, String pret,
                                      boolean topAles, Color imgColor) {
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

        JLabel imgIcon = new JLabel("\uD83C\uDFE8", SwingConstants.CENTER);
        imgIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 36));
        imgIcon.setForeground(new Color(255, 255, 255, 140));
        imgZone.add(imgIcon, BorderLayout.CENTER);

        JLabel badge = new JLabel(topAles ? "  \u2605 Top ales  " : "  Proprietate  ",
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

        JLabel lblL = new JLabel("\uD83D\uDCCD " + locatie);
        lblL.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblL.setForeground(new Color(0, 100, 180));
        lblL.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Rating row: scor verde + stele galbene + nr recenzii
        int stele = (int) Math.round(Double.parseDouble(rating));
        JPanel ratingRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        ratingRow.setBackground(ALB);
        ratingRow.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel scoreBox = new JLabel(" " + rating + " ");
        scoreBox.setFont(new Font("Segoe UI", Font.BOLD, 12));
        scoreBox.setForeground(ALB);
        scoreBox.setBackground(GREEN_DARK);
        scoreBox.setOpaque(true);

        JLabel steleLabel = new JLabel("\u2605".repeat(stele) + "\u2606".repeat(5 - stele));
        steleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        steleLabel.setForeground(ORANGE_STAR);

        JLabel recLabel = new JLabel("\u00B7 " + nrRec + " recenzii");
        recLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        recLabel.setForeground(GRI_TEXT);

        ratingRow.add(scoreBox);
        ratingRow.add(steleLabel);
        ratingRow.add(recLabel);

        // Tag-uri facilitati
        JPanel tagsRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        tagsRow.setBackground(ALB);
        tagsRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        String[] tags = topAles
            ? new String[]{"Wi-Fi", "Mic dejun", "Spa"}
            : new String[]{"Wi-Fi", "Parcare"};
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

        JLabel lblP = new JLabel(pret + "\u20AC");
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

        // Buton Rezerva - verde aprins, navigheaza la pagina de detalii
        JButton btnRez = new JButton("Rezerva");
        btnRez.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnRez.setBackground(GREEN_PRIMARY);
        btnRez.setForeground(ALB);
        btnRez.setBorder(new EmptyBorder(9, 0, 9, 0));
        btnRez.setFocusPainted(false);
        btnRez.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnRez.setPreferredSize(new Dimension(120, 36));
        btnRez.addActionListener(e ->
            navigateToDetail(titlu, locatie, descriere, rating, nrRec, pret));

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

    // ================================================================
    //  SEARCH PAGE
    // ================================================================
    /*
     * buildSearchPage() - pagina de rezultate dupa apasarea butonului Cauta.
     * Afiseaza trei sectiuni: Cazari, Atractii, Restaurante.
     * Fiecare are un header verde si carduri de listing.
     * Butonul "Rezerva" din fiecare card navigheaza la detalii.
     */
    private JPanel buildSearchPage() {
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
        btnBack.addActionListener(e -> {
            setActiveNavButton(btnHome);
            showPage("home");
        });
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
        content.add(createSectionTitle("Cazari gasite"));
        content.add(Box.createVerticalStrut(10));
        String[][] cazari = {
            {"Hotel Central",    "Centru, Iasi", "Hotel modern cu vedere panoramica.",  "4.5", "89",  "160"},
            {"Vila Panoramic",   "Tatarasi",     "Vila cu gradina si piscina privata.", "4.2", "45",  "120"},
            {"Hostel Backpacker","Centru, Iasi", "Cazare accesibila in centrul orasului.","3.8","210","35"},
        };
        for (String[] c : cazari) {
            content.add(createListingCard(c[0], c[1], c[2], c[3], c[4], c[5], false,
                new Color(60, 130, 180)));
            content.add(Box.createVerticalStrut(10));
        }

        // Sectiunea Atractii
        content.add(Box.createVerticalStrut(16));
        content.add(createSectionTitle("Atractii in zona"));
        content.add(Box.createVerticalStrut(10));
        String[][] atractii = {
            {"Palatul Culturii",     "Centru, Iasi", "4 muzee nationale sub acelasi acoperis.", "4.8", "1200", "0"},
            {"Gradina Botanica",     "Copou",         "10.000 de specii de plante.",              "4.6", "850",  "0"},
            {"Catedrala Mitropolitana","Centru",       "Cel mai mare lacas ortodox din Moldova.", "4.9", "2100", "0"},
        };
        for (String[] a : atractii) {
            content.add(createListingCard(a[0], a[1], a[2], a[3], a[4], a[5], false,
                new Color(40, 140, 80)));
            content.add(Box.createVerticalStrut(10));
        }

        // Sectiunea Restaurante
        content.add(Box.createVerticalStrut(16));
        content.add(createSectionTitle("Restaurante recomandate"));
        content.add(Box.createVerticalStrut(10));
        String[][] restaurante = {
            {"Bolta Rece",       "Str. Rece 10",  "Restaurant traditional moldovenesc.",  "4.7", "560", "45"},
            {"La Mama",          "Centru, Iasi",  "Bucatarie romaneasca autentica.",       "4.5", "890", "55"},
            {"Vivo Cafe",        "Copou",          "Cafenea si bistro cu terasa.",          "4.3", "320", "30"},
        };
        for (String[] r : restaurante) {
            content.add(createListingCard(r[0], r[1], r[2], r[3], r[4], r[5], false,
                new Color(180, 80, 40)));
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

    // Header de sectiune verde pentru pagina de search
    private JPanel createSectionTitle(String text) {
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

    // ================================================================
    //  ATRACTII PAGE
    // ================================================================
    /*
     * buildAtractiiPage() - grid 2 coloane cu atractii turistice.
     * Fiecare card are o bara colorata unica in stanga.
     */
    private JPanel buildAtractiiPage() {
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
        header.add(titlu, BorderLayout.NORTH);
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

    private JPanel createAtractieCard(String nume, String desc, String program,
                                       String distanta, Color culoare) {
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

    // ================================================================
    //  REZERVARI PAGE
    // ================================================================
    private JPanel buildRezervariPage() {
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

        lista.add(createRezervareCard("Hotel Traian", "Piata Unirii 1, Iasi",
            "15 Mai 2025", "18 Mai 2025", "3 nopti", "555\u20AC", "Confirmata"));
        lista.add(Box.createVerticalStrut(10));
        lista.add(createRezervareCard("Apartament Copou View", "Bd. Carol I 22, Iasi",
            "2 Iun 2025",  "5 Iun 2025",  "3 nopti", "180\u20AC", "Asteptare"));
        lista.add(Box.createVerticalStrut(10));
        lista.add(createRezervareCard("Casa Pogor Suites", "Str. Vasile Pogor 4, Iasi",
            "10 Iul 2025", "12 Iul 2025", "2 nopti", "440\u20AC", "Confirmata"));

        JScrollPane scroll = new JScrollPane(lista);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(WARM_BG);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        page.add(scroll, BorderLayout.CENTER);
        return page;
    }

    private JPanel createRezervareCard(String hotel, String adresa, String checkIn,
                                        String checkOut, String durata,
                                        String total, String status) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(ALB);
        card.setBorder(BorderFactory.createLineBorder(BORDER_C, 1));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 112));

        // Bara colorata stanga: verde pentru confirmat, portocaliu pentru asteptare
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

        JLabel lblD = new JLabel("  " + checkIn + "  \u2192  " + checkOut + "  (" + durata + ")");
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
        JLabel lblS = new JLabel("\u25CF " + status);
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

    // ================================================================
    //  PROFIL PAGE
    // ================================================================
    /*
     * buildProfilPage() - pagina de profil completa.
     * Header verde cu avatar initiale, badge nivel, buton Editeaza.
     * 4 carduri statistici cu accent colorat unic.
     * Sectiuni tabel: Informatii cont, Preferinte, Calatorii recente.
     */
    private JPanel buildProfilPage() {
        JPanel page = new JPanel(new BorderLayout());
        page.setBackground(BG_PAGE);

        // Header verde
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

        // Badge nivel - galben pe fond verde
        JLabel lblNivel = new JLabel("  Calator Frecvent  ");
        lblNivel.setFont(new Font("Segoe UI", Font.BOLD, 10));
        lblNivel.setForeground(GREEN_DARK);
        lblNivel.setBackground(new Color(255, 204, 0));
        lblNivel.setOpaque(true);
        lblNivel.setBorder(new EmptyBorder(2, 0, 2, 0));

        JLabel lblMembru = new JLabel("Membru din Ianuarie 2024  \u00B7  Iasi, Romania");
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

        // 4 carduri statistici cu accent colorat
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

        content.add(buildProfilSection("Informatii cont", new String[][]{
            {"Nume complet",   "Alexandru Ionescu"},
            {"Email",          "alex.ionescu@email.ro"},
            {"Telefon",        "+40 712 345 678"},
            {"Oras resedinta", "Iasi, Romania"},
            {"Limba",          "Romana"},
        }));
        content.add(Box.createVerticalStrut(14));

        content.add(buildProfilSection("Preferinte de calatorie", new String[][]{
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
            {"Paris, Franta",     "Hotel Grand Opera",   "Mar 2025", "4.7"},
            {"Roma, Italia",      "Colosseum Suites",    "Ian 2025", "4.5"},
            {"Praga, Cehia",      "Prague City Center",  "Oct 2024", "4.8"},
            {"Barcelona, Spania", "Barceloneta Beach",   "Iun 2024", "4.3"},
        };

        for (String[] c : calatorii) {
            JPanel row = new JPanel(new BorderLayout(10, 0));
            row.setBackground(ALB);
            row.setBorder(new MatteBorder(0, 0, 1, 0, new Color(240, 240, 240)));

            JPanel rowLeft = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
            rowLeft.setBackground(ALB);
            JLabel dot = new JLabel("\u25CF");
            dot.setForeground(GREEN_PRIMARY);
            JLabel dest = new JLabel(c[0]);
            dest.setFont(new Font("Segoe UI", Font.BOLD, 13));
            dest.setForeground(new Color(15, 15, 15));
            JLabel hotel = new JLabel("\u00B7 " + c[1]);
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

    /*
     * buildProfilSection() - sectiune tabel cu randuri label:valoare.
     */
    private JPanel buildProfilSection(String titluSectiune, String[][] randuri) {
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

    /*
     * createStatCard() - card numeric cu linie colorata sus.
     */
    private JPanel createStatCard(String valoare, String eticheta, Color accentColor) {
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

    // ================================================================
    //  DETAIL PAGE
    // ================================================================
    /*
     * navigateToDetail() - populeaza detailPanel si navigheaza la "detail".
     * Apelata din butonul "Rezerva" al oricarui listing card.
     */
    private void navigateToDetail(String titlu, String locatie, String descriere,
                                   String rating, String nrRec, String pret) {
        detailPanel.removeAll();
        detailPanel.add(buildDetailPage(titlu, locatie, descriere, rating, nrRec, pret));
        detailPanel.revalidate();
        detailPanel.repaint();
        clearSidebarSelection();
        showPage("detail");
    }

    /*
     * buildDetailPage() - pagina de detalii a unui anunt.
     * Header verde cu buton Inapoi.
     * Imagine placeholder, descriere, facilitati, sectiune rezervare.
     */
    private JPanel buildDetailPage(String titlu, String locatie, String descriere,
                                    String rating, String nrRec, String pret) {
        JPanel page = new JPanel(new BorderLayout());
        page.setBackground(WARM_BG);

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
        btnBack.addActionListener(e -> {
            setActiveNavButton(btnHome);
            showPage("home");
        });
        header.add(btnBack, BorderLayout.WEST);

        JLabel lblH = new JLabel(titlu);
        lblH.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblH.setForeground(ALB);
        header.add(lblH, BorderLayout.CENTER);
        page.add(header, BorderLayout.NORTH);

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
        JLabel lblR = new JLabel("\u2605".repeat(stele) + "\u2606".repeat(5 - stele)
            + "  " + rating + " din 5  \u00B7  " + nrRec + " recenzii");
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
            "<html><body style='width:680px;font-size:12pt;color:#3a3a3a'>" + descriere
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
        JLabel lblPretMare = new JLabel(pret + "\u20AC");
        lblPretMare.setFont(new Font("Segoe UI", Font.BOLD, 30));
        lblPretMare.setForeground(ORANGE_PRICE);
        JLabel lblPN = new JLabel("per noapte, taxe incluse");
        lblPN.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblPN.setForeground(GRI_TEXT);
        pretCard.add(lblPretMare, BorderLayout.CENTER);
        pretCard.add(lblPN, BorderLayout.SOUTH);
        rezervRow.add(pretCard);

        JButton btnRezMare = new JButton("  Rezerva acum  >");
        btnRezMare.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnRezMare.setBackground(GREEN_PRIMARY);
        btnRezMare.setForeground(ALB);
        btnRezMare.setBorder(new EmptyBorder(16, 30, 16, 30));
        btnRezMare.setFocusPainted(false);
        btnRezMare.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnRezMare.addActionListener(e ->
            JOptionPane.showMessageDialog(this,
                "Rezervare initiata!\n\n" + titlu + "\n" + locatie + "\nPret: " + pret + "\u20AC / noapte",
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