package uk.ac.cam.cl.historyphone;

import com.sun.net.httpserver.HttpServer;
import java.net.InetSocketAddress;
import java.io.IOException;

/**
   Top-level class encapsulating all components of the app
   server. Provides responses to HTTP requests (as implemented by the
   handlers).
 */
class Server {
	
	private HttpServer connection;
	private Database database;
	private IntentExtractor intentExtractor;

	/**
	   Initialise the HttpServer and bind handlers. 

	   @param host hostname that the server should run on
	   @param port port that the server should run on
	   @param imageFolder folder in which images are located (for ImageHandler)
	 */
	private void initConnection(String host, int port, String imageFolder) throws InitFailedException {
		InetSocketAddress addr = new InetSocketAddress(host, port);

		try {
			connection = HttpServer.create(addr, -1);
		} catch(IOException e) {
			throw new InitFailedException("HttpServer creation failed", e);
		}
		
		connection.createContext("/api/", new ApiHandler(this, database, intentExtractor));
		connection.createContext("/img/", new ImageHandler(this, imageFolder));		
	}

	/**
	   Initialise all components of the server. This leaves the server
	   ready but not running -- to run the server, start() should be
	   called.

	   @param configFile filename from which the configuration should
	   be read
	 */
	public Server(String configFile) throws InitFailedException {
		Configuration config = new Configuration(configFile);

		intentExtractor = new IntentExtractor(config.getAppID(), config.getSubscriptionKey());
		database = new Database(config.getDatabasePath());
			
		initConnection(config.getHost(), config.getPort(), config.getImageFolder());
	}

	/**
	   Starts the server. After this, the server will reply to HTTP
	   requests that it receives.
	 */
	public void start() {
		connection.start();
	}

	/**
	   Stop the server and clean up the components.
	 */
    public void stop() {
		database.close();
	}

	/**
	   Creates and runs the server, using the filename provided as a
	   command-line parameter for the configuration file.
	 */
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
