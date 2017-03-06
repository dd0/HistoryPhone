package uk.ac.cam.cl.historyphone;

import org.junit.Ignore;
import java.util.*;
import org.junit.Assert;
import org.junit.Test;
import java.util.concurrent.TimeUnit;
import javax.json.*;

public class IntentExtractorTest {
    
    // Declare variables storing the application ID, application key and the chatbot UUID.
    private String appID = "c60953a6-11d0-46ff-887a-2949728b1310";
    private String key = "5738862017d948c5b1205e8099c649f4";
    private long bot_uuid = 56;
    

    // Note: due to the server having low processing power, timeouts are written between every few tests
    // to avoid "too many HTTP Requests" error.

	
	
    // Test whether getTopIntent returns the correct intent.
    @Test public void getTopIntent() throws RemoteQueryException, InterruptedException {

		/*
		Note:
		There is a degree of non-determinism in the "score" field of an intent.
		Thus scores are tested using bounds, not exact values.
		There is also a large degree of non-determinism in the "score" field of entites.
		Thus this field is not used when comparing equality of two entities.
		*/
	

		// Declare an intent extractor variable, using the appropriate application ID and key.
		IntentExtractor intEx = new IntentExtractor(appID, key);

		
		// Test with "GetBasics" intent.
		TimeUnit.MILLISECONDS.sleep(200);
		Intent int_test = intEx.getTopIntent("Hello.");
		Intent int_actual = new Intent("GetBasics", 0.8);

		JsonObject tmp = Json.createObjectBuilder().add("type", "Basics::GenericGreeting").add("score", 0.0).build(); 
		int_actual.addEntity(new Entity(tmp));
					
		Assert.assertEquals(int_test.getName(),  int_actual.getName());
		Assert.assertEquals(int_test.getScore() > 0.75, int_actual.getScore() > 0.75);	

		comp_EntityList(int_test.getEntities(), int_actual.getEntities());

		

		// Test with "GetGoal" intent.
	    	int_test = intEx.getTopIntent("Why were you built?");
	    	int_actual = new Intent("GetGoal", 0.8);

		tmp = Json.createObjectBuilder().add("type", "CreatorQuery").add("score", 0.0).build(); 
		int_actual.addEntity(new Entity(tmp));
			
		Assert.assertEquals(int_test.getName(),  int_actual.getName());
		Assert.assertEquals(int_test.getScore() > 0.75, int_actual.getScore() > 0.75);		
		comp_EntityList(int_test.getEntities(), int_actual.getEntities());



		// Test with "None" intent.
		TimeUnit.MILLISECONDS.sleep(200);
	    	int_test = intEx.getTopIntent("Do you hear weird questions often?");
	    	int_actual = new Intent("None", 0.8);
			
		Assert.assertEquals(int_test.getName(),  int_actual.getName());
		Assert.assertEquals(int_test.getScore() > 0.75, int_actual.getScore() > 0.75);	
		comp_EntityList(int_test.getEntities(), int_actual.getEntities());



		// Test with "GetFeature" intent.
		int_test = intEx.getTopIntent("What operating system do you use?");
	    	int_actual = new Intent("GetFeature", 0.8);

	    	tmp = Json.createObjectBuilder().add("type", "Application").add("score", 0.0).build(); 
		int_actual.addEntity(new Entity(tmp));

	    	tmp = Json.createObjectBuilder().add("type", "Feature::OS").add("score", 0.0).build(); 
		int_actual.addEntity(new Entity(tmp));
		
		Assert.assertEquals(int_test.getName(),  int_actual.getName());
		Assert.assertEquals(int_test.getScore() > 0.75, int_actual.getScore() > 0.75);	
		comp_EntityList(int_test.getEntities(), int_actual.getEntities());


	       
		// Test with "GetCreationTime" intent.
		TimeUnit.MILLISECONDS.sleep(200);
	    	int_test = intEx.getTopIntent("When were you made?");
	   	int_actual = new Intent("GetCreationTime", 0.8);

		tmp = Json.createObjectBuilder().add("type", "CreatorQuery").add("score", 0.0).build(); 
		int_actual.addEntity(new Entity(tmp));
		tmp = Json.createObjectBuilder().add("type", "Time").add("score", 0.0).build(); 
		int_actual.addEntity(new Entity(tmp));
			
		Assert.assertEquals(int_test.getName(),  int_actual.getName());
		Assert.assertEquals(int_test.getScore() > 0.7, int_actual.getScore() > 0.7);	
		comp_EntityList(int_test.getEntities(), int_actual.getEntities());


		// Test with "GetCreator" intent.
		int_test = intEx.getTopIntent("Were you made by BBC?");
	    	int_actual = new Intent("GetCreator", 0.8);

		tmp = Json.createObjectBuilder().add("type", "CreatorQuery").add("score", 0.0).build(); 
		int_actual.addEntity(new Entity(tmp));
		tmp = Json.createObjectBuilder().add("type", "Creators::Team").add("score", 0.0).build(); 
		int_actual.addEntity(new Entity(tmp));
			
		Assert.assertEquals(int_test.getName(),  int_actual.getName());
		Assert.assertEquals(int_test.getScore() > 0.7, int_actual.getScore() > 0.7);	
		comp_EntityList(int_test.getEntities(), int_actual.getEntities());


	}

    


    // Helper method designed to compare two lists of entites.
	public void comp_EntityList(List<Entity> ents_test, List<Entity> ents_actual) {

		// Ensure sizes are the same.
	    Assert.assertEquals(ents_test.size(), ents_actual.size());

	    // Sort the tested entity list into alphabetical order by name.
	    Collections.sort(ents_test, new Comparator<Entity>() {
				@Override
				public int compare(Entity ent1, Entity ent2) {
					return ent1.getName().compareTo(ent2.getName());
				}
		});


	    // Compare entity names element-by-element.
	    for (int i = 0; i < ents_test.size(); i++) {
			Assert.assertEquals(ents_test.get(i).getName(),  ents_actual.get(i).getName());			    
	    }
	}
    
    
    // Test whether the DBQ method correctly forms database queries given message text.
    @Test public void getDBQ() throws RemoteQueryException, InterruptedException {

	/* 
	Note: 
	DBQuery only returns an intent with an entity if there are multiple potential replies 
	available for that intent and an entity is needed to disambiguate between these.
	If there is only a single reply for the intent, then all entities for it are disregarded, since they are not
    needed for reply lookup, and only the intent is returned, even if the original LUIS query does return them.
	*/

	
	// Declare an intent extractor variable, using the appropriate application ID and key.
        IntentExtractor intEx = new IntentExtractor(appID, key);


	// Test "GetPurchase" intent using appropriate utterance.
        TimeUnit.MILLISECONDS.sleep(200);
        DBQuery dbq_test = intEx.getDBQ(bot_uuid, "Can I buy you?");
     	DBQuery dbq_actual = new DBQuery("GetPurchase", null);
        
    	Assert.assertEquals(dbq_test.getEntity(), dbq_actual.getEntity());
		Assert.assertEquals(dbq_test.getIntent(), dbq_actual.getIntent());

	
	// Test "GetSuccess" intent using appropriate utterance.
        dbq_test = intEx.getDBQ(bot_uuid, "Are you famous?");
        dbq_actual = new DBQuery("GetSuccess", null);
        
        Assert.assertEquals(dbq_test.getEntity(), dbq_actual.getEntity());
		Assert.assertEquals(dbq_test.getIntent(), dbq_actual.getIntent());


	// Test "None" intent with a gibberish utterance.
		TimeUnit.MILLISECONDS.sleep(200);
        dbq_test = intEx.getDBQ(bot_uuid, "Qalxpc,q_123 DSolsa");
        dbq_actual = new DBQuery("None", null);
        
        Assert.assertEquals(dbq_test.getEntity(), dbq_actual.getEntity());
		Assert.assertEquals(dbq_test.getIntent(), dbq_actual.getIntent());

	
	// Test "None" intent with an unrecognisable utterance.
        dbq_test = intEx.getDBQ(bot_uuid, "Roses are red, violets are blue.");
        dbq_actual = new DBQuery("None", null);
        
        Assert.assertEquals(dbq_test.getEntity(), dbq_actual.getEntity());
		Assert.assertEquals(dbq_test.getIntent(), dbq_actual.getIntent());

	
	// Test "GetFeature" intent with the "Display" entity using appropriate utterance.
		TimeUnit.MILLISECONDS.sleep(200);
        dbq_test = intEx.getDBQ(bot_uuid, "Do you have a monitor?");
        dbq_actual = new DBQuery("GetFeature", "Display");
        
        Assert.assertEquals(dbq_test.getEntity(), dbq_actual.getEntity());
		Assert.assertEquals(dbq_test.getIntent(), dbq_actual.getIntent());


	// Test "GetCreator" intent with the "Steve Furber" entity using appropriate utterance.
		dbq_test = intEx.getDBQ(bot_uuid, "Were you made by Steve Furber?");
        dbq_actual = new DBQuery("GetCreator", "Steve Furber");
        
        Assert.assertEquals(dbq_test.getEntity(), dbq_actual.getEntity());
		Assert.assertEquals(dbq_test.getIntent(), dbq_actual.getIntent());
        
	}

    
    
}
