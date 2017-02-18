package HistoryPhone.src;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;


public class CreateDB {

	public static void main(String args[]) throws SQLException, ClassNotFoundException {

		// Set this to the database pathname (note that it includes the actual DB name as well).
		String pathname = "/Users/charl/Documents/CompSci/Yr 2/HistoryPhone/db/data";

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
			sqlStmt.execute("DROP TABLE botInfo");
			sqlStmt.execute("CREATE TABLE botInfo(UUID VARCHAR(255) NOT NULL,"+
			"name VARCHAR(30) NOT NULL, description VARCHAR(300) NOT NULL," +
			"imageURL VARCHAR(4096) NOT NULL)");

		} catch (SQLException e) {
			System.out.println("Warning: Database table already exists.");
		}

		//create the table for querying responses (uuid stored as strings)
		try {
			sqlStmt.execute("DROP TABLE responses");
            sqlStmt.execute("CREATE TABLE responses(uuid VARCHAR(30), key VARCHAR(255) NOT NULL, value VARCHAR(4096) NOT NULL)");
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
			System.out.println("Responses table problem");
			return;
        }


		// Prepare statement for adding rows.
		String stmt1 = "INSERT INTO BotInfo(UUID,name,description, imageURL) VALUES (?,?,?,?)";
		PreparedStatement insertMessage = connection.prepareStatement(stmt1);

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

			insertMessage.setString(1, "12345");
			insertMessage.setString(2, "Sample Bot Name 3.");
			insertMessage.setString(3, "This is the description of the second bot.");
			insertMessage.setString(4, "<filename>");
			insertMessage.executeUpdate();

		} finally { //Notice use of finally clause here to finish statement
			insertMessage.close();
		}

		String stmt2 = "INSERT INTO responses(uuid, key, value) VALUES (?,?,?)";
		PreparedStatement insertMessage1 = connection.prepareStatement(stmt2);

		try {//TODO Add all responses along with variations
			insertMessage1.setString(1,"123");
			insertMessage1.setString(2, "GetGreeting");
			insertMessage1.setString(3, "Hi There Friend");
			insertMessage1.executeUpdate();

			insertMessage1.setString(1,"123");
			insertMessage1.setString(2, "GetGreeting+Generic+1");
			insertMessage1.setString(3, "Hi There generic person");
			insertMessage1.executeUpdate();

			insertMessage1.setString(1,"123");
			insertMessage1.setString(2, "GetGreeting+Generic+2");
			insertMessage1.setString(3, "Yo waddup");
			insertMessage1.executeUpdate();
			//Create strings containing the results.

			// //How to query a result...
			// // Prepare appropriate connection statement.
			// String stmt = "SELECT value FROM responses WHERE key = " + "'GetGreeting+Generic+1'";
			//
			// // Create appropriate prepare statement.
			// PreparedStatement prepStmt = connection.prepareStatement(stmt);
			//
			// try {
			// 	// Extract matching table records.
			// 	ResultSet rs = prepStmt.executeQuery();
			//
			// 	// Store information in the declared string variables.
			// 	try {
			// 		while(rs.next()) {
			// 			System.out.println(rs.getString(1));
			// 		}
			// 	} finally {
			// 		rs.close();
			// 	}
			//
			// } finally {
			// 	prepStmt.close();
			// }

		} finally {
			insertMessage1.close();
		}
		//Commit the connection.
		connection.commit();

		// Close database connection.
		connection.close();

	}
}
