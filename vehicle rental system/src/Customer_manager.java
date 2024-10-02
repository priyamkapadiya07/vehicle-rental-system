import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Customer_manager {
    Scanner scanner = new Scanner(System.in);
    static CustomerLinkedList customers;
    static Customer_db c_data;
    // DatabaseConnector databaseConnector;
    static int id = 0;

    public Customer_manager() throws SQLException {
        customers = new CustomerLinkedList();
        c_data = new Customer_db();
        // databaseConnector = new DatabaseConnector();

    }

    boolean copyData() throws Exception {
        boolean f = true;
        try {
            String q = "select * from customer;";
            PreparedStatement ps = DatabaseConnector.conn.prepareStatement(q);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                id = rs.getInt(1);
                Customer temp = new Customer(rs.getInt(1), rs.getString(2), rs.getString(3),
                        rs.getString(4), rs.getString(5), rs.getString(6));
                customers.addLast(temp);
            }
        } catch (Exception e) {
            f = false;
        }
        return f;
    }

    public int register() throws SQLException {
        id++;
        scanner.nextLine();
        System.out.print("Enter name : ");
        String name = scanner.nextLine();
        System.out.print("Enter email : ");
        String email = scanner.nextLine();
        System.out.print("Enter city : ");
        String city = scanner.nextLine();
        String phone;
        while (true) {
            System.out.print("Enter phone number : ");
            phone = scanner.nextLine();
            if (isValidMobileNumber(phone)) {
                break;
            } else {
                System.out.println("Invalid mobile number. Please enter a 10-digit mobile number.");
            }
        }
        System.out.println("your id : " + id);
        System.out.print("set password : ");
        String password = scanner.nextLine();
        Customer customer = new Customer(id, name, email, phone, city, password);
        customers.addLast(customer);
        System.out.println("customer add successfully !");
        if (c_data.addCustomer(customer)) {
            System.out.println("Customer registered successfully Into Database!");
        }
        return id;
    }

    private boolean isValidMobileNumber(String mobileNumber) {
        String regex = "^[0-9]{10}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(mobileNumber);

        return matcher.matches();
    }

    public int login() throws SQLException {
        scanner.nextLine();
        System.out.print("Enter Email : ");
        String email = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        if (customers.loginIsValid(email, password)) {
            System.out.println("Login successful!");
        } else {
            System.out.println("Invalid Email or password");
        }
        return id;
    }

    public void login1() throws SQLException {
        scanner.nextLine();
        System.out.print("Enter Email : ");
        String email = scanner.nextLine();
        System.out.print("Enter password : ");
        String password = scanner.nextLine();
        if (customers.loginIsValid(email, password)) {
            System.out.println("Login successful!");
            CustomerInterface2(email, password);
        } else {
            System.out.println("Invalid Email or password");
        }
    }

    private void CustomerInterface2(String email, String password) throws SQLException {
        boolean b = true;
        while (b) {
            System.out.println("1. Update Profile");
            System.out.println("2. Delete profile ");
            System.out.println("3. Exit");
            int choice = 0;
            boolean validInput = false;

            while (!validInput) {
                try {
                    System.out.print("Enter your choice : ");
                    choice = scanner.nextInt();
                    validInput = true;
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input. Please enter an integer value !!!");
                    scanner.next();
                }
            }
            switch (choice) {
                case 1:
                    Customer updatedCustomer = customers.updateCustomer(id);
                    if (updatedCustomer != null) {
                        boolean isUpdated = c_data.updateCustomer(updatedCustomer);
                        if (isUpdated) {
                            System.out.println("Profile updated successfully in the database.");
                        } else {
                            System.out.println("Failed to update profile in the database.");
                        }
                    } else {
                        System.out.println("Profile update failed in the linked list.");
                    }
                    break;
                case 2:
                    System.out.println("-------> You can't delete your profile for some reason <-------");
                    break;
                case 3:
                    b = false;
                    break;
                default:
                    System.out.println("Invalid option");
            }
        }

    }
}