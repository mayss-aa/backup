package Controllers;

import Models.Maintenance;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import Services.MaintenanceService;


public class ListMaintenanceController implements Initializable {

    @FXML
    private ListView<String> ListViewMaintenance;

    private List<Maintenance> currentMaintenances;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadMaintenances();
    }

    private void loadMaintenances() {
        try {
            currentMaintenances = getMaintenancesFromDatabase();
            ObservableList<String> items = FXCollections.observableArrayList();

            for (Maintenance m : currentMaintenances) {
                String info = String.format("🛠️ %s | 🏷️ %s | 💰 %.2f | 🗓️ %s",
                        m.getMachine().getNomMachine(),
                        m.getDescription(),
                        m.getCoutMaintenance(),
                        m.getDateMaintenance());
                items.add(info);
            }

            ListViewMaintenance.setItems(items);
        } catch (SQLException e) {
            showError("Erreur lors du chargement des maintenances : " + e.getMessage());
        }
    }

    private List<Maintenance> getMaintenancesFromDatabase() throws SQLException {
        MaintenanceService service = new MaintenanceService();
        return service.findAll();
    }

    @FXML
    private void handleRefresh() {
        loadMaintenances();
    }

    @FXML
    private void handleDeleteMaintenance() {
        int selectedIndex = ListViewMaintenance.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0 && selectedIndex < currentMaintenances.size()) {
            Maintenance selectedMaintenance = currentMaintenances.get(selectedIndex);

            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Confirmation");
            confirm.setHeaderText("Supprimer cette maintenance ?");
            confirm.setContentText("Voulez-vous vraiment supprimer cette maintenance ?");

            Optional<ButtonType> result = confirm.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                try {
                    deleteMaintenanceFromDatabase(selectedMaintenance);
                    loadMaintenances();
                    showInfo("Maintenance supprimée avec succès.");
                } catch (SQLException e) {
                    showError("Erreur lors de la suppression : " + e.getMessage());
                }
            }
        } else {
            showError("Veuillez sélectionner une maintenance à supprimer.");
        }
    }

    private void deleteMaintenanceFromDatabase(Maintenance maintenance) throws SQLException {
        MaintenanceService service = new MaintenanceService();
        service.delete(maintenance);
    }

    @FXML
    private void handleEditMaintenance() {
        int selectedIndex = ListViewMaintenance.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0 && selectedIndex < currentMaintenances.size()) {
            Maintenance selectedMaintenance = currentMaintenances.get(selectedIndex);

            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/EditMaintenance.fxml")); // Chemin correct
                Parent root = loader.load();

                EditMaintenanceController controller = loader.getController();
                controller.setMaintenance(selectedMaintenance);

                Stage stage = new Stage();
                stage.setTitle("Modifier Maintenance");
                stage.setScene(new Scene(root));
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.showAndWait();

                loadMaintenances();

            } catch (IOException e) {
                e.printStackTrace();
                showError("Erreur lors de l'ouverture du formulaire de modification.");
            }
        } else {
            showError("Veuillez sélectionner une maintenance à modifier.");
        }
    }

    @FXML
    private void handleAddMaintenance() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/maintenance_form.fxml")); // Chemin correct
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Ajouter une Maintenance");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            loadMaintenances();

        } catch (IOException e) {
            e.printStackTrace();
            showError("Erreur lors de l'ouverture du formulaire d'ajout.");
        }
    }

    private void showError(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    private void showInfo(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
