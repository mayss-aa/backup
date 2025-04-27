package Controllers;

import Models.Categorie;
import Services.CategorieService;
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
import javafx.scene.layout.VBox;
import java.sql.SQLException;

public class CategorieController {

    @FXML private StackPane formPane;
    @FXML private ListView<Categorie> categorieListView;

    @FXML private TextField tfNom, tfDescription, tfSaison, tfTemperature;

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
        assert categorieListView != null : "fx:id=\"categorieListView\" not injected: check your FXML file.";

        loadCategories();

        categorieListView.setItems(categoriesList);
        categorieListView.setCellFactory(list -> new ListCell<>() {
            @Override
            protected void updateItem(Categorie categorie, boolean empty) {
                super.updateItem(categorie, empty);
                if (empty || categorie == null) {
                    setGraphic(null);
                } else {
                    VBox container = new VBox(4);
                    container.setStyle("-fx-padding: 10; -fx-background-color: #f5f5f5; -fx-background-radius: 10;");

                    Label nom = new Label("Nom : " + categorie.getNomCategorie());
                    nom.setStyle("-fx-font-weight: bold; -fx-font-size: 14;");

                    Label description = new Label("Description : " + categorie.getDescriptionCategorie());
                    Label saison = new Label("Saison : " + categorie.getSaisonDeRecolte());
                    Label temperature = new Label("Température Idéale : " + categorie.getTemperatureIdeale());

                    container.getChildren().addAll(nom, description, saison, temperature);
                    setGraphic(container);
                }
            }
        });

        categorieListView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
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
            e.printStackTrace();
            showError("Erreur lors du chargement des catégories: " + e.getMessage());
        }

        // Lié à la ListView maintenant :
        categorieListView.setItems(categoriesList);
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
        categorieListView.getSelectionModel().clearSelection();
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
            Parent root = FXMLLoader.load(getClass().getResource("/produit_back.fxml"));
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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
    @FXML
    private void goToProduit(ActionEvent event) {
        try {
            Parent produitView = FXMLLoader.load(getClass().getResource("/produit_back.fxml"));
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(produitView));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void goToFournisseur(ActionEvent event) {
        try {
            Parent fournisseurView = FXMLLoader.load(getClass().getResource("/fournisseur_back.fxml"));
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(fournisseurView));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void goToCategorie(ActionEvent event) {
        try {
            Parent categorieView = FXMLLoader.load(getClass().getResource("/categories_back.fxml"));
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(categorieView));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
