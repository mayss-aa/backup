package Services;

import Models.Utilisateur;
import Utils.MyDb;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class UtilisateurService implements IUser<Utilisateur> {
    private Connection con;

    public UtilisateurService() {
        this.con = MyDb.getInstance().getConnection();
    }

    @Override
    public void insert(Utilisateur obj) throws SQLException {
        String sql = "INSERT INTO utilisateur (role_id, prenom, nom, email, genre, date_naissance, num_tel, password, cree_le, mis_ajour_le) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, obj.getRole_id());
            pstmt.setString(2, obj.getPrenom());
            pstmt.setString(3, obj.getNom());
            pstmt.setString(4, obj.getEmail());
            pstmt.setString(5, obj.getGenre());
            pstmt.setDate(6, Date.valueOf(obj.getDate_naissance()));
            pstmt.setString(7, obj.getNum_tel());
            pstmt.setString(8, obj.getPassword());
            pstmt.setTimestamp(9, Timestamp.valueOf(LocalDateTime.now()));
            pstmt.setTimestamp(10, Timestamp.valueOf(LocalDateTime.now()));// Auto-set timestamp

            pstmt.executeUpdate();
        }
    }

    @Override
    public void update(Utilisateur obj) throws SQLException {
        String sql = "UPDATE utilisateur SET "
                + "role_id = '" + obj.getRole_id() + "', "
                + "prenom = '" + obj.getPrenom() + "', "
                + "nom = '" + obj.getNom() + "', "
                + "email = '" + obj.getEmail() + "', "
                + "genre = '" + obj.getGenre() + "', "
                + "date_naissance = '" + obj.getDate_naissance() + "', "
                + "num_tel = '" + obj.getNum_tel() + "', "
                + "password = '" + obj.getPassword() + "', "
                + "mis_ajour_le = '" + LocalDateTime.now() + "' "
                + "WHERE id = '" + obj.getId() + "'";

        Statement stmt = this.con.createStatement();
        stmt.executeUpdate(sql);
    }

    @Override
    public void delete(Utilisateur obj) throws SQLException {
        String sql = "DELETE FROM utilisateur WHERE id = '" + obj.getId() + "'";
        Statement stmt = this.con.createStatement();
        stmt.executeUpdate(sql);
    }

    @Override
    public List<Utilisateur> findAll() throws SQLException {
        String sql = "SELECT * FROM utilisateur";
        Statement stmt = this.con.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        List<Utilisateur> list = new ArrayList<>();

        while (rs.next()) {
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

            list.add(user);
        }
        return list;
    }
}