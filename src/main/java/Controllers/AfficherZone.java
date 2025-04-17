package Controllers;

import Models.Plante;
import Models.Zone;
import Services.PlanteService;
import Services.ZoneService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class AfficherZone implements Initializable {

    @FXML
    private ListView<Zone> listViewZones;

    @FXML
    private TextField searchField;

    private final ZoneService zoneService = new ZoneService();
    private final PlanteService planteService = new PlanteService();

    // Liste source pour gestion dynamique
    private ObservableList<Zone> allZones = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            // Charger toutes les plantes pour affichage
            Map<Integer, Plante> planteMap = new HashMap<>();
            for (Plante p : planteService.readAll()) {
                planteMap.put(p.getId(), p);
            }

            // Charger toutes les zones dans la liste source
            List<Zone> zones = zoneService.findAll();
            allZones.setAll(zones);

            // Filtrage dynamique
            FilteredList<Zone> filteredZones = new FilteredList<>(allZones, z -> true);
            searchField.textProperty().addListener((obs, oldVal, newVal) -> {
                filteredZones.setPredicate(z -> {
                    if (newVal == null || newVal.isEmpty()) {
                        return true;
                    }
                    return z.getNom_zone().toLowerCase().contains(newVal.toLowerCase());
                });
            });

            // Tri (au besoin)
            SortedList<Zone> sortedZones = new SortedList<>(filteredZones);
            listViewZones.setItems(sortedZones);

            // Cellule personnalisée
            listViewZones.setCellFactory(lv -> new ListCell<Zone>() {
                @Override
                protected void updateItem(Zone z, boolean empty) {
                    super.updateItem(z, empty);
                    if (empty || z == null) {
                        setGraphic(null);
                        setText(null);
                    } else {
                        Plante p = planteMap.get(z.getPlante_id());
                        String nomPlante = p != null ? p.getNom_plante() : "Inconnue";

                        Label info = new Label(
                                "Zone : " + z.getNom_zone() +
                                        "\nSuperficie : " + z.getSuperficie() + " m²" +
                                        "\nPlante : " + nomPlante
                        );

                        Button btnDelete = new Button("Supprimer");
                        btnDelete.setOnAction(evt -> {
                            try {
                                zoneService.delete(z);
                                allZones.remove(z);
                            } catch (SQLException ex) {
                                ex.printStackTrace();
                            }
                        });

                        Button btnEdit = new Button("Modifier");
                        btnEdit.setOnAction(evt -> {
                            try {
                                FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterZone.fxml"));
                                Parent root = loader.load();
                                AjouterZone controller = loader.getController();
                                controller.setZoneToEdit(z);
                                Stage stage = (Stage) listViewZones.getScene().getWindow();
                                stage.setScene(new Scene(root));
                                stage.show();
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }
                        });

                        Region spacer = new Region();
                        HBox.setHgrow(spacer, Priority.ALWAYS);
                        HBox actions = new HBox(10, btnEdit, btnDelete);
                        HBox mainBox = new HBox(10, info, spacer, actions);
                        mainBox.setStyle("-fx-padding: 10px; -fx-alignment: center-left;");
                        setGraphic(mainBox);
                        setText(null);
                    }
                }
            });

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleAjouterZone() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterZone.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) listViewZones.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void goToZone(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/AfficherZone.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void goToPlante(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/AfficherPlante.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
