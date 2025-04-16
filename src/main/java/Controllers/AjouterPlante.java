package Controllers;

import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import Models.Plante;
import Services.PlanteService;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.sql.SQLException;

public class AjouterPlante {

    @FXML
    private DatePicker date_plantation;

    @FXML
    private TextField nom_plante;

    @FXML
    private TextField rendement_estime;

    private PlanteService ps = new PlanteService();

    private Plante planteToEdit;

    @FXML
    void AfficherListe(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherPlante.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) date_plantation.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setPlanteToEdit(Plante plante) {
        this.planteToEdit = plante;
        nom_plante.setText(plante.getNom_plante());
        date_plantation.setValue(((java.sql.Date) plante.getDate_plantation()).toLocalDate());
        rendement_estime.setText(String.valueOf(plante.getRendement_estime()));
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

    @FXML
    void valider(ActionEvent event) {
        try {
            String nom = nom_plante.getText().trim();
            String rendementText = rendement_estime.getText().trim();
            LocalDate localDate = date_plantation.getValue();

            // Contrôles de saisie
            if (nom.isEmpty() || rendementText.isEmpty() || localDate == null) {
                throw new IllegalArgumentException("Tous les champs doivent être remplis.");
            }

            if (!nom.matches("[a-zA-Z\\s]+")) {
                throw new IllegalArgumentException("Le nom de la plante doit contenir uniquement des lettres.");
            }

            int rendement;
            try {
                rendement = Integer.parseInt(rendementText);
                if (rendement < 0) {
                    throw new IllegalArgumentException("Le rendement estimé doit être un nombre positif.");
                }
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Le rendement estimé doit être un nombre entier valide.");
            }

            if (localDate.isBefore(LocalDate.now())) {
                throw new IllegalArgumentException("La date de plantation doit être aujourd'hui ou une date future.");
            }

            Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

            if (planteToEdit != null) {
                planteToEdit.setNom_plante(nom);
                planteToEdit.setDate_plantation(date);
                planteToEdit.setRendement_estime(rendement);
                ps.update(planteToEdit);
            } else {
                ps.insert(new Plante(rendement, nom, date));
            }

            nom_plante.clear();
            rendement_estime.clear();
            date_plantation.setValue(null);
            planteToEdit = null;

        } catch (IllegalArgumentException | SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur de saisie");
            alert.setHeaderText("Une erreur est survenue");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }
}
