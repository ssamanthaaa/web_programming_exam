package it.units;

// import java.util.Date;
import java.util.List;


import org.bson.Document;

public class SingleTrip {
    private int ID;
    private String DATE;
    private String PLACE;
    private String TRANSPORTATION;
    private List<Document> PATH;
    private List<Document> MAIN_STAGES;
    
    public int getTripId() {
        return ID;
    }

    public String getDate() {
        return DATE;
    }

    public String getPlace() {
        return PLACE;
    }

    public String getTransportation() {
        return TRANSPORTATION;
    }

    public List<Document> getPath() {
        return PATH;
    }

    public List<Document> getMainStages() {
        return MAIN_STAGES;
    }
    
    @Override
    public String toString() {
    	return "Trip: ID: " + ID + ", DATE: " + DATE + ", PLACE: " + PLACE + ", TRANSPORTATION: " + TRANSPORTATION + ", MAIN_STAGES: " + MAIN_STAGES;
    }
}
