package Services;

import Utils.MyDb;
import Models.Plante;
import Models.Zone;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ZoneService implements IPlanteZone<Zone> {
    private Connection con;

    public ZoneService() {
        this.con = MyDb.getInstance().getConnection();
    }

    @Override
    public void insert(Zone obj) throws SQLException {
        String sql = "INSERT INTO zone (superficie, nom_zone, plante_id) VALUES ('" + obj.getSuperficie() + "', '" + obj.getNom_zone() + "', '" + obj.getPlante_id() + "')";
        Statement stmt = this.con.createStatement();
        stmt.executeUpdate(sql);
    }

    @Override
    public void update(Zone obj) throws SQLException {
        String sql = "UPDATE zone SET superficie = '" + obj.getSuperficie() + "', nom_zone = '" + obj.getNom_zone() + "', plante_id = '" + obj.getPlante_id() + "' WHERE id = '" + obj.getId() + "'";
        Statement stmt = this.con.createStatement();
        stmt.executeUpdate(sql);
    }

    @Override
    public void delete(Zone obj) throws SQLException {
        String sql = "DELETE FROM zone WHERE id = '" + obj.getId() + "'";
        Statement stmt = this.con.createStatement();
        stmt.executeUpdate(sql);
    }

    @Override
    public List<Zone> findAll() throws SQLException {
        String sql = "SELECT * FROM zone";
        Statement stmt = this.con.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        List<Zone> list = new ArrayList<>();
        while (rs.next()) {
            Zone zone = new Zone();
            zone.setId(rs.getInt("id"));
            zone.setSuperficie(rs.getInt("superficie"));
            zone.setNom_zone(rs.getString("nom_zone"));
            zone.setPlante_id(rs.getInt("plante_id"));
            list.add(zone);
        }
        return list;
    }

    public void deleteByPlante(Plante plante) throws SQLException {
        String sql = "DELETE FROM zone WHERE plante_id = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, plante.getId());
        ps.executeUpdate();
    }

    public boolean nomZoneExiste(String nomZone) throws SQLException {
        String query = "SELECT COUNT(*) FROM zone WHERE nom_zone = ?";
        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, nomZone);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }
        return false;
    }




}
