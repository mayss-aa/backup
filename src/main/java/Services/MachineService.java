package Services;

import Models.Machine;
import Utils.MyDb;
import Services.MachineService;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class MachineService implements IMachine<Machine> {
    private Connection con;
    private ObservableList<Machine> machineList;

    public MachineService() {
        this.con = MyDb.getInstance().getConnection();
    }

    private void showAlert(String title, String message, AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @Override
    public void insert(Machine machine) {
        String sql = "INSERT INTO machine (nom_machine, etat_machine, brand_machine, date_achat) VALUES (?, ?, ?, ?)";

        // Paramètres de connexion à la base de données
        String url = "jdbc:mysql://localhost:3306/nom_de_ta_base"; // Remplace par ton URL
        String user = "root"; // Utilisateur MySQL
        String password = ""; // Mot de passe (vide si c'est le mot de passe par défaut)

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Préparer et exécuter la requête d'insertion
            stmt.setString(1, machine.getNomMachine());
            stmt.setString(2, machine.getEtatMachine());
            stmt.setString(3, machine.getBrandMachine());
            stmt.setDate(4, Date.valueOf(machine.getDateAchat()));

            stmt.executeUpdate(); // Exécution de la requête d'insertion
        } catch (SQLException e) {
            e.printStackTrace(); // Affiche l’erreur en cas d’échec
        }
    }


    @Override
    public void update(Machine machine) throws SQLException {
        String sql = "UPDATE machine SET nom_machine = ?, etat_machine = ?, brand_machine = ?, date_achat = ? WHERE id = ?";
        PreparedStatement stmt = this.con.prepareStatement(sql);
        stmt.setString(1, machine.getNomMachine());
        stmt.setString(2, machine.getEtatMachine());
        stmt.setString(3, machine.getBrandMachine());
        stmt.setDate(4, Date.valueOf(machine.getDateAchat()));
        stmt.setInt(5, machine.getId());
        stmt.executeUpdate();
    }

    @Override
    public void delete(Machine machine) throws SQLException {
        String sql = "DELETE FROM machine WHERE id = ?";
        PreparedStatement stmt = this.con.prepareStatement(sql);
        stmt.setInt(1, machine.getId());
        stmt.executeUpdate();
    }

    @Override
    public List<Machine> findAll() {
        List<Machine> machines = new ArrayList<>();
        String sql = "SELECT * FROM machine"; // SQL pour récupérer toutes les machines

        try (Statement stmt = con.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            // Parcours du ResultSet et création des objets Machine
            while (rs.next()) {
                Machine machine = new Machine(
                        rs.getInt("id"),
                        rs.getString("nom_machine"),
                        rs.getString("etat_machine"),
                        rs.getString("brand_machine"),
                        rs.getDate("date_achat").toLocalDate()
                );
                machines.add(machine); // Ajouter la machine à la liste
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Afficher l'exception si une erreur se produit
        }

        return machines; // Retourner la liste des machines
    }

}
