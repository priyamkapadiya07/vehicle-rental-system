import java.sql.*;

public class Customer_db {
    // DatabaseConnector DatabaseConnector;

    public Customer_db() throws SQLException {
        super();
        // DatabaseConnector = new DatabaseConnector();
    }

    public boolean addCustomer(Customer c) {

        boolean f = false;
        try {

            String sql = "insert into customer values (?,?,?,?,?,?)";
            PreparedStatement rs = DatabaseConnector.conn.prepareStatement(sql);
            rs.setInt(1, c.getId());
            rs.setString(2, c.getName());
            rs.setString(3, c.getEmail());
            rs.setString(4, c.getPhone());
            rs.setString(5, c.getCity());
            rs.setString(6, c.getPassword());
            int i = rs.executeUpdate();
            if (i == 1) {
                f = true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return f;
    }

    public boolean updateCustomer(Customer c) {
        boolean f = false;
        try {

            String sql = "update customer set name=?, email=?, phone=?,password=? where c_id=?";
            PreparedStatement ps = DatabaseConnector.conn.prepareStatement(sql);

            ps.setString(1, c.getName());
            ps.setString(2, c.getEmail());
            ps.setString(3, c.getPhone());
            ps.setString(4, c.getPassword());
            ps.setInt(5, c.getId());

            int i = ps.executeUpdate();
            if (i == 1) {
                f = true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return f;
    }

    boolean deleteCustomer(int c_id) throws SQLException {
        String q = "delete from Customer where c_id=" + c_id + ";";
        PreparedStatement ps = DatabaseConnector.conn.prepareStatement(q);
        if (ps.executeUpdate(q) == 1) {
            return true;
        } else {
            return false;
        }

    }

}