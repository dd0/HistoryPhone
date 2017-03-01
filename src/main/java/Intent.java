package uk.ac.cam.cl.historyphone;

import javax.json.JsonObject;
import java.util.List;
import java.util.ArrayList;

/*
This class represents the results of the LUIS API Query
'name' will describe the general class of the response e.g. GetBasics for questions like...
"Hello", "Goodbye", "How are you?"" etc
'score' describes the confidence with which LUIS thinks that the message matches the given intent
'entities' will describe specific things mentioned in the query e.g....
"CPU", "Screen", "Steve Furber" etc.
*/
class Intent {

	private String name;
	private double score;
	private List<Entity> entities;

	public Intent(JsonObject o) {
		name = o.getString("intent");
		score = o.getJsonNumber("score").doubleValue();
		entities = new ArrayList<Entity>();
	}

	public Intent(String _name, double _score) {
		name = _name;
		score = _score;
	}

	public void addEntity(Entity e) {
		entities.add(e);
	}

	public String getName() { return name; }
	public double getScore() { return score; }
	public List<Entity> getEntities() { return entities; } // todo: constList

}

/*
An Entity describes specific things mentioned in a query e.g....
"CPU", "Screen", "Steve Furber" etc.
'score' describes how likely it is that the user mentioned this entity in their query
*/
class Entity {
	private String name;
	private double score;

	public Entity(JsonObject o) {
		//retrieve the entity name from the JSON Object
		name = o.getString("type");

		//Some entites are split with '::' E.g. GetBasics::HowDoYouDo
		//Database contains entries for only the later part ('e.g. HowDoYouDo')
		//Therefore split the string and take the latter part
		if (name.contains("::")) {
			name = name.split("::")[1];
		};
		score = o.getJsonNumber("score").doubleValue();
	}

	public String getName() { return name; }
	public double getScore() { return score; }
}
