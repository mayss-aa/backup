package Controllers;

import Models.Utilisateur;
import Services.UtilisateurService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginController {

    @FXML private TextField email;
    @FXML private PasswordField password;

    private final UtilisateurService userService = new UtilisateurService();

    // Modèle de validation d'email
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
    private static final Pattern pattern = Pattern.compile(EMAIL_REGEX);

    @FXML
    private void login() {
        // Validation des entrées
        if (!validateInputs()) {
            return;
        }

        try {
            // Vérification des identifiants
            Utilisateur user = userService.authenticate(email.getText(), password.getText());

            if (user != null) {
                // Redirection selon le rôle
                redirectBasedOnRole(user.getRole_id());
            } else {
                showAlert("Échec de connexion", "Email ou mot de passe incorrect", Alert.AlertType.ERROR);
            }
        } catch (SQLException e) {
            showAlert("Erreur base de données", "Erreur d'accès à la base de données : " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private boolean validateInputs() {
        // Validation email
        if (email.getText().isEmpty() || !isValidEmail(email.getText())) {
            showAlert("Erreur de validation", "Veuillez entrer un email valide", Alert.AlertType.ERROR);
            return false;
        }

        // Validation mot de passe
        if (password.getText().isEmpty()) {
            showAlert("Erreur de validation", "Le mot de passe ne peut pas être vide", Alert.AlertType.ERROR);
            return false;
        }

        return true;
    }

    private boolean isValidEmail(String email) {
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private void redirectBasedOnRole(int role_id) {
        try {
            String fxmlPath;
            switch (role_id) {
                case 1:
                    fxmlPath = "/admininformation.fxml";
                    break;
                case 2:
                    fxmlPath = "/adduser.fxml";
                    break;
                default:
                    fxmlPath = "/admininformation.fxml";
                    break;
            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();

            Stage stage = (Stage) email.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            showAlert("Erreur de navigation", "Erreur de chargement de la vue : " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void showAlert(String titre, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Méthode pour l'inscription
    @FXML
    private void singup(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/adduser.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) email.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            showAlert("Erreur de navigation", "Impossible de charger la page d'inscription", Alert.AlertType.ERROR);
        }
    }

    // Autres méthodes (traduites mais non implémentées)
    @FXML
    private void togglePasswordVisibility(ActionEvent event) {} // Afficher/masquer mot de passe

    @FXML
    private void Remember_pass(ActionEvent event) {} // Se souvenir de moi

    @FXML
    private void forgotpass(ActionEvent event) {} // Mot de passe oublié

    @FXML
    private void faceid(ActionEvent event) {} // Authentification faciale
}