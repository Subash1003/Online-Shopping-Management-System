package onlineshoppingmanagement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
public class Login {
    public static void login() throws Exception {
        Scanner scanner = new Scanner(System.in);
        System.out.println("***Login***");
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        String role = checkCredentials(username, password);
      switch (role) {
            case "admin":
               adminPortal();
                break;
            case "Customer":
                CustomerPortal();
                break;
            default:
                System.out.println("User not exist or invalid role.");
        }
    }
    public static void signup() throws Exception {
        Scanner scanner = new Scanner(System.in);
        System.out.println("***Signup***");
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        try (Connection connection = DBConnection.getConnection()) {
            String sql = "INSERT INTO customer (username, password) VALUES (?, ?)";

            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, username);
                statement.setString(2, password);
                statement.executeUpdate();

                // **Correct Placement:** Print success message after successful insertion
                System.out.println("Signup successful for user: " + username);
            }
        } catch (SQLException e) {
            // Handle specific database errors
            System.out.println("Signup failed: " + e.getMessage());
        }
    }
    private static String checkCredentials(String username, String password) {
    	 try (Connection connection = DBConnection.getConnection())  {
            
            String query = "SELECT * FROM admin WHERE username = ? AND password = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, username);
                statement.setString(2, password);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        return "admin";
                    }
                }
            }
            String query1 = "SELECT * FROM customer WHERE username = ? AND password = ?";
            try (PreparedStatement statement = connection.prepareStatement(query1)) {
                statement.setString(1, username);
                statement.setString(2, password);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        return "Customer";
                    }
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return "invalid"; 
    }
    
     private static void adminPortal()throws Exception {
        System.out.println("\n***Welcome to Admin Portal***");
        Admin admin = new Admin();
        admin.performAdminProcesses();
     }
    
    private static void CustomerPortal() throws Exception {
        System.out.println("\n***Welcome to Customer Portal***");
        Customer customer=new Customer();
        customer.performCustomerProcesses();
        }
}

