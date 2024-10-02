import java.sql.*;

public class Vehicle_db {
    // DatabaseConnector DatabaseConnector;

    public Vehicle_db() throws SQLException {
        // DatabaseConnector = new DatabaseConnector();
    }

    public boolean addVehicle(Vehicle v) {
        boolean f = false;
        try {
            String sql = "insert into vehicle(v_id,type, model, year,price, isBooked) values(?,?,?,?,?,?)";
            PreparedStatement ps = DatabaseConnector.conn.prepareStatement(sql);
            ps.setInt(1, v.getId());
            ps.setString(2, v.getType());
            ps.setString(3, v.getModel());
            ps.setInt(4, v.getYear());
            ps.setInt(5, v.getPrice());
            ps.setBoolean(6, v.isBooked());

            int i = ps.executeUpdate();

            if (i == 1) {
                f = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return f;
    }

    public boolean updateVehicle(Vehicle v) {
        boolean f = false;
        try {
            String sql = "update vehicle set type=?, model=?, year=?, isBooked=?, price=? where v_id=?";
            PreparedStatement ps = DatabaseConnector.conn.prepareStatement(sql);
            ps.setString(1, v.getType());
            ps.setString(2, v.getModel());
            ps.setInt(3, v.getYear());
            ps.setBoolean(4, v.isBooked());
            ps.setInt(5, v.getPrice());
            ps.setInt(6, v.getId());

            int i = ps.executeUpdate();

            if (i == 1) {
                f = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return f;
    }

    public boolean deleteVehicle(int v_id) {
        boolean f = false;
        try {
            String sql = "delete from vehicle where v_id=?";
            PreparedStatement ps = DatabaseConnector.conn.prepareStatement(sql);

            ps.setInt(1, v_id);

            int i = ps.executeUpdate();

            if (i == 1) {
                f = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return f;
    }

}
