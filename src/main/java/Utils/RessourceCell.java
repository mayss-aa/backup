package Utils;

import Models.Ressource;
import javafx.scene.control.ListCell;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class RessourceCell extends ListCell<Ressource> {
    private final VBox content = new VBox();
    private final Text nom = new Text();
    private final Text typeQuantite = new Text();
    private final Text dates = new Text();
    private final Text statut = new Text();

    public RessourceCell() {
        content.getChildren().addAll(nom, typeQuantite, dates, statut);
        content.setSpacing(5);
        content.setStyle("-fx-padding: 10px;");
    }

    @Override
    protected void updateItem(Ressource r, boolean empty) {
        super.updateItem(r, empty);

        if (empty || r == null) {
            setText(null);
            setGraphic(null);
        } else {
            nom.setText("Nom : " + r.getNom_ressource());
            typeQuantite.setText("Type : " + r.getType_ressource() + " | Quantité : " + r.getQuantite_ressource() + " " + r.getUnite_mesure());
            dates.setText("Ajout : " + r.getDate_ajout_ressource() + " | Expire : " + r.getDate_expiration_ressource());
            statut.setText("Statut : " + r.getStatut_ressource());

            setGraphic(content);

            // 🎨 Applique un style si sélectionné
            if (isSelected()) {
                content.setStyle("-fx-background-color: #a5d6a7; -fx-padding: 10px; -fx-border-color: #388e3c;");
            } else {
                content.setStyle("-fx-background-color: white; -fx-padding: 10px; -fx-border-color: #cccccc;");
            }
        }
    }
}
