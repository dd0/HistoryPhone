package uk.ac.cam.cl.historyphone;

import com.sun.net.httpserver.HttpServer;
import java.net.InetSocketAddress;
import java.io.IOException;

class Server {

	private HttpServer connection;
	private Database database;
	private IntentExtractor intentExtractor;
	
	// Default database path to use if there is none provided on the
	// command line.
	private static String defaultDB = "db/data";

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
	
	private Server(String host, int port) throws InitFailedException {
		Configuration config = new Configuration("config.json");

		intentExtractor = new IntentExtractor(config.getAppID(), config.getSubscriptionKey());
		database = new Database(config.getDatabasePath());
		initConnection(host, port);
	}

	private void start() {
		connection.start();
	}

	// Cleanup after the server stops running.
	private void stop() {
		database.close();
	}
	
	public static void main(String[] args) {
		if(args.length != 2) {
			System.err.println("Usage: Server <address> <port>");
			return;
		}
		
		String host = args[0];
		int port = Integer.parseInt(args[1]);
		
		try {
		    Server s = new Server(host, port);
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
