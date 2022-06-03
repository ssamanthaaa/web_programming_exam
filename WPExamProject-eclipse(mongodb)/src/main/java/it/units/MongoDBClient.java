package it.units;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.google.gson.Gson;
// import com.google.gson.JsonObject;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.UpdateResult;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.mongodb.client.model.Filters.*;
 import com.mongodb.client.model.Projections.*;

import org.bson.Document;
import org.bson.conversions.Bson;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.*;

public class MongoDBClient {
    private static MongoDBClient instance = null;
    private String connectionString = "mongodb+srv://samantha_gallone:tA1h6c8XgNvhGKTB@examcluster.jeomm.mongodb.net/myFirstDatabase?retryWrites=true&w=majority";
    		//"mongodb+srv://samantha_gallone:xuXsvjZscrQfa5@cluster0.xqhsz.mongodb.net/trip?retryWrites=true&w=majority"; //"mongodb://localhost:27017";
    private MongoClient mongoClient;

    private MongoDBClient() {
        try {
            mongoClient = MongoClients.create(connectionString);
            System.out.println("trying to connect...");
        } catch (Exception e) {
            System.out.println("ERROR!!");
            System.out.println(e.getLocalizedMessage().toString());
        }
    }

    public static MongoDBClient getInstance() {
        if (instance == null) {
            instance = new MongoDBClient();
        }
        return instance;
    }
    
    
    private MongoCollection<Document> getTripCollection() {
//    	MongoDatabase tripDB = mongoClient.getDatabase("trip");
//        MongoCollection<Document> tripCollection = tripDB.getCollection("trip");
        return mongoClient.getDatabase("tripDB").getCollection("trip");
    }
    

    // OK IT WORKS
    public List<String> getTrips(int userId) {
        try {
//            MongoDatabase tripDB = mongoClient.getDatabase("trip");
            MongoCollection<Document> tripCollection = getTripCollection();//tripDB.getCollection("trip");
            Document data = tripCollection.find(new Document("USER_ID", userId)).first();
            System.out.println("data " + data);
            String dataJosn = data.toJson();
            Gson gson = new Gson();
            TripDocument trip = gson.fromJson(dataJosn, TripDocument.class);
            List<Document> trips = trip.getTrips();
            System.out.println("Trips: " + trips);
            List<String> tripsJson = new ArrayList<>(trips.size());
            for (Document t : trips) {
                tripsJson.add(t.toJson());
            }
            System.out.println("trips = " + tripsJson);
            return tripsJson;
        } catch (Exception e) {
            System.out.println("ERROR!!");
            System.out.println(e.getStackTrace());
            return Collections.singletonList(e.toString());
        }
    }

    
    // OK IT WORKS
    public String getTrip(int userId, int tripId) {
        try {
//            MongoDatabase tripDB = mongoClient.getDatabase("trip");
            MongoCollection<Document> tripCollection = getTripCollection();//tripDB.getCollection("trip");
        	
            // Document data = tripCollection.find(new Document("ID_UTENTE", idUtente)).first();
            Document data = tripCollection.find(new Document("USER_ID", userId)).first();
            System.out.println("data " + data);
            Gson gson = new Gson();
            TripDocument trip = gson.fromJson(data.toJson(), TripDocument.class);
            List<Document> tripDocuments = trip.getTrips();
            String foundTrip = "";
            for (Document t : tripDocuments) {
                SingleTrip singleTrip = gson.fromJson(t.toJson(), SingleTrip.class);
                if ( singleTrip.getTripId() == tripId) {
                    foundTrip = t.toJson();
                    System.out.println("foundTrip: " + foundTrip);
//                    System.out.println("trip found .toString(): " + t.toString());
                }
            }
            return foundTrip;
        } catch (Exception e) {
            System.out.println("ERROR!!");
            System.out.println(e.getStackTrace());
            return e.toString();
        }
    }
    // OK IT WORKS
    public String addTrip(int userId, SingleTrip trip) {
        try {
//            MongoDatabase tripDB = mongoClient.getDatabase("trip");
            MongoCollection<Document> tripCollection =  getTripCollection();//tripDB.getCollection("trip");
            Document data = tripCollection.find(new Document("USER_ID", userId)).first();
            // Document idMaxDoc = tripCollection.find().sort(new Document("ID", -1)).first();
            System.out.println("data " + data);
            Gson gson = new Gson();
            TripDocument tripDocument = gson.fromJson(data.toJson(), TripDocument.class);
            List<Document> tripDocumentList = tripDocument.getTrips();
            int idMax = tripDocumentList.size();
            // System.out.println("length: " + idMax);
            Document d = new Document();
            d.append("ID", idMax+1)
                .append("DATE", trip.getDate().toString())
                .append("PLACE", trip.getPlace())
                .append("TRANSPORTATION", trip.getTransportation())
                .append("GEOJSON", trip.getGeoJson());
//                .append("PATH", trip.getPath())
//                .append("MAIN_STAGES", trip.getMainStages())
                		
            tripDocumentList.add(d);
            // System.out.println("tripDocumentlist: " + tripDocumentList);
            Bson filter = Filters.eq("USER_ID", userId); 
            Bson updates = Updates.combine(Updates.set("TRIPS", tripDocumentList));
            tripCollection.findOneAndUpdate(filter, updates);
            Document t = tripCollection.find(filter).first();
            System.out.println("result " + t.toJson());
            return t.toJson();
        } catch (Exception e) {
            System.out.println("ERROR add!!");
            System.out.println(e.getMessage());
            return e.toString();
        }
    }
    // ok
    public String updateTrip(int userId, SingleTrip trip) {
        try {
//            MongoDatabase tripDB = mongoClient.getDatabase("trip");
            MongoCollection<Document> tripCollection = getTripCollection(); // tripDB.getCollection("trip");
            Document data = tripCollection.find(new Document("USER_ID", userId)).first();
            int idViaggio = trip.getTripId();
            Gson gson = new Gson();
            TripDocument tripDocument = gson.fromJson(data.toJson(), TripDocument.class);
            List<Document> tripDocumentList = tripDocument.getTrips();
            for (int i = 0; i < tripDocumentList.size(); ++i) {
                SingleTrip singleTrip = gson.fromJson(tripDocumentList.get(i).toJson(), SingleTrip.class);
                if (singleTrip.getTripId() == idViaggio) {
                    Document newDoc = new Document();
                        newDoc.append("ID", idViaggio)
                            .append("DATE", trip.getDate())
                            .append("PLACE", trip.getPlace())
                            .append("TRANSPORTATION", trip.getTransportation())
                            .append("GEOJSON", trip.getGeoJson());
//                            .append("PATH", trip.getPath())
//                            .append("MAIN_STAGES", trip.getMainStages());
                    tripDocumentList.set(i, newDoc);
                }
            }
            Bson filter = Filters.eq("USER_ID", userId);
            Bson updates = Updates.combine(Updates.set("TRIPS", tripDocumentList));
            tripCollection.findOneAndUpdate(filter, updates);
            Document t = tripCollection.find(filter).first();
            System.out.println("result update" + t.toJson());
            return t.toJson();
        } catch (Exception e) {
            System.out.println("ERROR update!!");
            System.out.println(e.getMessage());
            return e.toString();
        }
    }


    // UTENTI
    // public List<String> getUsers() {
    //     try {
    //         MongoDatabase tripDB = mongoClient.getDatabase("trip");
    //         MongoCollection<Document> userCollection = tripDB.getCollection("users");
    //         List<Document> users = userCollection.find().into(new ArrayList<Document>());
    //         List<String> usersJson = new ArrayList<>(users.size());
    //         for (Document d : users) {
    //             usersJson.add(d.toJson());
    //         }
    //         System.out.println("trips = " + usersJson);
    //         return usersJson;
    //     } catch (Exception e) {
    //         System.out.println("ERROR!!");
    //         System.out.println(e.getStackTrace());
    //         return Collections.singletonList(e.toString());
    //     }
    // }

    public boolean loginUser(String username, String password/*User user*/) {
        try {
//            MongoDatabase tripDB = mongoClient.getDatabase("trip");
            MongoCollection<Document> userCollection = getTripCollection();//tripDB.getCollection("user");
            Document credential = userCollection.find(new Document("USERNAME", username)).first(); // user.getUsername()
            
            if (credential == null) {
                // return "nulle";
                System.out.println("Credenziali nulle");
                return false;
            } else {
                if (password.equals(credential.get("PASSWORD"))) {
                    // ok
                    System.out.println("OK");
                    // return credential.toJson();
                    return true;
                } else {
                    // password non valida
                    System.out.println("ERROR: password non valida!");
                    // return "ERRORE";
                    return false;
                }
            }
        } catch (Exception e) {
            System.out.println("ERROR add!!");
            System.out.println(e.getMessage());
            // return e.toString();
            return false;
        }
    }
        // try {
        //     MongoDatabase tripDB = mongoClient.getDatabase("trip");
        //     MongoCollection<Document> userCollection = tripDB.getCollection("users");
        //     Document credential = userCollection.find(new Document("USERNAME", user.getUsername())).first();
        //     // System.out.println("user" + user);
        //     System.out.println(credential);
        //     if (credential == null) {
        //         System.out.println("ERROR: no credential found!");
        //         return "ERRORE";
        //     } else {
        //         System.out.println(credential.toJson());
        //         if (user.getPassword().equals(credential.get("PASSWORD"))) {
        //             // ok
        //             System.out.println("OK");
        //             String username = (String) credential.get("USERNAME");
        //             int id = (int) credential.get("ID");
        //             String nome = (String) credential.get("NOME");
        //             String cognome = (String) credential.get("COGNOME");
        //             String role = (String) credential.get("ROLE");
        //             String response = "{\"ID\": "+ id + ",\"NOME\":" + nome + ",\"COGNOME\":" + cognome + ",\"USERNAME\":" + username + ",\"ROLE\":" + role + ",\"STATUS\":" + 200 +"}";
        //             return response;

        //         } else {
        //             // password non valida
        //             System.out.println("ERROR: password non valida!");
        //             return "ERRORE";
        //         }
        //     }
        // } catch (Exception e) {
        //     System.out.println("ERROR!!");
        //     // System.out.println(e.getStackTrace());
        //     return "errore";       
        // }
}