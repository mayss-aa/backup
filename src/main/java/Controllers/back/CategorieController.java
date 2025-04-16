package com.esprit.Controllers.back;

import com.esprit.Models.Categorie;
import com.esprit.Services.CategorieService;
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
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.sql.SQLException;

public class CategorieController {

    @FXML private StackPane formPane;

    @FXML private TextField tfNom, tfDescription, tfSaison, tfTemperature;
    @FXML private TableView<Categorie> categorieTable;
    @FXML private TableColumn<Categorie, String> colNom, colDescription, colSaison, colTemperature;

    @FXML private Button btnAdd, btnUpdate, btnDelete;

    private final CategorieService categorieService = new CategorieService();
    private final ObservableList<Categorie> categoriesList = FXCollections.observableArrayList();

    private Categorie selectedCategorie = null;

    @FXML
    public void initialize() {
        assert tfNom != null : "fx:id=\"tfNom\" not injected: check your FXML file.";
        assert tfDescription != null : "fx:id=\"tfDescription\" not injected: check your FXML file.";
        assert tfSaison != null : "fx:id=\"tfSaison\" not injected: check your FXML file.";
        assert tfTemperature != null : "fx:id=\"tfTemperature\" not injected: check your FXML file.";
        assert categorieTable != null : "fx:id=\"categorieTable\" not injected: check your FXML file.";

        colNom.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNomCategorie()));
        colDescription.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDescriptionCategorie()));
        colSaison.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSaisonDeRecolte()));
        colTemperature.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTemperatureIdeale()));

        loadCategories();

        categorieTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectedCategorie = newSelection;
                tfNom.setText(newSelection.getNomCategorie());
                tfDescription.setText(newSelection.getDescriptionCategorie());
                tfSaison.setText(newSelection.getSaisonDeRecolte());
                tfTemperature.setText(newSelection.getTemperatureIdeale());
            }
        });
    }

    private void loadCategories() {
        categoriesList.clear();
        try {
            categoriesList.addAll(categorieService.findAll());
        } catch (SQLException e) {
            showError("Erreur lors du chargement des catégories: " + e.getMessage());
        }
        categorieTable.setItems(categoriesList);
    }

    @FXML
    public void handleAdd() {
        if (!validateFields()) return;

        if (tfNom.getText().isEmpty()) {
            showError("Le nom de la catégorie est requis.");
            return;
        }

        try {
            Categorie categorie = new Categorie(
                    tfNom.getText(),
                    tfDescription.getText(),
                    tfSaison.getText(),
                    tfTemperature.getText()
            );
            categorieService.insert(categorie);
            loadCategories();
            handleClear();
            showSuccess("Catégorie ajoutée avec succès !");

        } catch (SQLException e) {
            showError("Erreur lors de l'ajout: " + e.getMessage());
        }
    }

    @FXML
    public void handleUpdate() {
        if (!validateFields()) return;

        if (selectedCategorie != null) {
            try {
                selectedCategorie.setDescriptionCategorie(tfDescription.getText());
                selectedCategorie.setSaisonDeRecolte(tfSaison.getText());
                selectedCategorie.setTemperatureIdeale(tfTemperature.getText());
                categorieService.update(selectedCategorie);
                loadCategories();
                handleClear();
                showSuccess("Catégorie Modifié avec succès !");

            } catch (SQLException e) {
                showError("Erreur lors de la mise à jour: " + e.getMessage());
            }
        } else {
            showError("Veuillez sélectionner une catégorie à modifier.");
        }
    }

    @FXML
    public void handleDelete() {
        if (selectedCategorie != null) {
            try {
                categorieService.delete(selectedCategorie.getNomCategorie());
                loadCategories();
                handleClear();
            } catch (SQLException e) {
                showError("Erreur lors de la suppression: " + e.getMessage());
            }
        } else {
            showError("Veuillez sélectionner une catégorie à supprimer.");
        }
    }

    @FXML
    public void handleClear() {
        tfNom.clear();
        tfDescription.clear();
        tfSaison.clear();
        tfTemperature.clear();
        selectedCategorie = null;
        categorieTable.getSelectionModel().clearSelection();
    }

    @FXML
    public void hideForm() {
        formPane.setVisible(false);
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void Produits(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/back/produit_back.fxml"));
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void fournisseur(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/back/fournisseur_back.fxml"));
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
        String description = tfDescription.getText().trim();
        String saison = tfSaison.getText().trim();
        String temperature = tfTemperature.getText().trim();

        if (nom.isEmpty() || description.isEmpty() || saison.isEmpty() || temperature.isEmpty()) {
            showError("Tous les champs sont obligatoires.");
            return false;
        }

        if (nom.length() < 3) {
            showError("Le nom doit contenir au moins 3 caractères.");
            return false;
        }

        if (description.length() < 5) {
            showError("La description doit contenir au moins 5 caractères.");
            return false;
        }

        if (saison.length() < 3) {
            showError("La saison de récolte doit être précisée correctement.");
            return false;
        }

        if (!temperature.matches("^-?\\d+(\\.\\d+)?$")) {
            showError("La température idéale doit être un nombre valide (ex: 18 ou 18.5).");
            return false;
        }

        return true;
    }


}
