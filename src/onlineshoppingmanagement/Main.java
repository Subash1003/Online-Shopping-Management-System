package onlineshoppingmanagement;
import java.util.Scanner;
import java.sql.SQLException;
import java.util.InputMismatchException;
public class Main {
    public static void main(String[] args)throws Exception {
    	
    	System.out.println("***--------------------------------------------***");
    	 System.out.println("   Welcome to Online Shopping Management System");
    	 System.out.println("***--------------------------------------------***");
    	 log();
    }
    public static void log()throws Exception{
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\nSelect your option:");
            System.out.println("1. Login");
            System.out.println("2. Signup");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");
            try {
                int choice = scanner.nextInt();
                switch (choice) {
                case 1:
                    Login.login();
                    break;
                case 2:
                    Login.signup();
                    break;
                case 3:
                    System.out.println("***ThankYou!!!");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter an integer.");
                scanner.next();
            }

        }
    }
}
