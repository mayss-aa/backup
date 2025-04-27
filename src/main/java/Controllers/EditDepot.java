package Controllers;

import Models.Depot;
import Services.DepotService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class EditDepot implements Initializable {

    @FXML
    private TextField nomDepotField;

    @FXML
    private TextField localisationField;

    @FXML
    private TextField capaciteField;

    @FXML
    private ComboBox<String> uniteCapCombo;

    @FXML
    private ComboBox<String> typeStockageCombo;

    @FXML
    private ComboBox<String> statutCombo;

    private Depot depot; // Le dépôt à modifier
    private final DepotService depotService = new DepotService();

    public void setDepot(Depot depot) {
        this.depot = depot;
        if (depot != null) {
            nomDepotField.setText(depot.getNom_depot());
            localisationField.setText(depot.getLocalisation_depot());
            capaciteField.setText(String.valueOf(depot.getCapacite_depot()));
            uniteCapCombo.setValue(depot.getUnite_cap_depot());
            typeStockageCombo.setValue(depot.getType_stockage_depot());
            statutCombo.setValue(depot.getStatut_depot());
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        uniteCapCombo.getItems().addAll("L", "Kg", "Tonnes", "m³");
        typeStockageCombo.getItems().addAll("Froid", "Sec", "Humide", "Autre");
        statutCombo.getItems().addAll("Actif", "Inactif", "Maintenance");
    }

    @FXML
    private void handleOk() {
        try {
            String nom = nomDepotField.getText().trim();
            String localisation = localisationField.getText().trim();
            String capaciteStr = capaciteField.getText().trim();
            String unite = uniteCapCombo.getValue();
            String type = typeStockageCombo.getValue();
            String statut = statutCombo.getValue();

            // ✅ Champs obligatoires
            if (nom.isEmpty() || localisation.isEmpty() || capaciteStr.isEmpty() ||
                    unite == null || type == null || statut == null) {
                showAlert(Alert.AlertType.ERROR, "Tous les champs doivent être remplis !");
                return;
            }

            // ✅ Vérification nom : lettres uniquement
            if (!nom.matches("[a-zA-ZÀ-ÿ\\s]+")) {
                showAlert(Alert.AlertType.ERROR, "Le nom ne doit contenir que des lettres.");
                return;
            }

            // ✅ Vérification capacité : entier positif
            int capacite;
            try {
                capacite = Integer.parseInt(capaciteStr);
                if (capacite < 0) {
                    showAlert(Alert.AlertType.ERROR, "La capacité doit être un entier positif.");
                    return;
                }
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.ERROR, "Capacité doit être un nombre entier.");
                return;
            }

            // ✅ Mise à jour des données
            depot.setNom_depot(nom);
            depot.setLocalisation_depot(localisation);
            depot.setCapacite_depot(capacite);
            depot.setUnite_cap_depot(unite);
            depot.setType_stockage_depot(type);
            depot.setStatut_depot(statut);

            depotService.update(depot);
            closeWindow();

        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur SQL : " + e.getMessage());
        }
    }

    @FXML
    private void handleCancel() {
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) nomDepotField.getScene().getWindow();
        stage.close();
    }

    private void showAlert(Alert.AlertType type, String message) {
        Alert alert = new Alert(type);
        alert.setTitle("Modification Dépôt");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
