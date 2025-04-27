package Controllers;

import Models.Depot;
import Models.Ressource;
import Services.DepotService;
import Services.RessourceService;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AddRessource {

    @FXML private TextField nomField;
    @FXML private ComboBox<String> typeCombo;
    @FXML private TextField quantiteField;
    @FXML private ComboBox<String> uniteCombo;
    @FXML private DatePicker dateAjoutPicker;
    @FXML private DatePicker dateExpirationPicker;
    @FXML private ComboBox<String> statutCombo;
    @FXML private ComboBox<String> depotCombo;

    private final RessourceService ressourceService = new RessourceService();
    private final DepotService depotService = new DepotService();
    private Map<String, Integer> depotMap = new HashMap<>();

    @FXML
    private void initialize() {
        typeCombo.getItems().addAll("Semence", "Engrais", "Pesticide", "Eau", "Autre");
        uniteCombo.getItems().addAll("Kg", "L", "Tonnes", "m³");
        statutCombo.getItems().addAll("Disponible", "En rupture", "Périmé");

        try {
            depotMap = ressourceService.getDepotNomToIdMap();
            depotCombo.getItems().addAll(depotMap.keySet());
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger les dépôts : " + e.getMessage());
        }
    }

    @FXML
    private void handleSave() {
        try {
            String nom = nomField.getText().trim();
            String type = typeCombo.getValue();
            String qStr = quantiteField.getText().trim();
            String unit = uniteCombo.getValue();
            LocalDate dAj = dateAjoutPicker.getValue();
            LocalDate dEx = dateExpirationPicker.getValue();
            String stat = statutCombo.getValue();
            String depotNom = depotCombo.getValue();
            Integer dep = depotMap.get(depotNom);

            if (nom.isEmpty() || type == null || qStr.isEmpty() || unit == null || dAj == null || dEx == null || stat == null || dep == null) {
                showAlert(Alert.AlertType.WARNING, "Champs manquants", "Veuillez remplir tous les champs.");
                return;
            }

            if (!nom.matches("[a-zA-ZÀ-ÿ\\s]+")) {
                showAlert(Alert.AlertType.WARNING, "Nom invalide", "Le nom doit contenir uniquement des lettres.");
                return;
            }

            int qte;
            try {
                qte = Integer.parseInt(qStr);
                if (qte < 0) {
                    showAlert(Alert.AlertType.WARNING, "Quantité invalide", "La quantité doit être positive.");
                    return;
                }
            } catch (NumberFormatException ex) {
                showAlert(Alert.AlertType.WARNING, "Format invalide", "Quantité incorrecte.");
                return;
            }

            if (!dEx.isAfter(dAj)) {
                showAlert(Alert.AlertType.WARNING, "Dates invalides", "La date d'expiration doit être après la date d'ajout.");
                return;
            }

            Depot depot = depotService.findAll().stream()
                    .filter(d -> d.getId() == dep)
                    .findFirst()
                    .orElse(null);

            if (depot == null) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Dépôt introuvable.");
                return;
            }

            double capaciteDepotM3 = convertToM3(depot.getUnite_cap_depot(), depot.getCapacite_depot());
            double totalActuelM3 = ressourceService.findAll().stream()
                    .filter(r -> r.getDepot_id() == dep)
                    .mapToDouble(r -> convertToM3(r.getUnite_mesure(), r.getQuantite_ressource()))
                    .sum();
            double qteM3 = convertToM3(unit, qte);

            if (totalActuelM3 + qteM3 > capaciteDepotM3) {
                showAlert(Alert.AlertType.ERROR, "Dépôt plein", "La capacité du dépôt serait dépassée.");
                return;
            }

            Date dateAjout = Date.from(dAj.atStartOfDay(ZoneId.systemDefault()).toInstant());
            Date dateExpir = Date.from(dEx.atStartOfDay(ZoneId.systemDefault()).toInstant());

            Ressource ressource = new Ressource(dep, nom, type, qte, unit, dateAjout, dateExpir, stat);
            ressourceService.insert(ressource);

            showAlert(Alert.AlertType.INFORMATION, "Succès", "Ressource ajoutée avec succès.");
            loadScene("/ListRessource.fxml");

        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur SQL : " + e.getMessage());
        }
    }

    public static double convertToM3(String unite, int quantite) {
        String normalized = unite.trim().toLowerCase();
        return switch (normalized) {
            case "kg", "l", "litre", "litres" -> quantite / 1000.0;
            case "tonnes", "m³" -> quantite;
            default -> quantite;
        };
    }

    @FXML
    private void handleRetour() {
        loadScene("/ListRessource.fxml");
    }

    @FXML
    private void goToDepot() {
        loadScene("/AddDepot.fxml");
    }

    @FXML
    private void goToRessource() {
        loadScene("/ListRessource.fxml");
    }

    private void loadScene(String fxmlPath) {
        Platform.runLater(() -> {
            try {
                Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
                nomField.getScene().setRoot(root);
            } catch (IOException e) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Chargement de la vue échoué : " + e.getMessage());
            }
        });
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
