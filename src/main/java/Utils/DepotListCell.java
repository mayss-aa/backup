package Utils;

import Models.Depot;
import javafx.geometry.Insets;
import javafx.scene.control.ListCell;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class DepotListCell extends ListCell<Depot> {

    public DepotListCell() {
        // Plus besoin de contrôleur ici
    }

    @Override
    protected void updateItem(Depot depot, boolean empty) {
        super.updateItem(depot, empty);

        if (empty || depot == null) {
            setText(null);
            setGraphic(null);
        } else {
            Text nom = new Text("Nom : " + depot.getNom_depot());
            Text loc = new Text("Localisation : " + depot.getLocalisation_depot());
            Text cap = new Text("Capacité : " + depot.getCapacite_depot() + " " + depot.getUnite_cap_depot());
            Text type = new Text("Type : " + depot.getType_stockage_depot());
            Text statut = new Text("Statut : " + depot.getStatut_depot());

            nom.setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");

            VBox infoBox = new VBox(5, nom, loc, cap, type, statut);
            infoBox.setPadding(new Insets(10));
            infoBox.setStyle("-fx-background-color: white; -fx-border-color: #dcedc8; -fx-border-radius: 6px; "
                    + "-fx-background-radius: 6px; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 4, 0.2, 0, 1);");

            setGraphic(infoBox);
        }
    }
}
