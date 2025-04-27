package Controllers;

import Models.Depot;
import Models.Ressource;
import Services.DepotService;
import Services.RessourceService;
import Utils.DepotListCell;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

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
    private TextField searchField;

    @FXML
    private Label floatingAlert;

    private final DepotService depotService = new DepotService();
    private final RessourceService ressourceService = new RessourceService();
    private FilteredList<Depot> filteredDepots;
    private Timeline autoRefreshTimeline;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadDepots();
        ListViewDepot.setCellFactory(list -> new DepotListCell());
        detectDepotsPleinsEtNotifier();
        setupAutoRefresh();
    }

    private void loadDepots() {
        try {
            List<Depot> currentDepots = depotService.findAll();
            ObservableList<Depot> observableList = FXCollections.observableArrayList(currentDepots);

            filteredDepots = new FilteredList<>(observableList, p -> true);
            ListViewDepot.setItems(filteredDepots);

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

    private void detectDepotsPleinsEtNotifier() {
        try {
            List<Depot> depots = depotService.findAll();
            List<Ressource> ressources = ressourceService.findAll();

            long countFull = depots.stream().filter(depot -> {
                double capM3 = convertToM3(depot.getUnite_cap_depot(), depot.getCapacite_depot());
                double totalM3 = ressources.stream()
                        .filter(r -> r.getDepot_id() == depot.getId())
                        .mapToDouble(r -> convertToM3(r.getUnite_mesure(), r.getQuantite_ressource()))
                        .sum();
                return capM3 > 0 && totalM3 >= capM3;
            }).count();

            if (countFull > 0) {
                String msg = (countFull == 1) ? "🔴 1 dépôt est plein !" : "🔴 " + countFull + " dépôts sont pleins !";
                showFloatingAlert(msg);
            } else {
                floatingAlert.setVisible(false);
                floatingAlert.setManaged(false);
            }

        } catch (SQLException e) {
            System.err.println("Erreur détection dépôts pleins : " + e.getMessage());
        }
    }

    private void showFloatingAlert(String message) {
        floatingAlert.setText(message);
        floatingAlert.setVisible(true);
        floatingAlert.setManaged(true);

        FadeTransition fadeIn = new FadeTransition(Duration.millis(300), floatingAlert);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        fadeIn.play();

        Timeline hide = new Timeline(new KeyFrame(Duration.seconds(10), e -> {
            FadeTransition fadeOut = new FadeTransition(Duration.millis(300), floatingAlert);
            fadeOut.setFromValue(1.0);
            fadeOut.setToValue(0.0);
            fadeOut.setOnFinished(ev -> {
                floatingAlert.setVisible(false);
                floatingAlert.setManaged(false);
            });
            fadeOut.play();
        }));
        hide.play();
    }

    private double convertToM3(String unite, int quantite) {
        String normalized = unite.trim().toLowerCase();
        return switch (normalized) {
            case "kg", "l", "litre", "litres" -> quantite / 1000.0;
            case "tonnes", "m³" -> quantite;
            default -> quantite;
        };
    }

    private void setupAutoRefresh() {
        autoRefreshTimeline = new Timeline(new KeyFrame(Duration.seconds(15), e -> detectDepotsPleinsEtNotifier()));
        autoRefreshTimeline.setCycleCount(Timeline.INDEFINITE);
        autoRefreshTimeline.play();
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
            detectDepotsPleinsEtNotifier();

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
            detectDepotsPleinsEtNotifier();
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
                detectDepotsPleinsEtNotifier();
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
            searchField.getScene().setRoot(root);
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible d’ouvrir la page Ressources : " + e.getMessage());
        }
    }

    @FXML
    private void goToDepot() {
        // Déjà ici
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
