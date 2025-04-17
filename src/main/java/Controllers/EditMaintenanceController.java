package Controllers;

import Models.Maintenance;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import Services.MaintenanceService;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class EditMaintenanceController implements Initializable {

    @FXML
    private TextField descriptionField;

    @FXML
    private TextField coutField;

    @FXML
    private TextField dateMaintenanceField; // Ce champ est maintenant un TextField, mais la date sera convertie

    private Maintenance maintenance; // La maintenance à modifier

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd"); // Format de la date

    public void setMaintenance(Maintenance maintenance) {
        this.maintenance = maintenance;
        if (maintenance != null) {
            descriptionField.setText(maintenance.getDescription());
            coutField.setText(String.valueOf(maintenance.getCoutMaintenance()));

            // Convertir LocalDate en String pour afficher dans le TextField
            String dateMaintenance = maintenance.getDateMaintenance().format(DATE_FORMATTER);
            dateMaintenanceField.setText(dateMaintenance);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Vous pouvez initialiser les valeurs des champs ici si nécessaire
    }

    @FXML
    private void handleOk() {
        try {
            String description = descriptionField.getText();
            String coutString = coutField.getText();
            String dateMaintenanceString = dateMaintenanceField.getText();

            if (description.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "La description de la maintenance ne peut pas être vide !");
                return;
            }

            double cout = 0;
            try {
                cout = Double.parseDouble(coutString);
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.ERROR, "Le coût de la maintenance doit être un nombre valide !");
                return;
            }

            LocalDate dateMaintenance = null;
            try {
                dateMaintenance = LocalDate.parse(dateMaintenanceString, DATE_FORMATTER);
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "La date de maintenance est invalide. Assurez-vous du format 'yyyy-MM-dd'.");
                return;
            }

            if (dateMaintenance.isAfter(LocalDate.now())) {
                showAlert(Alert.AlertType.ERROR, "La date de maintenance ne peut pas être dans le futur !");
                return;
            }

            // Mettre à jour la maintenance si toutes les validations sont réussies
            maintenance.setDescription(description);
            maintenance.setCoutMaintenance(cout);
            maintenance.setDateMaintenance(dateMaintenance);

            // Appel pour mettre à jour la maintenance dans la base de données
            updateMaintenanceInDatabase(maintenance);

            closeWindow();

        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur SQL : " + e.getMessage());
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur de conversion de la date. Assurez-vous du format 'yyyy-MM-dd'.");
        }
    }

    @FXML
    private void handleCancel() {
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) descriptionField.getScene().getWindow();
        stage.close();
    }

    private void showAlert(Alert.AlertType type, String message) {
        Alert alert = new Alert(type);
        alert.setTitle("Modification Maintenance");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void updateMaintenanceInDatabase(Maintenance maintenance) throws SQLException {
        MaintenanceService service = new MaintenanceService();
        service.update(maintenance);
    }
}
