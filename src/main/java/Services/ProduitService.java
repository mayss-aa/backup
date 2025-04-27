package Services;

import Models.Produit;
import Utils.MyDb;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ProduitService {

    private final Connection con;

    public ProduitService() {
        this.con = MyDb.getInstance().getConnection();
    }

    public void insert(Produit p) throws SQLException {
        String sql = "INSERT INTO produit (nom_produit, cycle_culture, quantite_produit, quantite_vendue, unite_quant_prod, date_semis_prod, date_recolte_prod, cree_le_prod, mis_a_jour_le_prod, categorie_nom, fournisseur_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, p.getNomProduit());
            pstmt.setString(2, p.getCycleCulture());
            pstmt.setInt(3, p.getQuantiteProduit());
            pstmt.setInt(4, p.getQuantiteVendue());
            pstmt.setString(5, p.getUniteQuantProd());
            pstmt.setDate(6, Date.valueOf(p.getDateSemisProd()));
            pstmt.setDate(7, Date.valueOf(p.getDateRecolteProd()));
            pstmt.setDate(8, Date.valueOf(p.getCreeLeProd()));
            pstmt.setDate(9, Date.valueOf(p.getMisAJourLeProd()));
            pstmt.setString(10, p.getCategorie()); // id or nom based on your structure
            pstmt.setInt(11, p.getFournisseur());
            pstmt.executeUpdate();
        }
    }

    public List<Produit> findAll() throws SQLException {
        List<Produit> produits = new ArrayList<>();
        String sql = "SELECT * FROM produit";

        try (Statement stmt = con.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Produit p = new Produit(
                        rs.getInt("id"),
                        rs.getString("nom_produit"),
                        rs.getString("cycle_culture"),
                        rs.getInt("quantite_produit"),
                        rs.getInt("quantite_vendue"),
                        rs.getString("unite_quant_prod"),
                        rs.getDate("date_semis_prod").toLocalDate(),
                        rs.getDate("date_recolte_prod").toLocalDate(),
                        rs.getDate("cree_le_prod").toLocalDate(),
                        rs.getDate("mis_a_jour_le_prod").toLocalDate(),
                        rs.getString("categorie_nom"),
                        rs.getInt("fournisseur_id")
                );
                produits.add(p);
            }
        }
        return produits;
    }

    public void update(Produit p) throws SQLException {
        String sql = "UPDATE produit SET cycle_culture=?, quantite_produit=?, quantite_vendue=?, unite_quant_prod=?, date_semis_prod=?, date_recolte_prod=?, mis_a_jour_le_prod=?, categorie_nom=?, fournisseur_id=? WHERE id=?";
        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, p.getCycleCulture());
            pstmt.setInt(2, p.getQuantiteProduit());
            pstmt.setInt(3, p.getQuantiteVendue());
            pstmt.setString(4, p.getUniteQuantProd());
            pstmt.setDate(5, Date.valueOf(p.getDateSemisProd()));
            pstmt.setDate(6, Date.valueOf(p.getDateRecolteProd()));
            pstmt.setDate(7, Date.valueOf(p.getMisAJourLeProd()));
            pstmt.setString(8, p.getCategorie());
            pstmt.setInt(9, p.getFournisseur());
            pstmt.setInt(10, p.getId());
            pstmt.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM produit WHERE id=?";
        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }
}