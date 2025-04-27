package Controllers;

import Models.Machine;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.time.LocalDate;

public class MachineFormController {

    @FXML
    private TextField nomMachineField, brandMachineField;

    @FXML
    private ComboBox<String> etatMachineCombo;

    @FXML
    private DatePicker dateAchatPicker;

    @FXML
    public void initialize() {
        etatMachineCombo.getItems().addAll("Nouvelle", "En panne", "À réparer");
    }

    @FXML
    private void handleAjouter() {
        // Récupérer les valeurs des champs
        String nom = nomMachineField.getText().trim();
        String marque = brandMachineField.getText().trim();
        String etat = etatMachineCombo.getValue();
        LocalDate dateAchat = dateAchatPicker.getValue();

        if (nom.isEmpty() && marque.isEmpty() && etat == null && dateAchat == null) {
            showAlert(Alert.AlertType.ERROR, "Formulaire vide. Veuillez tout remplir.");
            return;
        }

        if (nom.isEmpty() || marque.isEmpty() || etat == null || dateAchat == null) {
            showAlert(Alert.AlertType.WARNING, "Champs obligatoires manquants.");
            return;
        }

        // 3. Validation du nom (longueur entre 3 et 30 caractères)
        if (nom.length() < 3 || nom.length() > 30) {
            showAlert(Alert.AlertType.ERROR, "Le nom de la machine ne doit pas dépasser 30 caractères.");
            return;
        }
        if (etat.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "L'état de la machine ne peut pas être vide !");
            return;
        }

        // 4. Validation de la marque (longueur entre 3 et 30 caractères)
        if (marque.length() < 3 || marque.length() > 30) {
            showAlert(Alert.AlertType.ERROR, "La marque de la machine ne doit pas dépasser 30 caractères.");
            return;
        }


        // 5. Afficher les valeurs pour débogage
        System.out.println("Nom: " + nom);
        System.out.println("Marque: " + marque);
        System.out.println("État: " + etat);
        System.out.println("Date d'achat: " + dateAchat);

        try {
            // Créer l'objet Machine
            Machine machine = new Machine(0, nom, etat, marque, dateAchat);

            // Appeler le service pour insérer dans la base de données
            Services.MachineService service = new Services.MachineService();
            service.insert(machine);

            // Afficher un message de succès
            showAlert(Alert.AlertType.INFORMATION, "✅ Machine ajoutée avec succès.");

            // Fermer la fenêtre actuelle
            Stage currentStage = (Stage) nomMachineField.getScene().getWindow();
            currentStage.close();

            // Charger la scène pour afficher la liste des machines
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ListeMachine.fxml"));
            Parent root = loader.load();

            // Ouvrir la nouvelle fenêtre de la liste des machines
            Stage newStage = new Stage();
            newStage.setTitle("Liste des Machines");
            newStage.setScene(new Scene(root));
            newStage.initModality(Modality.APPLICATION_MODAL);
            newStage.show();

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur lors de l'ajout : " + e.getMessage());
        }
    }

    @FXML
    private void handleAnnuler() {
        Stage stage = (Stage) nomMachineField.getScene().getWindow();
        stage.close();
    }

    private void showAlert(Alert.AlertType type, String message) {
        Alert alert = new Alert(type);
        alert.setTitle("Gestion des Machines");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void onprofilamen(ActionEvent event) {
       // loadFXML("/admininformation.fxml", event);

    }
    @FXML
    private void ommaint(ActionEvent event) {
        try {
            Parent categorieView = FXMLLoader.load(getClass().getResource("/maintenance_form.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(categorieView));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
