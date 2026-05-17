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
 *                       "loading"   -> LoadingScreen (apare la cautare)
 *                       "home"      -> HomePage
 *                       "search"    -> SearchPage
 *                       "atractii"  -> AtractiiPage
 *                       "rezervari" -> RezervariPage
 *                       "profil"    -> ProfilPage
 *                       "detail"    -> DetailPage (populat dinamic)
 */
public class Gui_Aplicatie extends JFrame implements AppNavigator {

    private CardLayout    rootLayout;
    private JPanel        rootPanel;

    private CardLayout    cardLayout;
    private JPanel        mainPanel;
    private JPanel        detailPanel;
    private JPanel        profilHost;
    private JPanel        searchHost;

    private LoadingScreen loadingScreen;

    public Gui_Aplicatie() {
        setTitle("TravaleRo - Travel Planner");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 700);
        setMinimumSize(new Dimension(860, 560));
        setLocationRelativeTo(null);

        rootLayout = new CardLayout();
        rootPanel  = new JPanel(rootLayout);

        rootPanel.add(LoginPage.build(this),    "login");
        rootPanel.add(RegisterPage.build(this), "register");
        rootPanel.add(buildAppPanel(),          "app");

        setContentPane(rootPanel);
        showRoot("login");
    }

    @Override
    public void showPage(String name) {
        // Rebuild pages that depend on Session / scraper output so they
        // always reflect the latest state instead of a stale cached snapshot.
        if ("profil".equals(name)) {
            profilHost.removeAll();
            profilHost.add(ProfilPage.build(), BorderLayout.CENTER);
            profilHost.revalidate();
            profilHost.repaint();
        } else if ("search".equals(name)) {
            searchHost.removeAll();
            searchHost.add(SearchPage.build(this), BorderLayout.CENTER);
            searchHost.revalidate();
            searchHost.repaint();
        }
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

    // Arata loading screen in zona centrala, apoi navigheaza la pagina tinta (durata fixa)
    @Override
    public void showPageWithLoading(String targetPage) {
        loadingScreen.reset(() -> showPage(targetPage));
        showPage("loading");
    }

    @Override
    public void runSearch(String city, String tagsCsv) {
        // Arata loading screen-ul si porneste animatia indeterminate
        showPage("loading");
        loadingScreen.startIndeterminate();

        ScraperService.runOverpassSearch(
            city,
            tagsCsv,
            // onSuccess - ruleaza pe EDT prin SwingWorker.done()
            () -> loadingScreen.markComplete(() -> showPage("search")),
            // onFailure
            errMsg -> loadingScreen.markFailed("Eroare scraper: " + errMsg,
                                                 () -> showPage("home"))
        );
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

        cardLayout    = new CardLayout();
        mainPanel     = new JPanel(cardLayout);
        loadingScreen = new LoadingScreen();

        searchHost = new JPanel(new BorderLayout());
        profilHost = new JPanel(new BorderLayout());

        mainPanel.add(loadingScreen,             "loading");
        mainPanel.add(HomePage.build(this),      "home");
        mainPanel.add(searchHost,                "search");
        mainPanel.add(AtractiiPage.build(),      "atractii");
        mainPanel.add(RezervariPage.build(),     "rezervari");
        mainPanel.add(profilHost,                "profil");

        detailPanel = new JPanel(new BorderLayout());
        mainPanel.add(detailPanel, "detail");

        app.add(mainPanel, BorderLayout.CENTER);
        showPage("home");
        return app;
    }
}
