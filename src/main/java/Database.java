package uk.ac.cam.cl.historyphone;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;

public class Database {

	// Variable storing DB connection.
	private Connection connection;

	public Database(String dbName) throws InitFailedException {
		try {
			// Load the HSQLDB driver and load/create the database with the appropriate name.
			Class.forName("org.hsqldb.jdbcDriver");

			// Initialize connection with appropriate values.
			connection = DriverManager.getConnection("jdbc:hsqldb:file:"
													 + dbName,"SA","");


			Statement delayStmt = connection.createStatement();

			try {
				//Always update data on disk
				delayStmt.execute("SET WRITE_DELAY FALSE");
			} finally {
				delayStmt.close();
			}

			// Ensure that a transaction commit is controlled manually.
			connection.setAutoCommit(false);
		} catch(Exception e) {
			throw new InitFailedException("DB initialisation failed", e);
		}
	}

	public void close() {
		try {
			connection.close();
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}

	/*
	public static void main(String args[]) throws SQLException, ClassNotFoundException {

		// Load the HSQLDB driver and load/create the database with the appropriate name.
		Class.forName("org.hsqldb.jdbcDriver");

		// Initialize connection with appropriate values.
		connection = DriverManager.getConnection("jdbc:hsqldb:file:"
								+ DBName,"SA","");


		Statement delayStmt = connection.createStatement();

		try {
			//Always update data on disk
			delayStmt.execute("SET WRITE_DELAY FALSE");
		} finally {
			delayStmt.close();
		}

		// Ensure that a transaction commit is controlled manually.
		connection.setAutoCommit(false);


		// Extract object.
		ObjectInfo obj = getObjectInfo("123");


		// Check if object was found.
		if (obj != null) {

			// Print out its details.
			System.out.println(obj.toJson());

		} else {

			// Or error statement.
			System.err.println("Object not found");
		}



		// Close database connection.
		connection.close();

	}
	*/

	public String getResponse(long uuid, DBQuery dBQ) throws SQLException, LookupException{
		String stmt;
		if (dBQ.hasEntity()){//there is an entity associated with this response
			stmt = String.format("SELECT response FROM responses WHERE intent = '%s' AND entity = '%s' AND uuid = '%s' ORDER BY RAND() LIMIT 1",dBQ.getIntent(), dBQ.getEntity(), uuid);
		} else {
			stmt = String.format("SELECT response FROM responses WHERE intent = '%s' AND entity = '%s' AND uuid = '%s' ORDER BY RAND() LIMIT 1",dBQ.getIntent(), "NONE", uuid);
		}

			// Create appropriate prepare statement.
			PreparedStatement prepStmt = connection.prepareStatement(stmt);

		try {
			// Extract matching table records.
			ResultSet rs = prepStmt.executeQuery();

			// Store information in the declared string variables.
			try {//return random entry if there are multiple
				if(rs.next()) {
					return rs.getString(1);
				} else {
					throw new LookupException("Bot Doesn't Exist");
				}
			} finally {
				rs.close();
			}

		} catch (SQLException s) {
			System.out.println("SQL Error");
			s.printStackTrace();
		} finally {
			prepStmt.close();
		}
		return null;

	}

	public ObjectInfo getObjectInfo(String UUID) throws SQLException {

		// Check if UUID sent is null.
		if ((UUID == null) || (UUID == "")) {
			System.err.println("UUID must not be empty.");
			return null;
		}

		// Create strings containing the results.
		String name = null;
		String desc = null;
		String img_url = null;

		// Prepare appropriate connection statement.
		String stmt = "SELECT name, description, imageURL FROM botInfo WHERE UUID = " + UUID;

		// Create appropriate prepare statement.
		PreparedStatement prepStmt = connection.prepareStatement(stmt);


		try {

			// Extract matching table records.
			ResultSet rs = prepStmt.executeQuery();

			// Store information in the declared string variables.
			try {
				while(rs.next()) {
					name = rs.getString(1);
					desc = rs.getString(2);
					img_url = rs.getString(3);
				}
			} finally {
				rs.close();
			}

		} finally {
			prepStmt.close();
		}


		// Check if all information was found.
		if ((name != null) && (desc != null) && (img_url != null)) {

			// Insert all information into an ObjectInfo object.
			ObjectInfo obj = new ObjectInfo(UUID, name, desc, img_url);
			return obj;

		} else {

			// If information was not found, return null.
			return null;
		}
	}

}
