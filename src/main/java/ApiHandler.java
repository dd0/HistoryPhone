package uk.ac.cam.cl.historyphone;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import java.net.URI;
import java.util.Map;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

class ApiHandler implements HttpHandler {

	private Server server;

	public ApiHandler(Server s) {
		server = s;
	}	

	public void sendResponse(HttpExchange ex, int statusCode, String resp) throws IOException {
		ex.getResponseHeaders().add("Content-Type", "text/json");
		ex.sendResponseHeaders(statusCode, resp.getBytes().length);
		
		OutputStream out = ex.getResponseBody();
		out.write(resp.getBytes());
		out.close();
	}

	// Responses are currently hard-coded placeholders, should
	// delegate to other components once they are implemented
	public void handle(HttpExchange ex) {
		URI request = ex.getRequestURI();
		
		try {
			if(!ex.getRequestMethod().equals("GET")) {
				sendResponse(ex, 404, "Request method is not GET");
				return;
			}
			
			String method = request.getPath();
			Map<String, String> params = HttpParameters.parse(request.getQuery());

			if(method.equals("/api/response")) {
				String reply = "{\n    \"response\": \"" + params.get("message") + "\"\n}";
				sendResponse(ex, 200, reply);
			} else if(method.equals("/api/info")) {
				try {
					int objectID = Integer.parseInt(params.get("id"));
					sendResponse(ex, 200, String.format("Data about object %d...", objectID));
				} catch(NumberFormatException e) {
					sendResponse(ex, 404, "Could not parse ID " + params.get("id"));
				}
			}
			else {
				sendResponse(ex, 404, "Nonexistent API call " + method);
			}
		} catch(IOException e) {
			System.err.println("Exception handling request " + request.toString());
			e.printStackTrace();
			
			try {
				// Try to notify the client of the exception (if this
				// fails as well, stop trying)
				sendResponse(ex, 404, e.getMessage());
			} catch(IOException err) {
				System.err.println("Exception while trying to report details of previous exception");
				err.printStackTrace();
			}
		}
	}
	
}
