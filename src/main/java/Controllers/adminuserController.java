package Controllers;

import Models.Utilisateur;
import Services.UtilisateurService;
import Utils.MyDb;
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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class adminuserController {

    // Table components
    @FXML
    private TableView<Utilisateur> usertabel;
    @FXML
    private TableColumn<Utilisateur, String> prenomCol;
    @FXML
    private TableColumn<Utilisateur, String> nomCol;
    @FXML
    private TableColumn<Utilisateur, String> numTelCol;
    @FXML
    private TableColumn<Utilisateur, String> emailCol;
    @FXML
    private TableColumn<Utilisateur, String> genreCol;
    @FXML
    private TableColumn<Utilisateur, LocalDate> dateNaissanceCol;
    @FXML
    private TableColumn<Utilisateur, String> roleCol;
    @FXML
    private TableColumn<Utilisateur, Void> actionCol;
    @FXML
    private TableColumn<Utilisateur, LocalDateTime> creeLeCol;
    @FXML
    private TableColumn<Utilisateur, LocalDateTime> misAJourLeCol;

    // Other components
    @FXML
    private TextField searchField;
    @FXML
    private ImageView headerProfileImage;
    @FXML
    private ImageView sidebarProfileImage;
    @FXML
    private Circle statusIndicator;
    @FXML
    private Label userStatusLabel;

    // Services and data
    private Connection cnx = MyDb.getInstance().getConnection();
    private ObservableList<Utilisateur> usersList = FXCollections.observableArrayList();
    private UtilisateurService service = new UtilisateurService();
    private UserSession session = UserSession.getInstance();
    private Utilisateur currentUser = session.getUser();

    @FXML
    public void initialize() throws SQLException {
        setupTableColumns();
        loadUsersFromDatabase();
        setupSearchFilter();
    }

    private String getNomRole(int roleId) {
        switch(roleId) {
            case 1: return "ROLE_ADMIN";
            case 2: return "ROLE_AGRI";
            case 3: return "Autre";
            default: return "Inconnu";
        }
    }

    private void setupTableColumns() {
        // Configure value factories for all columns
        prenomCol.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        nomCol.setCellValueFactory(new PropertyValueFactory<>("nom"));
        numTelCol.setCellValueFactory(new PropertyValueFactory<>("num_tel"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        genreCol.setCellValueFactory(new PropertyValueFactory<>("genre"));

        // Date column configuration
        dateNaissanceCol.setCellValueFactory(new PropertyValueFactory<>("date_naissance"));
        dateNaissanceCol.setCellFactory(column -> new TableCell<>() {
            private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            @Override
            protected void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setText(empty || date == null ? "" : date.format(dateFormatter));
            }
        });

        // Timestamp columns configuration
        DateTimeFormatter timestampFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        creeLeCol.setCellValueFactory(new PropertyValueFactory<>("cree_le"));
        creeLeCol.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(LocalDateTime date, boolean empty) {
                super.updateItem(date, empty);
                setText(empty || date == null ? "" : date.format(timestampFormatter));
            }
        });

        misAJourLeCol.setCellValueFactory(new PropertyValueFactory<>("mis_ajour_le"));
        misAJourLeCol.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(LocalDateTime date, boolean empty) {
                super.updateItem(date, empty);
                setText(empty || date == null ? "" : date.format(timestampFormatter));
            }
        });

        // Role column
        roleCol.setCellValueFactory(cellData ->
                new SimpleStringProperty(getNomRole(cellData.getValue().getRole_id()))
        );

        // Action column
        actionCol.setCellFactory(param -> new TableCell<>() {
            private final Button modifierButton = new Button("Modifier");
            private final Button supprimerButton = new Button("Supprimer");

            {
                modifierButton.setOnAction(event -> handleEditUser(getTableView().getItems().get(getIndex())));
                supprimerButton.setOnAction(event -> handleDeleteUser(getTableView().getItems().get(getIndex())));
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    GridPane pane = new GridPane();
                    pane.setHgap(10);
                    pane.add(modifierButton, 0, 0);
                    pane.add(supprimerButton, 1, 0);
                    setGraphic(pane);
                }
            }
        });
    }

    private void loadUsersFromDatabase() {
        usersList.clear();
        String query = "SELECT * FROM utilisateur";
        try (PreparedStatement pst = cnx.prepareStatement(query);
             ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                usersList.add(new Utilisateur(
                        rs.getInt("id"),
                        rs.getInt("role_id"),
                        rs.getString("prenom"),
                        rs.getString("nom"),
                        rs.getString("email"),
                        rs.getString("genre"),
                        rs.getDate("date_naissance").toLocalDate(),
                        rs.getString("num_tel"),
                        rs.getString("password"),
                        rs.getTimestamp("cree_le").toLocalDateTime(),
                        rs.getTimestamp("mis_ajour_le").toLocalDateTime(),
                        rs.getString("token"),
                        rs.getString("photo"),
                        rs.getString("reset_code"),
                        rs.getTimestamp("reset_code_expires_at") != null ?
                                rs.getTimestamp("reset_code_expires_at").toLocalDateTime() : null
                ));
            }
            usertabel.setItems(usersList);
        } catch (SQLException e) {
            showAlert("Erreur BDD", "Erreur de chargement : " + e.getMessage());
        }
    }

    // Rest of the methods remain unchanged...
    // [Keep all other methods exactly as you had them]

    // ... (Other methods here) ...


    private void setupSearchFilter() {
        FilteredList<Utilisateur> filteredUsers = new FilteredList<>(usersList, p -> true);
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredUsers.setPredicate(user -> {
                if (newValue == null || newValue.isEmpty()) return true;
                String lowerCaseFilter = newValue.toLowerCase();
                return user.getPrenom().toLowerCase().contains(lowerCaseFilter)
                        || user.getNom().toLowerCase().contains(lowerCaseFilter)
                        || user.getEmail().toLowerCase().contains(lowerCaseFilter)
                        || user.getNum_tel().contains(lowerCaseFilter)
                        || getNomRole(user.getRole_id()).toLowerCase().contains(lowerCaseFilter);
            });
        });
        usertabel.setItems(filteredUsers);
    }

    private void handleEditUser(Utilisateur user) {
        Dialog<Utilisateur> dialog = new Dialog<>();
        dialog.setTitle("Modifier Utilisateur");
        dialog.setHeaderText("Modification des informations");

        ButtonType saveButtonType = new ButtonType("Enregistrer", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        DatePicker datePicker = new DatePicker(user.getDate_naissance());
        datePicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(date.isAfter(LocalDate.now()));
            }
        });

        TextField prenomField = new TextField(user.getPrenom());
        TextField nomField = new TextField(user.getNom());
        TextField phoneField = new TextField(user.getNum_tel());
        TextField emailField = new TextField(user.getEmail());
        ComboBox<String> genreCombo = new ComboBox<>(FXCollections.observableArrayList("Homme", "Femme"));
        genreCombo.setValue(user.getGenre());
        ComboBox<String> roleCombo = new ComboBox<>(FXCollections.observableArrayList("ROLE_ADMIN", "ROLE_AGRI", "Autre"));
        roleCombo.setValue(getNomRole(user.getRole_id()));
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Laisser vide pour ne pas changer");

        grid.add(new Label("Prénom :"), 0, 0);
        grid.add(prenomField, 1, 0);
        grid.add(new Label("Nom :"), 0, 1);
        grid.add(nomField, 1, 1);
        grid.add(new Label("Téléphone :"), 0, 2);
        grid.add(phoneField, 1, 2);
        grid.add(new Label("Email :"), 0, 3);
        grid.add(emailField, 1, 3);
        grid.add(new Label("Genre :"), 0, 4);
        grid.add(genreCombo, 1, 4);
        grid.add(new Label("Rôle :"), 0, 5);
        grid.add(roleCombo, 1, 5);
        grid.add(new Label("Naissance :"), 0, 6);
        grid.add(datePicker, 1, 6);
        grid.add(new Label("Mot de passe :"), 0, 7);
        grid.add(passwordField, 1, 7);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                if (datePicker.getValue() == null || datePicker.getValue().isAfter(LocalDate.now())) {
                    showAlert("Date invalide", "Date de naissance incorrecte");
                    return null;
                }

                int selectedRoleId = switch(roleCombo.getValue()) {
                    case "ROLE_ADMIN" -> 1;
                    case "ROLE_AGRI" -> 2;
                    case "Autre" -> 3;
                    default -> 0;
                };

                String newPassword = passwordField.getText();
                String finalPassword = newPassword.isEmpty() ? user.getPassword() : newPassword;

                return new Utilisateur(
                        user.getId(),
                        selectedRoleId,
                        prenomField.getText(),
                        nomField.getText(),
                        emailField.getText(),
                        genreCombo.getValue(),
                        datePicker.getValue(),
                        phoneField.getText(),
                        finalPassword,
                        user.getCree_le(),
                        LocalDateTime.now(),
                        user.getToken(),
                        user.getPhoto(),
                        user.getReset_code(),
                        user.getReset_code_expires_at()
                );
            }
            return null;
        });

        Optional<Utilisateur> result = dialog.showAndWait();
        result.ifPresent(updatedUser -> {
            if (validateUser(updatedUser)) {
                try {
                    service.update(updatedUser);
                    loadUsersFromDatabase();
                } catch (SQLException e) {
                    showAlert("Erreur MAJ", "Échec de la mise à jour : " + e.getMessage());
                }
            }
        });
    }

    private boolean validateUser(Utilisateur user) {
        if (user.getPrenom().isBlank() || user.getNom().isBlank()) {
            showAlert("Champs requis", "Prénom et nom obligatoires");
            return false;
        }
        if (user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            showAlert("Email invalide", "Format email incorrect");
            return false;
        }
        return true;
    }

    private void handleDeleteUser(Utilisateur user) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Supprimer utilisateur");
        alert.setHeaderText("Êtes-vous sûr de vouloir supprimer cet utilisateur ?");
        alert.setContentText("User: " + user.getPrenom() + " " + user.getNom());

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                service.delete(user);  // Pass the whole object instead of user.getId()
                usersList.remove(user);
            } catch (SQLException e) {
                showAlert("Delete Error", "Failed to delete user: " + e.getMessage());
            }
        }
    }

    private void showAlert(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Méthodes de navigation (conservées en anglais comme dans l'original)
    @FXML
    private void goaddress(ActionEvent event) throws IOException {
        // Replace content in the same window
        Parent root = FXMLLoader.load(getClass().getResource("/adminadress.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
    }

    @FXML
    void gocommunity(ActionEvent event) throws IOException {
        SceneController.loadPage("/views/dashboard.fxml");
    }

    @FXML
    void goevent(ActionEvent event) throws IOException {
        SceneController.loadPage("/AdminAffichageEvents.fxml");
    }

    @FXML
    void goprofile(ActionEvent event) throws IOException {
        SceneController.loadPage("/admininformation.fxml");
    }

    @FXML
    void gouser(ActionEvent event) throws IOException {
        SceneController.loadPage("/admin.fxml");
    }

    @FXML
    private void logout(ActionEvent event) {
        try {
            // Charger la page de login
            Parent root = FXMLLoader.load(getClass().getResource("/login.fxml"));

            // Créer une nouvelle fenêtre
            Stage loginStage = new Stage();
            loginStage.setScene(new Scene(root));
            loginStage.setTitle("ArdhiSmart - Connexion");

            // Fermer la fenêtre actuelle
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();

            // Afficher la nouvelle fenêtre
            loginStage.show();

        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur de déconnexion");
            alert.setHeaderText(null);
            alert.setContentText("Erreur lors du chargement : \n" + e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    void gojob(ActionEvent event) throws IOException {
        SceneController.loadPage("/adminjob.fxml");
    }
    @FXML
    private void ajoutuser(ActionEvent event) {
        Dialog<Utilisateur> dialog = new Dialog<>();
        dialog.setTitle("Nouvel Utilisateur");
        dialog.setHeaderText("Création d'un nouvel utilisateur");

        ButtonType saveButtonType = new ButtonType("Créer", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        DatePicker datePicker = new DatePicker();
        datePicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(date.isAfter(LocalDate.now()));
            }
        });

        TextField prenomField = new TextField();
        TextField nomField = new TextField();
        TextField phoneField = new TextField();
        TextField emailField = new TextField();
        ComboBox<String> genreCombo = new ComboBox<>(FXCollections.observableArrayList("Homme", "Femme"));
        ComboBox<String> roleCombo = new ComboBox<>(FXCollections.observableArrayList("ROLE_ADMIN", "ROLE_AGRI", "Autre"));
        PasswordField passwordField = new PasswordField();

        // Set default values
        genreCombo.getSelectionModel().selectFirst();
        roleCombo.getSelectionModel().selectLast(); // Default to "Autre"

        grid.add(new Label("Prénom* :"), 0, 0);
        grid.add(prenomField, 1, 0);
        grid.add(new Label("Nom* :"), 0, 1);
        grid.add(nomField, 1, 1);
        grid.add(new Label("Téléphone* :"), 0, 2);
        grid.add(phoneField, 1, 2);
        grid.add(new Label("Email* :"), 0, 3);
        grid.add(emailField, 1, 3);
        grid.add(new Label("Genre* :"), 0, 4);
        grid.add(genreCombo, 1, 4);
        grid.add(new Label("Rôle* :"), 0, 5);
        grid.add(roleCombo, 1, 5);
        grid.add(new Label("Naissance* :"), 0, 6);
        grid.add(datePicker, 1, 6);
        grid.add(new Label("Mot de passe* :"), 0, 7);
        grid.add(passwordField, 1, 7);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                // Validate all required fields
                if (prenomField.getText().isEmpty() || nomField.getText().isEmpty() ||
                        phoneField.getText().isEmpty() || emailField.getText().isEmpty() ||
                        passwordField.getText().isEmpty() || datePicker.getValue() == null) {
                    showAlert("Champs manquants", "Tous les champs obligatoires (*) doivent être remplis");
                    return null;
                }

                if (!emailField.getText().contains("@")) {
                    showAlert("Email invalide", "Format email incorrect");
                    return null;
                }

                int selectedRoleId = switch(roleCombo.getValue()) {
                    case "ROLE_ADMIN" -> 1;
                    case "ROLE_AGRI" -> 2;
                    case "Autre" -> 3;
                    default -> 0;
                };

                return new Utilisateur(
                        0, // ID will be generated by database
                        selectedRoleId,
                        prenomField.getText(),
                        nomField.getText(),
                        emailField.getText(),
                        genreCombo.getValue(),
                        datePicker.getValue(),
                        phoneField.getText(),
                        passwordField.getText(), // Password is mandatory for new users
                        LocalDateTime.now(), // Creation date
                        LocalDateTime.now(), //  update date initially
                        null, // token
                        null, // photo
                        null, // reset_code
                        null  // reset_code_expires_at
                );
            }
            return null;
        });

        Optional<Utilisateur> result = dialog.showAndWait();
        result.ifPresent(newUser -> {
            try {
                // Check if email already exists
                if (service.emailExists(newUser.getEmail())) {
                    showAlert("Email existant", "Cet email est déjà utilisé par un autre utilisateur");
                    return;
                }

                service.addu(newUser);
                loadUsersFromDatabase();
                showAlert("Succès", "Utilisateur créé avec succès", Alert.AlertType.INFORMATION);
            } catch (SQLException e) {
                showAlert("Erreur BDD", "Échec de la création : " + e.getMessage());
            }
        });
    }

    // Add this overloaded showAlert method for success messages
    private void showAlert(String titre, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(titre);
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