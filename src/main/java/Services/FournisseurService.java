package Services;

import Models.Fournisseur;
import Utils.MyDb;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FournisseurService {;
    private Connection connection;

    public FournisseurService() {
        this.connection = MyDb.getInstance().getConnection();
    }

    public void insert(Fournisseur fournisseur) throws SQLException {
        String query = "INSERT INTO fournisseur (nom_fournisseur, email, telephone, adresse_fournisseur) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, fournisseur.getNomFournisseur());
            statement.setString(2, fournisseur.getEmail());
            statement.setString(3, fournisseur.getTelephone());
            statement.setString(4, fournisseur.getAdresseFournisseur());
            statement.executeUpdate();
        }
    }

    public void update(Fournisseur fournisseur) throws SQLException {
        String query = "UPDATE fournisseur SET nom_fournisseur = ?, email = ?, telephone = ?, adresse_fournisseur = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, fournisseur.getNomFournisseur());
            statement.setString(2, fournisseur.getEmail());
            statement.setString(3, fournisseur.getTelephone());
            statement.setString(4, fournisseur.getAdresseFournisseur());
            statement.setInt(5, fournisseur.getId());
            statement.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String query = "DELETE FROM fournisseur WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        }
    }

    public List<Fournisseur> findAll() throws SQLException {
        List<Fournisseur> fournisseurs = new ArrayList<>();
        String query = "SELECT * FROM fournisseur";
        try (Statement statement = connection.createStatement(); ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                Fournisseur fournisseur = new Fournisseur(
                        resultSet.getInt("id"),
                        resultSet.getString("nom_fournisseur"),
                        resultSet.getString("email"),
                        resultSet.getString("telephone"),
                        resultSet.getString("adresse_fournisseur")
                );
                fournisseurs.add(fournisseur);
            }
        }
        return fournisseurs;
    }
}