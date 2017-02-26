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
		//for responses which represent intents without entities, "NONE" is the string in the 'entity' column
		try {
			sqlStmt.execute("DROP TABLE responses");
            sqlStmt.execute("CREATE TABLE responses(uuid VARCHAR(30), intent VARCHAR(255) NOT NULL, entity VARCHAR(255) NOT NULL, response VARCHAR(5000) NOT NULL)");
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

		String stmt2 = "INSERT INTO responses(uuid, intent, entity, response) VALUES (?,?,?,?)";
		PreparedStatement insertMessage1 = connection.prepareStatement(stmt2);

		try {//TODO Add all responses along with variations
			insertMessage1.setString(1,"123");
			insertMessage1.setString(2, "init");
			insertMessage1.setString(3, "NONE");
			insertMessage1.setString(4, "Hello! I am a BBC Micro - you can ask me what I am, who made me, when they made me etc etc.\n It's up to you really!");
			insertMessage1.executeUpdate();

			insertMessage1.setString(1,"123");
			insertMessage1.setString(2, "GetGreeting");
			insertMessage1.setString(3, "NONE");
			insertMessage1.setString(4, "Hello!");
			insertMessage1.executeUpdate();

			insertMessage1.setString(1,"123");
			insertMessage1.setString(2, "GetGreeting");
			insertMessage1.setString(3, "Generic");
			insertMessage1.setString(4, "Greetings.");
			insertMessage1.executeUpdate();

			insertMessage1.setString(1,"123");
			insertMessage1.setString(2, "GetGreeting");
			insertMessage1.setString(3, "Generic");
			insertMessage1.setString(4, "I salute thee, oh anonymous carbon-based, sentient life form.");
			insertMessage1.executeUpdate();

			insertMessage1.setString(1,"123");
			insertMessage1.setString(2, "GetGreeting");
			insertMessage1.setString(3, "Generic");
			insertMessage1.setString(4, "Yo waddup");
			insertMessage1.executeUpdate();

			insertMessage1.setString(1,"123");
			insertMessage1.setString(2, "GetGreeting");
			insertMessage1.setString(3, "HowDoYouDo");
			insertMessage1.setString(4, "I have been locked in a box for decades, detached from the outside world. I am thus doing quite well.");
			insertMessage1.executeUpdate();

			insertMessage1.setString(1,"123");
			insertMessage1.setString(2, "GetGreeting");
			insertMessage1.setString(3, "HowDoYouDo");
			insertMessage1.setString(4, "I have a 2MHz CPU clock and 128KiB of main memory. Feeling pretty ancient.");
			insertMessage1.executeUpdate();

			insertMessage1.setString(1,"123");
			insertMessage1.setString(2, "GetCreationTime");
			insertMessage1.setString(3, "NONE");
			insertMessage1.setString(4, "I was first released on the 1st of December, 1981. I am thus just over 35 years old.");
			insertMessage1.executeUpdate();

			insertMessage1.setString(1,"123");
			insertMessage1.setString(2, "GetSummary");
			insertMessage1.setString(3, "NONE");
			insertMessage1.setString(4, "I am BBC Micro, a home computer created by Acorn Computers Ltd for BBC.\nYou are only seeing my core components. You can connect a display and peripheral storage devices to me as well.\nI was built for educational purposes.\nI am notable for my ruggedness, expandability and the quality of my operating system.\nAs you can see, I also recently became a chatbot (I guess you can teach an old computer new tricks). ");
			insertMessage1.executeUpdate();

			insertMessage1.setString(1,"123");
			insertMessage1.setString(2, "GetGoal");
			insertMessage1.setString(3, "NONE");
			insertMessage1.setString(4, "I was designed for educational purposes.\nIn 1982 BBC launched the “BBC Computer Literacy Project”, with an aim of educating people in the world of computing.\nBesides having a television series and a book associated with the project, BBC wanted a microcomputer to be used, to provide a direct experience.\nAcorn Computers won the contract for being the provider of such a microcomputer.\nAfter winning, these guys then went on to create me in order to fulfill their contract.");
			insertMessage1.executeUpdate();

			insertMessage1.setString(1,"123");
			insertMessage1.setString(2, "GetPurchase");
			insertMessage1.setString(3, "NONE");
			insertMessage1.setString(4, "Back in the day, I used to be worth £300-£400!\nI was so popular that they had to increase my price immediately after I was put on sale.\nNowadays, I'm sold for £100-£200 but you will only find me on eBay.");
			insertMessage1.executeUpdate();

			insertMessage1.setString(1,"123");
			insertMessage1.setString(2, "GetCreator");
			insertMessage1.setString(3, "NONE");
			insertMessage1.setString(4, "I was created by 'Acorn Computers' which is sometimes referred to as the British Apple... well, according to the Acorn Engineers anyway!");
			insertMessage1.executeUpdate();

			insertMessage1.setString(1,"123");
			insertMessage1.setString(2, "GetCreator");
			insertMessage1.setString(3, "Team (others)");
			insertMessage1.setString(4, "I was created by the Acorn team - namely Sophie Wilson and Steve Furber. As motivation, they were both told that the other had agreed that they could prototype me in a week. Surprisingly this trick paid off!");
			insertMessage1.executeUpdate();

			insertMessage1.setString(1,"123");
			insertMessage1.setString(2, "GetCreator");
			insertMessage1.setString(3, "Steve Furber");
			insertMessage1.setString(4, "My Creator - Steve Furber, who now works at the University of Manchester - is well known for making me, and developing the ARM 32-bit RISC microprocessor.\nHe studied Maths at St. John's College Cambridge. I know right, what a loser!");
			insertMessage1.executeUpdate();

			insertMessage1.setString(1,"123");
			insertMessage1.setString(2, "GetCreator");
			insertMessage1.setString(3, "Sophie Wilson");
			insertMessage1.setString(4, "My Creator - Sophie Wilson - decided to join Acorn Computers after designing a device which stopped people using cigarette lighters to win fruit machines.\nNowadays, she is a fellow at Selwyn College Cambridge.");
			insertMessage1.executeUpdate();

			insertMessage1.setString(1,"123");
			insertMessage1.setString(2, "None");
			insertMessage1.setString(3, "NONE");
			insertMessage1.setString(4, "I was born in the 80's and therefore I'm not very clever - please can you rephrase the question.");
			insertMessage1.executeUpdate();

			insertMessage1.setString(1,"123");
			insertMessage1.setString(2, "None");
			insertMessage1.setString(3, "NONE");
			insertMessage1.setString(4, "Despite my vast capabilities, I couldn't understand your question - please could you rephrase it?");
			insertMessage1.executeUpdate();

			insertMessage1.setString(1,"123");
			insertMessage1.setString(2, "None");
			insertMessage1.setString(3, "NONE");
			insertMessage1.setString(4, "Can you rephrase please? It's been a long day and I'm very tired.");
			insertMessage1.executeUpdate();

			insertMessage1.setString(1,"123");
			insertMessage1.setString(2, "GetFeature");
			insertMessage1.setString(3, "CPU");
			insertMessage1.setString(4, "I have an 8-bit 2MHz MOS 6502 Processor.");
			insertMessage1.executeUpdate();

			insertMessage1.setString(1,"123");
			insertMessage1.setString(2, "GetFeature");
			insertMessage1.setString(3, "Storage");
			insertMessage1.setString(4, "I am using a DFC (Disk File System) that is 800KB in size.");
			insertMessage1.executeUpdate();

			insertMessage1.setString(1,"123");
			insertMessage1.setString(2, "GetFeature");
			insertMessage1.setString(3, "Display");
			insertMessage1.setString(4, "A display is not part of my core components.\nYou can, however, connect an RGB monitor through a port on my back.");
			insertMessage1.executeUpdate();

			insertMessage1.setString(1,"123");
			insertMessage1.setString(2, "GetFeature");
			insertMessage1.setString(3, "OS");
			insertMessage1.setString(4, "I am running the Acorn MOS (Machine Operating System).\nVery old and outdated. Still beats Vista though.");
			insertMessage1.executeUpdate();

			insertMessage1.setString(1,"123");
			insertMessage1.setString(2, "GetFeature");
			insertMessage1.setString(3, "RAM/ROM");
			insertMessage1.setString(4, "I have 128kiB of RAM and 128KB of ROM.");
			insertMessage1.executeUpdate();

			insertMessage1.setString(1,"123");
			insertMessage1.setString(2, "GetFeature");
			insertMessage1.setString(3, "Keyboard");
			insertMessage1.setString(4, "The top 10 red function keys are capable of generating semi-graphics and could be programmed with keyboard macros (to print whatever the used wants).\nOther keys carry out usual functionality.");
			insertMessage1.executeUpdate();

			insertMessage1.setString(1,"123");
			insertMessage1.setString(2, "GetFeature");
			insertMessage1.setString(3, "Sound");
			insertMessage1.setString(4, "I have an internal speaker.");
			insertMessage1.executeUpdate();

			insertMessage1.setString(1,"123");
			insertMessage1.setString(2, "GetSuccess");
			insertMessage1.setString(3, "NONE");
			insertMessage1.setString(4, "Success");
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
