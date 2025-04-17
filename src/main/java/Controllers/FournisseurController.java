package Controllers;

import Models.Fournisseur;
import Services.FournisseurService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.scene.layout.VBox;
import java.sql.SQLException;

public class FournisseurController {

    @FXML private TextField tfNom, tfEmail, tfTelephone, tfAdresse;
    @FXML private ListView<Fournisseur> fournisseurListView;
    @FXML private TableColumn<Fournisseur, String> colNom, colEmail, colTelephone, colAdresse;
    @FXML private Button btnAdd, btnUpdate, btnDelete;

    private final FournisseurService fournisseurService = new FournisseurService();
    private final ObservableList<Fournisseur> fournisseurList = FXCollections.observableArrayList();
    private Fournisseur selectedFournisseur = null;

    @FXML
    public void initialize() {
        assert tfNom != null : "fx:id=\"tfNom\" not injected: check your FXML file.";
        assert tfEmail != null : "fx:id=\"tfEmail\" not injected: check your FXML file.";
        assert tfTelephone != null : "fx:id=\"tfTelephone\" not injected: check your FXML file.";
        assert tfAdresse != null : "fx:id=\"tfAdresse\" not injected: check your FXML file.";
        assert fournisseurListView != null : "fx:id=\"fournisseurListView\" not injected: check your FXML file.";

        // Chargement des fournisseurs depuis le service
        loadFournisseurs();

        // Configuration de l'affichage de la ListView
        fournisseurListView.setCellFactory(list -> new ListCell<>() {
            @Override
            protected void updateItem(Fournisseur fournisseur, boolean empty) {
                super.updateItem(fournisseur, empty);
                if (empty || fournisseur == null) {
                    setGraphic(null);
                } else {
                    VBox container = new VBox(4);
                    container.setStyle("-fx-padding: 10; -fx-background-color: #f9f9f9; -fx-background-radius: 8;");

                    Label nom = new Label("Nom : " + fournisseur.getNomFournisseur());
                    nom.setStyle("-fx-font-weight: bold; -fx-font-size: 14;");

                    Label email = new Label("Email : " + fournisseur.getEmail());
                    Label telephone = new Label("Téléphone : " + fournisseur.getTelephone());
                    Label adresse = new Label("Adresse : " + fournisseur.getAdresseFournisseur());

                    container.getChildren().addAll(nom, email, telephone, adresse);
                    setGraphic(container);
                }
            }
        });

        // Mise à jour des champs texte à la sélection d’un fournisseur
        fournisseurListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                selectedFournisseur = newVal;
                tfNom.setText(newVal.getNomFournisseur());
                tfEmail.setText(newVal.getEmail());
                tfTelephone.setText(newVal.getTelephone());
                tfAdresse.setText(newVal.getAdresseFournisseur());
            }
        });
    }


    private void loadFournisseurs() {
        fournisseurList.clear();
        try {
            fournisseurList.addAll(fournisseurService.findAll());
        } catch (SQLException e) {
            e.printStackTrace();
            showError("Erreur lors du chargement des fournisseurs: " + e.getMessage());
        }
        fournisseurListView.setItems(fournisseurList);
    }
    @FXML
    public void handleAdd() {
        if (!validateFields()) return;

        if (tfNom.getText().isEmpty()) {
            showError("Le nom du fournisseur est requis.");
            return;
        }

        try {
            Fournisseur f = new Fournisseur(tfNom.getText(), tfEmail.getText(), tfTelephone.getText(), tfAdresse.getText());
            fournisseurService.insert(f);
            loadFournisseurs();
            clearFields();
            showSuccess("Fournisseur ajouté avec succès !");

        } catch (SQLException e) {
            showError("Erreur lors de l'ajout: " + e.getMessage());
        }
    }

    @FXML
    public void handleUpdate() {
        if (!validateFields()) return;

        if (selectedFournisseur != null) {
            try {
                selectedFournisseur.setNomFournisseur(tfNom.getText());
                selectedFournisseur.setEmail(tfEmail.getText());
                selectedFournisseur.setTelephone(tfTelephone.getText());
                selectedFournisseur.setAdresseFournisseur(tfAdresse.getText());
                fournisseurService.update(selectedFournisseur);
                loadFournisseurs();
                clearFields();
                showSuccess("Fournisseur modifié  avec succès !");

            } catch (SQLException e) {
                showError("Erreur lors de la mise à jour: " + e.getMessage());
            }
        } else {
            showError("Veuillez sélectionner un fournisseur à modifier.");
        }
    }

    @FXML
    public void handleDelete() {
        if (selectedFournisseur != null) {
            try {
                fournisseurService.delete(selectedFournisseur.getId());
                loadFournisseurs();
                clearFields();
            } catch (SQLException e) {
                showError("Erreur lors de la suppression: " + e.getMessage());
            }
        } else {
            showError("Veuillez sélectionner un fournisseur à supprimer.");
        }
    }

    private void clearFields() {
        tfNom.clear();
        tfEmail.clear();
        tfTelephone.clear();
        tfAdresse.clear();
        selectedFournisseur = null;
        fournisseurListView.getSelectionModel().clearSelection();
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void produit(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/produit_back.fxml"));
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void categorie (ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/categories_back.fxml"));
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }}

    public void fournisseur(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fournisseur_back.fxml"));
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }}
    private void showSuccess(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Succès");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    private boolean validateFields() {
        String nom = tfNom.getText().trim();
        String email = tfEmail.getText().trim();
        String tel = tfTelephone.getText().trim();
        String adresse = tfAdresse.getText().trim();

        if (nom.isEmpty() || email.isEmpty() || tel.isEmpty() || adresse.isEmpty()) {
            showError("Tous les champs sont obligatoires.");
            return false;
        }

        if (nom.length() < 3) {
            showError("Le nom du fournisseur doit contenir au moins 3 caractères.");
            return false;
        }

        if (!email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            showError("Veuillez saisir une adresse email valide.");
            return false;
        }

        if (!tel.matches("^[0-9]{8,15}$")) {
            showError("Le numéro de téléphone doit contenir uniquement des chiffres (entre 8 et 15).");
            return false;
        }

        if (adresse.length() < 5) {
            showError("L'adresse doit contenir au moins 5 caractères.");
            return false;
        }

        return true;
    }

    private void navigateToView(String fxmlPath, ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void goToProduit(ActionEvent event) {
        navigateToView("/produit_back.fxml", event);
    }

    @FXML
    public void goToFournisseur(ActionEvent event) {
        navigateToView("/fournisseur_back.fxml", event);
    }

    @FXML
    public void goToCategorie(ActionEvent event) {
        navigateToView("/categories_back.fxml", event);
    }
}
