// make node and VehicleLinkedList class for manage vehicle.

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class VehicleLinkedList {
    Scanner sc = new Scanner(System.in);

    class Node {
        Vehicle vehicle;
        Node next;
        Node prev;

        Node(Vehicle v) {
            vehicle = v;
            next = null;
            prev = null;
        }
    }

    Node Head = null;

    boolean isLoginValid(int id, String password) {
        if (id == 777 && password.equals("update.com")) {
            System.out.println("----> Login Successful <----");
            return true;
        }
        System.out.println("----> Invalid id & Password. Try again <----");
        return false;
    }

    public Vehicle updateVehicle(int id) {
        Node temp = Head;
        while (temp != null) {
            if (temp.vehicle.getId() == id) {
                break;
            }
            temp = temp.next;
        }
        int choice = 0;
        while (true) {
            System.out.println("""
                    1:change Type
                    2:change Model
                    3:change Year
                    4:change Status(isBooked =true or false)
                    5:change Price
                    6:Exit
                    """);
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
            sc.nextLine();

            switch (choice) {
                case 1:
                    System.out.print("Enter Updated type : ");
                    String type = sc.nextLine();
                    temp.vehicle.setType(type);
                    System.out.println("update successfully...");
                    break;
                case 2:
                    System.out.print("Enter Updated model : ");
                    String model = sc.nextLine();
                    temp.vehicle.setModel(model);
                    System.out.println("update successfully...");
                    break;
                case 3:
                    System.out.print("Enter year : ");
                    int year = sc.nextInt();
                    temp.vehicle.setYear(year);
                    System.out.println("update successfully...");
                    break;
                case 4:
                    System.out.print("Enter isBooked true or false : ");
                    boolean status = sc.nextBoolean();
                    temp.vehicle.setBooked(status);
                    System.out.println("update successfully...");
                    break;
                case 5:
                    System.out.print("Enter updated price : ");
                    int price = sc.nextInt();
                    temp.vehicle.setPrice(price);
                    System.out.println("update successfully...");
                    break;
                case 6:
                    break;
                default:
                    System.out.println("Enter valid input : ");
                    break;
            }
            return temp.vehicle;
        }
    }

    void addLast(Vehicle vehicle) {
        Node n = new Node(vehicle);
        if (Head == null) {
            Head = n;
        } else {
            Node temp = Head;
            while (temp.next != null) {
                temp = temp.next;
            }
            temp.next = n;
            n.prev = temp;
        }
    }

    void displayVehicle() {
        Node temp = Head;
        while (temp != null) {
            Vehicle v = temp.vehicle;
            System.out.println("\nId : " + v.getId());
            System.out.println("Type : " + v.getType());
            System.out.println("Model : " + v.getModel());
            System.out.println("Year : " + v.getYear());
            System.out.println("price/Day : " + v.getPrice());
            System.out.println("Is Booked : " + v.isBooked());
            temp = temp.next;
        }
    }

    void deleteVehicle(int id) {
        Node temp = Head;
        while (temp != null) {
            if ((temp.vehicle.getId() == id)) {
                break;
            }
            temp = temp.next;
        }
        temp.prev.next = temp.next;
        temp.next.prev = temp.prev;
        temp.prev = null;
        temp.next = null;
    }

    public void refreshData() throws SQLException {
        // Clear the current linked list
        Head = null;

        // Fetch updated vehicle data from the database
        String query = "SELECT * FROM vehicle";
        PreparedStatement pstmt = DatabaseConnector.conn.prepareStatement(query);
        ResultSet rs = pstmt.executeQuery();

        while (rs.next()) {
            Vehicle v = new Vehicle(
                    rs.getInt("v_id"),
                    rs.getString("type"),
                    rs.getString("model"),
                    rs.getInt("year"),
                    rs.getInt("price"),
                    rs.getBoolean("isBooked"));
            addLast(v);
        }
    }
}