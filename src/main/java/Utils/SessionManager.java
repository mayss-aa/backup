package Utils;

import Models.Utilisateur;

public class SessionManager {
    private static Utilisateur currentUser;


    public static void login(Utilisateur user) {
        currentUser = user;
    }

    public static void logout() {
        currentUser = null;
    }

    public static Utilisateur getUser() {
        return currentUser;
    }

    public static boolean isLoggedIn() {
        return currentUser != null;
    }
}