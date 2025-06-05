//package onlineshoppingmanagement;
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.time.LocalDateTime;
//import com.mysql.cj.xdevapi.Statement;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Scanner;
//public class Customer {
//    private List<Product> cart;
//    private Connection connection;
//
//    public Customer() {
//        this.cart = new ArrayList<>();
//        try {
//            this.connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/onlineShoppingManagement", "root", "subash@2003");
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void performCustomerProcesses() throws Exception {
//        Scanner scanner = new Scanner(System.in);
//        boolean shopping = true;
//        while (shopping) {
//            System.out.println("1. Display Products");
//            System.out.println("2. Add to Cart");
//            System.out.println("3. View Cart");
//            System.out.println("4. Place Order");
//            System.out.println("5. View Order");            
//            System.out.println("6. Exit");
//            System.out.print("Enter your choice: ");
//            int choice = scanner.nextInt();
//            if(choice==1) {
//            	displayProducts();
//            }
//            else if(choice==2) {
//            	  System.out.print("Enter product ID: ");
//                  int productId = scanner.nextInt();
//                  System.out.print("Enter quantity: ");
//                  int quantity = scanner.nextInt();
//                  System.out.println("Enter username: ");
//                  String username=scanner.next();
//                  addToCart(productId, quantity,username);
//            }
//            else if(choice==3)
//            	viewCart();
//            else if(choice==4)
//            	placeOrder();
//            else if(choice==5) {
//            	String username=null;
//            	viewOrder(username);
//            }
//            else if(choice==6) {
//            	Main main=new Main();
//            main.log();
//        }
//            else
//            	System.out.println("Invalid");	
//        }
//       scanner.close();
//    }
//    public void displayProducts() {
//        try {
//            String sql = "SELECT * FROM products";
//            PreparedStatement statement = connection.prepareStatement(sql);
//            ResultSet resultSet = statement.executeQuery();
//            System.out.println("Available Products:");
//            while (resultSet.next()) {
//                int productId = resultSet.getInt("product_id");
//                String productName = resultSet.getString("product_name");
//                double price = resultSet.getDouble("price");
//                int stock = resultSet.getInt("stock");
//                String category = resultSet.getString("category");
//                System.out.print("Product ID: " + productId);
//                System.out.print(" Name: " + productName);
//                System.out.print(" Price: $" + price);
//                System.out.print(" Stock: " + stock);
//                System.out.println(" Category: " + category);
//                System.out.println("-----------------------------------------------------------------------------------------");
//            }
//            resultSet.close();
//            statement.close();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void addToCart(int productId, int quantity,String username) {
//        try {
//            String sql = "SELECT * FROM products WHERE product_id = ?";
//            PreparedStatement statement = connection.prepareStatement(sql);
//            statement.setInt(1, productId);
//            ResultSet resultSet = statement.executeQuery();
//            if (resultSet.next()) {
//                String productName = resultSet.getString("product_name");
//                double price = resultSet.getDouble("price");
//                int stock = resultSet.getInt("stock");
//
//                if (quantity > stock) {
//                    System.out.println("Sorry, the requested quantity exceeds the available stock for " + productName + ". Available stock: " + stock);
//                } else {
//                    Product product = new Product(productId, productName, price, quantity);
//                    cart.add(product);
//                    
//                    String insertOrderItemSql = "INSERT INTO order_items (product_id, quantity,username) VALUES (?, ?, ?)";
//                    PreparedStatement insertStatement = connection.prepareStatement(insertOrderItemSql);
//                    insertStatement.setInt(1, productId);
//                    insertStatement.setInt(2, quantity);
//                    insertStatement.setString(3, username);
//                    insertStatement.executeUpdate();
//
//                    String updateStockSql = "UPDATE products SET stock = stock - ? WHERE product_id = ?";
//                    PreparedStatement updateStatement = connection.prepareStatement(updateStockSql);
//                    updateStatement.setInt(1, quantity);
//                    updateStatement.setInt(2, productId);
//                    updateStatement.executeUpdate();
//
//                    System.out.println("Added to cart: " + product);
//                }
//            } else {
//                System.out.println("Product not found.");
//            }
//            resultSet.close();
//            statement.close();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void viewCart() {
//        if (cart.isEmpty()) {
//            System.out.println("Your cart is empty.");
//        } else {
//            System.out.println("Your Cart:");
//            for (Product item : cart) {
//                System.out.println(item);
//            }
//        }
//    }
//        public void placeOrder()throws Exception {
//            if (cart.isEmpty()) {
//                System.out.println("Your cart is empty. Unable to place order.");
//                return;
//            }Scanner scanner = new Scanner(System.in);
//                System.out.print("Enter your username: ");
//                String username=scanner.nextLine();
//                System.out.print("Enter your shipping address: ");
//                String shippingAddress = scanner.nextLine();
//                System.out.print("Enter your phone number: ");
//               String phonenumber=scanner.nextLine();
//               
//            double totalAmount = calculateTotalAmount();
//            generateBill(username,shippingAddress,phonenumber,totalAmount);
//            try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/onlineShoppingManagement", "root", "nithinithish18@")) {
//                String insertOrderSql = "INSERT INTO orders ( username, shipping_address, phone_number, total_amount) VALUES ( ?, ?, ?, ?)";
//                try (PreparedStatement orderStatement = connection.prepareStatement(insertOrderSql)) {
//                    orderStatement.setString(1, username);
//                    orderStatement.setString(2, shippingAddress);
//                    orderStatement.setString(3, phonenumber);
//                    orderStatement.setDouble(4, totalAmount);
//                    int rowsAffected = orderStatement.executeUpdate();
//                    if (rowsAffected > 0) {
//                        System.out.println("Order placed successfully!");
//                        cart.clear();
//                    } else {
//                        System.out.println("Failed to place order.");
//                    }
//                }
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//        }
//        public double calculateTotalAmount() {
//            double totalAmount = 0;
//            for (Product product : cart) {
//                totalAmount += product.getPrice() * product.getQuantity();
//            }
//            return totalAmount;
//        }
//            public void generateBill(String username,String shippingAddress,String phonenumber,double totalAmount) {
//            	System.out.println("-------- Bill --------");
//            	System.out.println("Customer Name: "+username);
//            	System.out.println("Shipping Address: "+shippingAddress);
//            	System.out.println("Phone number: "+phonenumber);
//                System.out.println("Order Date: " + LocalDateTime.now());
//                System.out.println("Total Amount: $" + totalAmount);
//            System.out.println("----------------------");
//        cart.clear();
//        }
//            
//            public void viewOrder(String username) throws Exception {
//                try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/onlineShoppingManagement", "root", "nithinithish18@")) {
//                    Scanner scanner = new Scanner(System.in);
//                    System.out.print("Enter username: ");
//                    username = scanner.nextLine();
//                    String selectOrdersSql = "SELECT orders.order_id, orders.username, orders.shipping_address, orders.phone_number, orders.total_amount, order_items.product_id, products.product_name " +
//                            "FROM orders " +
//                            "JOIN order_items ON orders.username = order_items.username " +
//                            "JOIN products ON order_items.product_id = products.product_id " +
//                            "WHERE orders.username = ?";
//                    try (PreparedStatement statement = connection.prepareStatement(selectOrdersSql)) {
//                        statement.setString(1, username);
//                        ResultSet resultSet = statement.executeQuery();
//                        boolean orderFound = false;
//                        while (resultSet.next()) {
//                            if (!orderFound) {
//                                int orderid = resultSet.getInt("order_id");
//                                username = resultSet.getString("username");
//                                String shippingAddress = resultSet.getString("shipping_address");
//                                String phoneNumber = resultSet.getString("phone_number");
//                                double totalAmount = resultSet.getDouble("total_amount");
//                                System.out.println("Order ID: " + orderid);
//                                System.out.println("Customer Name: " + username);
//                                System.out.println("Shipping Address: " + shippingAddress);
//                                System.out.println("Phone Number: " + phoneNumber);
//                                System.out.println("Order Date: " + LocalDateTime.now());
//                                System.out.println("Total Amount: $" + totalAmount);
//                                System.out.print("Ordered Product: ");
//                                orderFound = true;
//                            }
//                            int productId = resultSet.getInt("product_id");
//                            String productName = resultSet.getString("product_name");
//                            System.out.println(productName);
//                            System.out.println("Product ID: "+productId);
//                    }
//                        if (!orderFound) {
//                            System.out.println("No orders found for username: " + username);
//                        }
//                    }
//                } catch (SQLException e) {
//                    e.printStackTrace();
//                }
//            }
//          
//    public static void main(String[] args) throws Exception {
//        Customer customer = new Customer();
//        customer.performCustomerProcesses();
//    }
//}
//
//class Product {
//    private int id;
//    private String name;
//    private double price;
//    private int quantity;
//
//    public Product(int id, String name, double price, int quantity) {
//        this.id = id;
//        this.name = name;
//        this.price = price;
//        this.quantity = quantity;
//    }
//
//    public int getId() {
//        return id;
//    }
//
//    public double getPrice() {
//        return price;
//    }
//
//    public int getQuantity() {
//        return quantity;
//    }
//
//    @Override
//    public String toString() {
//        return "ID: " + id + ", Name: " + name + ", Price: $" + price + ", Quantity: " + quantity;
//    }
//}
//
//
package onlineshoppingmanagement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Scanner;
import java.time.LocalDate;
import java.sql.Statement;

public class Customer {
    private List<Product> cart;
    private Connection connection;

    public Customer() {
        this.cart = new ArrayList<>();
        try {
            // Use consistent credentials here
            this.connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/onlineShoppingManagement", "root", "Subash@2003");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private String username;
    public void performCustomerProcesses() throws Exception {
        Scanner scanner = new Scanner(System.in);

        // Ask username once at the beginning
        System.out.print("Enter your username: ");
        this.username = scanner.nextLine();  // Store it once

        boolean shopping = true;
        while (shopping) {
            System.out.println();
            System.out.println("1. Display Products");
            System.out.println("2. Add to Cart");
            System.out.println("3. View Cart");
            System.out.println("4. Place Order");
            System.out.println("5. View Invoice");
            System.out.println("6. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (choice) {
                case 1:
                    displayProducts();
                    break;
                case 2:
                    System.out.print("Enter product ID: ");
                    int productId = scanner.nextInt();
                    System.out.print("Enter quantity: ");
                    int quantity = scanner.nextInt();
                    addToCart(productId, quantity);
                    break;
                case 3:
                    viewCart();
                    break;
                case 4:
                    placeOrder();
                    break;
                case 5:
                    viewInvoice(this.username);
                    break;
                case 6:
                    shopping = false;
                    break;
                default:
                    System.out.println("Invalid choice");
            }
        }

        scanner.close();
    }

    public void displayProducts() {
        try {
            // Step 1: Get all distinct categories
            String categoryQuery = "SELECT DISTINCT category FROM products";
            List<String> categories = new ArrayList<>();
            try (PreparedStatement statement = connection.prepareStatement(categoryQuery);
                 ResultSet rs = statement.executeQuery()) {
                System.out.println("\nAvailable Categories:");
                int index = 1;
                while (rs.next()) {
                    String category = rs.getString("category");
                    categories.add(category);
                    System.out.println(index + ". " + category);
                    index++;
                }
            }

            if (categories.isEmpty()) {
                System.out.println("No categories available.");
                return;
            }

            // Step 2: Let user pick a category
            Scanner scanner = new Scanner(System.in);
            System.out.print("Choose a category number to view products: ");
            int categoryChoice = scanner.nextInt();
            if (categoryChoice < 1 || categoryChoice > categories.size()) {
                System.out.println("Invalid category selection.");
                return;
            }
            String selectedCategory = categories.get(categoryChoice - 1);

            // Step 3: Show products in that category
            String productQuery = "SELECT * FROM products WHERE category = ?";
            try (PreparedStatement statement = connection.prepareStatement(productQuery)) {
                statement.setString(1, selectedCategory);
                ResultSet resultSet = statement.executeQuery();

                System.out.println("\nProducts in Category: " + selectedCategory);
                boolean found = false;
                while (resultSet.next()) {
                    found = true;
                    int productId = resultSet.getInt("product_id");
                    String productName = resultSet.getString("product_name");
                    double price = resultSet.getDouble("price");
                    int stock = resultSet.getInt("stock");
                    System.out.print("Product ID: " + productId);
                    System.out.print(" Name: " + productName);
                    System.out.print(" Price: rs" + price);
                    System.out.print(" Stock: " + stock);
                    System.out.println(" Category: " + selectedCategory);
                    System.out.println("-----------------------------------------------------------------------------------------");
                }

                if (!found) {
                    System.out.println("No products found in this category.");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addToCart(int productId, int quantity) {
        String sql = "SELECT * FROM products WHERE product_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, productId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String productName = resultSet.getString("product_name");
                double price = resultSet.getDouble("price");
                int stock = resultSet.getInt("stock");

                if (quantity > stock) {
                    System.out.println("Sorry, the requested quantity exceeds the available stock for " + productName + ". Available stock: " + stock);
                } else {
                    Product product = new Product(productId, productName, price, quantity);
                    cart.add(product);

                    String insertOrderItemSql = "INSERT INTO order_items (product_id, quantity, username) VALUES (?, ?, ?)";
                    try (PreparedStatement insertStatement = connection.prepareStatement(insertOrderItemSql)) {
                        insertStatement.setInt(1, productId);
                        insertStatement.setInt(2, quantity);
                        insertStatement.setString(3, this.username);
                        insertStatement.executeUpdate();
                    }

                    String updateStockSql = "UPDATE products SET stock = stock - ? WHERE product_id = ?";
                    try (PreparedStatement updateStatement = connection.prepareStatement(updateStockSql)) {
                        updateStatement.setInt(1, quantity);
                        updateStatement.setInt(2, productId);
                        updateStatement.executeUpdate();
                    }

                    System.out.println("Added to cart: " + product);
                }
            } else {
                System.out.println("Product not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void viewCart() {
        if (cart.isEmpty()) {
            System.out.println("Your cart is empty.");
        } else {
            System.out.println("Your Cart:");
            for (Product item : cart) {
                System.out.println(item);
            }
        }
    }

    
    public void placeOrder() throws Exception {
        if (cart.isEmpty()) {
            System.out.println("Your cart is empty. Unable to place order.");
            return;
        }

        Scanner scanner = new Scanner(System.in);
        String username=this.username;
        System.out.print("Enter your shipping address: ");
        String shippingAddress = scanner.nextLine();
        System.out.print("Enter your phone number: ");
        String phoneNumber = scanner.nextLine();

        double totalAmount = 0;
        for (Product product : cart) {
            totalAmount += product.getPrice() * product.getQuantity();
        }

        String insertOrderSql = "INSERT INTO orders (username, shipping_address, phone_number, order_date, total_amount) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement orderStatement = connection.prepareStatement(insertOrderSql, Statement.RETURN_GENERATED_KEYS)) {
            orderStatement.setString(1, username);
            orderStatement.setString(2, shippingAddress);
            orderStatement.setString(3, phoneNumber);
            orderStatement.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));
            orderStatement.setDouble(5, totalAmount);
            orderStatement.executeUpdate();

            ResultSet generatedKeys = orderStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                int orderId = generatedKeys.getInt(1);

                String insertItemSql = "INSERT INTO order_items (order_id, product_id, quantity, username) VALUES (?, ?, ?, ?)";
                try (PreparedStatement itemStatement = connection.prepareStatement(insertItemSql)) {
                    for (Product product : cart) {
                        itemStatement.setInt(1, orderId);
                        itemStatement.setInt(2, product.getId());
                        itemStatement.setInt(3, product.getQuantity());
                        itemStatement.setString(4, username);
                        itemStatement.addBatch();
                    }
                    itemStatement.executeBatch();
                }

                cart.clear();  // empty cart after order placed
                System.out.println("Order placed successfully!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public double calculateTotalAmount() {
        double totalAmount = 0;
        for (Product product : cart) {
            totalAmount += product.getPrice() * product.getQuantity();
        }
        return totalAmount;
    }

    public void generateBill(String username, String shippingAddress, String phoneNumber, double totalAmount) {
        System.out.println("-------- Bill --------");
        System.out.println("Customer Name: " + username);
        System.out.println("Shipping Address: " + shippingAddress);
        System.out.println("Phone number: " + phoneNumber);
        System.out.println("Order Date: " + LocalDateTime.now().toString().replace("T", " Time "));
        System.out.println("\nItems Purchased:");
        System.out.println("--------------------------------------------------");
        System.out.printf("%-10s %-20s %-10s %-10s%n", "ID", "Product Name", "Price", "Quantity");
        System.out.println("--------------------------------------------------");

        for (Product product : cart) {
            System.out.printf("%-10d %-20s $%-9.2f %-10d%n",
                    product.getId(), product.toString().split(",")[1].split(":")[1].trim(), product.getPrice(), product.getQuantity());
        }

        System.out.println("--------------------------------------------------");
        System.out.printf("Total Amount: $%.2f%n", totalAmount);
        System.out.println("----------------------");
    }
    
    public void viewInvoice(String username) throws Exception {
        Scanner scanner = new Scanner(System.in);
        System.out.println("1. View Recent Invoice");
        System.out.println("2. View All Invoices");
        System.out.print("Enter your choice: ");
        int choice = scanner.nextInt();

        String sql;
        int recentOrderId = -1;

        // Step 1: If user chooses recent invoice, find the latest order_id
        if (choice == 1) {
            String getLatestOrderIdSql = "SELECT order_id FROM orders WHERE username = ? ORDER BY order_date DESC LIMIT 1";
            try (PreparedStatement stmt = connection.prepareStatement(getLatestOrderIdSql)) {
                stmt.setString(1, username);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    recentOrderId = rs.getInt("order_id");
                } else {
                    System.out.println("No recent invoice found.");
                    return;
                }
            }
            // Now fetch products for this recent order_id
            sql = "SELECT * FROM orders " +
                  "JOIN order_items ON orders.order_id = order_items.order_id " +
                  "JOIN products ON order_items.product_id = products.product_id " +
                  "WHERE orders.username = ? AND orders.order_id = ?";
        } else {
            // All invoices
            sql = "SELECT * FROM orders " +
                  "JOIN order_items ON orders.order_id = order_items.order_id " +
                  "JOIN products ON order_items.product_id = products.product_id " +
                  "WHERE orders.username = ? " +
                  "ORDER BY orders.order_date DESC";
        }

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            if (choice == 1) {
                statement.setInt(2, recentOrderId);  // bind order_id only for recent
            }

            ResultSet rs = statement.executeQuery();

            Map<Integer, List<Product>> orderProductsMap = new LinkedHashMap<>();
            Map<Integer, String[]> orderDetailsMap = new LinkedHashMap<>();

            while (rs.next()) {
                int orderId = rs.getInt("order_id");
                Product product = new Product(
                    rs.getInt("product_id"),
                    rs.getString("product_name"),
                    rs.getDouble("price"),
                    rs.getInt("quantity")
                );

                orderProductsMap.computeIfAbsent(orderId, k -> new ArrayList<>()).add(product);
                orderDetailsMap.putIfAbsent(orderId, new String[]{
                    rs.getString("username"),
                    rs.getString("shipping_address"),
                    rs.getString("phone_number"),
                    rs.getTimestamp("order_date").toString(),
                    String.format("%.2f", rs.getDouble("total_amount"))
                });
            }

            int invoiceCount = 1;
            for (Integer orderId : orderProductsMap.keySet()) {
                String[] details = orderDetailsMap.get(orderId);
                System.out.println("\n-------- Invoice --------");
                System.out.println("Invoice Number: INV" + String.format("%05d", invoiceCount++));
                System.out.println("Customer Name  : " + details[0]);
                System.out.println("Shipping Address: " + details[1]);
                System.out.println("Phone Number    : " + details[2]);
                System.out.println("Order Date      : " + details[3].replace("T", " Time "));

                System.out.println("\nItems Purchased:");
                System.out.println("--------------------------------------------------");
                System.out.printf("%-10s %-20s %-10s %-10s%n", "ID", "Product Name", "Price", "Quantity");
                System.out.println("--------------------------------------------------");

                for (Product p : orderProductsMap.get(orderId)) {
                    System.out.printf("%-10d %-20s $%-9.2f %-10d%n", p.getId(), p.getName(), p.getPrice(), p.getQuantity());
                }

                System.out.println("--------------------------------------------------");
                System.out.printf("Total Amount: $%s%n", details[4]);
                System.out.println("----------------------");
            }

            if (orderProductsMap.isEmpty()) {
                System.out.println("No invoices found for user: " + username);
            }
        }
    }

			    private void printInvoice(String username, String shippingAddress, String phoneNumber,
			            LocalDateTime orderDate, List<Product> products, double totalAmount) {
			
			System.out.println("-------- Invoice --------");
			System.out.println("Customer Name   : " + username);
			System.out.println("Shipping Address: " + shippingAddress);
			System.out.println("Phone Number    : " + phoneNumber);
			System.out.println("Order Date      : " + orderDate.toString().replace("T", " Time "));
			System.out.println("\nItems Purchased:");
			System.out.println("------------------------------------------------------------");
			System.out.printf("%-10s %-20s %-10s %-10s%n", "ID", "Product Name", "Price", "Quantity");
			System.out.println("------------------------------------------------------------");
			
			for (Product product : products) {
			System.out.printf("%-10d %-20s $%-9.2f %-10d%n",
			  product.getId(), product.toString().split(",")[1].split(":")[1].trim(), product.getPrice(), product.getQuantity());
			}
			
			System.out.println("------------------------------------------------------------");
			System.out.printf("Total Amount: $%.2f%n", totalAmount);
			System.out.println("------------------------------------------------------------\n");
			}
			


    public static void main(String[] args) throws Exception {
        Customer customer = new Customer();
        customer.performCustomerProcesses();
    }
}

class Product {
    private int id;
    private String name;
    private double price;
    private int quantity;

    public Product(int id, String name, double price, int quantity) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }


	public int getId() {
        return id;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    @Override
    public String toString() {
        return "ID: " + id + ", Name: " + name + ", Price: rs" + price + ", Quantity: " + quantity;
    }
}

