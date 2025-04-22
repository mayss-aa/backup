package Controllers;

import Models.Address;
import Models.Utilisateur;
import Services.AddressService;
import Services.UtilisateurService;
import Utils.UserSession;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;


import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

public class AdminAddressController {

    @FXML private TableView<Address> addressTable;
    @FXML private TableColumn<Address, String> userEmailCol;
    @FXML private TableColumn<Address, String> addressLineCol;
    @FXML private TableColumn<Address, String> cityCol;
    @FXML private TableColumn<Address, String> stateCol;
    @FXML private TableColumn<Address, String> postalCodeCol;
    @FXML private TableColumn<Address, String> countryCol;
    @FXML private TableColumn<Address, LocalDate> createdAtCol;
    @FXML private TableColumn<Address, Void> actionCol;
    @FXML private TextField searchField;

    private final AddressService addressService = new AddressService();
    private final UtilisateurService userService = new UtilisateurService();
    private ObservableList<Address> addresses;
    private FilteredList<Address> filteredAddresses;

    @FXML
    public void initialize() {
        setupTableColumns();
        loadAddressData();
        setupSearchFilter();
    }

    private void setupTableColumns() {
        // User email column
        userEmailCol.setCellValueFactory(cellData -> {
            int userId = cellData.getValue().getUser_id();
            try {
                return new SimpleStringProperty(userService.getEmailById(userId));
            } catch (SQLException e) {
                return new SimpleStringProperty("N/A");
            }
        });

        // Address columns - FIXED PROPERTY NAMES
        addressLineCol.setCellValueFactory(new PropertyValueFactory<>("address_line")); // Lowercase after underscore
        postalCodeCol.setCellValueFactory(new PropertyValueFactory<>("postal_code"));   // Lowercase after underscore
        cityCol.setCellValueFactory(new PropertyValueFactory<>("city"));
        stateCol.setCellValueFactory(new PropertyValueFactory<>("state"));
        countryCol.setCellValueFactory(new PropertyValueFactory<>("country"));
        createdAtCol.setCellValueFactory(new PropertyValueFactory<>("created_at"));    // Lowercase after underscore

        // Date formatting
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        createdAtCol.setCellFactory(column -> new TableCell<Address, LocalDate>() {
            @Override
            protected void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                setText((empty || item == null) ? "" : item.format(formatter));
            }
        });

        setupActionColumn();
    }

    private void setupActionColumn() {
        Callback<TableColumn<Address, Void>, TableCell<Address, Void>> cellFactory = param -> new TableCell<>() {
            private final Button editBtn = new Button("Modifier");
            private final Button deleteBtn = new Button("Supprimer");

            {
                editBtn.getStyleClass().add("edit-button");
                deleteBtn.getStyleClass().add("delete-button");
                editBtn.setOnAction(event -> handleEdit(getTableView().getItems().get(getIndex())));
                deleteBtn.setOnAction(event -> handleDelete(getTableView().getItems().get(getIndex())));
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : new HBox(5, editBtn, deleteBtn));
            }
        };
        actionCol.setCellFactory(cellFactory);
    }

    private void loadAddressData() {
        try {
            List<Address> addressList = addressService.getAllAddresses();
            if (addressList != null && !addressList.isEmpty()) {
                addresses = FXCollections.observableArrayList(addressList);
                filteredAddresses = new FilteredList<>(addresses);
                addressTable.setItems(filteredAddresses);
            } else {
                showAlert("Information", "No addresses found", Alert.AlertType.INFORMATION);
            }
        } catch (SQLException e) {
            showAlert("Error", "Failed to load data: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void setupSearchFilter() {
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredAddresses.setPredicate(address -> {
                if (newValue == null || newValue.isEmpty()) return true;
                String lowerCaseFilter = newValue.toLowerCase();
                return address.getAddress_line().toLowerCase().contains(lowerCaseFilter)
                        || address.getCity().toLowerCase().contains(lowerCaseFilter)
                        || address.getPostal_code().toLowerCase().contains(lowerCaseFilter);
            });
        });
    }

    // Rest of the methods remain unchanged...
    // [Keep handleEdit, handleDelete, refreshTable, navigation methods exactly as you had them]


    private void handleEdit(Address address) {
        try {
            URL resource = getClass().getResource("/EditAddressDialog.fxml");
            if (resource == null) {
                showAlert("Erreur", "Le fichier FXML pour l'édition n'a pas été trouvé.", Alert.AlertType.ERROR);
                return;
            }
            FXMLLoader loader = new FXMLLoader(resource);
            Parent root = loader.load();

            EditAddressController controller = loader.getController();
            controller.setAddressData(address);

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.showAndWait();

            refreshTable();

        } catch (IOException e) {
            showAlert("Erreur", "Erreur de chargement de l'éditeur", Alert.AlertType.ERROR);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void handleDelete(Address address) {
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmation");
        confirmation.setHeaderText("Supprimer l'adresse");
        confirmation.setContentText("Êtes-vous sûr de vouloir supprimer cette adresse ?");

        Optional<ButtonType> result = confirmation.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                addressService.deleteAddress(address.getAddress_id());
                addresses.remove(address);
            } catch (SQLException e) {
                showAlert("Erreur", "Échec de la suppression: " + e.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }

    private void refreshTable() throws SQLException {
        addresses.clear();
        addresses.addAll(addressService.getAllAddresses());
    }

    // Navigation methods
    @FXML
    private void gozones(ActionEvent event) throws IOException {
        loadPage(event, "/adduser.fxml");
    }

    @FXML
    private void goplantes(ActionEvent event) throws IOException {
        loadPage(event, "/adduser.fxml");
    }

    private void loadPage(ActionEvent event, String fxmlPath) throws IOException {
        URL resource = getClass().getResource(fxmlPath);
        if (resource == null) {
            showAlert("Erreur", "Le fichier FXML " + fxmlPath + " n'a pas été trouvé.", Alert.AlertType.ERROR);
            return;
        }
        Parent root = FXMLLoader.load(resource);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void gouser(ActionEvent event) throws IOException {
        URL resource = getClass().getResource("/admin.fxml");
        if (resource == null) {
            showAlert("Erreur", "Le fichier FXML pour les utilisateurs n'a pas été trouvé.", Alert.AlertType.ERROR);
            return;
        }
        Parent root = FXMLLoader.load(resource);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
    }

    @FXML
    void goprofile(ActionEvent event) throws IOException {
        SceneController.loadPage("/admininformation.fxml");
    }
    @FXML
    public void goToNote(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/AfficherNote.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void goaddress(ActionEvent event) throws IOException {
        // Add your navigation or business logic here.
        // For instance, you might load another FXML:
        Parent root = FXMLLoader.load(getClass().getResource("/adminadress.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
    }
    @FXML
    private void gocommunity(ActionEvent event) throws IOException {

    }
    @FXML
    private void logout(ActionEvent event) {
        try {
            // Charger la page de login
            Parent root = FXMLLoader.load(getClass().getResource("/login.fxml"));

            // Récupérer la scène actuelle
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Changer de scène
            stage.setScene(new Scene(root));
            stage.setTitle("ArdhiSmart - Connexion");
            stage.show();

        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur de déconnexion");
            alert.setHeaderText(null);
            alert.setContentText("Impossible de charger la page de connexion : \n" + e.getMessage());
            alert.showAndWait();
        }
    }


        @FXML
        private void ajouteradress() {
            // Create custom dialog
            Dialog<Address> dialog = new Dialog<>();
            dialog.setTitle("Nouvelle Adresse");
            dialog.setHeaderText("Ajouter une nouvelle adresse");

            // Set the button types
            DialogPane dialogPane = dialog.getDialogPane();
            dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

            // Create input fields
            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new Insets(20, 150, 10, 10));

            TextField emailField = new TextField();
            emailField.setPromptText("Email utilisateur");
            TextField rueField = new TextField();
            TextField villeField = new TextField();
            TextField etatField = new TextField();
            TextField codePostalField = new TextField();
            TextField paysField = new TextField();

            grid.add(new Label("Email Utilisateur:"), 0, 0);
            grid.add(emailField, 1, 0);
            grid.add(new Label("Rue:"), 0, 1);
            grid.add(rueField, 1, 1);
            grid.add(new Label("Ville:"), 0, 2);
            grid.add(villeField, 1, 2);
            grid.add(new Label("État:"), 0, 3);
            grid.add(etatField, 1, 3);
            grid.add(new Label("Code Postal:"), 0, 4);
            grid.add(codePostalField, 1, 4);
            grid.add(new Label("Pays:"), 0, 5);
            grid.add(paysField, 1, 5);

            dialog.getDialogPane().setContent(grid);

            // Convert result to Adresse object
            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == ButtonType.OK) {
                    // Validate inputs
                    if (emailField.getText().isEmpty() ||
                            rueField.getText().isEmpty() ||
                            villeField.getText().isEmpty()) {
                        showAlert("Champs manquants", "Veuillez remplir tous les champs obligatoires");
                        return null;
                    }

                    // Get user by email
                    UtilisateurService userService = new UtilisateurService();
                    try {
                        Utilisateur user = userService.getUserByEmail(emailField.getText());
                        if (user == null) {
                            showAlert("Utilisateur introuvable", "Aucun utilisateur avec cet email");
                            return null;
                        }

                        return new Address(
                                0, // ID will be generated by database
                                user.getId(),
                                rueField.getText(),
                                villeField.getText(),
                                etatField.getText(),
                                codePostalField.getText(),
                                paysField.getText(),
                                LocalDateTime.now(),
                                null
                        );
                    } catch (SQLException e) {
                        showAlert("Erreur base de données", e.getMessage());
                        return null;
                    }
                }
                return null;
            });

            // Process result
            Optional<Address> result = dialog.showAndWait();
            result.ifPresent(address -> {
                try {
                    AddressService addressService = new AddressService();
                    addressService.addaddres(address);
                    refreshAddressTable(); // Implement this method to reload table data
                } catch (SQLException e) {
                    showAlert("Erreur d'enregistrement", "Échec de l'enregistrement : " + e.getMessage());
                }
            });
        }

        private void refreshAddressTable() throws SQLException {
            // Implement your table refresh logic here
            // Example:
            addressTable.getItems().setAll(new AddressService().getAllAddresses());
        }

        private void showAlert(String title, String message) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        }





    @FXML
    private void gomachine(ActionEvent event) {

    }

    @FXML
    private void goproduit(ActionEvent event) {
    }

    @FXML
    private void gostock(ActionEvent event) {

    }
    }



