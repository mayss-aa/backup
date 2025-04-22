package Services;

import Utils.MyDb;
import Models.Note;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class NoteService implements IUser<Note> {
    private Connection con;

    public NoteService() {
        this.con = MyDb.getInstance().getConnection();
    }

    public void insert(Note note) throws SQLException {
        String sql = "INSERT INTO note (titre, contenu, utilisateur_id, date_creation, date_modification) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, note.getTitre()); // Ensure titre is included
            ps.setString(2, note.getContenu());
            ps.setInt(3, note.getUtilisateur_id());
            ps.setTimestamp(4, Timestamp.valueOf(note.getDateCreation()));
            ps.setTimestamp(5, Timestamp.valueOf(note.getDateModification()));
            ps.executeUpdate();
        }
    }

    @Override
    public void update(Note obj) throws SQLException {
        obj.setDateModification(LocalDateTime.now());
        String sql = "UPDATE note SET titre = ?, contenu = ?, date_modification = ?, utilisateur_id = ? WHERE id = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, obj.getTitre());
        ps.setString(2, obj.getContenu());
        ps.setTimestamp(3, Timestamp.valueOf(obj.getDateModification()));
        ps.setInt(4, obj.getUtilisateur_id());
        ps.setInt(5, obj.getId());
        ps.executeUpdate();
    }

    @Override
    public void delete(Note obj) throws SQLException {
        String sql = "DELETE FROM note WHERE id = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, obj.getId());
        ps.executeUpdate();
    }

    @Override
    public List<Note> findAll() throws SQLException {
        String sql = "SELECT * FROM note";
        Statement stmt = this.con.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        List<Note> list = new ArrayList<>();
        while (rs.next()) {
            Note note = new Note();
            note.setId(rs.getInt("id"));
            note.setTitre(rs.getString("titre"));
            note.setContenu(rs.getString("contenu"));
            note.setDateCreation(rs.getTimestamp("date_creation").toLocalDateTime());
            note.setDateModification(rs.getTimestamp("date_modification").toLocalDateTime());
            note.setUtilisateur_id(rs.getInt("utilisateur_id"));
            list.add(note);
        }
        return list;
    }

    public void deleteByUtilisateur(int utilisateurId) throws SQLException {
        String sql = "DELETE FROM note WHERE utilisateur_id = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, utilisateurId);
        ps.executeUpdate();
    }

    public boolean titreExiste(String titre) throws SQLException {
        String query = "SELECT COUNT(*) FROM note WHERE titre = ?";
        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, titre);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }
        return false;
    }
}