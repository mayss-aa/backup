package Controllers;

import Models.Depot;
import Services.DepotService;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.io.IOException;

public class AddDepot {

    @FXML private TextField nomDepotField, localisationField, capaciteField, uniteCapField;
    @FXML private ComboBox<String> typeStockageCombo, statutCombo;

    private final DepotService depotService = new DepotService();

    @FXML
    private void initialize() {
        typeStockageCombo.getItems().addAll("Froid", "Sec", "Humide", "Autre");
        statutCombo.getItems().addAll("Actif", "Inactif", "Maintenance");
    }

    @FXML
    private void handleAjouter() {
        String nom = nomDepotField.getText().trim();
        String localisation = localisationField.getText().trim();
        String capaciteStr = capaciteField.getText().trim();
        String unite = uniteCapField.getText().trim();
        String type = typeStockageCombo.getValue();
        String statut = statutCombo.getValue();

        // Vérification des champs vides
        if (nom.isEmpty() || localisation.isEmpty() || capaciteStr.isEmpty()
                || unite.isEmpty() || type == null || statut == null) {
            showAlert(AlertType.WARNING, "Champs manquants", "Veuillez remplir tous les champs.");
            return;
        }

        // Vérification que le nom contient uniquement des lettres et espaces
        if (!nom.matches("[a-zA-ZÀ-ÿ\\s]+")) {
            showAlert(AlertType.WARNING, "Nom invalide", "Le nom ne doit contenir que des lettres.");
            return;
        }

        // Vérification que l’unité contient uniquement des lettres
        if (!unite.matches("[a-zA-ZÀ-ÿ]+")) {
            showAlert(AlertType.WARNING, "Unité invalide", "L'unité ne doit contenir que des lettres.");
            return;
        }

        // Vérification que la capacité est un entier positif
        int capacite;
        try {
            capacite = Integer.parseInt(capaciteStr);
            if (capacite < 0) {
                showAlert(AlertType.WARNING, "Capacité invalide", "La capacité doit être un entier positif.");
                return;
            }
        } catch (NumberFormatException e) {
            showAlert(AlertType.ERROR, "Erreur", "Veuillez entrer un nombre valide pour la capacité.");
            return;
        }

        // Enregistrement si tout est valide
        try {
            Depot depot = new Depot(nom, localisation, capacite, unite, type, statut);
            depotService.insert(depot);

            showAlert(AlertType.INFORMATION, "Succès", "Dépôt ajouté avec succès !");
            loadScene("/ListDepot.fxml");

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Erreur", "Échec de l'ajout : " + e.getMessage());
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
                showAlert(AlertType.ERROR, "Erreur", "Chargement échoué : " + e.getMessage());
            }
        });
    }

    private void showAlert(AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle("Ardhismart");
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
