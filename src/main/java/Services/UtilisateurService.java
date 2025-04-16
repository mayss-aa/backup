package Services;

import Models.Utilisateur;
import Utils.MyDb;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class UtilisateurService implements IUser<Utilisateur> {
    private final Connection connection;

    public UtilisateurService() {
        this.connection = MyDb.getInstance().getConnection();
    }

    @Override

    public void insert(Utilisateur user) throws SQLException {
        String sql = "INSERT INTO utilisateur (role_id, prenom, nom, email, genre, date_naissance, num_tel, password, "
                + "cree_le, mis_ajour_le, token, photo, reset_code, reset_code_expires_at) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            // Set parameters
            setCommonParameters(ps, user);
            ps.setTimestamp(9, Timestamp.valueOf(LocalDateTime.now()));
            ps.setTimestamp(10, Timestamp.valueOf(LocalDateTime.now()));
            setNullableParameters(ps, user);

            // Execute insert
            ps.executeUpdate();

            // Retrieve generated ID
            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    user.setId(generatedKeys.getInt(1)); // Set the ID on the user object
                } else {
                    throw new SQLException("Failed to get generated user ID");
                }
            }
        }
    }
    public int getLastInsertId() throws SQLException {
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT LAST_INSERT_ID()")) {
            if(rs.next()) {
                return rs.getInt(1);
            }
        }
        throw new SQLException("Failed to get last insert ID");
    }



    @Override

    public void update(Utilisateur user) throws SQLException {
        String sql = "UPDATE utilisateur SET "
                + "role_id = ?, "
                + "prenom = ?, "
                + "nom = ?, "
                + "email = ?, "
                + "genre = ?, "
                + "date_naissance = ?, "
                + "num_tel = ?, "
                + "password = ?, "
                + "mis_ajour_le = ?, "
                + "token = ?, "
                + "photo = ?, "
                + "reset_code = ?, "
                + "reset_code_expires_at = ? "
                + "WHERE id = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, user.getRole_id());
            ps.setString(2, user.getPrenom());
            ps.setString(3, user.getNom());
            ps.setString(4, user.getEmail());
            ps.setString(5, user.getGenre());
            ps.setDate(6, Date.valueOf(user.getDate_naissance()));
            ps.setString(7, user.getNum_tel());
            ps.setString(8, user.getPassword());
            ps.setTimestamp(9, Timestamp.valueOf(LocalDateTime.now()));
            ps.setString(10, user.getToken());
            ps.setString(11, user.getPhoto());
            ps.setString(12, user.getReset_code());
            ps.setTimestamp(13, user.getReset_code_expires_at() != null ?
                    Timestamp.valueOf(user.getReset_code_expires_at()) : null);
            ps.setInt(14, user.getId());

            ps.executeUpdate();
        }
    }

    @Override
    public void delete(Utilisateur user) throws SQLException {
        String sql = "DELETE FROM utilisateur WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, user.getId());
            ps.executeUpdate();
        }
    }

    @Override
    public List<Utilisateur> findAll() throws SQLException {
        List<Utilisateur> users = new ArrayList<>();
        String sql = "SELECT * FROM utilisateur";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                users.add(mapResultSetToUser(rs));
            }
        }
        return users;
    }

    public static String getEmailById(int userId) throws SQLException {
        String sql = "SELECT email FROM utilisateur WHERE id = ?";
        Connection cnx = MyDb.getInstance().getConnection();

        try (PreparedStatement ste = cnx.prepareStatement(sql)) {
            ste.setInt(1, userId);

            try (ResultSet rs = ste.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("email");
                }
            }
        }
        return null;
    }

    // Helper methods
    private void setCommonParameters(PreparedStatement ps, Utilisateur user) throws SQLException {
        ps.setInt(1, user.getRole_id());
        ps.setString(2, user.getPrenom());
        ps.setString(3, user.getNom());
        ps.setString(4, user.getEmail());
        ps.setString(5, user.getGenre());
        ps.setDate(6, Date.valueOf(user.getDate_naissance()));
        ps.setString(7, user.getNum_tel());
        ps.setString(8, user.getPassword());
    }

    private void setNullableParameters(PreparedStatement ps, Utilisateur user) throws SQLException {
        ps.setString(11, user.getToken());
        ps.setString(12, user.getPhoto());
        ps.setString(13, user.getReset_code());

        if (user.getReset_code_expires_at() != null) {
            ps.setTimestamp(14, Timestamp.valueOf(user.getReset_code_expires_at()));
        } else {
            ps.setNull(14, Types.TIMESTAMP);
        }
    }

    private Utilisateur mapResultSetToUser(ResultSet rs) throws SQLException {
        Utilisateur user = new Utilisateur();
        user.setId(rs.getInt("id"));
        user.setRole_id(rs.getInt("role_id"));
        user.setPrenom(rs.getString("prenom"));
        user.setNom(rs.getString("nom"));
        user.setEmail(rs.getString("email"));
        user.setGenre(rs.getString("genre"));
        user.setDate_naissance(rs.getDate("date_naissance").toLocalDate());
        user.setNum_tel(rs.getString("num_tel"));
        user.setPassword(rs.getString("password"));
        user.setCree_le(rs.getTimestamp("cree_le").toLocalDateTime());
        user.setMis_ajour_le(rs.getTimestamp("mis_ajour_le").toLocalDateTime());
        user.setToken(rs.getString("token"));
        user.setPhoto(rs.getString("photo"));
        user.setReset_code(rs.getString("reset_code"));

        Timestamp expires = rs.getTimestamp("reset_code_expires_at");
        user.setReset_code_expires_at(expires != null ? expires.toLocalDateTime() : null);

        return user;
    }
    public Utilisateur authenticate(String email, String password) throws SQLException {
        String query = "SELECT * FROM utilisateur WHERE email = ? AND password = ?";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setString(1, email);
            pst.setString(2, password);

            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return new Utilisateur(
                            rs.getInt("id"),
                            rs.getString("email"),
                            rs.getString("password"),
                            rs.getInt("role_id")
                            // Add other user properties as needed
                    );
                }
            }
        }
        return null;
    }



    public Utilisateur getUserByEmail(String email) throws SQLException {
        String query = "SELECT * FROM utilisateur WHERE email = ?";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setString(1, email);
            ResultSet rs = pst.executeQuery();
            return rs.next() ? new Utilisateur(
                    rs.getInt("id"),
                    rs.getString("email"),
                    rs.getString("password"),
                    rs.getInt("role_id")
            ) : null;
        }
    }
}