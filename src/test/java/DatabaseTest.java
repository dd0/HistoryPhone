package uk.ac.cam.cl.historyphone;

import java.io.StringReader;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
   Tests for the Database class. The tests use an HSQL database
   defined in test/sql/testdb.sql, which is used to generate the test
   database before tests are run.
 */
public class DatabaseTest {

	private static final String path = "build/test/db/testdb";
	private static Database database;

	@BeforeClass
	public static void initDatabase() throws InitFailedException {
		
		database = new Database(path);
	}
	
	@Test public void getInfoOK() throws Exception {
		ObjectInfo obj = database.getObjectInfo("2");
		
		JsonReader reader = Json.createReader(new StringReader(obj.toJson()));
		JsonObject res = reader.readObject();
		
		assertEquals("2", res.getString("uuid"));
		assertEquals("Bot 2", res.getString("name"));
		assertEquals("Bot 2 description", res.getString("description"));
		assertEquals("bot2.png", res.getString("image"));
	}

	@Test public void getInfoNullForNonexistent() throws Exception {
		ObjectInfo obj = database.getObjectInfo("100");
		assertNull(obj);
	}

	@Test public void getResponseWithoutEntityOK() throws Exception {
		DBQuery query = new DBQuery("GetBasics");
		String res = database.getResponse(1, query);

		assertEquals("test response goes here", res);
	}

	@Test public void getResponseWithEntityOK() throws Exception {
		DBQuery query = new DBQuery("GetBasics", "e2");
		String res = database.getResponse(2, query);

		assertEquals("another test", res);
	}

	@Test public void getResponseForNonexistent() throws Exception {
		try {
			DBQuery query = new DBQuery("nonexistent");
			database.getResponse(1, query);
		} catch(LookupException e) {
			// expected
		}
	}
	
	@AfterClass
	public static void closeDatabase() {
		database.close();
	}
	
} 
