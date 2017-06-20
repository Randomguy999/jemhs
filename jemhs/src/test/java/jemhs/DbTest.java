package jemhs;

import java.sql.Connection;
import java.sql.DriverManager;

public class DbTest {

	public static void main(String[] args) {

		String jdbcUrl = "jdbc:mysql://localhost:3306/jemhs?useSSL=false";
		String user = "jemhs";
		String pass = "jemhs";
		
		try {
			System.out.println("Connecting to database: " + jdbcUrl);
			
			Connection myConn =
					DriverManager.getConnection(jdbcUrl, user, pass);
			
			System.out.println("Connection successful!!!");
			
		}
		catch (Exception exc) {
			exc.printStackTrace();
		}
		
	}

}