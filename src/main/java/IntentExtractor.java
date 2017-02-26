package uk.ac.cam.cl.historyphone;

import java.util.*;
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
	private Map<String, List<String>> map; //used to check if intent is associated with a certain entity

	private static String BASE_URL =
		"https://westus.api.cognitive.microsoft.com/luis/v2.0/apps/%s?subscription-key=%s&q=%s&verbose=true";

	public IntentExtractor(String id, String key) {
		appID = id;
		subscriptionKey = key;
		//use map of string to list of strings to check if a given set of entites can be associated with a given intent
		//if data set were larger, could use a database instead
		map = new HashMap<String, List<String>>();
		LinkedList<String> greet = new LinkedList<String>();
		greet.add("Generic");
		greet.add("HowDoYouDo");
		map.put("GetGreeting", greet);

		LinkedList<String> create = new LinkedList<String>();
		greet.add("Team");
		greet.add("SteveFurber");
		greet.add("SophieWilson");
		map.put("GetCreator", create);

		LinkedList<String> feat = new LinkedList<String>();
		greet.add("CPU");
		greet.add("Storage");
		greet.add("Display");
		greet.add("OS");
		greet.add("RAM");
		greet.add("Keyboard");
		greet.add("Sound");
		map.put("GetFeature", feat);
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

	//getKey will take a message and return a 'DBQuery' object to describe the correct lookup.
	public DBQuery getDBQ(long uuid, String message) throws RemoteQueryException {
		Intent originalIntent = getTopIntent(message);
		String intent = originalIntent.getName();
		List<Entity> entList = originalIntent.getEntities();
		//Sort list with highest ranking entities first
		Collections.sort(entList, new Comparator<Entity>() {
				@Override
				public int compare(Entity ent1, Entity ent2) {
					return ((int)ent1.getScore()) - ((int)ent2.getScore());
				}
		});
		List<String> realEntList = map.get(intent);

		for(Entity e : entList) { //check if there are valid entities in the query
			String entName = e.getName();
			if (realEntList != null && realEntList.contains(entName)) {
				return new DBQuery(intent, entName);
			}
		}
		//return result with no entity associated
		return new DBQuery(intent);
	}
}
