package Controllers;

import Models.Machine;
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
import Services.MachineService;
import java.util.List;
import java.sql.SQLException;


public class ListMachineController implements Initializable {

    @FXML
    private ListView<String> ListViewMachine;

    private List<Machine> currentMachines;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadMachines();
    }

    private void loadMachines() {
        try {
            currentMachines = getMachinesFromDatabase(); // Méthode pour récupérer les machines directement
            ObservableList<String> items = FXCollections.observableArrayList();

            for (Machine m : currentMachines) {
                String info = String.format("🔧 %s | 🏷️ %s | 🔄 %s | 🗓️ %s",
                        m.getNomMachine(),
                        m.getEtatMachine(),
                        m.getBrandMachine(),
                        m.getDateAchat());
                items.add(info);
            }

            ListViewMachine.setItems(items); // Remplir la ListView avec les machines
        } catch (SQLException e) {
            showError("Erreur lors du chargement des machines : " + e.getMessage());
        }
    }

    private List<Machine> getMachinesFromDatabase() throws SQLException {
        MachineService service = new MachineService();
        return service.findAll();
    }



    @FXML
    private void handleRefresh() {
        loadMachines(); // Recharger les machines
    }

    @FXML
    private void handleDeleteMachine() {
        int selectedIndex = ListViewMachine.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0 && selectedIndex < currentMachines.size()) {
            Machine selectedMachine = currentMachines.get(selectedIndex);

            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Confirmation");
            confirm.setHeaderText("Supprimer cette machine ?");
            confirm.setContentText("Voulez-vous vraiment supprimer : " + selectedMachine.getNomMachine() + " ?");

            Optional<ButtonType> result = confirm.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                try {
                    deleteMachineFromDatabase(selectedMachine); // Méthode pour supprimer la machine directement
                    loadMachines(); // Recharger la liste après suppression
                    showInfo("Machine supprimée avec succès.");
                } catch (SQLException e) {
                    showError("Erreur lors de la suppression : " + e.getMessage());
                }
            }
        } else {
            showError("Veuillez sélectionner une machine à supprimer.");
        }
    }

    private void deleteMachineFromDatabase(Machine machine) throws SQLException {
        MachineService service = new MachineService();
        service.delete(machine);
    }



    @FXML
    private void handleEditMachine() {
        int selectedIndex = ListViewMachine.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0 && selectedIndex < currentMachines.size()) {
            Machine selectedMachine = currentMachines.get(selectedIndex);

            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/EditMachine.fxml")); // Chemin correct
                Parent root = loader.load();

                EditMachineController controller = loader.getController();
                controller.setMachine(selectedMachine); // Passer la machine sélectionnée au contrôleur de modification

                Stage stage = new Stage();
                stage.setTitle("Modifier Machine");
                stage.setScene(new Scene(root));
                stage.initModality(Modality.APPLICATION_MODAL); // Bloquer jusqu'à fermeture
                stage.showAndWait();

                loadMachines(); // Recharger après modification

            } catch (IOException e) {
                e.printStackTrace();
                showError("Erreur lors de l'ouverture du formulaire de modification.");
            }
        } else {
            showError("Veuillez sélectionner une machine à modifier.");
        }
    }

    @FXML
    private void handleAddMachine() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/machine_form.fxml")); // Chemin correct
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Ajouter une Machine");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL); // Bloque jusqu'à fermeture
            stage.showAndWait();

            loadMachines(); // Recharger la liste après ajout

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
