import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Return {
    Scanner sc = new Scanner(System.in);
    BillGenerator bill;
    VehicleLinkedList vehicleLinkedList;

    // Constructor
    Return() throws SQLException {
        bill = new BillGenerator();
        vehicleLinkedList = new VehicleLinkedList();
    }

    public void returnVehicle() throws SQLException {
        System.out.print("Enter your name : ");
        String customerName = sc.nextLine();

        String query = "SELECT customer.c_id, booking.v_id, booking.b_id FROM customer " +
                "INNER JOIN booking ON customer.c_id = booking.c_id " +
                "WHERE customer.name = ? AND booking.return_date IS NULL;";

        PreparedStatement pstmt = DatabaseConnector.conn.prepareStatement(query);
        pstmt.setString(1, customerName);

        ResultSet rs = pstmt.executeQuery();

        // Collect all active bookings
        if (!rs.next()) {
            System.out.println("No active bookings found for customer: " + customerName);
            return;
        }

        // Display all active bookings
        System.out.println("Active bookings :");
        do {
            int vid = rs.getInt("v_id");
            int bookingId = rs.getInt("b_id");
            System.out.println("Booking ID: " + bookingId + ", Vehicle ID: " + vid);
        } while (rs.next());

        // Ask user to specify which booking to return
        System.out.print("Enter the Booking ID you want to return: ");
        int bookingIdToReturn = sc.nextInt();
        sc.nextLine(); // Consume newline

        // Validate Booking ID
        pstmt = DatabaseConnector.conn.prepareStatement(
                "SELECT v_id, c_id FROM booking WHERE b_id = ? AND return_date IS NULL");
        pstmt.setInt(1, bookingIdToReturn);
        ResultSet bookingResult = pstmt.executeQuery();

        if (!bookingResult.next()) {
            System.out.println("Invalid Booking ID or booking already returned.");
            return;
        }

        int vid = bookingResult.getInt("v_id");
        int cid = bookingResult.getInt("c_id");

        try {
            DatabaseConnector.conn.setAutoCommit(false);

            // Update return date in booking table
            query = "UPDATE booking SET return_date = ? WHERE b_id = ?";
            pstmt = DatabaseConnector.conn.prepareStatement(query);
            System.out.print("Enter return date in (YYYY-MM-DD) : ");
            pstmt.setString(1, sc.nextLine());
            pstmt.setInt(2, bookingIdToReturn);
            pstmt.executeUpdate();

            query = "UPDATE vehicle SET isBooked = false WHERE v_id = ?";
            pstmt = DatabaseConnector.conn.prepareStatement(query);
            pstmt.setInt(1, vid);
            pstmt.executeUpdate();

            // Refresh linked list to reflect the latest database state
            vehicleLinkedList.refreshData();

            System.out.println("---------> Vehicle returned successfully! <---------");
            bill.generateBill(vid, cid);
            DatabaseConnector.conn.commit();

        } catch (Exception e) {
            DatabaseConnector.conn.rollback();
            System.out.println("Error returning vehicle: " + e.getMessage());
        }
    }
}
