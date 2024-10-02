import java.sql.*;

public class DatabaseConnector {
    static Connection conn;

    DatabaseConnector() {
        try {
            // Load the JDBC driver
            // Class.forName("com.mysql.jdbc.Driver");

            // Establish a connection to the database
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/vehicle_rental", "root", "");
            if (conn != null) {
                System.out.println("Connected to the database successfully.");
            } else {
                System.out.println("Failed To Connect to database");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
