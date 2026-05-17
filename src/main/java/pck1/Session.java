package pck1;

import pck1.Proiect_PIP.Utilizator;

/**
 * Singleton holding the currently logged-in user for the lifetime of the
 * Swing application. Populated by LoginPage after successful authentication,
 * read by Sidebar (to get preferred tags), ProfilPage (to show user info),
 * and any other screen that needs to know who is using the app.
 *
 * Cleared on logout. Not thread-safe by design — all access goes through
 * the EDT.
 */
public final class Session {

    private static Utilizator currentUser;

    /** Istoric cautari per sesiune — accesibil din Sidebar si SearchPage. */
    private static final java.util.List<String> searchHistory = new java.util.ArrayList<>();

    private Session() {
        // utility class, no instances
    }

    /** Adauga un oras in istoric (evita duplicate consecutive). */
    public static void addToHistory(String city) {
        if (city == null || city.isBlank()) return;
        String c = city.trim();
        if (searchHistory.isEmpty() ||
            !searchHistory.get(searchHistory.size() - 1).equalsIgnoreCase(c)) {
            searchHistory.add(c);
        }
    }

    /** Returneaza sugestii din istoric care incep cu prefixul dat (max 5, cele mai recente primele). */
    public static java.util.List<String> getHistorySuggestions(String prefix) {
        java.util.List<String> result = new java.util.ArrayList<>();
        String p = prefix.toLowerCase();
        for (int i = searchHistory.size() - 1; i >= 0; i--) {
            String h = searchHistory.get(i);
            if (h.toLowerCase().startsWith(p) && !result.contains(h)) {
                result.add(h);
                if (result.size() == 5) break;
            }
        }
        return result;
    }

    /** Stores the user after a successful login. */
    public static void login(Utilizator user) {
        currentUser = user;
    }

    /** Clears the session. */
    public static void logout() {
        currentUser = null;
    }

    /** Returns the active user, or null if no one is logged in. */
    public static Utilizator getUser() {
        return currentUser;
    }

    public static boolean isLoggedIn() {
        return currentUser != null;
    }
}
