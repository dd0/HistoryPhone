package uk.ac.cam.cl.historyphone;

import javax.json.JsonObject;
import java.util.List;
import java.util.ArrayList;

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

class Entity {
	private String name;
	private double score;

	public Entity(JsonObject o) {
		name = o.getString("type");
		score = o.getJsonNumber("score").doubleValue();
	}

	public String getName() { return name; }
	public double getScore() { return score; }
}
