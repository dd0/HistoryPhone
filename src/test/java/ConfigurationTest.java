package uk.ac.cam.cl.historyphone;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.json.Json;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import static org.junit.Assert.*;

public class ConfigurationTest {

	@Rule public TemporaryFolder tempFolder = new TemporaryFolder();

	private String createConfig(String data) throws IOException {
		File f = tempFolder.newFile("test.json");

		FileWriter writer = new FileWriter(f);
		writer.write(data);
		writer.close();
		
		return f.getPath();
	}
	
	@Test public void configurationValuesOK() {
		try {
			String data = Json.createObjectBuilder()
				.add("nlpApplicationID", "nlp-id-123")
				.add("nlpSubscriptionKey", "nlp-key-321")
				.add("imageFolder", "img/")
				.add("databasePath", "db/")
				.add("host", "hostname")
				.add("port", 1010).build().toString();
			String config = createConfig(data);
				
			Configuration res = new Configuration(config);
			assertEquals("nlp-id-123", res.getAppID());
			assertEquals("nlp-key-321", res.getSubscriptionKey());
			assertEquals("img/", res.getImageFolder());
			assertEquals("db/", res.getDatabasePath());
			assertEquals("hostname", res.getHost());
			assertEquals(1010, res.getPort());
			
		} catch(Exception e) {
			fail("Exception: " + e.toString());
		}
	}

	@Test public void configurationDefaultsOK() {
		try {
			String data = Json.createObjectBuilder()
				.add("nlpApplicationID", "nlp-id-123")
				.add("nlpSubscriptionKey", "nlp-key-321")
				.build().toString();
			String config = createConfig(data);
				
			Configuration res = new Configuration(config);
			assertEquals("images/", res.getImageFolder());
			assertEquals("db/data", res.getDatabasePath());
			assertEquals("localhost", res.getHost());
			assertEquals(12345, res.getPort());
			
		} catch(Exception e) {
			fail("Exception: " + e.toString());
		}
	}

	@Test public void configurationFailNoValue() {
		try {
			String data = Json.createObjectBuilder()
				.add("nlpApplicationID", "nlp-id-123")
				.build().toString();
			String config = createConfig(data);
				
			Configuration res = new Configuration(config);
			fail("Expected InitFailedException");
		} catch(InitFailedException e) {
			// this should happen
		} catch(Exception e) {
			fail("Exception: " + e.toString());
		}
	}

	@Test public void configurationFailNonexistentFile() {
		try {
			Configuration res = new Configuration("somenonexistentfile123123");
			fail("Expected InitFailedException");
		} catch(InitFailedException e) {
			// this should happen
		} catch(Exception e) {
			fail("Exception: " + e.toString());
		}
	}
	
}
