package uk.ac.cam.cl.historyphone;

import com.sun.net.httpserver.HttpServer;
import java.net.InetSocketAddress;
import java.io.IOException;

class Server {

	private HttpServer connection;
	private Database database;
	private IntentExtractor intentExtractor;
	
	private void initConnection(String host, int port) throws InitFailedException {
		InetSocketAddress addr = new InetSocketAddress(host, port);

		try {
			connection = HttpServer.create(addr, -1);
		} catch(IOException e) {
			throw new InitFailedException("HttpServer creation failed", e);
		}
		
		connection.createContext("/api/", new ApiHandler(this, database, intentExtractor));
		connection.createContext("/img/", new ImageHandler(this));		
	}
	
	private Server(String configFile) throws InitFailedException {
		Configuration config = new Configuration(configFile);

		intentExtractor = new IntentExtractor(config.getAppID(), config.getSubscriptionKey());
		database = new Database(config.getDatabasePath());
			
		initConnection(config.getHost(), config.getPort());
	}

	private void start() {
		connection.start();
	}

	// Cleanup after the server stops running.
	private void stop() {
		database.close();
	}
	
	public static void main(String[] args) {
		if(args.length != 1) {
			System.err.println("Usage: Server <config>");
			return;
		}
	    
		try {
		    Server s = new Server(args[0]);
			try {
				s.start();
			} finally {
				// todo: see if this can be done in some other way
				// s.start() is non-blocking, so s.stop() will be
				// called immediately

				//s.stop();
			}
		} catch(InitFailedException e) {
			e.printStackTrace();
		} 
	}
	
}
