package uk.ac.cam.cl.historyphone;

import com.sun.net.httpserver.HttpServer;
import java.net.InetSocketAddress;
import java.io.IOException;

class Server {

	private HttpServer connection;
	
	private Server(String host, int port) throws IOException {
		InetSocketAddress addr = new InetSocketAddress(host, port);
		
		connection = HttpServer.create(addr, -1);
		connection.createContext("/api/", new ApiHandler(this));
		connection.createContext("/img/", new ImageHandler(this));
	}

	private void start() {
		connection.start();
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
			s.start();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
}
