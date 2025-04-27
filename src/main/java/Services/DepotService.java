package Services;

import Models.Depot;
import Utils.MyDb;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DepotService implements IDepotRessource<Depot> {
    private Connection con;

    public DepotService() {
        this.con = MyDb.getInstance().getConnection();
    }

    @Override
    public void insert(Depot obj) throws SQLException {
        String sql = "INSERT INTO depot (nom_depot, localisation_depot, capacite_depot, unite_cap_depot, type_stockage_depot, statut_depot) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement stmt = this.con.prepareStatement(sql);
        stmt.setString(1, obj.getNom_depot());
        stmt.setString(2, obj.getLocalisation_depot());
        stmt.setInt(3, obj.getCapacite_depot());
        stmt.setString(4, obj.getUnite_cap_depot());
        stmt.setString(5, obj.getType_stockage_depot());
        stmt.setString(6, obj.getStatut_depot());
        stmt.executeUpdate();
    }

    @Override
    public void update(Depot obj) throws SQLException {
        String sql = "UPDATE depot SET nom_depot = '" + obj.getNom_depot() +
                "', localisation_depot = '" + obj.getLocalisation_depot() +
                "', capacite_depot = '" + obj.getCapacite_depot() +
                "', unite_cap_depot = '" + obj.getUnite_cap_depot() +
                "', type_stockage_depot = '" + obj.getType_stockage_depot() +
                "', statut_depot = '" + obj.getStatut_depot() +
                "' WHERE id = '" + obj.getId() + "'";
        Statement stmt = this.con.createStatement();
        stmt.executeUpdate(sql);
    }

    @Override
    public void delete(Depot obj) throws SQLException {
        String sql = "DELETE FROM depot WHERE id = '" + obj.getId() + "'";
        Statement stmt = this.con.createStatement();
        stmt.executeUpdate(sql);
    }

    @Override
    public List<Depot> findAll() throws SQLException {
        String sql = "SELECT * FROM depot";
        Statement stmt = this.con.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        List<Depot> list = new ArrayList<>();
        while (rs.next()) {
            Depot depot = new Depot();
            depot.setId(rs.getInt("id"));
            depot.setNom_depot(rs.getString("nom_depot"));
            depot.setLocalisation_depot(rs.getString("localisation_depot"));
            depot.setCapacite_depot(rs.getInt("capacite_depot"));
            depot.setUnite_cap_depot(rs.getString("unite_cap_depot"));
            depot.setType_stockage_depot(rs.getString("type_stockage_depot"));
            depot.setStatut_depot(rs.getString("statut_depot"));
            list.add(depot);
        }
        return list;
    }

    // ✅ Nouvelle méthode : vérifie si un dépôt existe déjà par son nom
    public boolean existsByName(String nomDepot) throws SQLException {
        String sql = "SELECT COUNT(*) FROM depot WHERE LOWER(nom_depot) = LOWER(?)";
        PreparedStatement stmt = con.prepareStatement(sql);
        stmt.setString(1, nomDepot.trim());
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            return rs.getInt(1) > 0;
        }
        return false;
    }
}
