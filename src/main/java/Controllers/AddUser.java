package Controllers;

import Models.Utilisateur;
import Models.Role;
import Services.UtilisateurService;
import Services.RoleService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class AddUser implements Initializable {

    // Champs du formulaire
    @FXML private TextField fname;
    @FXML private TextField lname;
    @FXML private TextField mail;
    @FXML private TextField phn;
    @FXML private PasswordField pass;
    @FXML private DatePicker daten;
    @FXML private ChoiceBox<String> genrel;

    // Messages de validation
    @FXML private Text fnamev;
    @FXML private Text lnamev;
    @FXML private Text emailv;
    @FXML private Text phonev;
    @FXML private Text passwordv;
    @FXML private Text datenv;
    @FXML private Text genrev;

    private final UtilisateurService userService = new UtilisateurService();
    private final RoleService roleService = new RoleService();
    private Role agriRole;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setupGenderChoices();
        loadAgriRole();
    }

    private void setupGenderChoices() {
        genrel.getItems().addAll("Homme", "Femme");
    }

    private void loadAgriRole() {
        try {
            agriRole = roleService.findByName("ROLE_AGRI");
            if(agriRole == null) {
                showAlert("Erreur Critique", "ROLE_AGRI introuvable dans la base de données", Alert.AlertType.ERROR);
            }
        } catch (SQLException e) {
            showAlert("Erreur de Base de Données", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void singup(ActionEvent event) {
        clearValidationMessages();

        if(agriRole == null) {
            showAlert("Erreur", "Impossible de créer l'utilisateur - Rôle système manquant", Alert.AlertType.ERROR);
            return;
        }

        if(validateForm()) {
            try {
                Utilisateur newUser = createUserFromForm();
                userService.insert(newUser);
                handleSuccess(newUser.getId()); // Récupération de l'ID utilisateur
            } catch (SQLException e) {
                showAlert("Échec de l'Enregistrement", e.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }

    private Utilisateur createUserFromForm() {
        return new Utilisateur(
                agriRole.getrId(),
                fname.getText().trim(),
                lname.getText().trim(),
                mail.getText().trim(),
                genrel.getValue(),
                daten.getValue(),
                phn.getText().trim(),
                pass.getText()
        );
    }

    private boolean validateForm() {
        boolean isValid = true;

        isValid &= validateField(fname, fnamev, "Prénom requis");
        isValid &= validateField(lname, lnamev, "Nom de famille requis");
        isValid &= validateEmail();
        isValid &= validatePhone();
        isValid &= validatePassword();
        isValid &= validateDate();
        isValid &= validateGender();

        return isValid;
    }

    private boolean validateField(TextField field, Text error, String message) {
        if(field.getText().trim().isEmpty()) {
            error.setText(message);
            return false;
        }
        return true;
    }

    private boolean validateEmail() {
        if(!mail.getText().matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            emailv.setText("Format email invalide");
            return false;
        }
        return true;
    }

    private boolean validatePhone() {
        if(!phn.getText().matches("\\d{8}")) {
            phonev.setText("8 chiffres requis");
            return false;
        }
        return true;
    }

    private boolean validatePassword() {
        if(pass.getText().length() < 6) {
            passwordv.setText("Minimum 6 caractères");
            return false;
        }
        return true;
    }

    private boolean validateDate() {
        if(daten.getValue() == null || daten.getValue().isAfter(LocalDate.now().minusYears(12))) {
            datenv.setText("Date valide requise (12+ ans)");
            return false;
        }
        return true;
    }

    private boolean validateGender() {
        if(genrel.getValue() == null) {
            genrev.setText("Sélection du genre requise");
            return false;
        }
        return true;
    }

    private void clearValidationMessages() {
        fnamev.setText("");
        lnamev.setText("");
        emailv.setText("");
        phonev.setText("");
        passwordv.setText("");
        datenv.setText("");
        genrev.setText("");
    }

    private void handleSuccess(int userId) {
        showAlert("Succès", "Utilisateur enregistré avec succès !", Alert.AlertType.INFORMATION);
        redirectToAddressSignup(userId);
        clearForm();
    }

    private void redirectToAddressSignup(int userId) {
        try {
            // 1. Create new stage instead of reusing existing one
            Stage addressStage = new Stage();

            // 2. Load FXML with explicit controller initialization
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AdressSingup.fxml"));
            Parent root = loader.load();

            // 3. Get controller and set user ID
            AdressController controller = loader.getController();
            controller.setUserId(userId);  // Make sure this properly sets the ID

            // 4. Configure new stage
            addressStage.setTitle("Adresse de l'utilisateur");
            addressStage.setScene(new Scene(root));

            // 5. Close current window
            Stage currentStage = (Stage) fname.getScene().getWindow();
            currentStage.close();

            // 6. Show new stage
            addressStage.show();

        } catch (IOException e) {
            showAlert("Erreur", "Erreur de chargement: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    private void clearForm() {
        fname.clear();
        lname.clear();
        mail.clear();
        phn.clear();
        pass.clear();
        daten.setValue(null);
        genrel.setValue(null);
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML

    private void loginlink(ActionEvent event) {
        try {
            // Charger le fichier FXML de login
            Parent root = FXMLLoader.load(getClass().getResource("/login.fxml"));

            // Récupérer la scène actuelle
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Changer de scène
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            showAlert("Erreur Navigation", "Impossible de charger la page de connexion: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }
}