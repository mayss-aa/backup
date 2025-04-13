package Controllers;

import Models.Utilisateur;
import Models.Role;
import Services.UtilisateurService;
import Services.RoleService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.text.Text;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class AddUser implements Initializable {

    @FXML private TextField fname;
    @FXML private TextField lname;
    @FXML private TextField mail;
    @FXML private TextField phn;
    @FXML private PasswordField pass;
    @FXML private DatePicker daten;
    @FXML private ChoiceBox<String> genrel;

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
                showAlert("Critical Error", "ROLE_AGRI not found in database", Alert.AlertType.ERROR);
            }
        } catch (SQLException e) {
            showAlert("Database Error", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void singup(ActionEvent event) {
        clearValidationMessages();

        if(agriRole == null) {
            showAlert("Error", "Cannot create user - system role missing", Alert.AlertType.ERROR);
            return;
        }

        if(validateForm()) {
            try {
                Utilisateur newUser = createUserFromForm();
                userService.insert(newUser);
                handleSuccess();
            } catch (SQLException e) {
                showAlert("Registration Failed", e.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }

    private Utilisateur createUserFromForm() {
        return new Utilisateur(
                agriRole.getId(),
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

        isValid &= validateField(fname, fnamev, "First name is required");
        isValid &= validateField(lname, lnamev, "Last name is required");
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
            emailv.setText("Invalid email format");
            return false;
        }
        return true;
    }

    private boolean validatePhone() {
        if(!phn.getText().matches("\\d{8}")) {
            phonev.setText("8 digits required");
            return false;
        }
        return true;
    }

    private boolean validatePassword() {
        if(pass.getText().length() < 6) {
            passwordv.setText("Minimum 6 characters");
            return false;
        }
        return true;
    }

    private boolean validateDate() {
        if(daten.getValue() == null || daten.getValue().isAfter(LocalDate.now().minusYears(12))) {
            datenv.setText("Valid date required (12+ years)");
            return false;
        }
        return true;
    }

    private boolean validateGender() {
        if(genrel.getValue() == null) {
            genrev.setText("Gender selection required");
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

    private void handleSuccess() {
        showAlert("Success", "User registered successfully!", Alert.AlertType.INFORMATION);
        clearForm();
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
        // Implementation when needed
    }
}