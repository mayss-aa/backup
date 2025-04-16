package Controllers;

import Models.Maintenance;
import Models.Machine;
import Services.MachineService;
import Services.MaintenanceService;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.beans.binding.Bindings;
import javafx.scene.control.cell.TextFieldTableCell;

import java.time.LocalDate;
import java.util.List;

public class MaintenanceFormController {

    // Champs du formulaire
    @FXML private DatePicker dateMaintenancePicker;
    @FXML private TextField descriptionField;
    @FXML private TextField coutMaintenanceField;
    @FXML private ComboBox<Machine> machineComboBox;
    @FXML private Label errorLabel;
    @FXML private Button saveButton;
    @FXML private Button cancelButton;

    // TableView
    @FXML private TableView<Maintenance> maintenanceTable;
    @FXML private TableColumn<Maintenance, LocalDate> dateColumn;
    @FXML private TableColumn<Maintenance, String> descriptionColumn;
    @FXML private TableColumn<Maintenance, Double> coutColumn;
    @FXML private TableColumn<Maintenance, String> machineColumn;
    @FXML private TableColumn<Maintenance, Void> actionColumn;

    private final ObservableList<Maintenance> maintenanceList = FXCollections.observableArrayList();
    private final ObservableList<Machine> machineList = FXCollections.observableArrayList();
    private Maintenance maintenanceToEdit = null;

    private final MachineService machineService = new MachineService();
    private final MaintenanceService maintenanceService = new MaintenanceService();


    public void initialize() {
        loadMachines(); // Charger les machines dès l'initialisation
    }

    private void loadMachines() {
        try {
            // Récupérer les machines via le service
            List<Machine> machines = machineService.findAll();

            // Ajouter les machines dans le ComboBox
            machineList.setAll(machines);
            machineComboBox.setItems(machineList); // Associer la liste des machines au ComboBox
        } catch (Exception e) {
            e.printStackTrace(); // Afficher l'exception

        }
    }

    @FXML
    private void handleAjouter() {
        // Récupérer les valeurs des champs
        LocalDate dateMaintenance = dateMaintenancePicker.getValue();
        String description = descriptionField.getText().trim();
        String coutStr = coutMaintenanceField.getText().trim();
        Machine selectedMachine = machineComboBox.getValue();

        // Vérification si le formulaire est vide
        if ((dateMaintenance == null) && description.isEmpty() && coutStr.isEmpty() && selectedMachine == null) {
            showAlert(Alert.AlertType.ERROR, "Formulaire vide. Veuillez tout remplir.");
            return;
        }

        // Vérification des champs obligatoires
        if (dateMaintenance == null || description.isEmpty() || coutStr.isEmpty() || selectedMachine == null) {
            showAlert(Alert.AlertType.WARNING, "Veuillez remplir tous les champs obligatoires.");
            return;
        }

        // Vérification de la longueur de la description
        if (description.length() < 5 || description.length() > 255) {
            showAlert(Alert.AlertType.ERROR, "La description doit contenir entre 5 et 255 caractères.");
            return;
        }

        double coutMaintenance;
        try {
            coutMaintenance = Double.parseDouble(coutStr);
            if (coutMaintenance < 0) {
                showAlert(Alert.AlertType.ERROR, "Le coût doit être un nombre positif.");
                return;
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Le coût doit être un nombre valide.");
            return;
        }

        // Affichage pour débogage
        System.out.println("Date: " + dateMaintenance);
        System.out.println("Description: " + description);
        System.out.println("Coût: " + coutMaintenance);
        System.out.println("Machine: " + selectedMachine.getNomMachine());

        try {
            // Création de l'objet Maintenance
            Maintenance maintenance = new Maintenance(0, dateMaintenance, description, coutMaintenance, selectedMachine);

            // Insertion dans la base
            MaintenanceService service = new MaintenanceService();
            service.insert(maintenance);

            // Message de succès
            showAlert(Alert.AlertType.INFORMATION, "✅ Maintenance ajoutée avec succès.");

            // Fermer la fenêtre actuelle
            Stage currentStage = (Stage) dateMaintenancePicker.getScene().getWindow();
            currentStage.close();

            // Charger la liste des maintenances
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ListeMaintenance.fxml"));
            Parent root = loader.load();

            Stage newStage = new Stage();
            newStage.setTitle("Liste des Maintenances");
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
        Stage stage = (Stage) dateMaintenancePicker.getScene().getWindow();
        stage.close();
    }


    private boolean isValidInput() {
        StringBuilder errors = new StringBuilder();

        if (machineComboBox.getValue() == null) {
            errors.append("Veuillez choisir une machine.\n");
        }
        if (dateMaintenancePicker.getValue() == null) {
            errors.append("Veuillez choisir une date.\n");
        }
        if (descriptionField.getText().isEmpty()) {
            errors.append("Veuillez entrer une description.\n");
        }
        if (coutMaintenanceField.getText().isEmpty()) {
            errors.append("Veuillez entrer le coût de la maintenance.\n");
        } else {
            try {
                double cost = Double.parseDouble(coutMaintenanceField.getText());
                if (cost < 0) {
                    errors.append("Le coût doit être positif.\n");
                }
            } catch (NumberFormatException e) {
                errors.append("Le coût doit être un nombre valide.\n");
            }
        }

        if (errors.length() > 0) {
            errorLabel.setText(errors.toString());

            return false;
        }

        errorLabel.setText("");
        return true;
    }

    private void addActionButtons() {
        actionColumn.setCellFactory(param -> new TableCell<>() {
            private final Button btnModifier = new Button("Modifier");
            private final Button btnSupprimer = new Button("Supprimer");
            private final HBox pane = new HBox(10, btnModifier, btnSupprimer);

            {
                btnModifier.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;");
                btnSupprimer.setStyle("-fx-background-color: #f44336; -fx-text-fill: white;");

                btnModifier.setOnAction(e -> {
                    Maintenance selected = getTableView().getItems().get(getIndex());
                    setMaintenanceToEdit(selected);
                });

                btnSupprimer.setOnAction(e -> {
                    Maintenance selected = getTableView().getItems().get(getIndex());
                    try {
                        maintenanceService.delete(selected);
                        maintenanceList.remove(selected);
                    } catch (Exception ex) {
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : pane);
            }
        });
    }

    public void setMaintenanceToEdit(Maintenance maintenance) {
        this.maintenanceToEdit = maintenance;

        if (maintenance != null) {
            dateMaintenancePicker.setValue(maintenance.getDateMaintenance());
            descriptionField.setText(maintenance.getDescription());
            coutMaintenanceField.setText(String.valueOf(maintenance.getCoutMaintenance()));
            machineComboBox.setValue(maintenance.getMachine());
        }
    }

    private void clearForm() {
        dateMaintenancePicker.setValue(null);
        descriptionField.clear();
        coutMaintenanceField.clear();
        machineComboBox.setValue(null);
        errorLabel.setText("");
        maintenanceToEdit = null;
    }

    private void showAlert(Alert.AlertType type, String message) {
        Alert alert = new Alert(type);
        alert.setTitle("Gestion des Maintenances");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
