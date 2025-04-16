package Controllers;

import Models.Plante;
import Models.Zone;
import Services.PlanteService;
import Services.ZoneService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class AjouterZone implements Initializable {

    @FXML
    private TextField nom_zone;

    @FXML
    private TextField superficie;

    @FXML
    private ComboBox<Plante> planteComboBox;

    private final ZoneService zoneService = new ZoneService();
    private final PlanteService planteService = new PlanteService();

    private Zone zoneToEdit; // Zone à modifier

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
    public void goToZone(ActionEvent event) {
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
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            List<Plante> plantes = planteService.findAll();
            planteComboBox.getItems().addAll(plantes);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Affichage du nom de plante dans la ComboBox
        planteComboBox.setCellFactory(cb -> new javafx.scene.control.ListCell<Plante>() {
            @Override
            protected void updateItem(Plante plante, boolean empty) {
                super.updateItem(plante, empty);
                setText(empty || plante == null ? null : plante.getNom_plante());
            }
        });

        planteComboBox.setButtonCell(new javafx.scene.control.ListCell<Plante>() {
            @Override
            protected void updateItem(Plante plante, boolean empty) {
                super.updateItem(plante, empty);
                setText(empty || plante == null ? null : plante.getNom_plante());
            }
        });
    }

    // Permet de pré-remplir les champs pour modification
    public void setZoneToEdit(Zone zone) {
        this.zoneToEdit = zone;
        nom_zone.setText(zone.getNom_zone());
        superficie.setText(String.valueOf(zone.getSuperficie()));

        // Pré-sélectionner la plante liée
        for (Plante p : planteComboBox.getItems()) {
            if (p.getId() == zone.getPlante_id()) {
                planteComboBox.getSelectionModel().select(p);
                break;
            }
        }
    }
    @FXML
    void AfficherListe(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherZone.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) nom_zone.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void Valider(ActionEvent event) {
        try {
            String nom = nom_zone.getText().trim();
            String superficieText = superficie.getText().trim();
            Plante selectedPlante = planteComboBox.getSelectionModel().getSelectedItem();

            // Contrôle : tous les champs doivent être remplis
            if (nom.isEmpty() || superficieText.isEmpty() || selectedPlante == null) {
                showAlert("Erreur", "Tous les champs doivent être remplis.");
                return;
            }

            // Contrôle : nom valide (lettres, espaces, tirets, mais pas de chiffres)
            if (!nom.matches("^[a-zA-ZÀ-ÿ\\s\\-']+$")) {
                showAlert("Erreur", "Le nom de la zone doit contenir uniquement des lettres.");
                return;
            }

            // Contrôle : superficie numérique
            int sup;
            try {
                sup = Integer.parseInt(superficieText);
                if (sup <= 0) {
                    showAlert("Erreur", "La superficie doit être un entier positif.");
                    return;
                }
            } catch (NumberFormatException e) {
                showAlert("Erreur", "Veuillez entrer une superficie valide (nombre entier).");
                return;
            }

            // Contrôle : nom unique (uniquement en création)
            if (zoneToEdit == null && zoneService.nomZoneExiste(nom)) {
                showAlert("Erreur", "Ce nom de zone existe déjà.");
                return;
            }

            if (zoneToEdit != null) {
                // Modification
                zoneToEdit.setNom_zone(nom);
                zoneToEdit.setSuperficie(sup);
                zoneToEdit.setPlante_id(selectedPlante.getId());
                zoneService.update(zoneToEdit);
            } else {
                // Création
                Zone newZone = new Zone(selectedPlante.getId(), sup, nom);
                zoneService.insert(newZone);
            }

            // Redirection
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherZone.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) nom_zone.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            showAlert("Erreur", e.getMessage());
        }
    }


    private void showAlert(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titre);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
