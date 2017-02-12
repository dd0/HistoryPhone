package uk.ac.cam.cl.historyphone;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import java.net.URI;
import java.util.Map;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;

class ApiHandler implements HttpHandler {

	private Server server;
	private Database database;

	public ApiHandler(Server s, Database db) {
		server = s;
		database = db;
	}	

	private void sendResponse(HttpExchange ex, int statusCode, ApiResponse resp) throws IOException {
		String respText = resp.toJson();
		ex.getResponseHeaders().add("Content-Type", "text/json");
		ex.sendResponseHeaders(statusCode, respText.getBytes().length);
		
		OutputStream out = ex.getResponseBody();
		out.write(respText.getBytes());
		out.close();
	}

	private void handleInfo(HttpExchange ex, int objectID) throws IOException {
		try {
		    ObjectInfo res = database.getObjectInfo(Integer.toString(objectID));
			if(res != null) {
				sendResponse(ex, 200, res);
			}
			else {
				sendResponse(ex, 404, new ErrorResponse(
								 "Object with ID " + Integer.toString(objectID) + " not found"));
			}
		} catch(SQLException e) {
		    sendResponse(ex, 500, new ErrorResponse("Error doing a database lookup: " + e.getMessage()));
		}
	}
	
	// Responses are currently hard-coded placeholders, should
	// delegate to other components once they are implemented
	public void handle(HttpExchange ex) {
		URI request = ex.getRequestURI();
		
		try {
			if(!ex.getRequestMethod().equals("GET")) {
				sendResponse(ex, 400, new ErrorResponse("Request method is not GET"));
				return;
			}
			
			String method = request.getPath();
			Map<String, String> params = HttpParameters.parse(request.getQuery());

			if(method.equals("/api/response")) {
				// placeholder until a response system is implemented
				sendResponse(ex, 200, new ChatResponse(params.get("message")));
			} else if(method.equals("/api/info")) {
				try {
					int objectID = Integer.parseInt(params.get("id"));
					handleInfo(ex, objectID);
				} catch(NumberFormatException e) {
					sendResponse(ex, 400, new ErrorResponse("Could not parse ID " + params.get("id")));
				}
			}
			else {
				sendResponse(ex, 404, new ErrorResponse("Nonexistent API call " + method));
			}
		} catch(Exception e) {
			System.err.println("Exception handling request " + request.toString());
			e.printStackTrace();
			
			try {
				// Try to notify the client of the exception (if this
				// fails as well, stop trying)
				sendResponse(ex, 500, new ErrorResponse(e.getMessage()));
			} catch(Exception err) {
				System.err.println("Exception while trying to report details of previous exception");
				err.printStackTrace();
			}
		}
	}
	
}
