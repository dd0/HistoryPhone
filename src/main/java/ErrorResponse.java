package uk.ac.cam.cl.historyphone;

import javax.json.Json;
import javax.json.JsonObject;

class ErrorResponse implements ApiResponse {

	String message;

	ErrorResponse(String _message) {
		message = _message;
	}

	@Override
	public String toJson() {
		JsonObject res = Json.createObjectBuilder()
			.add("error", message)
			.build();
		return res.toString();
	}
	
}
