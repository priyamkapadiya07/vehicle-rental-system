import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;

class Main {
    static Customer_manager C_Manager;
    static Vehicle_manager V_Manager;
    static Booking b;
    static Return r;
    static Scanner sc = new Scanner(System.in);
    static {

        try {
            new DatabaseConnector();
            C_Manager = new Customer_manager();
            V_Manager = new Vehicle_manager();
            b = new Booking();
            r = new Return();
            C_Manager.copyData();
            V_Manager.copyData();
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws SQLException {
        System.out.println("||------------> Welcome to  Vehicle Rental System <------------||");
        while (true) {
            System.out.println("\n1 : show vehicle");
            System.out.println("2 : Booking vehicle");
            System.out.println("3 : Return vehicle");
            System.out.println("4 : Update customer profile");
            System.out.println("5 : Manage vehicle");
            System.out.println("6 : Exit");

            int choice = 0;
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
                    Vehicle_manager.vehicles.displayVehicle();
                    break;
                case 2:
                    b.bookVehicle();
                    break;
                case 3:
                    sc.nextLine();
                    System.out.print("Enter Email : ");
                    String email = sc.nextLine();
                    System.out.print("Enter password : ");
                    String password = sc.nextLine();
                    if (Customer_manager.customers.loginIsValid(email, password)) {
                        System.out.println("Login successful!");
                        r.returnVehicle();
                    } else {
                        System.out.println("Invalid Email or password");
                    }
                    break;
                case 4:
                    C_Manager.login1();
                    break;
                case 5:
                    V_Manager.VehicleInterface();
                    break;
                case 6:
                    System.out.println("||------------> Thank you for Using Our System <------------||");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Ente Valid Input");
                    break;
            }
        }
    }
}