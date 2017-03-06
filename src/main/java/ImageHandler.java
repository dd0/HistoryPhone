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
	private String folder;

	public ImageHandler(Server s, String f) {
		server = s;
		folder = f;
	}
	
	private boolean pathValid(String path) {
		final String pattern = "\\w+\\.png";
	    if(!Pattern.matches(pattern, path)) {
			// allow only a-zA-Z0-9_ in filename and enforce .png just in case
			return false;
		}

		return true;
	}

	private void sendError(HttpExchange ex, int statusCode) throws IOException {
		// empty response -> no header
		ex.sendResponseHeaders(statusCode, 0);
		ex.getResponseBody().close();
	}
	
    private void sendImage(HttpExchange ex, int statusCode, String path) throws IOException {
		File f = new File(path);
		
		if(!f.exists()) {
			sendError(ex, 404);
		} else {
			ex.getResponseHeaders().add("Content-Type", "image/png");
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
				sendImage(ex, 200, folder + path);
			} else {
				sendError(ex, 404);
			}
		} catch(Exception e) {e.printStackTrace(); }
	}
	
}
