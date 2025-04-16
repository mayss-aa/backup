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
import java.util.List;
import java.util.ResourceBundle;

public class EditRessource implements Initializable {

    @FXML private ComboBox<Integer> depotCombo;
    @FXML private TextField nomRessourceField;
    @FXML private TextField typeRessourceField;
    @FXML private TextField quantiteField;
    @FXML private ComboBox<String> uniteMesureCombo;
    @FXML private DatePicker dateAjoutPicker;
    @FXML private DatePicker dateExpirationPicker;
    @FXML private ComboBox<String> statutCombo;

    private Ressource ressource;  // la ressource à modifier
    private final RessourceService ressourceService = new RessourceService();

    public void setRessource(Ressource ressource) {
        this.ressource = ressource;

        depotCombo.setValue(ressource.getDepot_id());
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
            List<Integer> depotIds = ressourceService.getAllDepotIds();
            depotCombo.getItems().addAll(depotIds);
        } catch (SQLException e) {
            showAlert("Erreur", "Impossible de charger les dépôts : " + e.getMessage(), Alert.AlertType.ERROR);
        }

        uniteMesureCombo.getItems().addAll("Kg", "Litre", "Unité", "Tonnes", "m³");
        statutCombo.getItems().addAll("Disponible", "En rupture", "Périmé");
    }

    @FXML
    private void handleOk() {
        try {
            ressource.setDepot_id(depotCombo.getValue());
            ressource.setNom_ressource(nomRessourceField.getText().trim());
            ressource.setType_ressource(typeRessourceField.getText().trim());
            ressource.setQuantite_ressource(Integer.parseInt(quantiteField.getText().trim()));
            ressource.setUnite_mesure(uniteMesureCombo.getValue());
            ressource.setStatut_ressource(statutCombo.getValue());
            ressource.setDate_ajout_ressource(convertToDate(dateAjoutPicker.getValue()));
            ressource.setDate_expiration_ressource(convertToDate(dateExpirationPicker.getValue()));

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
