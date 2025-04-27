package Tests;
import Models.Utilisateur;
import Services.UtilisateurService;
import Services.RoleService;
import Models.Role;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        UtilisateurService userService = new UtilisateurService();
        RoleService roleService = new RoleService();

        try {
            // 1. First create a required role
            Role userRole = new Role("ROLE_USER", "Regular user");
            roleService.insert(userRole);
            System.out.println("Role created successfully");

            // 2. Create a user with all required fields
            Utilisateur newUser = new Utilisateur(
                    7,  // role_id
                    "Mohamed",         // prenom
                    "Neji",            // nom
                    "amine.neji@esprit.tn", // email
                    "Male",            // genre
                    LocalDate.of(2000, 1, 1), // date_naissance
                    "12345678",        // num_tel
                    "securePassword123" // password
            );

            // 3. Insert the user
            userService.insert(newUser);
            System.out.println("User created successfully");

            // 4. Retrieve and display all users
            List<Utilisateur> users = userService.findAll();
            System.out.println("\nList of all users:");
            for (Utilisateur u : users) {
                System.out.println(u); // Requires proper toString() in Utilisateur
            }


        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}