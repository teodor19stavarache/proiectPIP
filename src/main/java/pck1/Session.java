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

    private Session() {
        // utility class, no instances
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
