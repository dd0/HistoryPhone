package HistoryPhone;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;


public class CreateDB {
	
	public static void main(String args[]) throws SQLException, ClassNotFoundException {
		
		// Set this to the database pathname (note that it includes the actual DB name as well).
		String pathname = "/Users/AdminDK/Desktop/db/NewDB";
		
		// Load the HSQLDB driver and load/create the database with the appropriate name.
		Class.forName("org.hsqldb.jdbcDriver");
		
		// Create DB connection.
		Connection connection = DriverManager.getConnection("jdbc:hsqldb:file:"
				+ pathname,"SA","");		
		
		Statement delayStmt = connection.createStatement();
		
		try {delayStmt.execute("SET WRITE_DELAY FALSE");} //Always update data on disk
		finally {delayStmt.close();}		
		
			
		// Ensure that a transaction commit is controlled manually.
		connection.setAutoCommit(false);

		
		// Create connection statement.	
		Statement sqlStmt = connection.createStatement();
		
		// Create a table for storing information on chatbots.
		try {
			sqlStmt.execute("CREATE TABLE botInfo(UUID VARCHAR(255) NOT NULL,"+
			"name VARCHAR(30) NOT NULL, description VARCHAR(300) NOT NULL," +
			"imageURL VARCHAR(4096) NOT NULL)");
			
		} catch (SQLException e) {
			System.out.println("Warning: Database table already exists.");
		}
		
		
		// Prepare statement for adding rows.
		String stmt = "INSERT INTO BotInfo(UUID,name,description, imageURL) VALUES (?,?,?,?)";
		PreparedStatement insertMessage = connection.prepareStatement(stmt);
		
		// Add rows to the table.
		// Since only have 2 bots, can just hardcode their description entries here.
		try {
			insertMessage.setString(1, "123"); 
			insertMessage.setString(2, "Sample Bot Name 1.");
			insertMessage.setString(3, "This is the description of the first bot.");
			insertMessage.setString(4, "<filename>");
			insertMessage.executeUpdate();
			
			
			insertMessage.setString(1, "1234"); 
			insertMessage.setString(2, "Sample Bot Name 2.");
			insertMessage.setString(3, "This is the description of the second bot.");
			insertMessage.setString(4, "<filename>");
			insertMessage.executeUpdate();

		} finally { //Notice use of finally clause here to finish statement
			insertMessage.close();
		}
	
		// Commit the connection.
		connection.commit();
		
		// Close database connection.
		connection.close();
		
	}
}
