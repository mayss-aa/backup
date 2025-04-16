package Controllers;

import Models.Machine;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import Services.MachineService;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class EditMachineController implements Initializable {

    @FXML
    private TextField nomMachineField;

    @FXML
    private TextField etatMachineField;

    @FXML
    private TextField marqueMachineField;

    @FXML
    private TextField dateAchatField; // Ce champ est maintenant un TextField, mais la date sera convertie

    private Machine machine; // La machine à modifier

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd"); // Format de la date

    public void setMachine(Machine machine) {
        this.machine = machine;
        if (machine != null) {
            nomMachineField.setText(machine.getNomMachine());
            etatMachineField.setText(machine.getEtatMachine());
            marqueMachineField.setText(machine.getBrandMachine());

            // Convertir LocalDate en String pour afficher dans le TextField
            String dateAchat = machine.getDateAchat().format(DATE_FORMATTER);
            dateAchatField.setText(dateAchat);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Vous pouvez initialiser les valeurs des champs ici si nécessaire
    }

    @FXML
    private void handleOk() {
        try {
            String nom = nomMachineField.getText();
            String etat = etatMachineField.getText();
            String marque = marqueMachineField.getText();
            String dateAchatString = dateAchatField.getText();

            if (nom.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Le nom de la machine ne peut pas être vide !");
                return;
            }

            if (nom.length() < 3 || nom.length() > 30) {
                showAlert(Alert.AlertType.ERROR, "Le nom de la machine ne doit pas dépasser 30 caractères !");
                return;
            }

            if (etat.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "L'état de la machine ne peut pas être vide !");
                return;
            }

            if (marque.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "La marque de la machine ne peut pas être vide !");
                return;
            }

            if (marque.length() < 3 || marque.length() > 30) {
                showAlert(Alert.AlertType.ERROR, "La marque de la machine ne doit pas dépasser 30 caractères !");
                return;
            }

            LocalDate dateAchat = null;
            try {
                dateAchat = LocalDate.parse(dateAchatString, DATE_FORMATTER);
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "La date d'achat est invalide. Assurez-vous du format 'yyyy-MM-dd'.");
                return;
            }

            if (dateAchat.isAfter(LocalDate.now())) {
                showAlert(Alert.AlertType.ERROR, "La date d'achat ne peut pas être dans le futur !");
                return;
            }

            // Mettre à jour la machine si toutes les validations sont réussies
            machine.setNomMachine(nom);
            machine.setEtatMachine(etat);
            machine.setBrandMachine(marque);
            machine.setDateAchat(dateAchat);

            // Appel pour mettre à jour la machine dans la base de données
            updateMachineInDatabase(machine);

            closeWindow();

        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur SQL : " + e.getMessage());
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur de conversion de la date. Assurez-vous du format 'yyyy-MM-dd'.");
        }
    }

    @FXML
    private void handleCancel() {
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) nomMachineField.getScene().getWindow();
        stage.close();
    }

    private void showAlert(Alert.AlertType type, String message) {
        Alert alert = new Alert(type);
        alert.setTitle("Modification Machine");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void updateMachineInDatabase(Machine machine) throws SQLException {
        Services.MachineService service = new Services.MachineService();
        service.update(machine);
    }
}
