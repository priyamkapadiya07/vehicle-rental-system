import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Vehicle_manager {
    static VehicleLinkedList vehicles;
    static Vehicle_db v_data;
    static int id = 0;
    Scanner sc = new Scanner(System.in);

    public Vehicle_manager() throws SQLException {
        vehicles = new VehicleLinkedList();
        v_data = new Vehicle_db();
    }

    void copyData() throws Exception {
        String q = "select * from vehicle order by price DESC;";
        PreparedStatement ps = DatabaseConnector.conn.prepareStatement(q);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            id = rs.getInt(1);
            Vehicle temp = new Vehicle(rs.getInt(1), rs.getString(2), rs.getString(3),
                    rs.getInt(4), rs.getInt(5), rs.getBoolean(6));
            vehicles.addLast(temp);
        }
    }

    public void addVehicle() {
        id++;
        sc.nextLine();
        System.out.println("Vehicle id : " + id);
        System.out.print("enter type(car, activa, motorcycle)  : ");
        String type = sc.nextLine();
        System.out.print("enter model name : ");
        String model = sc.nextLine();
        System.out.print("enter year : ");
        int year = sc.nextInt();
        System.out.print("enter Price/Day : ");
        int price = sc.nextInt();
        System.out.print("enter isBooked (true or false) : ");
        boolean is_available = sc.nextBoolean();
        System.out.println("Vehicle add successfully!");
        vehicles.addLast(new Vehicle(id, type, model, year, price, is_available));
        if (v_data.addVehicle(new Vehicle(id, type, model, year, price, is_available))) {
            System.out.println("vehicle registered successfully Into Database!");
        }
    }

    // here id = 777 and password = update.com
    public void login() throws SQLException {
        sc.nextLine();
        System.out.print("Enter UserID: ");
        int id = sc.nextInt();
        sc.nextLine();
        System.out.print("Enter password: ");
        String password = sc.nextLine();
        if (vehicles.isLoginValid(id, password)) {
            System.out.println("Login successful!");
            VehicleInterface2(id, password);
        } else {
            System.out.println("Invalid email or password");
        }
    }

    void VehicleInterface2(int id, String password) throws SQLException {
        System.out.print("enter vehicle id for delete/update : ");
        int id1 = sc.nextInt();
        while (true) {
            System.out.println("""
                    \n1:Delete vehicle
                    2:Update vehicle
                    3:exit
                    """);
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
                    vehicles.deleteVehicle(id1);
                    v_data.deleteVehicle(id1);
                    break;
                case 2:
                    // v_data.updateVehicle(vehicles.updateVehicle(id1));
                    Vehicle updatedVehicle = vehicles.updateVehicle(id1);
                    if (updatedVehicle != null) {
                        boolean isUpdated = v_data.updateVehicle(updatedVehicle);
                        if (isUpdated) {
                            System.out.println("Profile updated successfully in the database.");
                        } else {
                            System.out.println("Failed to update profile in the database.");
                        }
                    } else {
                        System.out.println("Profile update failed in the linked list.");
                    }
                    break;
                case 3:
                    VehicleInterface();
                    break;
                default:
                    System.out.println("enter currect choice");
                    break;
            }
        }
    }

    void VehicleInterface() throws SQLException {
        int choice = 0;
        boolean b = true;
        while (b) {
            System.out.println("""
                    \n1:add Vehicle
                    2:Delete/Update vehicle
                    3:Exit
                    """);
            boolean validInput = false;

            while (!validInput) {
                try {
                    System.out.print("Enter your choice : ");
                    choice = sc.nextInt();
                    validInput = true;
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input. Please enter an integer value !!!");
                    sc.next();
                }
            }
            switch (choice) {
                case 1:
                    addVehicle();
                    break;
                case 2:
                    login();
                    break;
                case 3:
                    b = false;
                    String[] a = {};
                    Main.main(a);
                    break;
                default:
                    System.out.println("Invalid option");
            }
        }

    }
}
