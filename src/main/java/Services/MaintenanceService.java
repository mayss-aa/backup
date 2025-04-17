package Services;

import Models.Machine;
import Models.Maintenance;
import Utils.MyDb;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MaintenanceService {

    private Connection con;

    public MaintenanceService() {
        this.con = MyDb.getInstance().getConnection();
    }

    // Méthode pour insérer une nouvelle maintenance
    public void insert(Maintenance maintenance) throws SQLException {
        String sql = "INSERT INTO maintenance (machine_id, date_maintenance, description, cout_maintenance) VALUES (?, ?, ?, ?)";
        PreparedStatement stmt = this.con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        stmt.setInt(1, maintenance.getMachine().getId());
        stmt.setDate(2, Date.valueOf(maintenance.getDateMaintenance()));
        stmt.setString(3, maintenance.getDescription());
        stmt.setBigDecimal(4, BigDecimal.valueOf(maintenance.getCoutMaintenance()));


        int affectedRows = stmt.executeUpdate();

        if (affectedRows > 0) {
            ResultSet generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                int generatedId = generatedKeys.getInt(1);
                maintenance.setId(generatedId);  // Mettre à jour l'objet Maintenance avec l'ID généré
            }
        }
    }
    // Méthode pour récupérer toutes les maintenances
    public List<Maintenance> findAll() throws SQLException {
        String sql = "SELECT * FROM maintenance";
        Statement stmt = this.con.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        List<Maintenance> maintenances = new ArrayList<>();

        while (rs.next()) {
            // Récupération de l'ID de la machine
            int machineId = rs.getInt("machine_id");

            // Récupérer l'objet Machine correspondant à l'ID
            Machine machine = getMachineById(machineId); // Cette méthode doit être définie pour récupérer une Machine par son ID

            // Créer un objet Maintenance avec un BigDecimal pour le coût
            Maintenance maintenance = new Maintenance(
                    rs.getInt("id"),
                    rs.getDate("date_maintenance").toLocalDate(),
                    rs.getString("description"),
                    rs.getBigDecimal("cout_maintenance").doubleValue(), // Utiliser BigDecimal pour la précision
                    machine
            );
            maintenances.add(maintenance);
        }
        return maintenances;
    }

    // Méthode pour récupérer une Machine par son ID (à adapter selon votre logique de récupération des machines)
    private Machine getMachineById(int machineId) throws SQLException {
        String sql = "SELECT * FROM machine WHERE id = ?";
        PreparedStatement stmt = this.con.prepareStatement(sql);
        stmt.setInt(1, machineId);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            return new Machine(
                    rs.getInt("id"),
                    rs.getString("nom_machine"),
                    rs.getString("etat_machine"),
                    rs.getString("brand_machine"),
                    rs.getDate("date_achat").toLocalDate()
            );
        } else {
            throw new SQLException("Machine avec l'ID " + machineId + " non trouvée.");
        }
    }

    // Méthode pour mettre à jour une maintenance
    public void update(Maintenance maintenance) throws SQLException {
        String sql = "UPDATE maintenance SET machine_id = ?, date_maintenance = ?, description = ?, cout_maintenance = ? WHERE id = ?";
        PreparedStatement stmt = this.con.prepareStatement(sql);
        stmt.setInt(1, maintenance.getMachine().getId());
        stmt.setDate(2, Date.valueOf(maintenance.getDateMaintenance()));
        stmt.setString(3, maintenance.getDescription());
        stmt.setBigDecimal(4, BigDecimal.valueOf(maintenance.getCoutMaintenance()));
        stmt.setInt(5, maintenance.getId());
        stmt.executeUpdate();
    }

    // Méthode pour supprimer une maintenance
    public void delete(Maintenance maintenance) throws SQLException {
        String sql = "DELETE FROM maintenance WHERE id = ?";
        PreparedStatement stmt = this.con.prepareStatement(sql);
        stmt.setInt(1, maintenance.getId());
        stmt.executeUpdate();
    }
}
