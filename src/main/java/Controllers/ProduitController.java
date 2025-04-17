package Controllers;

import Models.Categorie;
import Models.Fournisseur;
import Models.Produit;
import Services.CategorieService;
import Services.FournisseurService;
import Services.ProduitService;
import javafx.beans.property.SimpleIntegerProperty;
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
import java.time.LocalDate;

public class ProduitController {

    @FXML private TextField tfNomProduit, tfCycleCulture, tfQuantiteProd, tfQuantiteVendue, tfUnite;
    @FXML private DatePicker dpDateSemis, dpDateRecolte;
    @FXML private ComboBox<Categorie> cbCategorie;
    @FXML private ComboBox<Fournisseur> cbFournisseur;
    @FXML private ListView<Produit> produitListView;
    @FXML private TableColumn<Produit, String> colNom, colCycle, colCategorie, colFournisseur;

    private final ObservableList<Produit> produits = FXCollections.observableArrayList();
    private final ProduitService produitService = new ProduitService();
    private final CategorieService categorieService = new CategorieService();
    private final FournisseurService fournisseurService = new FournisseurService();

    private Produit selectedProduit = null;

    @FXML
    public void initialize() {
        assert tfNomProduit != null : "fx:id=\"tfNomProduit\" not injected: check your FXML file.";
        assert tfCycleCulture != null : "fx:id=\"tfCycleCulture\" not injected: check your FXML file.";
        assert tfQuantiteProd != null : "fx:id=\"tfQuantiteProd\" not injected: check your FXML file.";
        assert tfQuantiteVendue != null : "fx:id=\"tfQuantiteVendue\" not injected: check your FXML file.";
        assert tfUnite != null : "fx:id=\"tfUnite\" not injected: check your FXML file.";
        assert cbCategorie != null : "fx:id=\"cbCategorie\" not injected: check your FXML file.";
        assert cbFournisseur != null : "fx:id=\"cbFournisseur\" not injected: check your FXML file.";
        assert dpDateSemis != null : "fx:id=\"dpDateSemis\" not injected: check your FXML file.";
        assert dpDateRecolte != null : "fx:id=\"dpDateRecolte\" not injected: check your FXML file.";
        assert produitListView != null : "fx:id=\"produitListView\" not injected: check your FXML file.";

        try {
            cbCategorie.setItems(FXCollections.observableArrayList(categorieService.findAll()));
            cbFournisseur.setItems(FXCollections.observableArrayList(fournisseurService.findAll()));
            loadProduits();

            produitListView.setItems(produits);
            produitListView.setCellFactory(list -> new ListCell<>() {
                @Override
                protected void updateItem(Produit produit, boolean empty) {
                    super.updateItem(produit, empty);
                    if (empty || produit == null) {
                        setGraphic(null);
                    } else {
                        VBox container = new VBox(4);
                        container.setStyle("-fx-padding: 10; -fx-background-color: #e8f5e9; -fx-background-radius: 8;");

                        Label nom = new Label("Nom : " + produit.getNomProduit());
                        nom.setStyle("-fx-font-weight: bold; -fx-font-size: 14;");

                        Label cycle = new Label("Cycle : " + produit.getCycleCulture());
                        Label quantite = new Label("Quantité Produite : " + produit.getQuantiteProduit());
                        Label unite = new Label("Unité : " + produit.getUniteQuantProd());
                        Label dateSemis = new Label("Semis : " + produit.getDateSemisProd());
                        Label dateRecolte = new Label("Récolte : " + produit.getDateRecolteProd());

                        container.getChildren().addAll(nom, cycle, quantite, unite, dateSemis, dateRecolte);
                        setGraphic(container);
                    }
                }
            });

            produitListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
                if (newVal != null) {
                    selectedProduit = newVal;
                    tfNomProduit.setText(newVal.getNomProduit());
                    tfCycleCulture.setText(newVal.getCycleCulture());
                    tfQuantiteProd.setText(String.valueOf(newVal.getQuantiteProduit()));
                    tfQuantiteVendue.setText(String.valueOf(newVal.getQuantiteVendue()));
                    tfUnite.setText(newVal.getUniteQuantProd());
                    dpDateSemis.setValue(newVal.getDateSemisProd());
                    dpDateRecolte.setValue(newVal.getDateRecolteProd());
                    cbCategorie.getSelectionModel().select(new Categorie(newVal.getCategorie()));
                    cbFournisseur.getSelectionModel().select(new Fournisseur(newVal.getFournisseur()));
                }
            });
        } catch (SQLException e) {
            showError("Erreur d'initialisation : " + e.getMessage());
        }
    }

    private void loadProduits() throws SQLException {
        produits.clear();
        produits.addAll(produitService.findAll());
        produitListView.setItems(produits);
    }

    @FXML
    public void handleAdd() {
        if (!validateInputs()) {
            return; // Arrêter si les entrées ne sont pas valides
        }
        try {
            Produit produit = new Produit(
                    tfNomProduit.getText(),
                    tfCycleCulture.getText(),
                    Integer.parseInt(tfQuantiteProd.getText()),
                    Integer.parseInt(tfQuantiteVendue.getText()),
                    tfUnite.getText(),
                    dpDateSemis.getValue(),
                    dpDateRecolte.getValue(),
                    LocalDate.now(),
                    LocalDate.now(),
                    cbCategorie.getSelectionModel().getSelectedItem().getNomCategorie(),
                    cbFournisseur.getSelectionModel().getSelectedItem().getId()         );
            produitService.insert(produit);
            loadProduits();
            clearFields();
            showInfo("Enregistrement du produit avec succès !");

        } catch (Exception e) {
            showError("Erreur lors de l'ajout du produit: " + e.getMessage());
        }
    }

    @FXML
    public void handleUpdate() {
        if (selectedProduit == null) {
            showError("Veuillez sélectionner un produit à modifier.");
            return;
        }

        if (!validateInputs()) {
            return; // Arrêter si les entrées ne sont pas valides
        }

        try {
            selectedProduit.setNomProduit(tfNomProduit.getText());
            selectedProduit.setCycleCulture(tfCycleCulture.getText());
            selectedProduit.setQuantiteProduit(Integer.parseInt(tfQuantiteProd.getText()));
            selectedProduit.setQuantiteVendue(Integer.parseInt(tfQuantiteVendue.getText()));
            selectedProduit.setUniteQuantProd(tfUnite.getText());
            selectedProduit.setDateSemisProd(dpDateSemis.getValue());
            selectedProduit.setDateRecolteProd(dpDateRecolte.getValue());
            selectedProduit.setCategorie(cbCategorie.getSelectionModel().getSelectedItem().getNomCategorie());
            selectedProduit.setFournisseur(cbFournisseur.getSelectionModel().getSelectedItem().getId());
            selectedProduit.setMisAJourLeProd(LocalDate.now());

            produitService.update(selectedProduit);
            loadProduits();
            clearFields();
        } catch (Exception e) {
            showError("Erreur lors de la modification: " + e.getMessage());
        }
    }
    @FXML
    public void handleDelete() {
        if (selectedProduit != null) {
            try {
                produitService.delete(selectedProduit.getId());
                loadProduits();
                clearFields();
            } catch (SQLException e) {
                showError("Erreur lors de la suppression: " + e.getMessage());
            }
        } else {
            showError("Veuillez sélectionner un produit à supprimer.");
        }
    }

    private void clearFields() {
        tfNomProduit.clear();
        tfCycleCulture.clear();
        tfQuantiteProd.clear();
        tfQuantiteVendue.clear();
        tfUnite.clear();
        dpDateSemis.setValue(null);
        dpDateRecolte.setValue(null);
        cbCategorie.getSelectionModel().clearSelection();
        cbFournisseur.getSelectionModel().clearSelection();
        produitListView.getSelectionModel().clearSelection();
        selectedProduit = null;
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void fournisseur(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fournisseur_back.fxml"));
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void categorie(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/categories_back.fxml"));
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private boolean validateInputs() {
        StringBuilder errors = new StringBuilder();

        // Validation du nom du produit
        if (tfNomProduit.getText().isEmpty()) {
            errors.append("Le nom du produit est requis.\n");
        } else if (tfNomProduit.getText().length() < 3 || tfNomProduit.getText().length() > 50) {
            errors.append("Le nom du produit doit contenir entre 3 et 50 caractères.\n");
        }

        // Validation du cycle de culture
        if (tfCycleCulture.getText().isEmpty()) {
            errors.append("Le cycle de culture est requis.\n");
        } else if (tfCycleCulture.getText().length() < 3 || tfCycleCulture.getText().length() > 50) {
            errors.append("Le cycle de culture doit contenir entre 3 et 50 caractères.\n");
        }

        // Validation de la quantité produite
        try {
            int quantiteProd = Integer.parseInt(tfQuantiteProd.getText());
            if (quantiteProd <= 0) {
                errors.append("La quantité produite doit être un nombre positif.\n");
            }
        } catch (NumberFormatException e) {
            errors.append("La quantité produite doit être un nombre entier valide.\n");
        }

        // Validation de la quantité vendue
        try {
            int quantiteVendue = Integer.parseInt(tfQuantiteVendue.getText());
            if (quantiteVendue < 0) {
                errors.append("La quantité vendue ne peut pas être négative.\n");
            }
        } catch (NumberFormatException e) {
            errors.append("La quantité vendue doit être un nombre entier valide.\n");
        }

        // Validation de l'unité
        if (tfUnite.getText().isEmpty()) {
            errors.append("L'unité de mesure est requise.\n");
        } else if (tfUnite.getText().length() < 1 || tfUnite.getText().length() > 20) {
            errors.append("L'unité de mesure doit contenir entre 1 et 20 caractères.\n");
        }

        // Validation des dates
        LocalDate dateSemis = dpDateSemis.getValue();
        LocalDate dateRecolte = dpDateRecolte.getValue();
        if (dateSemis == null) {
            errors.append("La date de semis est requise.\n");
        }
        if (dateRecolte == null) {
            errors.append("La date de récolte est requise.\n");
        } else if (dateSemis != null && dateRecolte.isBefore(dateSemis)) {
            errors.append("La date de récolte doit être postérieure à la date de semis.\n");
        }

        // Validation des combobox
        if (cbCategorie.getSelectionModel().getSelectedItem() == null) {
            errors.append("Veuillez sélectionner une catégorie.\n");
        }
        if (cbFournisseur.getSelectionModel().getSelectedItem() == null) {
            errors.append("Veuillez sélectionner un fournisseur.\n");
        }

        // Afficher les erreurs si elles existent
        if (errors.length() > 0) {
            showError(errors.toString());
            return false;
        }

        return true;
    }
    private void showInfo(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Succès");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
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
}}
