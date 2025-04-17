package Services;

import Utils.MyDb;
import Models.Plante;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PlanteService implements IPlanteZone<Plante> {
    private Connection con;

    public PlanteService() {
        this.con = MyDb.getInstance().getConnection();
    }

    @Override
    public void insert(Plante obj) throws SQLException {
        String sql = "INSERT INTO plante (rendement_estime, nom_plante, date_plantation) VALUES ('"
                + obj.getRendement_estime() + "', '"
                + obj.getNom_plante() + "', '"
                + new Date(obj.getDate_plantation().getTime()) + "')";
        Statement stmt = con.createStatement();
        stmt.executeUpdate(sql);
    }

    @Override
    public void update(Plante obj) throws SQLException {
        String sql = "UPDATE plante SET rendement_estime = '"
                + obj.getRendement_estime() + "', nom_plante = '"
                + obj.getNom_plante() + "', date_plantation = '"
                + new Date(obj.getDate_plantation().getTime()) + "' WHERE id = '"
                + obj.getId() + "'";
        Statement stmt = con.createStatement();
        stmt.executeUpdate(sql);
    }

    @Override
    public void delete(Plante obj) throws SQLException {
        String sql = "DELETE FROM plante WHERE id = '" + obj.getId() + "'";
        Statement stmt = con.createStatement();
        stmt.executeUpdate(sql);
    }

    @Override
    public List<Plante> findAll() throws SQLException {
        String sql = "SELECT * FROM plante";
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        List<Plante> list = new ArrayList<>();
        while (rs.next()) {
            Plante plante = new Plante();
            plante.setId(rs.getInt("id"));
            plante.setRendement_estime(rs.getInt("rendement_estime"));
            plante.setNom_plante(rs.getString("nom_plante"));
            plante.setDate_plantation(rs.getDate("date_plantation"));
            list.add(plante);
        }
        return list;
    }

    public List<Plante> readAll() throws SQLException {
        List<Plante> plantes = new ArrayList<>();
        String req = "SELECT * FROM plante";
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(req);

        while (rs.next()) {
            Plante p = new Plante(
                    rs.getInt("rendement_estime"),
                    rs.getString("nom_plante"),
                    rs.getDate("date_plantation")
            );
            p.setId(rs.getInt("id")); // <<< CECI EST IMPORTANT
            plantes.add(p);
        }
        return plantes;
    }

}
