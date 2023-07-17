package Database;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DomMebeli {
	private static final String LOGIN;
	private static final String PASSWORD;
	private static final String URL;
	
	public static Connection conn;
	public static PreparedStatement statement;
	public static ResultSet resSet;
	public static PrintWriter writer;
	
	static {
		LOGIN = "root";
		PASSWORD = "root";
		URL = "jdbc:mysql://localhost:3306/dom_mebeli";
	}
	
	public static Connection connect() throws Exception {
		Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();
		return DriverManager.getConnection(URL, LOGIN, PASSWORD);
	}
}