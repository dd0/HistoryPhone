package uk.ac.cam.cl.historyphone;

public class DBQuery {

    private String intent;
    private String entity;
    private boolean hasEntity; //is false if no entity exists


    public DBQuery(String inte, String ent) {
        System.out.println("New DBQuery with intent: " + inte + ", and entity: " + ent);
        this.intent = inte;
        this.entity = ent;
        this.hasEntity = true;
    }

    public DBQuery(String inte) {
        this.intent = inte;
        this.entity = null;
        this.hasEntity = false;
        System.out.println("New DBQuery with intent: " + inte + ", and no entities");
    }

    public boolean hasEntity() {
        return this.hasEntity;
    }

    public String getIntent() {
        return this.intent;
    }

    public String getEntity() {
        return this.entity;
    }
}
