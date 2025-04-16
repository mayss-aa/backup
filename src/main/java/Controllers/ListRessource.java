package Controllers;

import Models.Ressource;
import Services.RessourceService;
import Utils.RessourceCell;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class ListRessource {

    @FXML
    private ListView<Ressource> ressourceListView;

    @FXML
    private TextField searchField;

    private final RessourceService ressourceService = new RessourceService();
    private ObservableList<Ressource> observableList;
    private FilteredList<Ressource> filteredList;

    @FXML
    public void initialize() {
        chargerRessources();

        filteredList = new FilteredList<>(observableList, p -> true);
        ressourceListView.setItems(filteredList);

        // 🔍 Recherche dynamique
        searchField.textProperty().addListener((ObservableValue<? extends String> obs, String oldValue, String newValue) -> {
            String filter = newValue.toLowerCase();
            filteredList.setPredicate(ressource -> {
                if (filter == null || filter.isEmpty()) return true;
                return ressource.getNom_ressource().toLowerCase().contains(filter)
                        || ressource.getType_ressource().toLowerCase().contains(filter)
                        || ressource.getUnite_mesure().toLowerCase().contains(filter)
                        || ressource.getStatut_ressource().toLowerCase().contains(filter);
            });
        });

        // ✅ Cellules personnalisées sans gestion manuelle du clic
        ressourceListView.setCellFactory(list -> new RessourceCell());
    }

    private void chargerRessources() {
        try {
            List<Ressource> ressourceList = ressourceService.findAll();
            observableList = FXCollections.observableArrayList(ressourceList);
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger les ressources :\n" + e.getMessage());
        }
    }

    @FXML
    private void handleAddRessource() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AddRessource.fxml"));
            Parent root = loader.load();
            ressourceListView.getScene().setRoot(root);
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible d'ouvrir le formulaire d'ajout :\n" + e.getMessage());
        }
    }

    @FXML
    private void handleRefresh() {
        chargerRessources();
        filteredList = new FilteredList<>(observableList, p -> true);
        ressourceListView.setItems(filteredList);
    }

    @FXML
    private void handleEditRessource() {
        Ressource selected = ressourceListView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "Aucune sélection", "Veuillez sélectionner une ressource à modifier.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/editressource.fxml"));
            Parent root = loader.load();

            // Récupération du contrôleur et injection de la ressource
            EditRessource controller = loader.getController();
            controller.setRessource(selected);

            // Ouverture d'une nouvelle fenêtre modale
            Stage stage = new Stage();
            stage.setTitle("Modifier Ressource");
            stage.setScene(new Scene(root));
            stage.showAndWait();

            // Rafraîchir la liste après la modification
            handleRefresh();

        } catch (IOException e) {
            e.printStackTrace(); // pour debug
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible d'ouvrir la fenêtre de modification :\n" + e.getMessage());
        }
    }


    @FXML
    private void handleDeleteRessource() {
        Ressource selected = ressourceListView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "Aucune sélection", "Veuillez sélectionner une ressource à supprimer.");
            return;
        }

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION, "Supprimer cette ressource ?", ButtonType.YES, ButtonType.NO);
        confirmation.setTitle("Confirmation");
        confirmation.setHeaderText(null);

        confirmation.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                try {
                    ressourceService.delete(selected);
                    observableList.remove(selected);
                    ressourceListView.getSelectionModel().clearSelection();
                    showAlert(Alert.AlertType.INFORMATION, "Suppression réussie", "La ressource a été supprimée avec succès !");
                } catch (SQLException e) {
                    showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de la suppression :\n" + e.getMessage());
                }
            }
        });
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    @FXML
    private void goToDepot() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ListDepot.fxml"));
            Parent root = loader.load();
            searchField.getScene().setRoot(root);
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible d'accéder à la vue Dépôt :\n" + e.getMessage());
        }
    }

    @FXML
    private void goToRessource() {
        // Rien à faire, déjà sur cette vue
    }

}
