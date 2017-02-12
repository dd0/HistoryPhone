package uk.ac.cam.cl.historyphone;

import javax.json.Json;
import javax.json.JsonObject;

class ChatResponse implements ApiResponse {

	private String messageText;
	
	public ChatResponse(String msg) {
		messageText = msg;
	}
	
	@Override
	public String toJson() {
		JsonObject res = Json.createObjectBuilder()
			.add("message", messageText)
			.build();
		return res.toString();
	}
	
}
