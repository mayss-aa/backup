// AddressService.java
package Services;

import Models.Address;
import Utils.MyDb;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AddressService implements IUser<Address> {
    private Connection con;

    public AddressService() {
        this.con = MyDb.getInstance().getConnection();
    }

    @Override
    public void insert(Address obj) throws SQLException {
        String sql = "INSERT INTO address(user_id, address_line, city, state, postal_code, country, created_at) "
                + "VALUES('" + obj.getUser_id() + "','"
                + obj.getAddress_line() + "','"
                + obj.getCity() + "','"
                + obj.getState() + "','"
                + obj.getPostal_code() + "','"
                + obj.getCountry() + "','"
                + LocalDate.now() + "')";

        Statement stmt = this.con.createStatement();
        stmt.executeUpdate(sql);
    }

    @Override
    public void update(Address obj) throws SQLException {
        String sql = "UPDATE address SET "
                + "user_id = '" + obj.getUser_id() + "', "
                + "address_line = '" + obj.getAddress_line() + "', "
                + "city = '" + obj.getCity() + "', "
                + "state = '" + obj.getState() + "', "
                + "postal_code = '" + obj.getPostal_code() + "', "
                + "country = '" + obj.getCountry() + "' "
                + "WHERE address_id = '" + obj.getAddress_id() + "'";

        Statement stmt = this.con.createStatement();
        stmt.executeUpdate(sql);
    }

    @Override
    public void delete(Address obj) throws SQLException {
        String sql = "DELETE FROM address WHERE address_id = '" + obj.getAddress_id() + "'";
        Statement stmt = this.con.createStatement();
        stmt.executeUpdate(sql);
    }

    @Override
    public List<Address> findAll() throws SQLException {
        String sql = "SELECT * FROM address";
        Statement stmt = this.con.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        List<Address> list = new ArrayList<>();

        while (rs.next()) {
            Address address = new Address();
            address.setAddress_id(rs.getInt("address_id"));
            address.setUser_id(rs.getInt("user_id"));
            address.setAddress_line(rs.getString("address_line"));
            address.setCity(rs.getString("city"));
            address.setState(rs.getString("state"));
            address.setPostal_code(rs.getString("postal_code"));
            address.setCountry(rs.getString("country"));
            address.setCreated_at(rs.getDate("created_at").toLocalDate());

            list.add(address);
        }
        return list;
    }
}