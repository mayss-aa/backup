package Controllers;

import Models.Depot;
import Services.DepotService;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.sql.SQLException;

public class AddDepot {

    @FXML private TextField nomDepotField, localisationField, capaciteField;
    @FXML private ComboBox<String> uniteCapField;
    @FXML private ComboBox<String> typeStockageCombo, statutCombo;

    private final DepotService depotService = new DepotService();

    @FXML
    private void initialize() {
        typeStockageCombo.getItems().addAll("Froid", "Sec", "Humide", "Autre");
        statutCombo.getItems().addAll("Actif", "Inactif", "Maintenance");
        uniteCapField.getItems().addAll("Kg", "Litre", "Tonne", "m³");
    }

    @FXML
    private void handleAjouter() {
        String nom = nomDepotField.getText().trim();
        String localisation = localisationField.getText().trim();
        String capaciteStr = capaciteField.getText().trim();
        String unite = uniteCapField.getValue();
        String type = typeStockageCombo.getValue();
        String statut = statutCombo.getValue();

        if (nom.isEmpty() || localisation.isEmpty() || capaciteStr.isEmpty()
                || unite == null || type == null || statut == null) {
            showAlert(Alert.AlertType.WARNING, "Champs manquants", "Veuillez remplir tous les champs.");
            return;
        }

        if (!nom.matches("[a-zA-ZÀ-ÿ\\s]+")) {
            showAlert(Alert.AlertType.WARNING, "Nom invalide", "Le nom ne doit contenir que des lettres.");
            return;
        }

        try {
            if (depotService.existsByName(nom)) {
                showAlert(Alert.AlertType.ERROR, "Nom existant", "Un dépôt avec ce nom existe déjà.");
                return;
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de la vérification du nom.");
            return;
        }

        int capacite;
        try {
            capacite = Integer.parseInt(capaciteStr);
            if (capacite < 0) {
                showAlert(Alert.AlertType.WARNING, "Capacité invalide", "La capacité doit être un entier positif.");
                return;
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez entrer un nombre valide pour la capacité.");
            return;
        }

        try {
            Depot depot = new Depot(nom, localisation, capacite, unite, type, statut);
            depotService.insert(depot);

            showAlert(Alert.AlertType.INFORMATION, "Succès", "Dépôt ajouté avec succès !");
            loadScene("/ListDepot.fxml");

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Échec de l'ajout : " + e.getMessage());
        }
    }

    @FXML
    private void handleRetour() {
        loadScene("/ListDepot.fxml");
    }

    @FXML
    private void goToDepot() {
        // Déjà ici
    }

    @FXML
    private void goToRessource() {
        loadScene("/AddRessource.fxml");
    }

    private void loadScene(String fxmlPath) {
        Platform.runLater(() -> {
            try {
                Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
                nomDepotField.getScene().setRoot(root);
            } catch (IOException e) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Chargement échoué : " + e.getMessage());
            }
        });
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle("ArdhiSmart");
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
