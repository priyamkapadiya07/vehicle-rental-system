
// BillGenerator.java
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class BillGenerator {
    // DatabaseConnector DatabaseConnector;

    public BillGenerator() {
        // DatabaseConnector = new DatabaseConnector();
    }

    public void generateBill(int vehicleId, int customerId) throws Exception {
        try {
            DatabaseConnector.conn.setAutoCommit(false);
            // Create a prepared statement to insert a new bill into the bills table
            PreparedStatement pstmt = DatabaseConnector.conn
                    .prepareStatement("INSERT INTO bills (v_id,c_id, amount) VALUES (?,?, ?)");
            pstmt.setInt(1, vehicleId);
            pstmt.setInt(2, customerId);
            pstmt.setDouble(3, calculateBillAmount(vehicleId, customerId));
            pstmt.executeUpdate();

            System.out.println("Bill generated successfully for vehicle ID " + vehicleId);
            // Print bill details for the customer
            printBillDetails(vehicleId, customerId);
            DatabaseConnector.conn.commit();
        } catch (SQLException e) {
            DatabaseConnector.conn.rollback();
            System.out.println("Error generating bill: " + e.getMessage());
        }
    }

    public double calculateBillAmount(int vehicleId, int customerId) throws SQLException {
        double billAmount = 0.0;

        try {
            DatabaseConnector.conn.setAutoCommit(false);
            // Retrieve the booking ID from the booking table
            PreparedStatement pstmt = DatabaseConnector.conn
                    .prepareStatement("SELECT b_id FROM booking WHERE v_id = ? and c_id = ?");
            pstmt.setInt(1, vehicleId);
            pstmt.setInt(2, customerId);
            ResultSet resultSet = pstmt.executeQuery();

            if (resultSet.next()) {
                int bookingId = resultSet.getInt("b_id");

                // Call the stored procedure to calculate the rental days
                CallableStatement cstmt = DatabaseConnector.conn.prepareCall("{call calculate_rental_days(?,?,?)}");
                cstmt.setInt(1, bookingId);
                cstmt.execute();
                String b_date = cstmt.getString(2);
                String r_date = cstmt.getString(3);

                // Parse the dates using DateTimeFormatter
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate bDate = LocalDate.parse(b_date, formatter);
                LocalDate rDate = LocalDate.parse(r_date, formatter);

                // Calculate the total number of days
                long rentalDays = ChronoUnit.DAYS.between(bDate, rDate);

                // Retrieve the price per day from the vehicle table
                pstmt = DatabaseConnector.conn.prepareStatement("SELECT price FROM vehicle WHERE v_id = ?");
                pstmt.setInt(1, vehicleId);
                ResultSet rs = pstmt.executeQuery();

                if (rs.next()) {
                    int pricePerDay = rs.getInt("price");

                    // Calculate the total bill amount
                    billAmount = rentalDays * pricePerDay;
                }
            }
            DatabaseConnector.conn.commit();
        } catch (SQLException e) {
            DatabaseConnector.conn.rollback();
            System.out.println("Error calculating bill amount: " + e.getMessage());
        }

        return billAmount;
    }

    public void printBillDetails(int vehicleId, int customerId) {
        try {
            // Retrieve the bill amount from the bills table
            PreparedStatement pstmt = DatabaseConnector.conn
                    .prepareStatement("SELECT amount FROM bills WHERE v_id = ? and c_id = ?");
            pstmt.setInt(1, vehicleId);
            pstmt.setInt(2, customerId);
            ResultSet resultSet = pstmt.executeQuery();

            if (resultSet.next()) {
                double billAmount = resultSet.getDouble("amount");

                // Retrieve the customer name from the customer table
                pstmt = DatabaseConnector.conn.prepareStatement("SELECT name FROM customer WHERE c_id = ?");
                pstmt.setInt(1, customerId);
                ResultSet rs = pstmt.executeQuery();

                if (rs.next()) {
                    String customerName = rs.getString("name");

                    // Retrieve the vehicle details from the vehicle table
                    pstmt = DatabaseConnector.conn
                            .prepareStatement("SELECT model, type FROM vehicle WHERE v_id = ?");
                    pstmt.setInt(1, vehicleId);
                    ResultSet rs2 = pstmt.executeQuery();

                    if (rs2.next()) {
                        String vehicleName = rs2.getString("model");
                        String vehicleType = rs2.getString("type");

                        // Print the bill details
                        System.out.println("\nBill Details:");
                        System.out.println("\n######################################################");
                        System.out.println("##     Customer Name : " + customerName);
                        System.out.println("##     Vehicle ID : " + vehicleId);
                        System.out.println("##     Vehicle Name : " + vehicleName);
                        System.out.println("##     Vehicle Type : " + vehicleType);
                        System.out.println("##     Bill Amount : " + billAmount);
                        System.out.println("######################################################");
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Error printing bill details: " + e.getMessage());
        }
    }
}