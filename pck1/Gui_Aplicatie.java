package pck1;

import java.awt.*;
import javax.swing.*;

/*
 * Gui_Aplicatie - fereastra principala TravaleRo.
 * Implementeaza AppNavigator si coordoneaza toate paginile.
 *
 * Structura navigare:
 *   rootLayout (CardLayout)
 *     "login"    -> LoginPage
 *     "register" -> RegisterPage
 *     "app"      -> buildAppPanel()
 *                     mainPanel (CardLayout)
 *                       "home"      -> HomePage
 *                       "search"    -> SearchPage
 *                       "atractii"  -> AtractiiPage
 *                       "rezervari" -> RezervariPage
 *                       "profil"    -> ProfilPage
 *                       "detail"    -> DetailPage (populat dinamic)
 */
public class Gui_Aplicatie extends JFrame implements AppNavigator {

    private static final long serialVersionUID = 1L;
	private CardLayout rootLayout;
    private JPanel     rootPanel;

    private CardLayout cardLayout;
    private JPanel     mainPanel;
    private JPanel     detailPanel;

    public Gui_Aplicatie() {
        setTitle("TravaleRo - Travel Planner");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 700);
        setLocationRelativeTo(null);

        rootLayout = new CardLayout();
        rootPanel  = new JPanel(rootLayout);

        rootPanel.add(LoginPage.build(this),    "login");
        rootPanel.add(RegisterPage.build(this), "register");
        rootPanel.add(buildAppPanel(),          "app");

        setContentPane(rootPanel);
        showRoot("login");  // porneste de la pagina de login
    }

    @Override
    public void showPage(String name) {
        cardLayout.show(mainPanel, name);
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    @Override
    public void showRoot(String name) {
        rootLayout.show(rootPanel, name);
        rootPanel.revalidate();
        rootPanel.repaint();
    }

    @Override
    public void navigateToDetail(String titlu, String locatie, String descriere,
                                  String rating, String nrRec, String pret) {
        detailPanel.removeAll();
        detailPanel.add(DetailPage.build(titlu, locatie, descriere, rating, nrRec, pret, this));
        detailPanel.revalidate();
        detailPanel.repaint();
        showPage("detail");
    }

    @Override
    public void clearSidebarSelection() {
        // rezervat pentru selectia activa in sidebar
    }

    private JPanel buildAppPanel() {
        JPanel app = new JPanel(new BorderLayout());
        app.add(TopBar.build(this),  BorderLayout.NORTH);
        app.add(new Sidebar(this),   BorderLayout.WEST);

        cardLayout = new CardLayout();
        mainPanel  = new JPanel(cardLayout);

        mainPanel.add(HomePage.build(this),      "home");
        mainPanel.add(SearchPage.build(this),    "search");
        mainPanel.add(AtractiiPage.build(),      "atractii");
        mainPanel.add(RezervariPage.build(),     "rezervari");
        mainPanel.add(ProfilPage.build(),        "profil");

        detailPanel = new JPanel(new BorderLayout());
        mainPanel.add(detailPanel, "detail");

        app.add(mainPanel, BorderLayout.CENTER);
        showPage("home");
        return app;
    }
}
