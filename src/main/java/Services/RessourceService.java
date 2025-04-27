package Services;

import Models.Ressource;
import Utils.MyDb;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RessourceService implements IDepotRessource<Ressource> {
    private Connection con;

    public RessourceService() {
        this.con = MyDb.getInstance().getConnection();
    }

    @Override
    public void insert(Ressource obj) throws SQLException {
        String sql = "INSERT INTO ressource (depot_id, nom_ressource, type_ressource, quantite_ressource, unite_mesure, date_ajout_ressource, date_expiration_ressource, statut_ressource) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement stmt = this.con.prepareStatement(sql);
        stmt.setInt(1, obj.getDepot_id());
        stmt.setString(2, obj.getNom_ressource());
        stmt.setString(3, obj.getType_ressource());
        stmt.setInt(4, obj.getQuantite_ressource());
        stmt.setString(5, obj.getUnite_mesure());
        stmt.setDate(6, new Date(obj.getDate_ajout_ressource().getTime()));
        stmt.setDate(7, new Date(obj.getDate_expiration_ressource().getTime()));
        stmt.setString(8, obj.getStatut_ressource());
        stmt.executeUpdate();
    }

    @Override
    public void update(Ressource obj) throws SQLException {
        String sql = "UPDATE ressource SET depot_id = '" + obj.getDepot_id() +
                "', nom_ressource = '" + obj.getNom_ressource() +
                "', type_ressource = '" + obj.getType_ressource() +
                "', quantite_ressource = '" + obj.getQuantite_ressource() +
                "', unite_mesure = '" + obj.getUnite_mesure() +
                "', date_ajout_ressource = '" + new Date(obj.getDate_ajout_ressource().getTime()) +
                "', date_expiration_ressource = '" + new Date(obj.getDate_expiration_ressource().getTime()) +
                "', statut_ressource = '" + obj.getStatut_ressource() +
                "' WHERE id = '" + obj.getId() + "'";
        Statement stmt = this.con.createStatement();
        stmt.executeUpdate(sql);
    }

    @Override
    public void delete(Ressource obj) throws SQLException {
        String sql = "DELETE FROM ressource WHERE id = '" + obj.getId() + "'";
        Statement stmt = this.con.createStatement();
        stmt.executeUpdate(sql);
    }

    @Override
    public List<Ressource> findAll() throws SQLException {
        String sql = "SELECT * FROM ressource";
        Statement stmt = this.con.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        List<Ressource> list = new ArrayList<>();
        while (rs.next()) {
            Ressource res = new Ressource();
            res.setId(rs.getInt("id"));
            res.setDepot_id(rs.getInt("depot_id"));
            res.setNom_ressource(rs.getString("nom_ressource"));
            res.setType_ressource(rs.getString("type_ressource"));
            res.setQuantite_ressource(rs.getInt("quantite_ressource"));
            res.setUnite_mesure(rs.getString("unite_mesure"));
            res.setDate_ajout_ressource(rs.getDate("date_ajout_ressource"));
            res.setDate_expiration_ressource(rs.getDate("date_expiration_ressource"));
            res.setStatut_ressource(rs.getString("statut_ressource"));
            list.add(res);
        }
        return list;
    }

    public List<Integer> getAllDepotIds() throws SQLException {
        List<Integer> ids = new ArrayList<>();
        String sql = "SELECT id FROM depot";
        Statement stmt = this.con.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        while (rs.next()) {
            ids.add(rs.getInt("id"));
        }
        return ids;
    }

    public Map<String, Integer> getDepotNomToIdMap() throws SQLException {
        Map<String, Integer> map = new HashMap<>();
        String sql = "SELECT id, nom_depot FROM depot";
        Statement stmt = this.con.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        while (rs.next()) {
            map.put(rs.getString("nom_depot"), rs.getInt("id"));
        }
        return map;
    }

    // ✅ Étape 1 : calcul total des quantités pour un dépôt donné
    public int getTotalQuantiteByDepot(int depotId) throws SQLException {
        String sql = "SELECT SUM(quantite_ressource) AS total FROM ressource WHERE depot_id = ?";
        PreparedStatement stmt = con.prepareStatement(sql);
        stmt.setInt(1, depotId);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            return rs.getInt("total");
        }
        return 0;
    }

    // ✅ Vérifie si un dépôt est plein
    public boolean isDepotFull(int depotId, int capaciteDepot) throws SQLException {
        int total = getTotalQuantiteByDepot(depotId);
        return total >= capaciteDepot;
    }
}
