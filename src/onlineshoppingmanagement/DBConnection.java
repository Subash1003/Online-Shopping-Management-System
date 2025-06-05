//package onlineshoppingmanagement;
//
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.SQLException;
//
//public class DBConnection {
//    private static final String DB_URL = "jdbc:mysql://localhost:3306/onlineshoppingmanagement";
//    private static final String USER = "root";
//    private static final String PASSWORD = "Subash@2003"; // Replace with your MySQL password
//
//    public static Connection getConnection() throws SQLException {
//        return DriverManager.getConnection(DB_URL, USER, PASSWORD);
//    }
//}


package onlineshoppingmanagement;
import java.sql.*;

public class DBConnection {
	static final String url="jdbc:mysql://localhost:3306/onlineShoppingManagement";
    static final String user="root";
	static final String password="Subash@2003";
	
	public static Connection getConnection() throws SQLException{
		
		return DriverManager.getConnection(url,user,password);
	}
}