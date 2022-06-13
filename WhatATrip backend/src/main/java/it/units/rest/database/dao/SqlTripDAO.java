package it.units.rest.database.dao;

import java.io.Console;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import it.units.rest.database.dao.TripDAO;
//import it.units.rest.exception.UserExistingException;
//import it.units.rest.exception.UserNotFoundException;
import it.units.rest.exception.TripNotFoundException;
import it.units.rest.exception.UserNotFoundException;
import it.units.rest.model.Trip;
import it.units.rest.model.User;
import it.units.rest.model.UserSecurity;

public class SqlTripDAO implements TripDAO{


	final static Logger logger = Logger.getLogger( SqlUserDAO.class );
	
	private Connection connection = null;
	
	public SqlTripDAO( it.units.rest.database.connection.Connection connection ) {
		this.connection = (Connection) connection.get();
	}


	public boolean createTrip( Trip trip ) throws SQLException {
		System.out.println(trip);
		PreparedStatement stmt = null;
	    try {
	    	
	    	stmt = connection.prepareStatement( "INSERT INTO TRIP(" + 
	    									    "DATE,PLACE, TRANSPORTATION, GEOJSON, USER, DESCRIPTION) VALUES" +
	    									    "(?,?,?,?,?,?)" );
	    	stmt.setDate( 1, trip.getDate() );
	    	stmt.setString( 2, trip.getPlace() );
	    	stmt.setString( 3, trip.getTransportation() );
	    	stmt.setObject(4, trip.getGeoJson());
	    	stmt.setInt(5, trip.getUser());
	    	stmt.setString(6, trip.getDescription());
		    stmt.executeUpdate();
		    System.out.println(stmt);
		    
	    } catch ( SQLException e ) {
	    	logger.debug( e.getClass().getName() + ": " + e.getMessage() );
	    }
	    finally {
	    	try {
				stmt.close();
			} catch ( SQLException e ) {
				logger.warn( e.getMessage() );
			}
	    }
	    
	    return true;
	}

	
	public JsonObject getTrip(Integer userId, Integer tripId) throws TripNotFoundException {
		logger.debug( "getTrip: " + tripId + " idUser: " + userId);
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Trip trip = null;
		JsonObject tripJson = new JsonObject();
		
	    try {
	    	stmt = connection.prepareStatement( "SELECT ID, DATE, PLACE, TRANSPORTATION, GEOJSON, USER, DESCRIPTION FROM TRIP WHERE ID=? AND USER=?;" );
	    	stmt.setInt(1, tripId);
	    	stmt.setInt(2, userId);
		    rs = stmt.executeQuery();
		    if( rs.next() ) {
		    	Date tripDate = rs.getDate("DATE");
		    	String tripPlace = rs.getString("PLACE");
		    	String tripTransportation = rs.getString("TRANSPORTATION");
		    	Object tripGeoJson = rs.getObject("GEOJSON");
		    	String tripDescription = rs.getString("DESCRIPTION");
		    	
		    	trip = new Trip( tripId, tripDate, tripPlace, tripTransportation, tripGeoJson, userId, tripDescription);
		    	
				tripJson.addProperty("ID", tripId);
				tripJson.addProperty("DATE", tripDate.toString());
				tripJson.addProperty("PLACE", tripPlace);
				tripJson.addProperty("TRANSPORTATION", tripTransportation);
				tripJson.addProperty("GEOJSON", tripGeoJson.toString());
				tripJson.addProperty("USER", userId);
				tripJson.addProperty("DESCRIPTION", tripDescription);
		    }
		    else {
		    	throw new TripNotFoundException( tripId );
		    }
		    
	    } catch ( SQLException e ) {
	    	logger.debug( e.getClass().getName() + ": " + e.getMessage() );
	    }
	    finally {
	    	try {
				rs.close();
				stmt.close();
			} catch ( SQLException e ) {
				logger.warn( e.getMessage() );
			}
	    }
	    System.out.println(trip);
	    return tripJson;
	}

	public JsonArray getAllUserTrips(Integer userId) {
		logger.debug( "getAllUsers " + userId );
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<Trip> trip = new ArrayList<Trip>();
		JsonArray tripArray = new JsonArray();
		
	    try {
	    	stmt = connection.prepareStatement( "SELECT ID, DATE, PLACE, TRANSPORTATION, GEOJSON, USER, DESCRIPTION FROM TRIP WHERE USER=?;" );
	    	stmt.setInt(1, userId);
	    	rs = stmt.executeQuery();
		    System.out.println(stmt);

		    while( rs.next() ) {
		    	JsonObject tripJson = new JsonObject();
		    	
		    	Integer tripId = rs.getInt("ID");
		    	Date tripDate = rs.getDate("DATE");
		    	String tripPlace = rs.getString("PLACE");
		    	String tripTransportation = rs.getString("TRANSPORTATION");
		    	Object tripGeoJson = rs.getObject("GEOJSON");
		    	Integer tripUser = rs.getInt("USER");
		    	String tripDescription = rs.getString("DESCRIPTION");
		    	
		    	tripJson.addProperty("ID", tripId);
				tripJson.addProperty("DATE", tripDate.toString());
				tripJson.addProperty("PLACE", tripPlace);
				tripJson.addProperty("TRANSPORTATION", tripTransportation);
				tripJson.addProperty("GEOJSON", tripGeoJson.toString());
				tripJson.addProperty("USER", userId);
				tripJson.addProperty("DESCRIPTION", tripDescription);
				System.out.println("tripJson: " + tripJson);
		    	
				tripArray.add(tripJson);
		    }
		    
	    } catch ( SQLException e ) {
	    	System.out.println(e);
	    	logger.debug( e.getClass().getName() + ": " + e.getMessage() );
	    }
	    finally {
	    	try {
				rs.close();
				stmt.close();
			} catch ( SQLException e ) {
				logger.warn( e.getMessage() );
			}
	    }
	    return tripArray;
	}


	public boolean updateTrip( Trip trip) throws TripNotFoundException {
		logger.debug( "updateTrip: " + trip.getId() );
		PreparedStatement stmt = null;
		
	    try {
	    	stmt = connection.prepareStatement( "UPDATE TRIP SET DATE=?, PLACE=?, TRANSPORTATION=?, GEOJSON=?, USER=?, DESCRIPTION=? WHERE ID=?;" );
	    	stmt.setDate(1, trip.getDate());
	    	stmt.setString(2, trip.getPlace());
	    	stmt.setString(3, trip.getTransportation());
	    	stmt.setObject(4, trip.getGeoJson());
	    	stmt.setInt(5, trip.getUser());
	    	stmt.setString(6, trip.getDescription());
	    	stmt.setInt(7, trip.getId());
	    	
	    	stmt.executeUpdate();
		    
	    } catch ( SQLException e ) {
	    	logger.debug( e.getClass().getName() + ": " + e.getMessage() );
	    }
	    finally {
	    	try {
				stmt.close();
			} catch ( SQLException e ) {
				logger.warn( e.getMessage() );
			}
	    }
		
		return true;
	}


	public boolean deleteTrip( Integer id ) throws TripNotFoundException {
		logger.debug( "deleteTrip: " + id );
		
		PreparedStatement stmt = null;
		
	    try {
	    	
	    	stmt = connection.prepareStatement( "DELETE FROM TRIP WHERE ID=?" );
	    	stmt.setInt( 1, id );
	    	
		    stmt.executeUpdate();
		    
	    } catch ( SQLException e ) {
	    	logger.debug( e.getClass().getName() + ": " + e.getMessage() );
	    }
	    finally {
	    	try {
				stmt.close();
			} catch ( SQLException e ) {
				logger.warn( e.getMessage() );
			}
	    }
	    
	    return true;
	}
}