package uk.ac.cam.cl.historyphone;

import javax.json.JsonObject;

class Intent {

	private String name;
	private double score;

	public Intent(JsonObject o) {
		name = o.getString("intent");
		score = o.getJsonNumber("score").doubleValue();
	}
	
	public Intent(String _name, double _score) {
		name = _name;
		score = _score;
	}
	
	public String getName() { return name; }
	public double getScore() { return score; }
	
}
