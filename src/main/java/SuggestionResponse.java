package uk.ac.cam.cl.historyphone;

import javax.json.Json;
import javax.json.JsonObject;

class SuggestionResponse implements ApiResponse {

	private String text;
	
	public SuggestionResponse(String msg) {
		text = msg;
	}
	
	@Override
	public String toJson() {
		JsonObject res = Json.createObjectBuilder()
			.add("suggestion", text)
			.build();
		return res.toString();
	}
	
}
