package Controllers;

import Models.Address;
import Models.Utilisateur;
import Services.AddressService;
import Utils.UserSession;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;

public class AdressController {
    private int userId;
    private final AddressService addressService = new AddressService();
    private final UserSession session = UserSession.getInstance();

    @FXML private TextField adressline;
    @FXML private TextField city;
    @FXML private TextField country;
    @FXML private TextField postalcode;
    @FXML private TextField state;
    @FXML private Text addressError;
    @FXML private Text cityError;
    @FXML private Text postalCodeError;
    @FXML private Text countryError;

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @FXML


    void addadressSingup(ActionEvent event) {
        clearErrors();

        if(validateAddress()) {
            try {
                // Création et sauvegarde de l'adresse
                Address address = new Address(
                        userId,
                        adressline.getText().trim(),
                        city.getText().trim(),
                        state.getText().trim(),
                        postalcode.getText().trim(),
                        country.getText().trim(),
                        LocalDate.now()
                );

                addressService.addaddres(address); // Correction du nom de méthode

                // Fermer la fenêtre actuelle
                Stage currentStage = (Stage) adressline.getScene().getWindow();
                currentStage.close();

                // Charger admin.fxml
                Parent root = FXMLLoader.load(getClass().getResource("/admin.fxml"));
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.show();

            } catch (SQLException e) {
                showAlert("Erreur", "Échec de sauvegarde: " + e.getMessage(), Alert.AlertType.ERROR);
            } catch (IOException e) {
                showAlert("Erreur", "Redirection impossible: " + e.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }

    private boolean validateAddress() {
        boolean isValid = true;

        if(adressline.getText().isBlank()) {
            addressError.setText("Adresse requise");
            isValid = false;
        }

        if(city.getText().isBlank()) {
            cityError.setText("Ville requise");
            isValid = false;
        }

        if(!postalcode.getText().matches("\\d{4,}")) {
            postalCodeError.setText("Code postal invalide (min. 4 chiffres)");
            isValid = false;
        }

        if(country.getText().isBlank()) {
            countryError.setText("Pays requis");
            isValid = false;
        }

        return isValid;
    }

    @FXML
    void skip(ActionEvent event) throws IOException {
        redirectBasedOnRole();
    }

    private void redirectBasedOnRole() throws IOException {
        Utilisateur currentUser = session.getUser();
        if(currentUser != null && currentUser.getRole_id() == 1) {
            SceneController.loadPage("/admininformation.fxml");
        } else {
            SceneController.loadPage("/newprofile.fxml");
        }
    }

    private void clearErrors() {
        addressError.setText("");
        cityError.setText("");
        postalCodeError.setText("");
        countryError.setText("");
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    public void skipsingup(ActionEvent actionEvent) {
        try {
            // Récupérer l'utilisateur de la session
            Utilisateur currentUser = session.getUser();

            // Redirection conditionnelle

                SceneController.loadPage("/admininformation.fxml"); // Redirection directe

        } catch (IOException e) {
            showAlert("Erreur", "Échec de redirection: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }
}