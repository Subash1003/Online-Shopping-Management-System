package onlineshoppingmanagement;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.time.LocalDateTime;
import java.util.Scanner;
import java.util.ArrayList;
public class Admin {
    public void performAdminProcesses() throws Exception {
        System.out.println("Admin processes:");
        Scanner scanner = new Scanner(System.in);
        boolean loggedIn = false;
        
        while(!loggedIn) {
        	System.out.println("1. View Products");
        	System.out.println("2. Add Product");
        	System.out.println("3. Edit Product");
        	System.out.println("4. Remove Product");
        	System.out.println("5. Add Stock"); // <-- NEW
        	System.out.println("6. Logout");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            if (choice == 1)
                viewProduct();
            else if (choice == 2)
                addProduct();
            else if (choice == 3)
                editProduct();
            else if (choice == 4)
                removeProduct();
            else if (choice == 5)
                addStock(); // <-- NEW
            else if (choice == 6) {
                Main main = new Main();
                System.out.println("--------------------Logout Successfully------------------------\n");
                main.log();
            }
            else
                System.out.println("Invalid");

            }
        scanner.close();
        }
    private void viewProduct() {
        Scanner scanner = new Scanner(System.in);

        try (Connection connection = DBConnection.getConnection()) {
            // Step 1: Fetch distinct categories from database
            String categorySql = "SELECT DISTINCT category FROM products";
            try (PreparedStatement categoryStmt = connection.prepareStatement(categorySql);
                 ResultSet categoryRs = categoryStmt.executeQuery()) {

                System.out.println("\n--- Available Categories ---");
                int index = 1;
                ArrayList<String> categories = new ArrayList<>();

                while (categoryRs.next()) {
                    String cat = categoryRs.getString("category");
                    categories.add(cat);
                    System.out.println(index + ". " + cat);
                    index++;
                }

                System.out.println(index + ". View All Products");

                // Step 2: Ask admin to choose a category
                System.out.print("Enter your choice: ");
                int choice = scanner.nextInt();
                scanner.nextLine(); // consume newline

                String selectedCategory = null;
                boolean viewAll = false;

                if (choice >= 1 && choice <= categories.size()) {
                    selectedCategory = categories.get(choice - 1);
                } else if (choice == categories.size() + 1) {
                    viewAll = true;
                } else {
                    System.out.println("Invalid choice.");
                    return;
                }

                // Step 3: Query products
                String productSql = viewAll ?
                    "SELECT * FROM products" :
                    "SELECT * FROM products WHERE category = ?";
                
                try (PreparedStatement productStmt = connection.prepareStatement(productSql)) {
                    if (!viewAll) {
                        productStmt.setString(1, selectedCategory);
                    }

                    try (ResultSet resultSet = productStmt.executeQuery()) {
                        System.out.println("\n---- Product List ----");
                        boolean found = false;
                        while (resultSet.next()) {
                            found = true;
                            int productId = resultSet.getInt("product_id");
                            String productName = resultSet.getString("product_name");
                            double price = resultSet.getDouble("price");
                            int stock = resultSet.getInt("stock");
                            String category = resultSet.getString("category");

                            System.out.print("Product ID: " + productId);
                            System.out.print(" | Name: " + productName);
                            System.out.print(" | Price: ₹" + price);
                            System.out.print(" | Stock: " + stock);
                            System.out.println(" | Category: " + category);
                        }

                        if (!found) {
                            System.out.println("No products found.");
                        }
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private void addProduct() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Add Product:");
        System.out.print("Enter product name: ");
        String productName = scanner.nextLine();
        System.out.print("Enter product price: ");
        double price = scanner.nextDouble();
        System.out.print("Enter product stock: ");
        int stock = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter product category: ");
        String category = scanner.nextLine();
        try (Connection connection = DBConnection.getConnection()) {
            String sql = "INSERT INTO products (product_name, price, stock, category) VALUES (?, ?, ?, ? )";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, productName);
                statement.setDouble(2, price);
                statement.setInt(3, stock);
                statement.setString(4, category);
                int rowsInserted = statement.executeUpdate();
                if (rowsInserted > 0) {
                    System.out.println("Product added successfully.");
                } else {
                    System.out.println("Failed to add product.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void editProduct() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the ID of the product you want to edit: ");
        int productId = scanner.nextInt();
        scanner.nextLine(); 

        try (Connection connection = DBConnection.getConnection()){                                                        
            String sql = "SELECT * FROM products WHERE product_id = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, productId);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        System.out.print("Enter new name for the product: ");
                        String newName = scanner.nextLine();
                        System.out.print("Enter new price for the product: ");
                        double newPrice = scanner.nextDouble();
                        System.out.print("Enter new stock for the product: ");
                        int newStock = scanner.nextInt();
                        scanner.nextLine();
                        System.out.print("Enter new category for the product: ");
                        String newCategory = scanner.nextLine();
                        resultSet.close();
                         sql = "UPDATE products SET product_name = ?, price = ?, stock = ?, category = ? WHERE product_id = ?";
                        try (PreparedStatement updateStatement = connection.prepareStatement(sql)) {
                            updateStatement.setString(1, newName);
                            updateStatement.setDouble(2, newPrice);
                            updateStatement.setInt(3, newStock);
                            updateStatement.setString(4, newCategory);                       
                            updateStatement.setInt(5, productId);
                            int rowsAffected = updateStatement.executeUpdate();
                            if (rowsAffected > 0) {
                                System.out.println("Product updated successfully.");
                            } else {
                                System.out.println("Failed to update product.");
                            }
                        }
                    } else {
                        System.out.println("Product not found.");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void removeProduct() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the ID of the product you want to remove: ");
        int productId = scanner.nextInt();
        scanner.nextLine(); 

        try (Connection connection = DBConnection.getConnection()) {
            String sql = "DELETE FROM products WHERE product_id = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, productId);
                int rowsAffected = statement.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Product removed successfully.");
                } else {
                    System.out.println("Failed to remove product. Product not found.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void addStock() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the ID of the product to restock: ");
        int productId = scanner.nextInt();
        System.out.print("Enter quantity to add: ");
        int quantityToAdd = scanner.nextInt();

        try (Connection connection = DBConnection.getConnection()) {
            String checkSql = "SELECT stock FROM products WHERE product_id = ?";
            try (PreparedStatement checkStmt = connection.prepareStatement(checkSql)) {
                checkStmt.setInt(1, productId);
                try (ResultSet rs = checkStmt.executeQuery()) {
                    if (rs.next()) {
                        int currentStock = rs.getInt("stock");
                        int updatedStock = currentStock + quantityToAdd;

                        String updateSql = "UPDATE products SET stock = ? WHERE product_id = ?";
                        try (PreparedStatement updateStmt = connection.prepareStatement(updateSql)) {
                            updateStmt.setInt(1, updatedStock);
                            updateStmt.setInt(2, productId);
                            int rows = updateStmt.executeUpdate();
                            if (rows > 0) {
                                System.out.println("Stock updated successfully. New stock: " + updatedStock);
                            } else {
                                System.out.println("Failed to update stock.");
                            }
                        }
                    } else {
                        System.out.println("Product ID not found.");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
