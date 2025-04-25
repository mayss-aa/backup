package Controllers;

import Models.Ressource;
import Services.RessourceService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Date;
import java.util.Map;
import java.util.ResourceBundle;

public class EditRessource implements Initializable {

    @FXML private ComboBox<String> depotCombo;
    @FXML private TextField nomRessourceField;
    @FXML private TextField typeRessourceField;
    @FXML private TextField quantiteField;
    @FXML private ComboBox<String> uniteMesureCombo;
    @FXML private DatePicker dateAjoutPicker;
    @FXML private DatePicker dateExpirationPicker;
    @FXML private ComboBox<String> statutCombo;

    private Ressource ressource;
    private final RessourceService ressourceService = new RessourceService();
    private Map<String, Integer> depotMap;

    public void setRessource(Ressource ressource) {
        this.ressource = ressource;

        depotCombo.setValue(getDepotNameById(ressource.getDepot_id()));
        nomRessourceField.setText(ressource.getNom_ressource());
        typeRessourceField.setText(ressource.getType_ressource());
        quantiteField.setText(String.valueOf(ressource.getQuantite_ressource()));
        uniteMesureCombo.setValue(ressource.getUnite_mesure());
        statutCombo.setValue(ressource.getStatut_ressource());

        dateAjoutPicker.setValue(convertToLocalDate(ressource.getDate_ajout_ressource()));
        dateExpirationPicker.setValue(convertToLocalDate(ressource.getDate_expiration_ressource()));
    }

    private LocalDate convertToLocalDate(Date date) {
        return ((java.sql.Date) date).toLocalDate();
    }

    private Date convertToDate(LocalDate localDate) {
        return java.sql.Date.valueOf(localDate);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            depotMap = ressourceService.getDepotNomToIdMap();
            depotCombo.getItems().addAll(depotMap.keySet());
        } catch (SQLException e) {
            showAlert("Erreur", "Impossible de charger les dépôts : " + e.getMessage(), Alert.AlertType.ERROR);
        }

        uniteMesureCombo.getItems().addAll("Kg", "Litre", "Unité", "Tonnes", "m³");
        statutCombo.getItems().addAll("Disponible", "En rupture", "Périmé");
    }

    private String getDepotNameById(int id) {
        for (Map.Entry<String, Integer> entry : depotMap.entrySet()) {
            if (entry.getValue().equals(id)) return entry.getKey();
        }
        return null;
    }

    @FXML
    private void handleOk() {
        try {
            String nom = nomRessourceField.getText().trim();
            String type = typeRessourceField.getText().trim();
            String qStr = quantiteField.getText().trim();
            String unite = uniteMesureCombo.getValue();
            String statut = statutCombo.getValue();
            String depotNom = depotCombo.getValue();
            Integer depotId = depotMap.get(depotNom);
            LocalDate dateAjout = dateAjoutPicker.getValue();
            LocalDate dateExp = dateExpirationPicker.getValue();

            if (nom.isEmpty() || type.isEmpty() || qStr.isEmpty() ||
                    unite == null || statut == null || depotId == null ||
                    dateAjout == null || dateExp == null) {
                showAlert("Champs manquants", "Veuillez remplir tous les champs.", Alert.AlertType.WARNING);
                return;
            }

            if (!nom.matches("[a-zA-ZÀ-ÿ\\s]+")) {
                showAlert("Nom invalide", "Le nom ne doit contenir que des lettres.", Alert.AlertType.WARNING);
                return;
            }

            int quantite;
            try {
                quantite = Integer.parseInt(qStr);
                if (quantite < 0) {
                    showAlert("Quantité invalide", "La quantité doit être un entier positif.", Alert.AlertType.WARNING);
                    return;
                }
            } catch (NumberFormatException e) {
                showAlert("Format invalide", "Veuillez entrer une quantité valide.", Alert.AlertType.WARNING);
                return;
            }

            if (!dateExp.isAfter(dateAjout)) {
                showAlert("Dates invalides", "La date d'expiration doit être postérieure à la date d'ajout.", Alert.AlertType.WARNING);
                return;
            }

            ressource.setDepot_id(depotId);
            ressource.setNom_ressource(nom);
            ressource.setType_ressource(type);
            ressource.setQuantite_ressource(quantite);
            ressource.setUnite_mesure(unite);
            ressource.setStatut_ressource(statut);
            ressource.setDate_ajout_ressource(convertToDate(dateAjout));
            ressource.setDate_expiration_ressource(convertToDate(dateExp));

            ressourceService.update(ressource);
            showAlert("Succès", "Ressource mise à jour avec succès.", Alert.AlertType.INFORMATION);
            closeWindow();

        } catch (Exception e) {
            showAlert("Erreur", "Échec de la mise à jour : " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleCancel() {
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) depotCombo.getScene().getWindow();
        stage.close();
    }

    private void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
