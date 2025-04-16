package Controllers;

import Models.Depot;
import Services.DepotService;
import Utils.DepotListCell;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class ListDepot implements Initializable {

    @FXML
    private ListView<Depot> ListViewDepot;

    @FXML
    private TextField searchField; // 🔍 Ajout du champ de recherche

    private final DepotService depotService = new DepotService();
    private FilteredList<Depot> filteredDepots;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadDepots();
        ListViewDepot.setCellFactory(list -> new DepotListCell()); // design de cellule
    }

    private void loadDepots() {
        try {
            List<Depot> currentDepots = depotService.findAll();
            ObservableList<Depot> observableList = FXCollections.observableArrayList(currentDepots);

            filteredDepots = new FilteredList<>(observableList, p -> true);
            ListViewDepot.setItems(filteredDepots);

            // 🔍 Activer la recherche en temps réel
            if (searchField != null) {
                searchField.textProperty().addListener((observable, oldValue, newValue) -> {
                    String filter = newValue.toLowerCase();

                    filteredDepots.setPredicate(depot -> {
                        if (filter == null || filter.isEmpty()) return true;

                        return depot.getNom_depot().toLowerCase().contains(filter)
                                || depot.getLocalisation_depot().toLowerCase().contains(filter)
                                || depot.getType_stockage_depot().toLowerCase().contains(filter);
                    });
                });
            }

        } catch (SQLException e) {
            showError("Erreur lors du chargement des dépôts : " + e.getMessage());
        }
    }

    @FXML
    private void handleRefresh() {
        loadDepots();
    }

    @FXML
    private void handleAddDepot() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AddDepot.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Ajouter un Dépôt");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            loadDepots();

        } catch (IOException e) {
            showError("Erreur lors de l'ouverture du formulaire d'ajout.");
        }
    }

    @FXML
    public void handleEditDepot(ActionEvent event) {
        Depot selected = ListViewDepot.getSelectionModel().getSelectedItem();
        if (selected != null) {
            editDepot(selected);
        } else {
            showError("Veuillez sélectionner un dépôt à modifier.");
        }
    }

    @FXML
    public void handleDeleteDepot(ActionEvent event) {
        Depot selected = ListViewDepot.getSelectionModel().getSelectedItem();
        if (selected != null) {
            deleteDepot(selected);
        } else {
            showError("Veuillez sélectionner un dépôt à supprimer.");
        }
    }

    public void editDepot(Depot depot) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/EditDepot.fxml"));
            Parent root = loader.load();

            EditDepot controller = loader.getController();
            controller.setDepot(depot);

            Stage stage = new Stage();
            stage.setTitle("Modifier Dépôt");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            loadDepots();
        } catch (IOException e) {
            showError("Erreur lors de la modification.");
        }
    }

    public void deleteDepot(Depot depot) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirmation");
        confirm.setHeaderText("Supprimer ce dépôt ?");
        confirm.setContentText("Supprimer : " + depot.getNom_depot());

        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                depotService.delete(depot);
                loadDepots();
                showInfo("Dépôt supprimé.");
            } catch (SQLException e) {
                showError("Erreur lors de la suppression : " + e.getMessage());
            }
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
    @FXML
    private void goToRessource() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AddRessource.fxml"));
            Parent root = loader.load();
            searchField.getScene().setRoot(root);  // remplace tout le contenu
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible d’ouvrir la page Ressources : " + e.getMessage());
        }
    }

    @FXML
    private void goToDepot() {
        // Rien à faire : on est déjà dans ListDepot.fxml
    }

    // Méthode utilitaire pour afficher les erreurs
    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

}
