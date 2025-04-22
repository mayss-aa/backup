package Controllers;

import Models.Note;
import Services.NoteService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

import static Services.UtilisateurService.getEmailById;

public class AfficherNote implements Initializable {

    @FXML
    private ListView<Note> listViewNotes;

    @FXML
    private TextField searchField;

    private final NoteService noteService = new NoteService();
    private ObservableList<Note> allNotes = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            List<Note> notes = noteService.findAll();
            allNotes.setAll(notes);

            FilteredList<Note> filteredNotes = new FilteredList<>(allNotes, n -> true);
            searchField.textProperty().addListener((obs, oldVal, newVal) -> {
                filteredNotes.setPredicate(n -> {
                    if (newVal == null || newVal.isEmpty()) {
                        return true;
                    }
                    String lowerCaseFilter = newVal.toLowerCase();

                    // 3. Check if title or content contains search text
                    boolean matchesTitre = n.getTitre().toLowerCase().contains(lowerCaseFilter);
                    boolean matchesContenu = n.getContenu().toLowerCase().contains(lowerCaseFilter);

                    // 4. Show note if either field matches
                    return matchesTitre || matchesContenu;

                });
            });

            SortedList<Note> sortedNotes = new SortedList<>(filteredNotes);
            listViewNotes.setItems(sortedNotes);

            listViewNotes.setCellFactory(lv -> new ListCell<Note>() {
                @Override
                protected void updateItem(Note n, boolean empty) {
                    super.updateItem(n, empty);
                    if (empty || n == null) {
                        setGraphic(null);
                        setText(null);
                    } else {
                        Label info = null;
                        try {
                            info = new Label(
                                    "Titre : " + n.getTitre() +
                                            "\nContenu : " + n.getContenu() +
                                            "\nUtilisateur Email : " + getEmailById(n.getUtilisateur_id())
                            );
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }

                        Button btnDelete = new Button("Supprimer");
                        btnDelete.setOnAction(evt -> {
                            try {
                                noteService.delete(n);
                                allNotes.remove(n);
                            } catch (SQLException ex) {
                                ex.printStackTrace();
                            }
                        });

                        Button btnEdit = new Button("Modifier");
                        btnEdit.setOnAction(evt -> {
                            try {
                                FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterNote.fxml"));
                                Parent root = loader.load();
                                AjouterNote controller = loader.getController();
                                controller.setNoteToEdit(n);
                                Stage stage = (Stage) listViewNotes.getScene().getWindow();
                                stage.setScene(new Scene(root));
                                stage.show();
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }
                        });

                        Region spacer = new Region();
                        HBox.setHgrow(spacer, Priority.ALWAYS);
                        HBox actions = new HBox(10, btnEdit, btnDelete);
                        HBox mainBox = new HBox(10, info, spacer, actions);
                        mainBox.setStyle("-fx-padding: 10px; -fx-alignment: center-left;");
                        setGraphic(mainBox);
                        setText(null);
                    }
                }
            });

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleAjouterNote() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterNote.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) listViewNotes.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
    private void goutilisateurs(ActionEvent event) {
        loadFXML("/admin.fxml", event);
    }

    @FXML
    private void goadresses(ActionEvent event) {
        loadFXML("/adminadress.fxml", event);
    }



    private void loadFXML(String fxmlPath, ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showAlert("Erreur", "Échec du chargement : " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Autres méthodes
    @FXML
    private void goprofile(ActionEvent event) {
        loadFXML("/admininformation.fxml", event);
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
    private void gomachine(ActionEvent event) {
        loadFXML("/machine_form.fxml", event);

    }

    @FXML
    private void goproduit(ActionEvent event) {
        loadFXML("/produit_back.fxml", event);
    }

    @FXML
    private void gostock(ActionEvent event) {
        loadFXML("/AddDepot.fxml", event);

    }
    @FXML
    private void gozones(ActionEvent event) {
        loadFXML("/AjouterZone.fxml", event);
    }

    @FXML
    private void goplantes(ActionEvent event) {
        loadFXML("/AjouterPlante.fxml", event);
    }

}