package uk.ac.cam.cl.historyphone;

import javax.json.Json;
import javax.json.JsonObject;

class ObjectInfo implements ApiResponse {

	// Dummy example parameters, replace with actual data
	private String name;
	private int creationYear;

	public ObjectInfo(String _name, int _creationYear) {
		name = _name;
		creationYear = _creationYear;
	}
	
	@Override
	public String toJson() {
		JsonObject res = Json.createObjectBuilder()
			.add("name", name)
			.add("creationYear", creationYear)
			.build();
		return res.toString();
	}
	
}
