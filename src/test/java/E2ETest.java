package uk.ac.cam.cl.historyphone;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;


/**
   End-to-end tests for the entire server.

   Since testing the handlers requires constructing mock
   HttpExchanges, for simplicity, all testing of handlers is done by
   these tests.
 */
public class E2ETest {

	private static Server server;
	private static final String baseUrl = "http://localhost:20476";
	
	/**
	   If the server is not responding or sends a malformed response,
	   the tests can potentially hang, so we use a timeout for each
	   test to ensure completion.
	 */
	@Rule public Timeout globalTimeout = Timeout.seconds(10);
	
	@BeforeClass
	public static void startServer() throws Exception {
		String config = E2ETest.class.getResource("/testconfig.json").getFile();
		String img = E2ETest.class.getResource("/testimg/").getFile();
		server = new Server(config);

		server.start();
	}

	/**
	   Performs a GET request on the given URL and checks if it
	   returns the appropriate status code and if the given string is
	   a prefix of the response. Assumes that the response will be a
	   single line (no newline characters).

	   If response is null, ignore it.
	 */
	private void assertResponse(String url, int code, String response) throws Exception {
		HttpURLConnection conn = (HttpURLConnection)(new URL(url).openConnection());
		conn.setRequestMethod("GET");
		conn.connect();

		assertEquals(code, conn.getResponseCode());

		if(response != null) {
			BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		
			String res = reader.readLine();
			assert(res.startsWith(response));
		}
	}

	private void assertStatus(String url, int code) throws Exception {
		assertResponse(url, code, null);
	}

	// ImageHandler tests
	
	@Test public void ImageHandlerOK() throws Exception {
		String url = baseUrl + "/img/hello.png";
	    assertResponse(url, 200, "this is not a real png");
	}

	@Test public void ImageHandlerNotFound() throws Exception {
		String url = baseUrl + "/img/nonexistent.png";
		assertStatus(url, 404);
	}

	@Test public void ImageHandlerInvalidPath() throws Exception {
		String url = baseUrl + "/img/notimage.txt";
		assertStatus(url, 404);
	}

	// ApiHandler /api/info tests
	
	@Test public void InfoOK() throws Exception {
		String url = baseUrl + "/api/info?id=5551";
		
		// don't check the whole response to keep the test readable,
		// this prefix should be sufficient
		String resp = "{\"name\":\"Bot 3\",\"description\":\"Bot 3 desc";
		assertResponse(url, 200, resp);
	}

    @Test public void InfoNonNumeric() throws Exception {
		String url = baseUrl + "/api/info?id=words";
		assertStatus(url, 400);
	}

	@Test public void InfoNonexistent() throws Exception {
		String url = baseUrl + "/api/info?id=99";
		assertStatus(url, 404);
	}
	
	@AfterClass
	public static void stopServer() {
		server.stop();
	}
	
}
