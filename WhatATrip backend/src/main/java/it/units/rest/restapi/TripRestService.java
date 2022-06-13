package it.units.rest.restapi;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.security.DeclareRoles;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.server.ResourceConfig;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

//import it.units.MongoDBClient;
import it.units.rest.database.connection.SQLConnection;
import it.units.rest.database.dao.TripDAO;
import it.units.rest.database.dao.TripDAOFactory;
import it.units.rest.database.dao.UserDAO;
import it.units.rest.database.dao.UserDAOFactory;
import it.units.rest.exception.UserExistingException;
import it.units.rest.exception.UserNotFoundException;
import it.units.rest.filter.AuthenticationFilter;
import it.units.rest.model.Credentials;
import it.units.rest.model.JsonSerializable;
import it.units.rest.model.Trip;
import it.units.rest.model.User;
import it.units.rest.security.PasswordSecurity;

@Path("{userId}/trips")
public class TripRestService extends ResourceConfig{
	
	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllUserTrips(@Context HttpHeaders headers, @PathParam("userId") Integer userId) {
		TripDAO tripDAO = TripDAOFactory.getTripDAO();
		
		try {
			Integer id = getId( headers );
			
			JsonArray tripsJson = new JsonArray();
			tripsJson.addAll(tripDAO.getAllUserTrips(userId));
			
			return Response.status(Response.Status.OK).entity(tripsJson.toString()).type(MediaType.APPLICATION_JSON).build();

		}
		catch( UserNotFoundException e ) {
			return ResponseBuilder.createResponse( Response.Status.NOT_FOUND, e.getMessage() );
		}
		catch ( Exception e ) {
			return ResponseBuilder.createResponse( Response.Status.UNAUTHORIZED );
		}
	}
	
	@GET
	@Path("/{tripId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getTrip(@Context HttpHeaders headers, @PathParam("userId") Integer userId, @PathParam("tripId") Integer tripId) {
		TripDAO tripDAO = TripDAOFactory.getTripDAO();

		try {
			Integer id = getId( headers );
			if (id != userId) {
				return ResponseBuilder.createResponse( Response.Status.FORBIDDEN, "This user is not allowed to see other users trips!" );
			}
			
			JsonObject trip = tripDAO.getTrip(userId, tripId);
			
			System.out.println("trip --> " + trip);
			// Return the users on the response
			return Response.status(Response.Status.OK).entity(trip.toString()).type(MediaType.APPLICATION_JSON).build();
		}
		catch( UserNotFoundException e ) {
			return ResponseBuilder.createResponse( Response.Status.NOT_FOUND, e.getMessage() );
		}
		catch ( Exception e ) {
			return ResponseBuilder.createResponse( Response.Status.UNAUTHORIZED );
		}
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
	public Response createTrip(@Context HttpHeaders headers, @PathParam("userId") Integer userId, String tripString) {
		TripDAO tripDAO = TripDAOFactory.getTripDAO();
		try {
			System.out.println("Controllo JWT");
			Integer id = getId(headers );
			System.out.println(id);
			System.out.println(userId.equals(id));
			if ( !userId.equals(id)) {
				System.out.println("QUA");
				return ResponseBuilder.createResponse( Response.Status.NOT_FOUND );
			}
			System.out.println(tripString);
			Gson gson = new GsonBuilder().setDateFormat("yyy-MM-dd").create();

	        Trip trip = gson.fromJson(tripString, Trip.class);
			tripDAO.createTrip(trip);
			return ResponseBuilder.createResponse( Response.Status.OK, "Trip created" );
		} 
		catch( UserNotFoundException e ) {
			return ResponseBuilder.createResponse( Response.Status.NOT_FOUND, e.getMessage() );
		}
		catch ( Exception e ) {
			System.out.println(e);
			return ResponseBuilder.createResponse( Response.Status.INTERNAL_SERVER_ERROR );
		}
	}
	
	@PUT
	@Path("/{tripId}")
	@Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
	public Response updateTrip(@Context HttpHeaders headers, @PathParam("userId") Integer userId, @PathParam("tripId") Integer tripId, String tripString) {
		TripDAO tripDAO = TripDAOFactory.getTripDAO();
		try {
			System.out.println("Controllo JWT");
			Integer id = getId(headers );
			if ( id != userId) {
				return ResponseBuilder.createResponse( Response.Status.FORBIDDEN );
			}
			System.out.println(tripString);
			Gson gson = new GsonBuilder().setDateFormat("yyy-MM-dd").create();
	        Trip trip = gson.fromJson(tripString, Trip.class);
	        if (trip.getId() != tripId) {
	        	return ResponseBuilder.createResponse( Response.Status.FORBIDDEN );
	        }
			tripDAO.updateTrip(trip);
			return ResponseBuilder.createResponse( Response.Status.OK, "Trip updated" );
		} 
		catch( UserNotFoundException e ) {
			return ResponseBuilder.createResponse( Response.Status.NOT_FOUND, e.getMessage() );
		}
		catch ( Exception e ) {
			System.out.println("SONO QUA");
			System.out.println(e);
			return ResponseBuilder.createResponse( Response.Status.INTERNAL_SERVER_ERROR );
		}
	}
	
	
	@DELETE
	@Path("/{tripId}")
	@Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
	public Response deleteTrip(@Context HttpHeaders headers, @PathParam("userId") Integer userId, @PathParam("tripId") Integer tripId) {
		TripDAO tripDAO = TripDAOFactory.getTripDAO();
		try {
			System.out.println("Controllo JWT");
			Integer id = getId(headers );
			if ( id != userId) {
				return ResponseBuilder.createResponse( Response.Status.FORBIDDEN );
			}
			Gson gson = new GsonBuilder().setDateFormat("yyy-MM-dd").create();

			tripDAO.deleteTrip(tripId);
			return ResponseBuilder.createResponse( Response.Status.OK, "Trip deleted" );
		} 
		catch( UserNotFoundException e ) {
			return ResponseBuilder.createResponse( Response.Status.NOT_FOUND, e.getMessage() );
		}
		catch ( Exception e ) {
			System.out.println("SONO QUA");
			System.out.println(e);
			return ResponseBuilder.createResponse( Response.Status.INTERNAL_SERVER_ERROR );
		}
	}
	
	
	
	private Integer getId( HttpHeaders headers) {
		// get the email we set in AuthenticationFilter
		List<String> id = headers.getRequestHeader( AuthenticationFilter.HEADER_PROPERTY_ID );
		
		if( id == null || id.size() != 1 )
			throw new NotAuthorizedException("Unauthorized!");
		
		return Integer.parseInt(id.get(0));
	}
	
}
