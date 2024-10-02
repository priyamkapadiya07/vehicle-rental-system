import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Booking {
    Scanner sc = new Scanner(System.in);
    Customer_manager cm;
    VehicleLinkedList vehicles;
    static int id;

    Booking() throws SQLException {
        cm = new Customer_manager();
        vehicles = new VehicleLinkedList();
    }

    public void bookVehicle() throws SQLException {

        System.out.println(
                "-----------> If you want to book a vehicle then you have to login or register first <-----------");
        boolean bv = true;
        while (bv) {
            System.out.println("\n1. Register");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            int choice = 0;
            boolean validInput = false;

            while (!validInput) {
                try {
                    System.out.print("Enter your choice : ");
                    choice = sc.nextInt();
                    validInput = true; // Break out of the loop if the input is valid
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input. Please enter an integer value !!!");
                    sc.next();
                }
            }
            switch (choice) {
                case 1:
                    id = cm.register();
                    book();
                    break;
                case 2:
                    id = cm.login();
                    book();
                    break;
                case 3:
                    bv = false;
                    break;
                default:
                    System.out.println("Invalid option");
            }

        }
    }

    void book() throws SQLException {
        // Ask for type of vehicle
        sc.nextLine();
        System.out.print("Enter type of vehicle (car, activa, motorcycle) : ");
        String vehicleType = sc.nextLine().toLowerCase();

        // Show vehicle details using vehicle type for booking.
        showVehicleDetailsByType(vehicleType);

        // Ask for vehicle ID
        System.out.print("\nEnter vehicle ID : ");
        int vehicleId = sc.nextInt();

        bookVehicleById(vehicleId);
    }

    void showVehicleDetailsByType(String vehicleType) throws SQLException {
        String query = "SELECT * FROM vehicle WHERE type = ?";
        PreparedStatement pstmt = DatabaseConnector.conn.prepareStatement(query);
        pstmt.setString(1, vehicleType);
        ResultSet rs = pstmt.executeQuery();

        while (rs.next()) {
            int id = rs.getInt("v_id");
            String type = rs.getString("type");
            String model = rs.getString("model");
            int year = rs.getInt("year");
            int price = rs.getInt("price");
            boolean availability = rs.getBoolean("isBooked");

            System.out.println("\nVehicle ID : " + id);
            System.out.println("Vehicle Type : " + type);
            System.out.println("Vehicle Model : " + model);
            System.out.println("Vehicle Year : " + year);
            System.out.println("Vehice price/day : " + price);
            System.out.println("isBooked : " + availability);
            System.out.println("-----------------------------------");
        }
    }

    void bookVehicleById(int vehicleId) throws SQLException {
        // Query to check if the vehicle is available
        String query = "SELECT * FROM vehicle WHERE v_id = ? AND isBooked = false";
        PreparedStatement pstmt = DatabaseConnector.conn.prepareStatement(query);
        pstmt.setInt(1, vehicleId);
        ResultSet rs = pstmt.executeQuery();

        while (rs.next()) {
            try {
                DatabaseConnector.conn.setAutoCommit(false);

                String updateQuery = "UPDATE vehicle SET isBooked = true WHERE v_id = ?";
                PreparedStatement updatePstmt = DatabaseConnector.conn.prepareStatement(updateQuery);
                updatePstmt.setInt(1, vehicleId);
                updatePstmt.executeUpdate();

                // Refresh linked list to reflect the latest database state
                vehicles.refreshData();
                // Get the customer ID from the Customer_manager class
                int customerId = id;

                // Insert a new booking record into the booking table
                insertBookingRecord(vehicleId, customerId);

                // Commit the changes
                DatabaseConnector.conn.commit();
                System.out.println("-----------> Vehicle booked successfully! <-----------");
            } catch (SQLException e) {
                // Rollback the changes
                DatabaseConnector.conn.rollback();
                System.out.println("Error booking vehicle: " + e.getMessage());
            }
        }
    }

    void insertBookingRecord(int vehicleId, int customerId) throws SQLException {
        // Get the current date for the booking date
        // java.sql.Date bookingDate = new java.sql.Date(System.currentTimeMillis());
        System.out.print("enter booking date in (YYYY-MM-DD) : ");
        String bookingDate = sc.next();

        // Insert a new booking record into the booking table
        String query = "INSERT INTO booking (v_id, c_id, booking_date) VALUES (?, ?, ?)";
        PreparedStatement pstmt = DatabaseConnector.conn.prepareStatement(query);
        pstmt.setInt(1, vehicleId);
        pstmt.setInt(2, customerId);
        pstmt.setString(3, bookingDate);
        // pstmt.setString(4, null);
        pstmt.executeUpdate();
        System.out.println("Booking record inserted successfully!");
    }
}