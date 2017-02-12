package uk.ac.cam.cl.historyphone;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.HashMap;
import java.io.IOException;

public class HttpParamTest {
    @Test public void httpParamTest() {
        try {
            HashMap<String, String> testMap = (HashMap<String,String>) HttpParameters.parse("param1=val1&param2=val2");
            assertEquals(testMap.get("param1"), "val1");
            assertEquals(testMap.get("param2"), "val2");
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }
}
