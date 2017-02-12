package uk.ac.cam.cl.historyphone;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import java.io.OutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.regex.Pattern;
import java.nio.file.Paths;
import java.io.File;
import java.nio.file.Files;

class ImageHandler implements HttpHandler {

	private Server server;

	public ImageHandler(Server s) {
		server = s;
	}
	
	private boolean pathValid(String path) {
		final String pattern = "\\w+\\.png";
	    if(!Pattern.matches(pattern, path)) {
			// allow only a-zA-Z0-9_ in filename and enforce .png just in case
			return false;
		}

		// todo: don't open the file twice?
	    File tmp = new File("images/" + path);
		return tmp.exists();
	}

	// todo: refactor
	public void sendResponse(HttpExchange ex, int statusCode, String path) throws IOException {
		ex.getResponseHeaders().add("Content-Type", "image/png");
		if(path == null) {
			// todo: do this in a more reasonable way
			ex.sendResponseHeaders(statusCode, 0);
			ex.getResponseBody().close();
		} else {
			File f = new File(path);
			
			ex.sendResponseHeaders(statusCode, f.length());
			OutputStream out = ex.getResponseBody();
			Files.copy(f.toPath(), out);
			out.close();
		}
	}

	public void handle(HttpExchange ex) {
		// take the /img/... path and drop the /img/ prefix
		URI request = ex.getRequestURI();
		String path = request.getPath().substring(5);

		try {
			if(pathValid(path)) {
				sendResponse(ex, 200, "images/" + path);
			} else {
				sendResponse(ex, 404, null);
			}
		} catch(Exception e) {e.printStackTrace(); }
	}
	
}
