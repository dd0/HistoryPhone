package uk.ac.cam.cl.historyphone;

import java.sql.SQLException;

class LuisResponder implements Responder {

	private IntentExtractor intentExtractor;
	private Database database;

	public LuisResponder(IntentExtractor ie, Database db) {
		intentExtractor = ie;
		database = db;
	}
	
	public String getResponse(long uuid, String message) {
		String response = null;

		//intentExtractor will return a DBQuery which holds the top intent and the top relevant entity (if present)
		DBQuery dBQ;
		try {
			dBQ = intentExtractor.getDBQ(uuid, message);
		} catch (RemoteQueryException r) {
				response = "Sorry I'm having a problem with getting to LUIS";
				return response;
		}

		//retrieve the response.
		try {
			response = database.getResponse(uuid, dBQ);
		} catch (SQLException s) {
			s.printStackTrace();
		} catch (LookupException l) {
			response = "I can't understand sorry :(";
		}

		return response;
	}
	
}
