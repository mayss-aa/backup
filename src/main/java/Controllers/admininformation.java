package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;

public class admininformation {

    // Navigation principale
    @FXML
    private void goutilisateurs(ActionEvent event) {
        loadFXML("/admin.fxml", event);
    }

    @FXML
    private void goadresses(ActionEvent event) {
        loadFXML("/adminadress.fxml", event);
    }



    private void loadFXML(String fxmlPath, ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showAlert("Erreur", "Échec du chargement : " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Autres méthodes
    @FXML
    private void goprofile(ActionEvent event) {
    }



    @FXML
    private void logout(ActionEvent event) {
        try {
            // Charger la page de login
            Parent root = FXMLLoader.load(getClass().getResource("/login.fxml"));

            // Créer une nouvelle fenêtre
            Stage loginStage = new Stage();
            loginStage.setScene(new Scene(root));
            loginStage.setTitle("ArdhiSmart - Connexion");

            // Fermer la fenêtre actuelle
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();

            // Afficher la nouvelle fenêtre
            loginStage.show();

        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur de déconnexion");
            alert.setHeaderText(null);
            alert.setContentText("Erreur lors du chargement : \n" + e.getMessage());
            alert.showAndWait();
        }
    }
    @FXML
    private void changephoto(ActionEvent event) {
        // Logique pour changer la photo
        System.out.println("Changement de photo...");
    }




    @FXML
    public void goToNote(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/AfficherNote.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void gomachine(ActionEvent event) {
        loadFXML("/machine_form.fxml", event);

    }

    @FXML
    private void goproduit(ActionEvent event) {
        loadFXML("/produit_back.fxml", event);
    }

    @FXML
    private void gostock(ActionEvent event) {
        loadFXML("/AddDepot.fxml", event);

    }
    @FXML
    private void gozones(ActionEvent event) {
        loadFXML("/AjouterZone.fxml", event);
    }

    @FXML
    private void goplantes(ActionEvent event) {
          loadFXML("/AjouterPlante.fxml", event);
    }

}