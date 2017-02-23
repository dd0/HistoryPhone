package uk.ac.cam.cl.historyphone;

import java.io.FileInputStream;
import java.io.IOException;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonNumber;
import javax.json.Json;

class Configuration {

	private String nlpAppID;
	private String nlpSubKey;
	private String imageFolder;
	private String dbPath;
	private String host;
	private int port;
	
	public Configuration(String filename) throws InitFailedException {
		try {
			JsonReader in = Json.createReader(new FileInputStream(filename));
			JsonObject config = in.readObject();

			try {
				nlpAppID = config.getString("nlpApplicationID");
				nlpSubKey = config.getString("nlpSubscriptionKey");
				imageFolder = config.getString("imageFolder", "images/");
				dbPath = config.getString("databasePath", "db/data");
				host = config.getString("host", "localhost");

				JsonNumber p = config.getJsonNumber("port");
				port = (p == null) ? 12345 : p.intValue();
			} catch(NullPointerException e) {
				throw new InitFailedException("Missing value in config", e);
			}
			
		} catch(IOException e) {
			throw new InitFailedException("Could not load config from file " + filename, e);
		}
	}

	public String getImageFolder() { return imageFolder; }
	public String getDatabasePath() { return dbPath; }
	public String getAppID() { return nlpAppID; }
	public String getSubscriptionKey() { return nlpSubKey; }
	public String getHost() { return host; }
	public int getPort() { return port; }
	
}
