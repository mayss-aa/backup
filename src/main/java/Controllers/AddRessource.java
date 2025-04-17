package Controllers;

import Models.Ressource;
import Services.RessourceService;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class AddRessource {

    /* ---------- champs FXML ---------- */
    @FXML private TextField            nomField;
    @FXML private ComboBox<String>     typeCombo;
    @FXML private TextField            quantiteField;
    @FXML private ComboBox<String>     uniteCombo;
    @FXML private DatePicker           dateAjoutPicker;
    @FXML private DatePicker           dateExpirationPicker;
    @FXML private ComboBox<String>     statutCombo;
    @FXML private ComboBox<Integer>    depotCombo;

    private final RessourceService ressourceService = new RessourceService();

    /* ---------- initialisation ---------- */
    @FXML
    private void initialize() {
        typeCombo.getItems().addAll("Semence","Engrais","Pesticide","Eau","Autre");
        uniteCombo.getItems().addAll("Kg","Litre","Unité","Tonnes","m³");
        statutCombo.getItems().addAll("Disponible","En rupture","Périmé");

        try {
            depotCombo.getItems().addAll(ressourceService.getAllDepotIds());
        } catch (SQLException e) {
            showAlert(AlertType.ERROR,"Erreur",
                    "Impossible de charger les dépôts : "+e.getMessage());
        }
    }

    /* ---------- CRUD ---------- */
    @FXML
    private void handleSave() {
        try {
            String nom  = nomField.getText().trim();
            String type = typeCombo.getValue();
            String qStr = quantiteField.getText().trim();
            String unit = uniteCombo.getValue();
            LocalDate dAj = dateAjoutPicker.getValue();
            LocalDate dEx = dateExpirationPicker.getValue();
            String stat = statutCombo.getValue();
            Integer dep = depotCombo.getValue();

            if (nom.isEmpty()||type==null||qStr.isEmpty()||unit==null||
                    dAj==null||dEx==null||stat==null||dep==null) {
                showAlert(AlertType.WARNING,"Champs manquants",
                        "Veuillez remplir tous les champs.");
                return;
            }

            int qte;
            try {
                qte = Integer.parseInt(qStr);
                if (qte<0) {
                    showAlert(AlertType.WARNING,"Quantité invalide",
                            "La quantité ne peut pas être négative.");
                    return;
                }
            } catch (NumberFormatException ex) {
                showAlert(AlertType.WARNING,"Erreur de format",
                        "Veuillez entrer une quantité valide.");
                return;
            }

            if (!dEx.isAfter(dAj)) {
                showAlert(AlertType.WARNING,"Dates invalides",
                        "La date d'expiration doit être postérieure à la date d'ajout.");
                return;
            }

            Date dateAjout = Date.from(dAj.atStartOfDay(ZoneId.systemDefault()).toInstant());
            Date dateExpir = Date.from(dEx.atStartOfDay(ZoneId.systemDefault()).toInstant());

            Ressource r = new Ressource(dep,nom,type,qte,unit,dateAjout,dateExpir,stat);
            ressourceService.insert(r);

            showAlert(AlertType.INFORMATION,"Succès","Ressource ajoutée !");
            loadScene("/ListRessource.fxml");

        } catch (SQLException ex) {
            showAlert(AlertType.ERROR,"Erreur",
                    "Erreur lors de l'ajout : "+ex.getMessage());
        }
    }

    @FXML
    private void handleRetour() {
        loadScene("/ListRessource.fxml");
    }

    /* ---------- navigation ---------- */
    @FXML
    private void goToDepot()     { loadScene("/AddDepot.fxml"); }
    @FXML
    private void goToRessource() { /* déjà ici */ }

    /* ---------- utilitaires ---------- */
    private void loadScene(String fxmlPath) {
        Platform.runLater(() -> {
            try {
                Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
                nomField.getScene().setRoot(root);     // remplace TOUT le root
            } catch (IOException ex) {
                showAlert(AlertType.ERROR,"Erreur",
                        "Impossible de charger la vue : "+ex.getMessage());
            }
        });
    }

    private void showAlert(AlertType type,String title,String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
