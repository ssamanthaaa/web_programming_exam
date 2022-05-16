package it.units;

// import javax.sql.DataSource;
import javax.annotation.security.RolesAllowed;
//import javax.servlet.annotation.WebServlet;
// import javax.json.Json;
// import javax.json.JsonObjectBuilder;
// import javax.security.auth.login.LoginException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;

import java.util.List;

//@WebServlet("/exam")
@Path("{userId}/trips")
public class TripResource {
	// OK IT WORKS
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTrips(@PathParam("userId") int userId) {
        MongoDBClient mongo = MongoDBClient.getInstance();
        List<String> trips = mongo.getTrips(userId);
        StringBuilder sb = new StringBuilder("[");
        String prefix = "";
        for (String trip : trips) {
            sb.append(prefix);
            prefix = ",";
            sb.append(trip);
        }
        sb.append("]");

        return Response.ok(sb.toString()).build();

    }

    // OK IT WORKS
    @GET
    @Path("/{tripId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTrip(@PathParam("userId") int userId, @PathParam("tripId") int tripId) {
        MongoDBClient mongo = MongoDBClient.getInstance();
        return Response.ok(mongo.getTrip(userId, tripId)).build();

    }

    // OK IT WORKS
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response setTrip(@PathParam("userId") int userId, String tripJsonString) {
        Gson gson = new Gson();
        SingleTrip trip = gson.fromJson(tripJsonString, SingleTrip.class);
        MongoDBClient.getInstance().addTrip(userId, trip);
        return Response.status(201).entity(gson.toJson(trip)).build();
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateTrip(@PathParam("userId") int userId, String tripJsonString) {
        Gson gson = new Gson();
        SingleTrip trip = gson.fromJson(tripJsonString, SingleTrip.class);
        MongoDBClient.getInstance().updateTrip(userId, trip);
        return Response.status(201).entity(gson.toJson(trip)).build();
    }


//    @RolesAllowed("admin")
//    @POST
//    @Path("/auth")
//    @Produces(MediaType.APPLICATION_JSON)
//    @Consumes(MediaType.APPLICATION_JSON)
//    public Response loginUser(@Context HttpServletRequest req, String userJsonString) {
//        HttpSession session = req.getSession();
//        Gson gson = new Gson();
//        User user = gson.fromJson(userJsonString, User.class);
//        // DataSource.getConnection(user.getUsername(), user.getPassword());
//        MongoDBClient.getInstance().loginUser(user.getUsername(), user.getPassword());
//        return Response.status(201).entity(gson.toJson(user)).build();
//    }

    // @DELETE
    // @Path("/logout")
    // @Produces(MediaType.APPLICATION_JSON)
    // public Response logoutUser() {

    // }
   
}
