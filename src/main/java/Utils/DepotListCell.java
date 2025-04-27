package Utils;

import Models.Depot;
import Models.Ressource;
import Services.RessourceService;
import com.google.zxing.*;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.sql.SQLException;
import java.util.List;

public class DepotListCell extends ListCell<Depot> {

    private final RessourceService ressourceService = new RessourceService();

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

            // QR Code & PDF
            Button qrButton = new Button("Afficher QR Code");
            qrButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-padding: 5 10 5 10;");
            qrButton.setOnAction(event -> {
                try {
                    List<Ressource> ressources = ressourceService.findAll().stream()
                            .filter(r -> r.getDepot_id() == depot.getId())
                            .toList();

                    StringBuilder content = new StringBuilder();
                    content.append("[ Ressources du dépôt : ").append(depot.getNom_depot()).append(" ]\n");
                    content.append("Total : ").append(ressources.size()).append(" ressource(s)\n\n");

                    if (ressources.isEmpty()) {
                        content.append("Actuellement, aucune ressource ajoutée.");
                    } else {
                        for (Ressource r : ressources) {
                            content.append("- ").append(r.getNom_ressource()).append(" : ")
                                    .append(r.getQuantite_ressource()).append(" ").append(r.getUnite_mesure()).append("\n");
                        }
                    }

                    BitMatrix matrix = new MultiFormatWriter().encode(content.toString(), BarcodeFormat.QR_CODE, 300, 300);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    MatrixToImageWriter.writeToStream(matrix, "PNG", stream);
                    ByteArrayInputStream inputStream = new ByteArrayInputStream(stream.toByteArray());
                    Image qrImage = new Image(inputStream);

                    ImageView qrView = new ImageView(qrImage);
                    qrView.setFitWidth(300);
                    qrView.setFitHeight(300);
                    qrView.setPreserveRatio(true);

                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("QR Code");
                    alert.setHeaderText("Ressources du dépôt : " + depot.getNom_depot());
                    alert.getDialogPane().setContent(qrView);
                    alert.showAndWait();

                } catch (Exception e) {
                    e.printStackTrace();
                    Alert error = new Alert(Alert.AlertType.ERROR);
                    error.setTitle("Erreur QR");
                    error.setContentText("Impossible de générer le QR Code.");
                    error.showAndWait();
                }
            });

            Button pdfButton = new Button("Exporter en PDF");
            pdfButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-padding: 5 10 5 10;");
            pdfButton.setOnAction(event -> {
                try {
                    List<Ressource> ressources = ressourceService.findAll().stream()
                            .filter(r -> r.getDepot_id() == depot.getId())
                            .toList();

                    String fileName = "Fiche_Depot_" + depot.getNom_depot().replaceAll("\\s+", "_") + ".pdf";
                    String filePath = System.getProperty("user.home") + File.separator + fileName;

                    PDFGenerator.generateDepotPDF(depot, ressources, filePath);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            HBox buttonBox = new HBox(10, qrButton, pdfButton);
            VBox infoBox = new VBox(5, nom, loc, cap, type, statut, buttonBox);

            // 🔍 Détection si plein
            try {
                int totalRessources = ressourceService.getTotalQuantiteByDepot(depot.getId());
                double capM3 = convertToM3(depot.getUnite_cap_depot(), depot.getCapacite_depot());
                double totalM3 = convertToM3(depot.getUnite_cap_depot(), totalRessources); // ✅ Correction ici

                if (capM3 > 0 && totalM3 >= capM3) {
                    infoBox.setStyle("-fx-background-color: #fff0f0; -fx-border-color: red; -fx-border-width: 2; -fx-border-radius: 6;");

                    Label alerte = new Label("⚠ Dépôt plein");
                    alerte.setStyle("-fx-text-fill: red; -fx-font-weight: bold; -fx-font-size: 14px;");

                    Timeline blink = new Timeline(
                            new KeyFrame(Duration.seconds(0), e -> alerte.setOpacity(1)),
                            new KeyFrame(Duration.seconds(0.5), e -> alerte.setOpacity(0)),
                            new KeyFrame(Duration.seconds(1), e -> alerte.setOpacity(1))
                    );
                    blink.setCycleCount(Animation.INDEFINITE);
                    blink.play();

                    infoBox.getChildren().add(alerte);
                } else {
                    infoBox.setStyle("-fx-background-color: white; -fx-border-color: #dcedc8; -fx-border-radius: 6px; " +
                            "-fx-background-radius: 6px; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 4, 0.2, 0, 1);");
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }

            infoBox.setPadding(new Insets(10));
            setGraphic(infoBox);
        }
    }

    private double convertToM3(String unite, int quantite) {
        return switch (unite.toLowerCase()) {
            case "kg", "l", "litre", "litres" -> quantite / 1000.0;
            case "tonnes", "m³" -> quantite;
            default -> quantite;
        };
    }
}
