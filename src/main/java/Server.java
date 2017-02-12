package uk.ac.cam.cl.historyphone;

import com.sun.net.httpserver.HttpServer;
import java.net.InetSocketAddress;
import java.io.IOException;

class Server {

	private HttpServer connection;
	private Database database;

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
		
		connection.createContext("/api/", new ApiHandler(this, database));
		connection.createContext("/img/", new ImageHandler(this));		
	}
	
	private Server(String host, int port, String dbPath) throws InitFailedException {
		database = new Database(dbPath);
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
		if(args.length != 2 && args.length != 3) {
			System.err.println("Usage: Server <address> <port>");
			return;
		}

		String host = args[0];
		int port = Integer.parseInt(args[1]);
		String dbPath = args.length >= 3 ? args[2] : defaultDB;

		try {
		    Server s = new Server(host, port, dbPath);
			try {
				s.start();
			} finally {
				s.stop();
			}
		} catch(InitFailedException e) {
			e.printStackTrace();
		} 
	}
	
}
