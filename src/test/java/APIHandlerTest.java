package uk.ac.cam.cl.historyphone;

import org.junit.Test;
import static org.junit.Assert.*;
import java.net.URI;

public class APIHandlerTest {
    @Test public void APITest() {
        assertEquals(2+2, 4);
        //Insert some APIHandler Tests
    }

/*	@Test public void IDLookupCorrect() {
		try {
			MockDatabase db = new MockDatabase();
			ObjectInfo obj1 = new ObjectInfo("100", "testObject", "this is a test object", "/blah/asdf");
			ObjectInfo obj2 = new ObjectInfo("4321", "anotherObj", "some description here", "/something/");
			db.addObject("100", obj1);
			db.addObject("4321", obj2);
			
			ApiHandler h = new ApiHandler(null, db);
			MockHttpExchange ex = new MockHttpExchange(new URI("/api/info?id=100"));
			h.handle(ex);
		} catch(Exception e) {
			fail(e.getMessage());
		}
		
		}*/
}
