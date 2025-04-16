package Utils;

import Models.Utilisateur;
//import Models.Users;

import java.sql.Timestamp;
import java.time.LocalDate;

public final class UserSession {
    private static UserSession instance;
    private Utilisateur currentUser;
    //private Users currentUsers;

    // Private constructor to prevent direct instantiation
    private UserSession() {}

    // Get the singleton instance (no parameters)
    public static synchronized UserSession getInstance() {
        if (instance == null) {
            instance = new UserSession();
        }
        return instance;
    }

    // Update user data
    public void setUser(int role_id, String prenom, String nom, String email,
                        String genre, LocalDate date_naissance, String num_tel,
                        String password) {
        this.currentUser = new Utilisateur(role_id, prenom, nom, email,
                genre, date_naissance, num_tel,
                password);
    }

    // Update users data

    // Update users data
    /*public void setUsers(int userId, String firstName, String lastName, String phone,
                         String email, String password, Users.Role role, Timestamp createdAt) {
        this.currentUsers = new Users(userId, firstName, lastName, phone, email, password, role, createdAt);
    }
*/
    // Get the current user
    public Utilisateur getUser() {
        return currentUser;
    }
    // Get the current user
   /* public Users getUsers() {
        return currentUsers;
    }*/

    // Clear the session (e.g., on logout)
    public void cleanUserSession() {
        instance = null;
    }
}