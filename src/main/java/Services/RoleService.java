// RoleService.java
package Services;

import Models.Role;
import Utils.MyDb;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RoleService implements IUser<Role> {
    private Connection con;

    public RoleService() {
        this.con = MyDb.getInstance().getConnection();
    }

    @Override
    public void insert(Role obj) throws SQLException {
        String sql = "INSERT INTO role(nom_role, description) "
                + "VALUES('" + obj.getNomRole() + "','"
                + obj.getDescription() + "')";

        Statement stmt = this.con.createStatement();
        stmt.executeUpdate(sql);
    }

    @Override
    public void update(Role obj) throws SQLException {
        String sql = "UPDATE role SET "
                + "nom_role = '" + obj.getNomRole() + "', "
                + "description = '" + obj.getDescription() + "' "
                + "WHERE id = '" + obj.getrId() + "'";

        Statement stmt = this.con.createStatement();
        stmt.executeUpdate(sql);
    }

    @Override
    public void delete(Role obj) throws SQLException {
        String sql = "DELETE FROM role WHERE id = '" + obj.getrId() + "'";
        Statement stmt = this.con.createStatement();
        stmt.executeUpdate(sql);
    }

    @Override
    public List<Role> findAll() throws SQLException {
        String sql = "SELECT * FROM role";
        Statement stmt = this.con.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        List<Role> list = new ArrayList<>();

        while (rs.next()) {
            Role role = new Role();
            role.setrId(rs.getInt("id"));
            role.setNomRole(rs.getString("nom_role"));
            role.setDescription(rs.getString("description"));

            list.add(role);
        }
        return list;
    }

    public Role findByName(String roleName) throws SQLException {
        String sql = "SELECT * FROM role WHERE nom_role = ?";
        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, roleName);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Role(
                        rs.getInt("id"),
                        rs.getString("nom_role"),
                        rs.getString("description")
                );
            }
        }
        return null;
    }

    public String getRoleNameById(int id) throws SQLException {
        String sql = "SELECT nom_role FROM role WHERE id = ?";
        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            return rs.next() ? rs.getString("nom_role") : null;
        }
    }


}