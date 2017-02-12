package uk.ac.cam.cl.historyphone;

import javax.json.Json;
import javax.json.JsonObject;

class ObjectInfo implements ApiResponse {

	// Dummy example parameters, replace with actual data
	private String name;
	private String desc;
	private String imgUrl;
	private String UUID;

	public ObjectInfo(String _uuid, String _name, String _desc, String _url) {
		name = _name;
		desc = _desc;
		imgUrl = _url;
		UUID = _uuid;
	}
	
	@Override
	public String toJson() {
		JsonObject res = Json.createObjectBuilder()
			.add("name", name)
			.add("description", desc)
			.add("image", imgUrl)
			.add("uuid", UUID)
			.build();
		return res.toString();
	}
	
}
