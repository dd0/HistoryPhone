package uk.ac.cam.cl.historyphone;

import java.net.URL;
import java.net.URLEncoder;
import java.net.URLConnection;

import javax.json.Json;
import javax.json.JsonReader;

import java.io.InputStream;

class ManualResponder implements Responder {

	private String baseUrl;
	
	public ManualResponder(int port) {
		baseUrl = String.format("http://localhost:%d/response?msg=", port);
	}
	
	public String getResponse(long uuid, String message) {
		try {
			URL url = new URL(baseUrl + URLEncoder.encode(message, "UTF-8"));
			URLConnection conn = url.openConnection();
			
			InputStream in = conn.getInputStream();
			JsonReader jsonIn = Json.createReader(in);
			return jsonIn.readObject().getString("response");
		} catch(Exception e) {
			return null;
		}
	}
	
}
