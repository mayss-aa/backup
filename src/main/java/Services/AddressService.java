package Services;

import Models.Address;
import Models.Utilisateur;
import Utils.MyDb;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AddressService {
    private Connection connection;

    public AddressService() {
        this.connection = MyDb.getInstance().getConnection();
    }

    public void addaddres(Address adress) throws SQLException {
        String sql = "INSERT INTO address (user_id, address_line, city, state, postal_code, country) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ste = connection.prepareStatement(sql)) {
            ste.setInt(1, adress.getUser_id());
            ste.setString(2, adress.getAddress_line());
            ste.setString(3, adress.getCity());
            ste.setString(4, adress.getState());
            ste.setString(5, adress.getPostal_code()); // Correction ici
            ste.setString(6, adress.getCountry());

            ste.executeUpdate();
            System.out.println("Adresse ajoutée");
        }
    }

    public static Address getAddressByUserId(Utilisateur user) throws SQLException {
        String sql = "SELECT * FROM address WHERE user_id = ?";
        Connection cnx = MyDb.getInstance().getConnection();

        try (PreparedStatement ste = cnx.prepareStatement(sql)) {
            ste.setInt(1, user.getId());

            try (ResultSet rs = ste.executeQuery()) {
                if (rs.next()) {
                    return new Address(
                            rs.getInt("address_id"),
                            rs.getInt("user_id"),
                            rs.getString("address_line"),
                            rs.getString("city"),
                            rs.getString("state"),
                            rs.getString("postal_code"),
                            rs.getString("country"),
                            rs.getDate("created_at").toLocalDate()
                    );
                }
            }
        }
        return null;
    }

    public void deleteAddress(int addressId) throws SQLException { // Changé de Address à int
        String sql = "DELETE FROM address WHERE address_id = ?";
        try (PreparedStatement ste = connection.prepareStatement(sql)) {
            ste.setInt(1, addressId); // Utilisation directe de l'ID
            ste.executeUpdate();
        }
    }

    public void updateAddress(Address address) throws SQLException {
        String query = "UPDATE address SET " +
                "address_line = ?, city = ?, state = ?, postal_code = ?, country = ? " +
                "WHERE address_id = ?";

        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setString(1, address.getAddress_line());
            pst.setString(2, address.getCity());
            pst.setString(3, address.getState());
            pst.setString(4, address.getPostal_code());
            pst.setString(5, address.getCountry());
            pst.setInt(6, address.getAddress_id());
            pst.executeUpdate();
        }
    }

    private boolean isValidAddress(Address address) {
        if (address.getPostal_code() == null || !address.getPostal_code().matches("\\d{4,}")) {
            System.out.println("Code postal invalide");
            return false;
        }
        return true;
    }

    public List<Address> getAllAddresses() throws SQLException {
        List<Address> addresses = new ArrayList<>();
        String sql = "SELECT * FROM address";

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Address address = new Address(
                        rs.getInt("address_id"),
                        rs.getInt("user_id"),
                        rs.getString("address_line"),
                        rs.getString("city"),
                        rs.getString("state"),
                        rs.getString("postal_code"),
                        rs.getString("country"),
                        rs.getDate("created_at") != null ? rs.getDate("created_at").toLocalDate() : null
                );
                addresses.add(address);
            }
        }
        return addresses;
    }

}