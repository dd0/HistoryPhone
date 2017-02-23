package uk.ac.cam.cl.historyphone;

import java.util.List;
import java.util.ArrayList;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonArray;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.io.InputStream;

class IntentExtractor {

	private String appID;
	private String subscriptionKey;

	private static String BASE_URL =
		"https://westus.api.cognitive.microsoft.com/luis/v2.0/apps/%s?subscription-key=%s&q=%s&verbose=true";

	public IntentExtractor(String id, String key) {
		appID = id;
		subscriptionKey = key;
	}

    private JsonObject getRawData(String message) throws RemoteQueryException {
		try {
			URL url = new URL(String.format(BASE_URL, appID, subscriptionKey, URLEncoder.encode(message, "UTF-8")));
			URLConnection conn = url.openConnection();

			InputStream in = conn.getInputStream();
			JsonReader jsonIn = Json.createReader(in);
			JsonObject res = jsonIn.readObject();

			return res;
		} catch(Exception e) {
			throw new RemoteQueryException(e);
		}
	}

	public Intent getTopIntent(String message) throws RemoteQueryException {
	    JsonObject raw = getRawData(message);
	    Intent res = new Intent(raw.getJsonObject("topScoringIntent"));

		JsonArray entities = raw.getJsonArray("entities");
		if(entities != null) {
			// raw.getClass() -- stupid way to get Class<JsonObject>
			for(JsonObject e : entities.getValuesAs(raw.getClass())) {
				res.addEntity(new Entity(e));
			}
		}
		
		return res;
	}

	//getKey will take a message and return a string encoding the key for the relevant response
	//the return string will be of the form 'intent(+entities(+rand#))'
	public String getKey(long uuid, String message) throws RemoteQueryException {
		return getTopIntent(message).getName();
	}

}
