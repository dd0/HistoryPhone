package uk.ac.cam.cl.historyphone;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.HashMap;
import java.io.IOException;

public class HttpParamTest {
    @Test public void httpParamTest() {
        try {
            //runs the parse method with the sample string
            HashMap<String, String> testMap1 = (HashMap<String,String>) HttpParameters.parse("param1=val1&param2=val2");
            //asserts correct keys map to correct values.
            assertEquals(testMap1.get("param1"), "val1");
            assertEquals(testMap1.get("param2"), "val2");
            //sanity check
            assertNotEquals(testMap1.get("param1"), "val");

            //testing use of URL Decoder
            HashMap<String, String> testMap2 = (HashMap<String,String>) HttpParameters.parse("param1=val1+val2&param2=val2");
            assertEquals(testMap2.get("param1"), "val1 val2");

        } catch (IOException e) {
            //the test will fail if there is a parsing error from HttpParameters class
            fail(e.getMessage());
        }
    }
}
