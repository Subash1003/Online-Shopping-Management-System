
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
