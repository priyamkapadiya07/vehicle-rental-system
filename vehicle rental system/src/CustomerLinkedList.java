// make node and CustomerLinkedList class for manage customer.

import java.util.InputMismatchException;
import java.util.Scanner;

public class CustomerLinkedList {
    static Customer_db customer_data;
    static Scanner sc = new Scanner(System.in);

    class Node {
        Customer customer;
        Node next;
        Node prev;

        Node(Customer c) {
            customer = c;
            next = null;
            prev = null;
        }
    }

    Node Head = null;

    void displayCustomer(int id, String password) {
        Node temp = Head;
        int flag = 0;
        while (temp != null) {
            if ((temp.customer.getId() == id) && (temp.customer.getPassword().equals(password))) {
                flag++;
                break;
            }
            temp = temp.next;
        }
        if (flag == 0) {
            System.out.println("Customer is not availabe");
        } else {
            Customer c = temp.customer;
            System.out.println("ID : " + c.getId());
            System.out.println("Name : " + c.getName());
            System.out.println("E-Mail : " + c.getEmail());
            System.out.println("Phone Number : " + c.getPhone());
            System.out.println("City : " + c.getCity());
            System.out.println("Password : " + c.getPassword());
        }
    }

    boolean loginIsValid(String email, String password) {
        Node temp = Head;
        while (temp != null) {
            if (temp.customer.getEmail().equals(email) && temp.customer.getPassword().equals(password)) {
                return true;
            }
            temp = temp.next;
        }
        return false;
    }

    public Customer updateCustomer(int id) {
        Node temp = Head;
        while (temp != null) {
            if (temp.customer.getId() == id) {
                break;
            }
            temp = temp.next;
        }
        int choice = 0;
        while (true) {
            System.out.println("""
                    1:change Name
                    2:change Email
                    3:change Phone Number
                    4:change Password
                    5:Exit
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
                    System.out.print("Enter Updated name : ");
                    String updated_name = sc.nextLine();
                    temp.customer.setName(updated_name);
                    System.out.println("update successfully...");
                    break;
                case 2:
                    System.out.print("Enter Updated email : ");
                    String updated_email = sc.nextLine();
                    temp.customer.setEmail(updated_email);
                    System.out.println("update successfully...");
                    break;
                case 3:
                    System.out.print("Enter Updated phone_number : ");
                    String updated_phone = sc.nextLine();
                    temp.customer.setPhone(updated_phone);
                    System.out.println("update successfully...");
                    break;
                case 4:
                    System.out.print("Enter Updated password : ");
                    String updated_password = sc.nextLine();
                    temp.customer.setPassword(updated_password);
                    System.out.println("update successfully...");
                    break;
                case 5:
                    break;
                default:
                    System.out.println("Enter valid input : ");
                    break;
            }
            return temp.customer;
        }
    }

    void addLast(Customer customer) {
        Node n = new Node(customer);
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

    void deleteCustomer(int id, String password) {
        Node temp = Head;
        // int flag = 0;
        while (temp != null) {
            if ((temp.customer.getId() == id) && (temp.customer.getPassword().equals(password))) {
                // flag++;
                break;
            }
            temp = temp.next;
        }
        temp.prev.next = temp.next;
        temp.next.prev = temp.prev;
        temp.prev = null;
        temp.next = null;
    }

}