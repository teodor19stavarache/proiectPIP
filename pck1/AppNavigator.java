package pck1;

public interface AppNavigator {
    void showPage(String name);
    void showRoot(String name);
    void navigateToDetail(String titlu, String locatie, String descriere,
                          String rating, String nrRec, String pret);
    void clearSidebarSelection();
}
