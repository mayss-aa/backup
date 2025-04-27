package Services;

import Models.Categorie;
import Utils.MyDb;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;



import Utils.MyDb;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;
public class CategorieService {
    private Connection con;

    public CategorieService() {
        this.con = MyDb.getInstance().getConnection();
    }

    // Create
    public void insert(Categorie categorie) throws SQLException {
        String sql = "INSERT INTO categorie (nom_categorie, description_categorie, saison_de_recolte, temperature_ideale) VALUES (?, ?, ?, ?)";

        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, categorie.getNomCategorie());
            pstmt.setString(2, categorie.getDescriptionCategorie());
            pstmt.setString(3, categorie.getSaisonDeRecolte());
            pstmt.setString(4, categorie.getTemperatureIdeale());
            pstmt.executeUpdate();
        }
    }

    // Read
    public List<Categorie> findAll() throws SQLException {
        List<Categorie> categories = new ArrayList<>();
        String sql = "SELECT * FROM categorie";

        try (Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Categorie categorie = new Categorie(
                        rs.getString("nom_categorie"),
                        rs.getString("description_categorie"),
                        rs.getString("saison_de_recolte"),
                        rs.getString("temperature_ideale")
                );
                categories.add(categorie);
            }
        }
        return categories;
    }

    // Update
    public void update(Categorie categorie) throws SQLException {
        String sql = "UPDATE categorie SET description_categorie = ?, saison_de_recolte = ?, temperature_ideale = ? WHERE nom_categorie = ?";

        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, categorie.getDescriptionCategorie());
            pstmt.setString(2, categorie.getSaisonDeRecolte());
            pstmt.setString(3, categorie.getTemperatureIdeale());
            pstmt.setString(4, categorie.getNomCategorie());
            pstmt.executeUpdate();
        }
    }

    // Delete
    public void delete(String nomCategorie) throws SQLException {
        String sql = "DELETE FROM categorie WHERE nom_categorie = ?";

        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, nomCategorie);
            pstmt.executeUpdate();
        }
    }
}