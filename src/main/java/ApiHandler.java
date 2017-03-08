package uk.ac.cam.cl.historyphone;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.sql.SQLException;
import java.util.Map;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

/*
'ApiHandler' is a class that handles all the http requests from the app to the server
'handle()' is a general handling method which checks what type of request has been made and then invokes the correct method
'sendResponse()' will actually send the text to the app via a json
'handleInfo()' will supply a name, description and image filename(which is no longer used) to the app
Image filenames aren't needed as they just follow the format 'uuid.png' for all bots
'handleSuggestion()' will return a random prompt for the user of an app although it is currently not used
At the moment, the app received prompts by simply sending the message "init" which is intercepted by the server
*/
class ApiHandler implements HttpHandler {

	private Server server;
	private Database database;
	private IntentExtractor intentExtractor;

	private Responder responder;
	
	public ApiHandler(Server s, Database db, IntentExtractor ie) {
		server = s;
		database = db;
		intentExtractor = ie;

		responder = new LuisResponder(ie, db);
	}

	private void sendResponse(HttpExchange ex, int statusCode, ApiResponse resp) throws IOException {
		//Response classes which implement ApiResponse are ChatResponse,SuggestionResponse,ErrorResponse
		//This string represents e.g. response message
		String respText = resp.toJson();
		ex.getResponseHeaders().add("Content-Type", "text/json");
		ex.sendResponseHeaders(statusCode, respText.getBytes().length);

		OutputStream out = ex.getResponseBody();
		out.write(respText.getBytes());
		out.close();
	}

	private void handleInfo(HttpExchange ex, long objectID) throws IOException {
		try {
			//getObjectInfo() is in the Database.java file and uses the uuid to query object info
		    ObjectInfo res = database.getObjectInfo(Long.toString(objectID));
			if(res != null) {
				sendResponse(ex, 200, res);
			}
			else {
				sendResponse(ex, 404, new ErrorResponse(
								 "Object with ID " + Long.toString(objectID) + " not found"));
			}
		} catch(SQLException e) {
			e.printStackTrace();
		    sendResponse(ex, 500, new ErrorResponse("Error doing a database lookup: " + e.getMessage()));
		}
	}

	private void handleSuggestion(HttpExchange ex, long objectID) throws IOException, LookupException {
		try {
			//call method in Database.java
			String res = database.getSuggestion(objectID);
			if(res != null) {
				sendResponse(ex, 200, new SuggestionResponse(res));
			} else {
				sendResponse(ex, 404, new ErrorResponse("No suggestions for ID " + Long.toString(objectID)));
			}
		} catch(SQLException e) {
			e.printStackTrace();
			sendResponse(ex, 500, new ErrorResponse("Error doing a database lookup: " + e.getMessage()));
		}
	}


	private void handleResponse(HttpExchange ex, long uuid, String message) throws IOException, LookupException {
		//get the relevant key for the message and the bot
		String response = null;

		//'init' is sent in standard message form by the app when it wants a prompt message
		//this is a hack that should be migrated to a handleSuggestion() request on the app side.
		if (message.equals("init")) {
			try {
				response = database.getSuggestion(uuid);
				sendResponse(ex, 200, new ChatResponse(response));
				return;
			} catch (LookupException l) {//something has gone wrong
				response = "this is not ideal... seems like your bot doesn't exist";
				sendResponse(ex, 200, new ChatResponse(response));
				return;
			} catch (SQLException s) {
				response = "Sorry I'm having an identity crisis";
				sendResponse(ex, 200, new ChatResponse(response));
				return;
			}
		}

		response = responder.getResponse(uuid, message);
		if (response == null) {
			//prompt user to rephrase...
			sendResponse(ex, 200, new ChatResponse("Sorry I don't understand - please rephrase the question!"));
		} else {
			sendResponse(ex, 200, new ChatResponse(response));
		}
	}


	public void handle(HttpExchange ex) {
		URI request = ex.getRequestURI();

		try {
			if(!ex.getRequestMethod().equals("GET")) {
				sendResponse(ex, 400, new ErrorResponse("Request method is not GET"));
				return;
			}
			//used for deciding type of query e.g. object info/chat response
			String method = request.getPath();
			//this get's a uuid and a message from a query. See HttpParameters.java
			Map<String, String> params = HttpParameters.parse(request.getQuery());

			//This corresponds to requesting a response for the message sent
			if(method.equals("/api/response")) {
				try {
					//get uuid
					long uuid = Long.valueOf(params.get("uuid"));
					String message = params.get("message");
					handleResponse(ex, uuid, message);
				} catch(NumberFormatException e) {
					sendResponse(ex, 400, new ErrorResponse("Could not parse ID " + params.get("uuid")));
				}

			//The app is querying metadata about the object
			} else if(method.equals("/api/info")) {
				try {
					//get uuid (called simply objectID in this method)
					long objectID = Long.parseLong(params.get("id"));
					handleInfo(ex, objectID);
				} catch(NumberFormatException e) {
					sendResponse(ex, 400, new ErrorResponse("Could not parse ID " + params.get("id")));
				}
			//The app wants an object suggestions to prompt the user
			//These are displayed whenever user opens up new chat window
			} else if(method.equals("/api/suggestion")) {
				try {
					int objectID = Integer.parseInt(params.get("id")); // todo: no reason for this not to be String
					handleSuggestion(ex, objectID);
				} catch(NumberFormatException e) {
					sendResponse(ex, 400, new ErrorResponse("Could not parse ID " + params.get("id")));
				}
			} else if(method.equals("/api/sethandler")) {
				String type = params.get("type");
				if(type == null) {
					System.err.println("Tried to switch handler without parameters");
				}
				if(type.equals("luis")) {
					System.err.println("Switching to LUIS responder...");
					responder = new LuisResponder(intentExtractor, database);
				} else if(type.equals("manual")) {
					try {
						System.err.println("Switching to manual responder...");
						if(params.get("port") == null) {
							System.err.println("no port");
							throw new NumberFormatException(); // hack
						}
						
						int port = Integer.parseInt(params.get("port"));
						if(port == -1) {
							System.err.println("Cannot recognise port number, aborting");
						} else {
							responder = new ManualResponder(port);
						}
					} catch(NumberFormatException e) {
						System.err.println(String.format("Cannot recognise port number, aborting"));
					}
				} else {
					System.err.println("Tried to switch to non-existent handler!");
				}

				sendResponse(ex, 200, new ErrorResponse("dummy error, does not matter"));
			} else {
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
