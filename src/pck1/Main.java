package pck1;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

/*
 * Gui_Aplicatie - fereastra principala a aplicatiei TravaleRo.
 *
 * Paleta Booking-style - rosu pur, saturat, profesional:
 *
 *   RED_PRIMARY  = #CC0000  rosu curat si puternic - culoarea principala
 *   RED_DARK     = #990000  rosu inchis pentru hover, topbar, sidebar header
 *   RED_LIGHT    = #FF3333  rosu deschis pentru accente minore
 *   ORANGE_PRICE = #FF6600  portocaliu pentru preturi - iese in evidenta pe alb
 *   ORANGE_STAR  = #FF9900  portocaliu deschis pentru stele si rating
 *   BG_PAGE      = #F2F2F2  gri foarte deschis pentru fundalul paginii (ca Booking)
 *   SIDEBAR_BG   = #1A0000  negru cu tenta rosie - contrast maxim cu RED_PRIMARY
 *   SIDEBAR_SEC  = #2D0000  varianta mai deschisa pentru carduri sidebar
 *   GRI_TEXT     = #6B6B6B  gri neutru pentru texte secundare
 *   BORDER_C     = #E0E0E0  bordura neutra, curata
 */
public class Main extends JFrame {

    private static final Color RED_PRIMARY  = new Color(204,   0,   0);
    private static final Color RED_DARK     = new Color(153,   0,   0);
    private static final Color RED_LIGHT    = new Color(255,  51,  51);
    private static final Color ORANGE_PRICE = new Color(255, 102,   0);
    private static final Color ORANGE_STAR  = new Color(255, 153,   0);
    private static final Color BG_PAGE      = new Color(242, 242, 242);
    private static final Color SIDEBAR_BG   = new Color( 26,   0,   0);
    private static final Color SIDEBAR_SEC  = new Color( 45,   0,   0);
    private static final Color GRI_TEXT     = new Color(107, 107, 107);
    private static final Color BORDER_C     = new Color(224, 224, 224);
    private static final Color ALB          = Color.WHITE;

    // Alias-uri pentru compatibilitate cu codul existent (nu mai trebuie schimbat in tot fisierul)
    private static final Color ROS_VIU      = RED_PRIMARY;
    private static final Color ROS_HOVER    = RED_DARK;
    private static final Color PORTOCALIU   = ORANGE_PRICE;
    private static final Color PORTOCALIU_2 = ORANGE_STAR;
    private static final Color WARM_BG      = BG_PAGE;
    private static final Color CARD_BG      = ALB;

    // CardLayout la nivel de JFrame - comuta intre login si aplicatie
    private CardLayout rootLayout;
    private JPanel     rootPanel;

    // CardLayout pentru paginile din interiorul aplicatiei
    private CardLayout cardLayout;
    private JPanel     mainPanel;
    private JPanel     detailPanel;
    private JButton    btnHome, btnAtractii, btnRezervari;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                Main frame = new Main();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public Main() {
        setTitle("TravaleRo - Travel Planner");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 700);
        setLocationRelativeTo(null);

        // rootPanel contine DOUA straturi:
        //   "login" = ecranul de autentificare (fara topbar/sidebar)
        //   "app"   = aplicatia completa cu topbar + sidebar + pagini
        rootLayout = new CardLayout();
        rootPanel  = new JPanel(rootLayout);

        // Pagina de login - se vede prima
        rootPanel.add(buildLoginPage(),    "login");

        // Pagina de register - accesibila din butonul "Creeaza cont" de pe login
        rootPanel.add(buildRegisterPage(), "register");

        // Aplicatia completa - ascunsa pana la autentificare
        rootPanel.add(buildAppPanel(), "app");

        setContentPane(rootPanel);

        // Pornim pe ecranul de login
        showRoot("login");
    }

    /*
     * buildLoginPage() - ecranul de autentificare.
     *
     * Layout: tot ecranul e impartit in doua coloane cu GridLayout:
     *   - Stanga: panel rosu cu logo-ul mare si un slogan
     *   - Dreapta: formularul de login (email, parola, butoane)
     *
     * Simplu intentionat - fara date reale, fara validare complexa.
     * La click pe "Intra" se trece direct la aplicatie.
     */
    private JPanel buildLoginPage() {
        JPanel page = new JPanel(new GridLayout(1, 2, 0, 0));

        // Jumatatea stanga - branding colorat
        JPanel leftSide = new JPanel(new GridBagLayout());
        leftSide.setBackground(ROS_VIU);

        JPanel brandContent = new JPanel();
        brandContent.setLayout(new BoxLayout(brandContent, BoxLayout.Y_AXIS));
        brandContent.setBackground(ROS_VIU);

        // Logo mare
        JLabel logoMare = new JLabel(
            "<html><div style='text-align:center;'>" +
            "<span style='color:white;font-size:42pt;font-weight:bold;'>Travale</span>" +
            "<span style='color:#FF9900;font-size:42pt;font-weight:bold;'>Ro</span>" +
            "</div></html>"
        );
        logoMare.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Icona avion sub logo
        JLabel avion = new JLabel("✈", SwingConstants.CENTER);
        avion.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 48));
        avion.setForeground(new Color(255, 200, 180));
        avion.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Slogan sub logo
        JLabel slogan = new JLabel(
            "<html><div style='text-align:center;color:#FFD0C0;font-size:13pt;'>" +
            "Planifica-ti urmatoarea aventura<br>din inima Iasului spre lume." +
            "</div></html>"
        );
        slogan.setAlignmentX(Component.CENTER_ALIGNMENT);

        brandContent.add(Box.createVerticalGlue());
        brandContent.add(logoMare);
        brandContent.add(Box.createVerticalStrut(8));
        brandContent.add(avion);
        brandContent.add(Box.createVerticalStrut(24));
        brandContent.add(slogan);
        brandContent.add(Box.createVerticalGlue());

        leftSide.add(brandContent);
        page.add(leftSide);

        // Jumatatea dreapta - formularul de login
        JPanel rightSide = new JPanel(new GridBagLayout());
        rightSide.setBackground(WARM_BG);

        // Cardul alb cu formularul - centrat in dreapta
        JPanel formCard = new JPanel();
        formCard.setLayout(new BoxLayout(formCard, BoxLayout.Y_AXIS));
        formCard.setBackground(ALB);
        formCard.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_C, 1),
            new EmptyBorder(40, 44, 32, 44)
        ));

        // Titlul formularului
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

        // Label + camp email
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

        // Label + camp parola
        JLabel lblParola = new JLabel("Parola");
        lblParola.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblParola.setForeground(GRI_TEXT);
        lblParola.setAlignmentX(Component.LEFT_ALIGNMENT);
        formCard.add(lblParola);
        formCard.add(Box.createVerticalStrut(5));

        // JPasswordField ascunde caracterele introduse cu puncte
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

        // Butonul principal de login - lat, rosu aprins
        JButton btnLogin = new JButton("Intra in cont");
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnLogin.setBackground(ROS_VIU);
        btnLogin.setForeground(ALB);
        btnLogin.setBorder(new EmptyBorder(12, 0, 12, 0));
        btnLogin.setFocusPainted(false);
        btnLogin.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnLogin.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
        btnLogin.setAlignmentX(Component.LEFT_ALIGNMENT);

        // La click trecem la aplicatie - in practica aici ar fi validarea credentialelor
        btnLogin.addActionListener(e -> showRoot("app"));

        // Apasarea Enter pe oricare camp face acelasi lucru ca butonul Login
        campEmail.addActionListener(e -> showRoot("app"));
        campParola.addActionListener(e -> showRoot("app"));

        formCard.add(btnLogin);
        formCard.add(Box.createVerticalStrut(8));

        // Buton t MODE - vizibil doar in timpul dezvoltarii.
        // Sare direct la o pagina specifica fara credentiale.
        // Sterge sau comenteaza acest bloc inainte de productie.
        JPanel devPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 6, 0));
        devPanel.setBackground(ALB);
        devPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        devPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 28));

        JLabel devLabel = new JLabel("[DEV]");
        devLabel.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        devLabel.setForeground(new Color(180, 180, 180));
        devPanel.add(devLabel);

        // Un buton mic pentru fiecare pagina - click sare direct acolo
        for (String[] p : new String[][]{
            {"Home",      "home"},
            {"Atractii",  "atractii"},
            {"Rezervari", "rezervari"},
            {"Profil",    "profil"}
        }) {
            JButton b = new JButton(p[0]);
            b.setFont(new Font("Segoe UI", Font.PLAIN, 10));
            b.setForeground(new Color(150, 150, 150));
            b.setBackground(new Color(245, 245, 245));
            b.setBorder(BorderFactory.createLineBorder(new Color(210, 210, 210)));
            b.setFocusPainted(false);
            b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            final String pageName = p[1];
            b.addActionListener(e -> {
                // Intra in app si naviga direct la pagina dorita
                showRoot("app");
                showPage(pageName);
            });
            devPanel.add(b);
        }
        formCard.add(devPanel);
        formCard.add(Box.createVerticalStrut(16));

        // Rand de jos: Register (stanga) si Forgot password (dreapta)
        // Folosim BorderLayout pe un panel de 1 rand pentru a-i pozitiona la capete
        JPanel bottomRow = new JPanel(new BorderLayout());
        bottomRow.setBackground(ALB);
        bottomRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 28));
        bottomRow.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton btnRegister = new JButton("Creeaza cont");
        btnRegister.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnRegister.setForeground(PORTOCALIU);
        btnRegister.setBackground(ALB);
        btnRegister.setBorder(BorderFactory.createEmptyBorder());
        btnRegister.setFocusPainted(false);
        btnRegister.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        // Navigam la pagina de register
        btnRegister.addActionListener(e -> showRoot("register"));

        JButton btnForgot = new JButton("Ai uitat parola?");
        btnForgot.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnForgot.setForeground(GRI_TEXT);
        btnForgot.setBackground(ALB);
        btnForgot.setBorder(BorderFactory.createEmptyBorder());
        btnForgot.setFocusPainted(false);
        btnForgot.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnForgot.addActionListener(e ->
            JOptionPane.showMessageDialog(this,
                "Resetare parola - in curand!",
                "TravaleRo", JOptionPane.INFORMATION_MESSAGE)
        );

        bottomRow.add(btnRegister, BorderLayout.WEST);
        bottomRow.add(btnForgot,   BorderLayout.EAST);
        formCard.add(bottomRow);

        // Fixam latimea cardului de login
        formCard.setPreferredSize(new Dimension(380, 440));
        rightSide.add(formCard);
        page.add(rightSide);

        return page;
    }

    /*
     * buildRegisterPage() - ecranul de creare cont nou.
     *
     * Acelasi layout ca loginul: stanga branding, dreapta formular.
     * Campuri: Nume, Prenume, Email, Parola, Oras resedinta.
     * Butonul "Inapoi la login" duce inapoi la "login".
     * Butonul "Creeaza cont" va trimite datele la baza de date (de conectat ulterior).
     */
    private JPanel buildRegisterPage() {
        JPanel page = new JPanel(new GridLayout(1, 2, 0, 0));

        // Jumatatea stanga - acelasi branding ca la login
        JPanel leftSide = new JPanel(new GridBagLayout());
        leftSide.setBackground(PORTOCALIU);

        JPanel brandContent = new JPanel();
        brandContent.setLayout(new BoxLayout(brandContent, BoxLayout.Y_AXIS));
        brandContent.setBackground(PORTOCALIU);

        JLabel logoMare = new JLabel(
            "<html><div style='text-align:center;'>" +
            "<span style='color:white;font-size:40pt;font-weight:bold;'>Travale</span>" +
            "<span style='color:#FFD080;font-size:40pt;font-weight:bold;'>Ro</span>" +
            "</div></html>"
        );
        logoMare.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel avion = new JLabel("✈", SwingConstants.CENTER);
        avion.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 46));
        avion.setForeground(new Color(255, 230, 180));
        avion.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel slogan = new JLabel(
            "<html><div style='text-align:center;color:#FFE5CC;font-size:13pt;'>" +
            "Alatura-te comunitatii TravaleRo<br>si descopera lumea." +
            "</div></html>"
        );
        slogan.setAlignmentX(Component.CENTER_ALIGNMENT);

        brandContent.add(Box.createVerticalGlue());
        brandContent.add(logoMare);
        brandContent.add(Box.createVerticalStrut(8));
        brandContent.add(avion);
        brandContent.add(Box.createVerticalStrut(24));
        brandContent.add(slogan);
        brandContent.add(Box.createVerticalGlue());

        leftSide.add(brandContent);
        page.add(leftSide);

        // Jumatatea dreapta - formularul de inregistrare
        JPanel rightSide = new JPanel(new GridBagLayout());
        rightSide.setBackground(WARM_BG);

        JPanel formCard = new JPanel();
        formCard.setLayout(new BoxLayout(formCard, BoxLayout.Y_AXIS));
        formCard.setBackground(ALB);
        formCard.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_C, 1),
            new EmptyBorder(32, 44, 28, 44)
        ));

        // Titlul formularului
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

        // Rand cu Nume si Prenume - doua campuri pe acelasi rand cu GridLayout
        JPanel numeRow = new JPanel(new GridLayout(1, 2, 10, 0));
        numeRow.setBackground(ALB);
        numeRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 68));
        numeRow.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Campul Nume
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

        // Campul Prenume
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

        // Campul Email
        formCard.add(buildFormField("Adresa de email", false));
        formCard.add(Box.createVerticalStrut(14));

        // Campul Parola - JPasswordField ascunde textul introdus
        formCard.add(buildFormField("Parola", true));
        formCard.add(Box.createVerticalStrut(14));

        // Campul Oras resedinta
        formCard.add(buildFormField("Oras resedinta", false));
        formCard.add(Box.createVerticalStrut(26));

        // Butonul principal de creare cont - portocaliu vibrant
        JButton btnCreaza = new JButton("Creeaza cont");
        btnCreaza.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnCreaza.setBackground(ROS_VIU);
        btnCreaza.setForeground(ALB);
        btnCreaza.setBorder(new EmptyBorder(12, 0, 12, 0));
        btnCreaza.setFocusPainted(false);
        btnCreaza.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnCreaza.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
        btnCreaza.setAlignmentX(Component.LEFT_ALIGNMENT);
        // TODO: conectare la baza de date - trimite campNume, campPrenume, email, parola, oras
        btnCreaza.addActionListener(e ->
            JOptionPane.showMessageDialog(this,
                "Cont creat cu succes! Te poti autentifica acum.",
                "TravaleRo - Bun venit!", JOptionPane.INFORMATION_MESSAGE)
        );
        formCard.add(btnCreaza);
        formCard.add(Box.createVerticalStrut(14));

        // Randul de jos: link inapoi la login
        JPanel bottomRow = new JPanel(new BorderLayout());
        bottomRow.setBackground(ALB);
        bottomRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 26));
        bottomRow.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblAiCont = new JLabel("Ai deja un cont?");
        lblAiCont.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblAiCont.setForeground(GRI_TEXT);

        // Buton text care duce inapoi la pagina de login
        JButton btnSpreLogin = new JButton("Autentifica-te");
        btnSpreLogin.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnSpreLogin.setForeground(PORTOCALIU);
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
     * buildFormField() - helper care construieste un label + camp de text.
     * Parametrul esteParola determina daca folosim JPasswordField (ascunde textul)
     * sau JTextField normal.
     * Returneaza un panel vertical cu label deasupra si campul dedesubt.
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

        // Alegem tipul campului in functie de parametru
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

    /*
     * buildAppPanel() - construieste intreaga aplicatie (topbar + sidebar + pagini).
     * Acesta e panelul care apare DUPA login.
     * Structura e identica cu ce era inainte in constructor.
     */
    private JPanel buildAppPanel() {
        JPanel app = new JPanel(new BorderLayout());

        // Topbar si sidebar sunt fixe - nu dispar la schimbarea paginii interioare
        app.add(buildTopBar(),  BorderLayout.NORTH);
        app.add(buildSidebar(), BorderLayout.WEST);

        // Zona centrala cu CardLayout pentru paginile aplicatiei
        cardLayout = new CardLayout();
        mainPanel  = new JPanel(cardLayout);
        mainPanel.add(buildHomePage(),      "home");
        mainPanel.add(buildAtractiiPage(),  "atractii");
        mainPanel.add(buildRezervariPage(), "rezervari");
        mainPanel.add(buildProfilPage(),    "profil");

        detailPanel = new JPanel(new BorderLayout());
        mainPanel.add(detailPanel, "detail");

        app.add(mainPanel, BorderLayout.CENTER);
        showPage("home");
        return app;
    }

    /*
     * showPage() - helper centralizat pentru navigarea intre paginile aplicatiei.
     *
     * Problema glitch: CardLayout schimba pagina dar Swing nu stie intotdeauna
     * ca trebuie sa redeseneze. Fara revalidate+repaint pot ramane artefacte
     * vizuale din pagina anterioara.
     *
     * Solutia: orice schimbare de pagina trece prin aceasta metoda care:
     *   1. Schimba pagina cu cardLayout.show()
     *   2. Forteaza recalcularea layout-ului cu revalidate()
     *   3. Forteaza redesenarea cu repaint()
     */
    private void showPage(String pageName) {
        cardLayout.show(mainPanel, pageName);
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    // Acelasi lucru dar pentru rootLayout (login <-> app <-> register)
    private void showRoot(String pageName) {
        rootLayout.show(rootPanel, pageName);
        rootPanel.revalidate();
        rootPanel.repaint();
    }
    
    private JPanel buildTopBar() {
        JPanel top = new JPanel(new BorderLayout(16, 0));
        top.setBackground(ROS_VIU);
        top.setBorder(new EmptyBorder(10, 18, 10, 18));

        // Logo - "Travale" alb, "Ro" portocaliu, avion portocaliu
        JLabel logo = new JLabel(
            "<html><span style='color:white;font-size:17pt;font-weight:bold;'>" +
            "Travale</span><span style='color:#FF9900;font-size:17pt;font-weight:bold;'>" +
            "Ro</span><span style='color:#FF9900;font-size:12pt;'> ✈</span></html>"
        );
        logo.setPreferredSize(new Dimension(175, 36));
        top.add(logo, BorderLayout.WEST);

        // Bara de cautare - alba cu bordura portocalie subtila
        JPanel searchRow = new JPanel(new BorderLayout());
        searchRow.setBackground(ALB);
        searchRow.setBorder(BorderFactory.createLineBorder(BORDER_C, 1));

        // Butonul "Pe harta" - stanga barei, fara functionalitate momentan
        JButton mapBtn = new JButton("   Pe harta");
        mapBtn.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        mapBtn.setForeground(PORTOCALIU);
        mapBtn.setBackground(new Color(255, 250, 245));
        mapBtn.setBorder(new MatteBorder(0, 0, 0, 1, BORDER_C));
        mapBtn.setFocusPainted(false);
        mapBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        mapBtn.setPreferredSize(new Dimension(115, 36));
        searchRow.add(mapBtn, BorderLayout.WEST);

        // Campul de cautare cu placeholder
        JTextField searchField = new JTextField("Cauta destinatie, hotel, atractie...");
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        searchField.setForeground(new Color(160, 155, 148));
        searchField.setBorder(new EmptyBorder(0, 12, 0, 12));
        searchField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent e) {
                if (searchField.getText().startsWith("Cauta")) {
                    searchField.setText("");
                    searchField.setForeground(new Color(30, 30, 30));
                }
            }
            public void focusLost(java.awt.event.FocusEvent e) {
                if (searchField.getText().isEmpty()) {
                    searchField.setText("Cauta destinatie, hotel, atractie...");
                    searchField.setForeground(new Color(160, 155, 148));
                }
            }
        });
        searchRow.add(searchField, BorderLayout.CENTER);

        // Butonul Cauta - rosu aprins, text alb, dreptunghi fara rotunjire
        JButton cautaBtn = new JButton("Cauta");
        cautaBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        cautaBtn.setBackground(ROS_VIU);
        cautaBtn.setForeground(ALB);
        cautaBtn.setBorder(new EmptyBorder(0, 18, 0, 18));
        cautaBtn.setFocusPainted(false);
        cautaBtn.setPreferredSize(new Dimension(85, 36));
        searchRow.add(cautaBtn, BorderLayout.EAST);
        top.add(searchRow, BorderLayout.CENTER);

        // Buton Profil - portocaliu ca accent diferit de rosul principal
        JButton btnProfil = new JButton("  Profil");
        btnProfil.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnProfil.setForeground(ALB);
        btnProfil.setBackground(PORTOCALIU);
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

    /*
     * buildSidebar() - panelul lateral stang.
     * Fundal aproape negru cu nuanta calduta - contrast maxim cu rosul aprins.
     * Contine: navigare principala + lista rezervari recente.
     */
    private JPanel buildSidebar() {
        JPanel sidebar = new JPanel(new BorderLayout());
        sidebar.setBackground(SIDEBAR_BG);
        sidebar.setPreferredSize(new Dimension(205, 0));

        // Sectiunea de navigare - sus in sidebar
        JPanel navSection = new JPanel();
        navSection.setLayout(new BoxLayout(navSection, BoxLayout.Y_AXIS));
        navSection.setBackground(SIDEBAR_BG);
        navSection.setBorder(new EmptyBorder(14, 0, 8, 0));

        JLabel navLabel = new JLabel("  MENIU");
        navLabel.setFont(new Font("Segoe UI", Font.BOLD, 9));
        navLabel.setForeground(new Color(150, 100, 90));
        navLabel.setBorder(new EmptyBorder(0, 16, 8, 0));
        navSection.add(navLabel);

        // Cream butoanele de navigare - primul e activ la pornire
        btnHome      = createNavButton("Hoteluri",   true);
        btnAtractii  = createNavButton("Atractii",   false);
        btnRezervari = createNavButton("Rezervari",  false);

        // Fiecare buton schimba pagina si se evidentiaza
        btnHome.addActionListener(e -> {
            setActiveNavButton(btnHome);
            showPage("home");
        });
        btnAtractii.addActionListener(e -> {
            setActiveNavButton(btnAtractii);
            showPage("atractii");
        });
        btnRezervari.addActionListener(e -> {
            setActiveNavButton(btnRezervari);
            showPage("rezervari");
        });

        navSection.add(btnHome);
        navSection.add(btnAtractii);
        navSection.add(btnRezervari);
        sidebar.add(navSection, BorderLayout.NORTH);

        // Sectiunea de rezervari recente - jos in sidebar
        JPanel rezSection = new JPanel();
        rezSection.setLayout(new BoxLayout(rezSection, BoxLayout.Y_AXIS));
        rezSection.setBackground(SIDEBAR_BG);
        rezSection.setBorder(new EmptyBorder(0, 0, 16, 0));

        // Linie separatoare subtila
        JSeparator sep = new JSeparator();
        sep.setForeground(new Color(60, 20, 22));
        sep.setMaximumSize(new Dimension(205, 1));
        rezSection.add(sep);
        rezSection.add(Box.createVerticalStrut(12));

        JLabel secLabel = new JLabel("  REZERVARI RECENTE");
        secLabel.setFont(new Font("Segoe UI", Font.BOLD, 9));
        secLabel.setForeground(new Color(150, 100, 90));
        secLabel.setBorder(new EmptyBorder(0, 16, 8, 0));
        rezSection.add(secLabel);

        // Carduri mici pentru rezervarile recente din sidebar
        rezSection.add(createSidebarCard("Hotel Traian", "15-18 Mai", "Confirmata"));
        rezSection.add(Box.createVerticalStrut(5));
        rezSection.add(createSidebarCard("Copou View",   "2-5 Iun",  "Asteptare"));
        rezSection.add(Box.createVerticalStrut(5));
        rezSection.add(createSidebarCard("Casa Pogor",   "10 Iul",   "Confirmata"));

        sidebar.add(rezSection, BorderLayout.SOUTH);
        return sidebar;
    }

    // Buton de navigare din sidebar cu stare activa/inactiva
    private JButton createNavButton(String text, boolean activ) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btn.setForeground(activ ? ALB : new Color(180, 140, 130));
        // Butonul activ are o bara rosie vizibila in stanga
        btn.setBackground(activ ? new Color(200, 16, 46, 60) : SIDEBAR_BG);
        btn.setBorder(new MatteBorder(0, activ ? 3 : 0, 0, 0, ROS_VIU));
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setMaximumSize(new Dimension(205, 44));
        btn.setOpaque(true);
        return btn;
    }

    // Evidentiaza butonul activ si le reseteaza pe celelalte
    private void setActiveNavButton(JButton active) {
        for (JButton b : new JButton[]{btnHome, btnAtractii, btnRezervari}) {
            boolean isActive = (b == active);
            b.setForeground(isActive ? ALB : new Color(180, 140, 130));
            b.setBackground(isActive ? new Color(200, 16, 46, 60) : SIDEBAR_BG);
            b.setBorder(new MatteBorder(0, isActive ? 3 : 0, 0, 0, ROS_VIU));
        }
    }

    // Reseteaza evidentierea (ex: cand mergem la Profil din topbar)
    private void clearSidebarSelection() {
        for (JButton b : new JButton[]{btnHome, btnAtractii, btnRezervari}) {
            b.setForeground(new Color(180, 140, 130));
            b.setBackground(SIDEBAR_BG);
            b.setBorder(new MatteBorder(0, 0, 0, 0, ROS_VIU));
        }
    }

    /*
     * createSidebarCard() - card mic pentru o rezervare in sidebar.
     * Statusul e colorat: verde pentru confirmata, portocaliu pentru in asteptare.
     */
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
        lblDate.setForeground(new Color(180, 140, 130));

        // Verde pentru confirmat, portocaliu pentru in asteptare
        Color sColor = status.equals("Confirmata")
            ? new Color(80, 210, 110)
            : new Color(255, 160, 40);
        JLabel lblStatus = new JLabel("● " + status);
        lblStatus.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        lblStatus.setForeground(sColor);

        card.add(lblHotel);
        card.add(Box.createVerticalStrut(2));
        card.add(lblDate);
        card.add(Box.createVerticalStrut(2));
        card.add(lblStatus);
        return card;
    }

    /*
     * buildHomePage() - pagina principala cu listinguri de hoteluri.
     *
     * Structura:
     *   NORTH: bara de filtre + mic header de context
     *   CENTER: lista de carduri intr-un scroll
     */
    private JPanel buildHomePage() {
        JPanel page = new JPanel(new BorderLayout());
        page.setBackground(BG_PAGE);

        // Sectiunea de sus: filtru + context
        JPanel topSection = new JPanel(new BorderLayout());
        topSection.setBackground(ALB);

        // Banner subtil de context - "Iasi · X proprietati"
        JPanel contextBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
        contextBar.setBackground(ALB);
        contextBar.setBorder(new MatteBorder(0, 0, 1, 0, new Color(230, 230, 230)));

        JLabel lblOras = new JLabel("Iasi, Romania");
        lblOras.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblOras.setForeground(new Color(30, 30, 30));
        contextBar.add(lblOras);

        JLabel lblSep = new JLabel("·");
        lblSep.setForeground(new Color(180, 180, 180));
        contextBar.add(lblSep);

        JLabel lblNr = new JLabel("4 proprietati disponibile");
        lblNr.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblNr.setForeground(GRI_TEXT);
        contextBar.add(lblNr);

        // Sortare dreapta
        JLabel lblSort = new JLabel("Sorteaza dupa:");
        lblSort.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblSort.setForeground(GRI_TEXT);
        contextBar.add(Box.createHorizontalStrut(200));
        contextBar.add(lblSort);

        String[] optiuniSort = {"Recomandate", "Pret crescator", "Rating"};
        JComboBox<String> sortBox = new JComboBox<>(optiuniSort);
        sortBox.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        sortBox.setBackground(ALB);
        contextBar.add(sortBox);

        topSection.add(buildFiltersBar(), BorderLayout.NORTH);
        topSection.add(contextBar, BorderLayout.SOUTH);
        page.add(topSection, BorderLayout.NORTH);

        // Lista carduri - padding generos, fundal gri deschis
        JPanel lista = new JPanel();
        lista.setLayout(new BoxLayout(lista, BoxLayout.Y_AXIS));
        lista.setBackground(BG_PAGE);
        lista.setBorder(new EmptyBorder(16, 20, 20, 20));

        // Fiecare card primeste si o culoare unica pentru zona de imagine
        lista.add(createListingCard(
            "Hotel Traian", "Piata Unirii 1, Iasi",
            "Hotel emblematic cu vedere la Palatul Culturii. Spa, restaurant si bar in incinta.",
            "4.3", "128", "185", true,  new Color(180, 60, 60)));
        lista.add(Box.createVerticalStrut(12));

        lista.add(createListingCard(
            "Pensiunea La Conac", "Str. Lapusneanu 14, Iasi",
            "Pensiune boutique cu gradina interioara, mic dejun inclus si parcare gratuita.",
            "4.8", "74", "95", false, new Color(60, 110, 180)));
        lista.add(Box.createVerticalStrut(12));

        lista.add(createListingCard(
            "Apartament Copou View", "Bd. Carol I 22, Copou",
            "Apartament modern cu 2 camere, la 5 minute de Gradina Copou si Universitate.",
            "4.1", "39", "60", false, new Color(40, 140, 100)));
        lista.add(Box.createVerticalStrut(12));

        lista.add(createListingCard(
            "Casa Pogor Suites", "Str. Vasile Pogor 4, Iasi",
            "Cazare de lux in casa istorica Pogor restaurata. Mic dejun gourmet inclus in pret.",
            "4.9", "51", "220", false, new Color(130, 60, 160)));

        JScrollPane scroll = new JScrollPane(lista);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(BG_PAGE);
        scroll.getVerticalScrollBar().setUnitIncrement(20);
        // Ascunde bara de scroll orizontala - nu avem nevoie
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        page.add(scroll, BorderLayout.CENTER);
        return page;
    }

    /*
     * buildFiltersBar() - bara de filtre curata, stil Booking.
     * Fundal alb, chip activ rosu solid, restul outline subtil.
     * La click pe un chip, acesta devine activ si celelalte se reseteaza.
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

            // Primul chip e activ implicit
            if (i == 0) {
                chip.setBackground(RED_PRIMARY);
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

            // La click: activeaza acest chip si reseteaza celelalte
            final int idx = i;
            chip.addActionListener(e -> {
                for (int j = 0; j < chipuri.length; j++) {
                    if (j == idx) {
                        chipuri[j].setBackground(RED_PRIMARY);
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
     * createListingCard() - card polish pentru un listing de hotel.
     *
     * Layout intern:
     *   WEST:   zona de imagine cu culoare unica + badge optional
     *   CENTER: titlu, locatie, stele, tag-uri facilitati, descriere
     *   EAST:   pret + buton Rezerva
     *
     * Parametrul imgColor da fiecarui card o culoare unica de imagine
     * ca sa nu para toate identice cand nu exista poze reale.
     */
    private JPanel createListingCard(String titlu, String locatie, String descriere,
                                      String rating, String nrRec, String pret,
                                      boolean topAles, Color imgColor) {
        // Wrapper cu shadow simulat printr-un panel gri dedesubt
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(BG_PAGE);
        wrapper.setMaximumSize(new Dimension(Integer.MAX_VALUE, 160));

        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(ALB);
        card.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 1));

        // Zona de imagine - culoare unica per card, gradient simulat cu panel suprapus
        JPanel imgZone = new JPanel(new BorderLayout());
        imgZone.setPreferredSize(new Dimension(160, 160));
        imgZone.setBackground(imgColor);

        // Icon centrat in zona de imagine
        JLabel imgIcon = new JLabel("🏨", SwingConstants.CENTER);
        imgIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 36));
        imgIcon.setForeground(new Color(255, 255, 255, 160));
        imgZone.add(imgIcon, BorderLayout.CENTER);

        // Badge "Top ales" sau tip proprietate deasupra imaginii
        JLabel badge = new JLabel(topAles ? "  ★ Top ales  " : "  Proprietate  ",
                                  SwingConstants.CENTER);
        badge.setFont(new Font("Segoe UI", Font.BOLD, 10));
        badge.setForeground(ALB);
        badge.setBackground(topAles ? RED_PRIMARY : new Color(0, 0, 0, 80));
        badge.setOpaque(true);
        badge.setPreferredSize(new Dimension(160, 22));
        imgZone.add(badge, BorderLayout.NORTH);

        card.add(imgZone, BorderLayout.WEST);

        // Sectiunea centrala cu informatii
        JPanel info = new JPanel();
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
        info.setBackground(ALB);
        info.setBorder(new EmptyBorder(14, 16, 14, 10));

        // Titlu - font mare, bold, negru
        JLabel lblT = new JLabel(titlu);
        lblT.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblT.setForeground(new Color(15, 15, 15));
        lblT.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Locatie - albastru link, exact ca Booking
        JLabel lblL = new JLabel("📍 " + locatie);
        lblL.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblL.setForeground(new Color(0, 100, 180));
        lblL.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Stele + scor numeric + nr recenzii
        int stele = (int) Math.round(Double.parseDouble(rating));
        JPanel ratingRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        ratingRow.setBackground(ALB);
        ratingRow.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Scor in dreptunghi colorat ca Booking
        JLabel scoreBox = new JLabel(" " + rating + " ");
        scoreBox.setFont(new Font("Segoe UI", Font.BOLD, 12));
        scoreBox.setForeground(ALB);
        scoreBox.setBackground(RED_DARK);
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

        // Tag-uri de facilitati rapide sub rating
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

        // Descriere scurta
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

        // Sectiunea dreapta: separator, pret, buton
        JPanel right = new JPanel(new BorderLayout());
        right.setBackground(new Color(252, 252, 252));
        right.setBorder(BorderFactory.createCompoundBorder(
            new MatteBorder(0, 1, 0, 0, new Color(230, 230, 230)),
            new EmptyBorder(14, 16, 14, 18)
        ));

        // Sus: eticheta discreta
        JLabel lblDisp = new JLabel("Disponibil");
        lblDisp.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblDisp.setForeground(new Color(0, 140, 60));
        lblDisp.setHorizontalAlignment(SwingConstants.RIGHT);

        // Centru: pretul mare
        JPanel pretPanel = new JPanel();
        pretPanel.setLayout(new BoxLayout(pretPanel, BoxLayout.Y_AXIS));
        pretPanel.setBackground(new Color(252, 252, 252));

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

        // Buton Rezerva - rosu solid, fara border vizibil
        JButton btnRez = new JButton("Rezerva");
        btnRez.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnRez.setBackground(RED_PRIMARY);
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
        // Linie subtire jos - simuleaza shadow discret
        JPanel shadow = new JPanel();
        shadow.setBackground(new Color(210, 210, 210));
        shadow.setPreferredSize(new Dimension(0, 2));
        wrapper.add(shadow, BorderLayout.SOUTH);

        return wrapper;
    }

    /*
     * buildAtractiiPage() - pagina cu atractii turistice din Iasi.
     * Grid 2 coloane cu carduri colorate in portocaliu si rosu.
     */
    private JPanel buildAtractiiPage() {
        JPanel page = new JPanel(new BorderLayout());
        page.setBackground(WARM_BG);

        // Header cu gradient vizual - rosu aprins la portocaliu
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(ROS_VIU);
        header.setBorder(new EmptyBorder(18, 22, 18, 22));

        JLabel titlu = new JLabel("Atractii turistice in Iasi");
        titlu.setFont(new Font("Segoe UI", Font.BOLD, 21));
        titlu.setForeground(ALB);

        JLabel subtitlu = new JLabel("Descopera cele mai frumoase locuri din oras");
        subtitlu.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtitlu.setForeground(new Color(255, 200, 190));
        header.add(titlu, BorderLayout.NORTH);
        header.add(subtitlu, BorderLayout.SOUTH);
        page.add(header, BorderLayout.NORTH);

        // Grid cu carduri de atractii
        JPanel grid = new JPanel(new GridLayout(0, 2, 12, 12));
        grid.setBackground(WARM_BG);
        grid.setBorder(new EmptyBorder(16, 20, 16, 20));

        grid.add(createAtractieCard("Palatul Culturii",
            "Cel mai important monument al Iasului, gazduieste 4 muzee nationale.",
            "Ma-Du: 10:00-17:00", "0.5 km", PORTOCALIU));
        grid.add(createAtractieCard("Gradina Botanica",
            "A doua gradina botanica din Romania, cu 10.000 de specii de plante.",
            "Zilnic: 09:00-19:00", "2.0 km", new Color(255, 130, 0)));
        grid.add(createAtractieCard("Catedrala Mitropolitana",
            "Cea mai mare catedrala ortodoxa din Moldova, construita in 1880.",
            "Zilnic: 07:00-20:00", "0.3 km", ROS_VIU));
        grid.add(createAtractieCard("Universitatea UAIC",
            "Prima universitate din Romania, fondata in 1860. Arhitectura impresionanta.",
            "Campus deschis", "1.0 km", new Color(220, 60, 30)));
        grid.add(createAtractieCard("Gradina Copou",
            "Cea mai veche gradina publica din Iasi, cu teiul lui Eminescu.",
            "Zilnic: 06:00-22:00", "1.5 km", PORTOCALIU_2));
        grid.add(createAtractieCard("Curtea Domneasca",
            "Ruinele fostei curti medievale, simbol al istoriei Moldovei.",
            "Ma-Du: 09:00-18:00", "0.8 km", new Color(200, 50, 20)));

        JScrollPane scroll = new JScrollPane(grid);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(WARM_BG);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        page.add(scroll, BorderLayout.CENTER);
        return page;
    }

    /*
     * createAtractieCard() - card vizual pentru o atractie.
     * Bara colorata in stanga cu culoarea unica a atractiei.
     */
    private JPanel createAtractieCard(String nume, String desc, String program,
                                       String distanta, Color culoare) {
        JPanel card = new JPanel(new BorderLayout(0, 0));
        card.setBackground(ALB);
        card.setBorder(BorderFactory.createLineBorder(BORDER_C, 1));

        // Bara colorata in stanga - culoarea unica a fiecarei atractii
        JPanel bara = new JPanel();
        bara.setBackground(culoare);
        bara.setPreferredSize(new Dimension(5, 0));
        card.add(bara, BorderLayout.WEST);

        // Continut card
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
        lblDist.setForeground(culoare); // culoarea atractiei pentru consistenta

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

    /*
     * buildRezervariPage() - pagina cu toate rezervarile utilizatorului.
     */
    private JPanel buildRezervariPage() {
        JPanel page = new JPanel(new BorderLayout());
        page.setBackground(WARM_BG);

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(ROS_VIU);
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
            "15 Mai 2025", "18 Mai 2025", "3 nopti", "555€", "Confirmata"));
        lista.add(Box.createVerticalStrut(10));
        lista.add(createRezervareCard("Apartament Copou View", "Bd. Carol I 22, Iasi",
            "2 Iun 2025", "5 Iun 2025", "3 nopti", "180€", "Asteptare"));
        lista.add(Box.createVerticalStrut(10));
        lista.add(createRezervareCard("Casa Pogor Suites", "Str. Vasile Pogor 4, Iasi",
            "10 Iul 2025", "12 Iul 2025", "2 nopti", "440€", "Confirmata"));

        JScrollPane scroll = new JScrollPane(lista);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(WARM_BG);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        page.add(scroll, BorderLayout.CENTER);
        return page;
    }

    // Card detaliat pentru o rezervare - bara colorata stanga + informatii + status
    private JPanel createRezervareCard(String hotel, String adresa, String checkIn,
                                        String checkOut, String durata,
                                        String total, String status) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(ALB);
        card.setBorder(BorderFactory.createLineBorder(BORDER_C, 1));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 112));

        // Bara de status - rosie pentru confirmat, portocalie pentru asteptare
        Color sColor = status.equals("Confirmata") ? ROS_VIU : PORTOCALIU;
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
        lblA.setForeground(new Color(60, 130, 200));

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

        // Pretul total in portocaliu
        JLabel lblT = new JLabel("Total: " + total);
        lblT.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblT.setForeground(PORTOCALIU);
        lblT.setAlignmentX(Component.RIGHT_ALIGNMENT);

        // Statusul - rosu pentru confirmat, portocaliu-cald pentru asteptare
        Color textS = status.equals("Confirmata") ? new Color(180, 0, 30) : new Color(190, 100, 0);
        JLabel lblS = new JLabel("● " + status);
        lblS.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lblS.setForeground(textS);
        lblS.setAlignmentX(Component.RIGHT_ALIGNMENT);

        JButton btnA = new JButton("Anuleaza");
        btnA.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        btnA.setBackground(new Color(255, 248, 245));
        btnA.setForeground(ROS_VIU);
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

    /*
     * buildProfilPage() - pagina de profil imbunatatita.
     *
     * Sectiuni:
     *   1. Header rosu cu avatar initiale, nume, badge nivel
     *   2. 4 carduri statistici cu linie colorata unica fiecare
     *   3. Informatii cont - randuri label:valoare
     *   4. Preferinte de calatorie
     *   5. Calatorii recente - lista cu destinatii vizitate
     */
    private JPanel buildProfilPage() {
        JPanel page = new JPanel(new BorderLayout());
        page.setBackground(BG_PAGE);

        // Header rosu cu avatar initiale si date utilizator
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(RED_PRIMARY);
        header.setBorder(new EmptyBorder(20, 24, 20, 24));

        JPanel headerLeft = new JPanel(new FlowLayout(FlowLayout.LEFT, 18, 0));
        headerLeft.setBackground(RED_PRIMARY);

        // Avatar cu initiale - mai elegant decat emoji
        JPanel avatarBox = new JPanel(new GridBagLayout());
        avatarBox.setBackground(RED_DARK);
        avatarBox.setPreferredSize(new Dimension(70, 70));
        avatarBox.setBorder(BorderFactory.createLineBorder(new Color(255, 255, 255, 60), 2));
        JLabel initiale = new JLabel("AI");
        initiale.setFont(new Font("Segoe UI", Font.BOLD, 26));
        initiale.setForeground(ALB);
        avatarBox.add(initiale);
        headerLeft.add(avatarBox);

        // Date utilizator
        JPanel namePanel = new JPanel();
        namePanel.setLayout(new BoxLayout(namePanel, BoxLayout.Y_AXIS));
        namePanel.setBackground(RED_PRIMARY);

        JLabel lblNume = new JLabel("Alexandru Ionescu");
        lblNume.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblNume.setForeground(ALB);

        JLabel lblEmail = new JLabel("alex.ionescu@email.ro");
        lblEmail.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblEmail.setForeground(new Color(255, 200, 195));

        // Badge nivel cont pe fond portocaliu
        JLabel lblNivel = new JLabel("  Calator Frecvent  ");
        lblNivel.setFont(new Font("Segoe UI", Font.BOLD, 10));
        lblNivel.setForeground(RED_DARK);
        lblNivel.setBackground(ORANGE_STAR);
        lblNivel.setOpaque(true);
        lblNivel.setBorder(new EmptyBorder(2, 0, 2, 0));

        JLabel lblMembru = new JLabel("Membru din Ianuarie 2024  ·  Iasi, Romania");
        lblMembru.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblMembru.setForeground(new Color(255, 175, 170));

        namePanel.add(lblNume);
        namePanel.add(Box.createVerticalStrut(3));
        namePanel.add(lblEmail);
        namePanel.add(Box.createVerticalStrut(5));
        namePanel.add(lblNivel);
        namePanel.add(Box.createVerticalStrut(4));
        namePanel.add(lblMembru);
        headerLeft.add(namePanel);

        // Buton Editeaza - alb cu text rosu, in dreapta header-ului
        JButton btnEdit = new JButton("Editeaza profilul");
        btnEdit.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnEdit.setForeground(RED_PRIMARY);
        btnEdit.setBackground(ALB);
        btnEdit.setBorder(new EmptyBorder(8, 16, 8, 16));
        btnEdit.setFocusPainted(false);
        btnEdit.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        header.add(headerLeft, BorderLayout.CENTER);
        header.add(btnEdit, BorderLayout.EAST);
        page.add(header, BorderLayout.NORTH);

        // Continut scrollabil
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(BG_PAGE);
        content.setBorder(new EmptyBorder(18, 22, 22, 22));

        // 4 carduri statistici pe un rand - fiecare cu culoare proprie
        JPanel statsRow = new JPanel(new GridLayout(1, 4, 12, 0));
        statsRow.setBackground(BG_PAGE);
        statsRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));
        statsRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        statsRow.add(createStatCard("3",   "Rezervari active",  RED_PRIMARY));
        statsRow.add(createStatCard("7",   "Calatorii totale",  RED_DARK));
        statsRow.add(createStatCard("4.8", "Rating mediu",      ORANGE_PRICE));
        statsRow.add(createStatCard("12",  "Orase vizitate",    ORANGE_STAR));
        content.add(statsRow);
        content.add(Box.createVerticalStrut(18));

        // Sectiunea informatii cont
        content.add(buildProfilSection("Informatii cont", new String[][]{
            {"Nume complet",    "Alexandru Ionescu"},
            {"Email",           "alex.ionescu@email.ro"},
            {"Telefon",         "+40 712 345 678"},
            {"Oras resedinta",  "Iasi, Romania"},
            {"Limba",           "Romana"},
        }));
        content.add(Box.createVerticalStrut(14));

        // Sectiunea preferinte de calatorie
        content.add(buildProfilSection("Preferinte de calatorie", new String[][]{
            {"Tip cazare preferat",   "Hotel (4-5 stele)"},
            {"Buget mediu / noapte",  "100 - 200 EUR"},
            {"Destinatii favorite",   "Europa, Asia"},
            {"Durata medie sejur",    "3-5 nopti"},
        }));
        content.add(Box.createVerticalStrut(14));

        // Calatorii recente - card cu lista de randuri
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

        // Fiecare calatorie: destinatie, hotel, data, rating
        String[][] calatorii = {
            {"Paris, Franta",      "Hotel Grand Opera",   "Mar 2025", "4.7"},
            {"Roma, Italia",       "Colosseum Suites",    "Ian 2025", "4.5"},
            {"Praga, Cehia",       "Prague City Center",  "Oct 2024", "4.8"},
            {"Barcelona, Spania",  "Barceloneta Beach",   "Iun 2024", "4.3"},
        };

        for (String[] c : calatorii) {
            JPanel row = new JPanel(new BorderLayout(10, 0));
            row.setBackground(ALB);
            row.setBorder(new MatteBorder(0, 0, 1, 0, new Color(240, 240, 240)));

            JPanel rowLeft = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
            rowLeft.setBackground(ALB);

            JLabel dot = new JLabel("●");
            dot.setForeground(RED_PRIMARY);

            JLabel dest = new JLabel(c[0]);
            dest.setFont(new Font("Segoe UI", Font.BOLD, 13));
            dest.setForeground(new Color(15, 15, 15));

            JLabel hotel = new JLabel("· " + c[1]);
            hotel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            hotel.setForeground(GRI_TEXT);

            rowLeft.add(dot);
            rowLeft.add(dest);
            rowLeft.add(hotel);
            row.add(rowLeft, BorderLayout.WEST);

            JPanel rowRight = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 8));
            rowRight.setBackground(ALB);

            JLabel data = new JLabel(c[2]);
            data.setFont(new Font("Segoe UI", Font.PLAIN, 11));
            data.setForeground(GRI_TEXT);

            // Scor in dreptunghi rosu inchis - consistent cu restul UI
            JLabel scoreTag = new JLabel(" " + c[3] + " ");
            scoreTag.setFont(new Font("Segoe UI", Font.BOLD, 11));
            scoreTag.setForeground(ALB);
            scoreTag.setBackground(RED_DARK);
            scoreTag.setOpaque(true);

            rowRight.add(data);
            rowRight.add(scoreTag);
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
     * buildProfilSection() - helper pentru sectiunile de tip tabel in profil.
     * Primeste un titlu si un array de perechi [label, valoare].
     * Returneaza un card alb cu randuri separate de o linie subtila.
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
     * createStatCard() - card cu o statistica numerica.
     * Linie colorata sus (accentColor unica per card), cifra mare, eticheta.
     */
    private JPanel createStatCard(String valoare, String eticheta, Color accentColor) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(ALB);
        card.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 1));

        // Linie colorata sus - identitatea vizuala a cardului
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


    /*
     * navigateToDetail() - navigheaza la pagina de detalii a unui listing.
     *
     * Pasii:
     *   1. Golim panelul de detalii (din rezervarea anterioara)
     *   2. Construim continut nou cu datele anuntului selectat
     *   3. Cerem Swing sa redeseneze - revalidate + repaint sunt esentiale
     *   4. Schimbam pagina activa cu CardLayout
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
     * buildDetailPage() - pagina completa de detalii a unui anunt.
     * Header rosu cu buton inapoi, imagine, descriere, facilitati, rezervare.
     */
    private JPanel buildDetailPage(String titlu, String locatie, String descriere,
                                    String rating, String nrRec, String pret) {
        JPanel page = new JPanel(new BorderLayout());
        page.setBackground(WARM_BG);

        // Header rosu aprins cu buton Inapoi si titlul anuntului
        JPanel header = new JPanel(new BorderLayout(12, 0));
        header.setBackground(ROS_VIU);
        header.setBorder(new EmptyBorder(10, 16, 10, 16));

        JButton btnBack = new JButton("< Inapoi");
        btnBack.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnBack.setForeground(ALB);
        btnBack.setBackground(ROS_HOVER);
        btnBack.setBorder(new EmptyBorder(6, 14, 6, 14));
        btnBack.setFocusPainted(false);
        btnBack.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        // La click: reactivam butonul Home in sidebar si aratam pagina home
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

        // Continut scrollabil
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(WARM_BG);
        content.setBorder(new EmptyBorder(22, 32, 22, 32));

        // Imagine mare - placeholder portocaliu cald
        JPanel imgMare = new JPanel(new GridBagLayout());
        imgMare.setBackground(new Color(255, 220, 195));
        imgMare.setMaximumSize(new Dimension(Integer.MAX_VALUE, 240));
        imgMare.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel imgL = new JLabel("Galerie foto - " + titlu, SwingConstants.CENTER);
        imgL.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        imgL.setForeground(new Color(160, 60, 10));
        imgMare.add(imgL);
        content.add(imgMare);
        content.add(Box.createVerticalStrut(22));

        // Titlu + locatie ca link + rating cu stele portocalii
        JLabel lblT = new JLabel(titlu);
        lblT.setFont(new Font("Segoe UI", Font.BOLD, 23));
        lblT.setForeground(new Color(20, 20, 20));
        lblT.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(lblT);
        content.add(Box.createVerticalStrut(4));

        JLabel lblL = new JLabel("  " + locatie);
        lblL.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblL.setForeground(new Color(60, 130, 200));
        lblL.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(lblL);
        content.add(Box.createVerticalStrut(4));

        int stele = (int) Math.round(Double.parseDouble(rating));
        JLabel lblR = new JLabel("★".repeat(stele) + "☆".repeat(5 - stele)
            + "  " + rating + " din 5  ·  " + nrRec + " recenzii");
        lblR.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblR.setForeground(PORTOCALIU_2);
        lblR.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(lblR);
        content.add(Box.createVerticalStrut(22));

        // Descriere extinsa
        JLabel secDesc = new JLabel("Despre proprietate");
        secDesc.setFont(new Font("Segoe UI", Font.BOLD, 15));
        secDesc.setForeground(new Color(20, 20, 20));
        secDesc.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(secDesc);
        content.add(Box.createVerticalStrut(8));

        JLabel lblDesc = new JLabel(
            "<html><body style='width:680px;font-size:12pt;color:#3a3a3a'>" + descriere
            + " Camerele sunt dotate cu aer conditionat, TV si Wi-Fi gratuit."
            + " Personalul nostru este disponibil non-stop la receptie."
            + "</body></html>"
        );
        lblDesc.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(lblDesc);
        content.add(Box.createVerticalStrut(22));

        // Facilitati - chipuri cu bordura portocalie
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
            c.setForeground(PORTOCALIU);
            c.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_C, 1),
                new EmptyBorder(4, 6, 4, 6)
            ));
            c.setBackground(new Color(255, 248, 240));
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

        // Sectiunea de rezervare - pret portocaliu + buton rosu mare
        JLabel secRez = new JLabel("Rezerva acum");
        secRez.setFont(new Font("Segoe UI", Font.BOLD, 15));
        secRez.setForeground(new Color(20, 20, 20));
        secRez.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(secRez);
        content.add(Box.createVerticalStrut(14));

        JPanel rezervRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 14, 0));
        rezervRow.setBackground(WARM_BG);
        rezervRow.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Card pret cu fundal cald
        JPanel pretCard = new JPanel(new BorderLayout(0, 4));
        pretCard.setBackground(ALB);
        pretCard.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_C, 1),
            new EmptyBorder(14, 20, 14, 20)
        ));
        JLabel lblPretMare = new JLabel(pret + "€");
        lblPretMare.setFont(new Font("Segoe UI", Font.BOLD, 30));
        lblPretMare.setForeground(PORTOCALIU); // portocaliu pentru pret mare
        JLabel lblPN = new JLabel("per noapte, taxe incluse");
        lblPN.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblPN.setForeground(GRI_TEXT);
        pretCard.add(lblPretMare, BorderLayout.CENTER);
        pretCard.add(lblPN, BorderLayout.SOUTH);
        rezervRow.add(pretCard);

        // Butonul mare de rezervare - rosu aprins, text alb, padding generos
        JButton btnRezMare = new JButton("  Rezerva acum  >");
        btnRezMare.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnRezMare.setBackground(ROS_VIU);
        btnRezMare.setForeground(ALB);
        btnRezMare.setBorder(new EmptyBorder(16, 30, 16, 30));
        btnRezMare.setFocusPainted(false);
        btnRezMare.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnRezMare.addActionListener(e ->
            JOptionPane.showMessageDialog(this,
                "Rezervare initiata!\n\n" + titlu + "\n" + locatie + "\nPret: " + pret + "€ / noapte",
                "TravaleRo - Rezervare", JOptionPane.INFORMATION_MESSAGE)
        );
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