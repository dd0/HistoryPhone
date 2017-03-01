package uk.ac.cam.cl.historyphone;

import java.util.Map;
import java.util.HashMap;
import java.net.URLDecoder;
import java.io.UnsupportedEncodingException;
import java.io.IOException;

class HttpParameters {

	// Parse parameters for a GET request (of the form param1=val1&param2=val2)
    public static Map<String, String> parse(String rawParams) throws IOException {
	    HashMap<String, String> res = new HashMap<String, String>();

		if(rawParams == null) {
			// no query string, return empty map
			return res;
		}

		for(String param : rawParams.split("&")) {
			String parts[] = param.split("=");
			if(parts.length != 2) {
				//This shouldn't ever happen
				throw new IOException("Parsing error when splitting " + param);
			}

			res.put(parts[0], URLDecoder.decode(parts[1], "UTF-8"));
		}

		return res;
	}


}
