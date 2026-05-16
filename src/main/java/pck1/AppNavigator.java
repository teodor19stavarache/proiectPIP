package pck1;

public interface AppNavigator {
    void showPage(String name);
    void showRoot(String name);
    void navigateToDetail(String titlu, String locatie, String descriere,
                          String rating, String nrRec, String pret);
    void clearSidebarSelection();

    // Afiseaza loading screen, apoi navigheaza la pagina specificata (durata fixa, fara scraper).
    void showPageWithLoading(String targetPage);

    /**
     * Afiseaza loading screen-ul si ruleaza scraperul Python in background.
     * Bara avanseaza asimptotic spre 95% si sare la 100% cand scraperul
     * termina (oricat ar dura - 5s, 30s, etc.). La final, navigheaza la
     * pagina cu rezultate; in caz de eroare, revine la home cu mesaj.
     */
    void runSearch(String city, String tagsCsv);
}
