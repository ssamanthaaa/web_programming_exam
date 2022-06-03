package it.units;

import java.util.Date;
import java.util.List;

// import java.util.ArrayList;

import org.bson.Document;
// import com.google.gson.Gson;
// import com.google.gson.JsonObject;


public class TripDocument {
    private String NAME;
    private String SURNAME;
    private String USERNAME;
    private String PASSWORD;
    private String ROLE;
    private int USER_ID;
    private List<Document> TRIPS;

    public int getUserId() {
        return USER_ID;
    }

    public String getName() {
        return NAME;
    }

    public String getSurname() {
        return SURNAME;
    }
    public String getUsername() {
        return USERNAME;
    }

    public String getPassword() {
        return PASSWORD;
    }

    public String getRole() {
        return ROLE;
    }

    public List<Document> getTrips() {
        return TRIPS;
    }


}
