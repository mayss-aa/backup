package Controllers;


import Models.Note;
import Models.Utilisateur;
import Services.NoteService;
import Services.UtilisateurService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

public class AjouterNote implements Initializable {

    @FXML
    private TextField titreField;

    @FXML
    private TextField contenuField;

    @FXML
    private ComboBox<Utilisateur> userComboBox;

    private final NoteService noteService = new NoteService();
    private final UtilisateurService userService = new UtilisateurService();

    private Note noteToEdit; // Note à modifier

    public void goToUser(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/AjouterUser.fxml"));
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            List<Utilisateur> users = userService.findAll();
            userComboBox.getItems().addAll(users);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Affichage de l'email utilisateur dans la ComboBox
        userComboBox.setCellFactory(cb -> new javafx.scene.control.ListCell<Utilisateur>() {
            @Override
            protected void updateItem(Utilisateur user, boolean empty) {
                super.updateItem(user, empty);
                setText(empty || user == null ? null : user.getEmail());
            }
        });

        userComboBox.setButtonCell(new javafx.scene.control.ListCell<Utilisateur>() {
            @Override
            protected void updateItem(Utilisateur user, boolean empty) {
                super.updateItem(user, empty);
                setText(empty || user == null ? null : user.getEmail());
            }
        });
    }

    public void setNoteToEdit(Note note) {
        this.noteToEdit = note;
        titreField.setText(note.getTitre());
        contenuField.setText(note.getContenu());

        // Pré-sélectionner l'utilisateur lié
        for (Utilisateur u : userComboBox.getItems()) {
            if (u.getId() == note.getUtilisateur_id()) {
                userComboBox.getSelectionModel().select(u);
                break;
            }
        }
    }

    @FXML
    void AfficherListe(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherNote.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) titreField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    void goToNote(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherNote.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) titreField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void Valider(ActionEvent event) {
        try {
            String titre = titreField.getText().trim();
            String contenu = contenuField.getText().trim();
            Utilisateur selectedUser = userComboBox.getSelectionModel().getSelectedItem();

            // Existing validations
            if (titre.isEmpty() || contenu.isEmpty() || selectedUser == null) {
                showAlert("Erreur", "Tous les champs doivent être remplis.");
                return;
            }

            if (!titre.matches("^[A-Za-zÀ-ÿ].*")) {
                showAlert("Erreur", "Le titre doit commencer par une lettre.");
                return;
            }

            if (titre.length() < 3 || titre.length() > 50) {
                showAlert("Erreur", "Le titre doit contenir entre 3 et 50 caractères.");
                return;
            }

            String trimmedContenu = contenu.trim();
            if (trimmedContenu.isEmpty()) {
                showAlert("Erreur", "Le contenu ne peut pas être vide.");
                return;
            }

            if (!trimmedContenu.matches("^[A-Za-zÀ-ÿ].*")) {
                showAlert("Erreur", "Le contenu doit commencer par une lettre.");
                return;
            }

            if (trimmedContenu.length() < 15 || trimmedContenu.length() > 2000) {
                showAlert("Erreur", "Le contenu doit contenir entre 20 et 2000 caractères.");
                return;
            }

            // New nonsense detection validations
            if (isGibberish(titre) || hasConsecutiveConsonants(titre, 4)) {
                showAlert("Erreur", "Le titre semble contenir du texte non-sensique.");
                return;
            }

            if (isGibberish(contenu) || hasConsecutiveConsonants(contenu, 5)) {
                showAlert("Erreur", "Le contenu semble contenir du texte non-sensique.");
                return;
            }

          /*  if (!containsMeaningfulWords(titre, 3) || !containsMeaningfulWords(titre, 3)) {
                showAlert("Erreur", "Le texte doit contenir des mots valides.");
                return;
            }*/

            // Existing uniqueness check
            if (noteToEdit == null && noteService.titreExiste(titre)) {
                showAlert("Erreur", "Ce titre existe déjà.");
                return;
            }

            // Existing create/update logic
            if (noteToEdit != null) {
                noteToEdit.setTitre(titre);
                noteToEdit.setContenu(contenu);
                noteToEdit.setUtilisateur_id(selectedUser.getId());
                noteService.update(noteToEdit);
            } else {
                Note newNote = new Note(titre, contenu, selectedUser.getId());
                noteService.insert(newNote);
            }

            // Existing redirection
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherNote.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) titreField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            showAlert("Erreur", "Erreur: " + e.getMessage());
        }
    }

    // New validation helpers (add these inside the controller class)
    private boolean isGibberish(String text) {
        // Check for repeated character patterns
        return text.matches(".*(\\w{3,})\\1.*") ||  // Repeated substring
                text.replaceAll("[^aeiouyAEIOUY]", "").length() < text.length()/4;  // Vowel ratio
    }

    private boolean hasConsecutiveConsonants(String text, int maxAllowed) {
        String consonants = "[bcdfghjklmnpqrstvwxzBCDFGHJKLMNPQRSTVWXZ]";
        return text.matches(".*" + consonants + "{" + (maxAllowed+1) + ",}.*");
    }

    private boolean containsMeaningfulWords(String text, int minWords) {
        // Basic French word list (should be expanded)
        Set<String> dictionary = Set.of("le", "la", "un", "une", "et", "ou", "mais",
                "avec", "pour", "dans", "sur", "sous", "par",
                "bonjour", "merci", "s'il", "vous", "nous");

        long validCount = Arrays.stream(text.toLowerCase().split("\\s+"))
                .filter(word -> dictionary.contains(word))
                .count();

        return validCount >= minWords;
    }

    private void showAlert(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titre);
        alert.setContentText(message);
        alert.showAndWait();
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