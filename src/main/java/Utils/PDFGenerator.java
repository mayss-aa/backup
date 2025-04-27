package Utils;

import Models.Depot;
import Models.Ressource;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import java.awt.Color;
import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class PDFGenerator {

    public static void generateDepotPDF(Depot depot, List<Ressource> ressources, String filePath) {
        try {
            Document document = new Document(PageSize.A4);
            PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();

            // En-tête ArdhiSmart
            Paragraph header = new Paragraph("ArdhiSmart", new Font(Font.HELVETICA, 20, Font.BOLD, Color.BLUE));
            header.setAlignment(Paragraph.ALIGN_CENTER);
            document.add(header);
            document.add(new Paragraph(" "));

            // Titre principal
            Paragraph title = new Paragraph("FICHE DU DÉPÔT : " + depot.getNom_depot(), new Font(Font.HELVETICA, 16, Font.BOLD));
            title.setAlignment(Paragraph.ALIGN_CENTER);
            document.add(title);
            document.add(new Paragraph(" "));

            // Infos dépôt
            PdfPTable depotTable = new PdfPTable(2);
            depotTable.setWidthPercentage(100);
            depotTable.setSpacingBefore(10);
            depotTable.setSpacingAfter(10);

            addDepotRow(depotTable, "ID", String.valueOf(depot.getId()));
            addDepotRow(depotTable, "Nom", depot.getNom_depot());
            addDepotRow(depotTable, "Localisation", depot.getLocalisation_depot());
            addDepotRow(depotTable, "Capacité", depot.getCapacite_depot() + " " + depot.getUnite_cap_depot());
            addDepotRow(depotTable, "Type de stockage", depot.getType_stockage_depot());
            addDepotRow(depotTable, "Statut", depot.getStatut_depot());

            document.add(depotTable);

            // Titre ressources
            Paragraph resourceTitle = new Paragraph("Liste des ressources", new Font(Font.HELVETICA, 14, Font.BOLD));
            resourceTitle.setAlignment(Paragraph.ALIGN_LEFT);
            document.add(resourceTitle);
            document.add(new Paragraph(" "));

            // Vérification des ressources
            if (ressources == null || ressources.isEmpty()) {
                Paragraph noData = new Paragraph("Actuellement, aucune ressource ajoutée.", new Font(Font.HELVETICA, 12, Font.ITALIC));
                noData.setAlignment(Paragraph.ALIGN_CENTER);
                document.add(noData);
            } else {
                PdfPTable resTable = new PdfPTable(7);
                resTable.setWidthPercentage(100);
                resTable.setSpacingBefore(10);

                String[] headers = {"Nom", "Type", "Quantité", "Unité", "Date ajout", "Expiration", "Statut"};
                for (String h : headers) {
                    PdfPCell cell = new PdfPCell(new Phrase(h));
                    cell.setBackgroundColor(Color.LIGHT_GRAY);
                    cell.setPadding(5);
                    resTable.addCell(cell);
                }

                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                for (Ressource r : ressources) {
                    resTable.addCell(r.getNom_ressource());
                    resTable.addCell(r.getType_ressource());
                    resTable.addCell(String.valueOf(r.getQuantite_ressource()));
                    resTable.addCell(r.getUnite_mesure());
                    resTable.addCell(sdf.format(r.getDate_ajout_ressource()));
                    resTable.addCell(sdf.format(r.getDate_expiration_ressource()));
                    resTable.addCell(r.getStatut_ressource());
                }

                document.add(resTable);
            }

            // Pied de page
            document.add(new Paragraph(" "));
            String date = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
            Paragraph datePara = new Paragraph("Date : " + date);
            datePara.setAlignment(Paragraph.ALIGN_RIGHT);
            document.add(datePara);

            Paragraph signPara = new Paragraph("Signature de l'agriculteur : ___________________________");
            signPara.setAlignment(Paragraph.ALIGN_RIGHT);
            document.add(signPara);

            document.close();

            // Ouvrir automatiquement le fichier PDF
            try {
                if (Desktop.isDesktopSupported()) {
                    Desktop.getDesktop().open(new File(filePath));
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void addDepotRow(PdfPTable table, String key, String value) {
        PdfPCell cell1 = new PdfPCell(new Phrase(key));
        cell1.setBackgroundColor(new Color(230, 230, 250));
        cell1.setPadding(5);

        PdfPCell cell2 = new PdfPCell(new Phrase(value));
        cell2.setPadding(5);

        table.addCell(cell1);
        table.addCell(cell2);
    }
}
