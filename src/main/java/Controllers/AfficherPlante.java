package Controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import Models.Plante;
import Services.PlanteService;
import Services.ZoneService;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.EventObject;
import java.util.List;
import java.util.ResourceBundle;

public class AfficherPlante implements Initializable {

    @FXML
    private ListView<Plante> listViewPlantes;
    private ZoneService zoneService = new ZoneService();
    private PlanteService planteService = new PlanteService();

    @FXML
    private TextField searchField;

    private ObservableList<Plante> allPlantes = FXCollections.observableArrayList();

    @FXML
    private void handleAjouterPlante() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterPlante.fxml"));
            javafx.scene.Parent root = loader.load();
            javafx.stage.Stage stage = (javafx.stage.Stage) listViewPlantes.getScene().getWindow();
            stage.setScene(new javafx.scene.Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            List<Plante> plantes = planteService.readAll();
            allPlantes.setAll(plantes);
            listViewPlantes.setItems(allPlantes);

            listViewPlantes.setCellFactory(param -> new ListCell<Plante>() {
                @Override
                protected void updateItem(Plante p, boolean empty) {
                    super.updateItem(p, empty);
                    if (empty || p == null) {
                        setText(null);
                        setGraphic(null);
                    } else {
                        javafx.scene.control.Button deleteButton = new javafx.scene.control.Button("Supprimer");
                        javafx.scene.control.Button editButton = new javafx.scene.control.Button("Modifier");

                        deleteButton.setOnAction(event -> {
                            try {
                                zoneService.deleteByPlante(p);
                                planteService.delete(p);
                                allPlantes.remove(p); // Mettre à jour la liste originale
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        });

                        editButton.setOnAction(event -> {
                            try {
                                javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/AjouterPlante.fxml"));
                                javafx.scene.Parent root = loader.load();

                                AjouterPlante controller = loader.getController();
                                controller.setPlanteToEdit(p);

                                javafx.stage.Stage stage = (javafx.stage.Stage) listViewPlantes.getScene().getWindow();
                                stage.setScene(new javafx.scene.Scene(root));
                                stage.show();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        });

                        javafx.scene.control.Label planteInfo = new javafx.scene.control.Label(
                                "Nom : " + p.getNom_plante() +
                                        "\nDate de plantation : " + p.getDate_plantation() +
                                        "\nRendement estimé : " + p.getRendement_estime() + " kg"
                        );

                        javafx.scene.layout.Region spacer = new javafx.scene.layout.Region();
                        javafx.scene.layout.HBox.setHgrow(spacer, javafx.scene.layout.Priority.ALWAYS);
                        HBox buttonBox = new HBox(10, editButton, deleteButton);
                        HBox mainBox = new HBox(10, planteInfo, spacer, buttonBox);
                        mainBox.setStyle("-fx-padding: 10px; -fx-alignment: center-left;");
                        setText(null);
                        setGraphic(mainBox);
                    }
                }
            });

            // 🔍 Filtrage dynamique
            searchField.textProperty().addListener((observable, oldValue, newValue) -> {
                String lowerCaseFilter = newValue.toLowerCase();
                if (lowerCaseFilter.isEmpty()) {
                    listViewPlantes.setItems(allPlantes);
                } else {
                    ObservableList<Plante> filtered = FXCollections.observableArrayList();
                    for (Plante p : allPlantes) {
                        if (p.getNom_plante().toLowerCase().contains(lowerCaseFilter)) {
                            filtered.add(p);
                        }
                    }
                    listViewPlantes.setItems(filtered);
                }
            });

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void goToZone(javafx.event.ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/AjouterZone.fxml"));
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void goToPlante(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/AjouterPlante.fxml"));
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
