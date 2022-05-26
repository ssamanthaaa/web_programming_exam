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
//		logger.debug( "createTrip: " + trip.getId() );
		System.out.println(trip);
		PreparedStatement stmt = null;
	    try {
//	    	System.out.println(trip.getGeoJson());
	    	
	    	stmt = connection.prepareStatement( "INSERT INTO TRIP(" + 
	    									    "DATE,PLACE, TRANSPORTATION, GEOJSON, USER) VALUES" +
	    									    "(?,?,?,?,?)" );
	    	stmt.setDate( 1, trip.getDate() );
	    	stmt.setString( 2, trip.getPlace() );
	    	stmt.setString( 3, trip.getTransportation() );
	    	stmt.setObject(4, trip.getGeoJson());
	    	stmt.setInt(5, trip.getUser());
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
	    	stmt = connection.prepareStatement( "SELECT ID, DATE, PLACE, TRANSPORTATION, GEOJSON, USER FROM TRIP WHERE ID=? AND USER=?;" );
	    	stmt.setInt(1, tripId);
	    	stmt.setInt(2, userId);
		    rs = stmt.executeQuery();
		    if( rs.next() ) {
	//		    Integer tripId = rs.getInt("ID");
		    	Date tripDate = rs.getDate("DATE");
		    	String tripPlace = rs.getString("PLACE");
		    	String tripTransportation = rs.getString("TRANSPORTATION");
		    	Object tripGeoJson = rs.getObject("GEOJSON");
	//		    Integer tripUser = rs.getInt("USER");
		    	
		    	trip = new Trip( tripId, tripDate, tripPlace, tripTransportation, tripGeoJson, userId);
		    	
				tripJson.addProperty("ID", tripId);
				tripJson.addProperty("DATE", tripDate.toString());
				tripJson.addProperty("PLACE", tripPlace);
				tripJson.addProperty("TRANSPORTATION", tripTransportation);
				tripJson.addProperty("GEOJSON", tripGeoJson.toString());
				tripJson.addProperty("USER", userId);
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
//		List<JsonObject> tripList = new ArrayList<JsonObject>();
		JsonArray tripArray = new JsonArray();
		
	    try {
	    	stmt = connection.prepareStatement( "SELECT ID, DATE, PLACE, TRANSPORTATION, GEOJSON, USER FROM TRIP WHERE USER=?;" );
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
		    	
		    	tripJson.addProperty("ID", tripId);
				tripJson.addProperty("DATE", tripDate.toString());
				tripJson.addProperty("PLACE", tripPlace);
				tripJson.addProperty("TRANSPORTATION", tripTransportation);
				tripJson.addProperty("GEOJSON", tripGeoJson.toString());
				tripJson.addProperty("USER", userId);
				System.out.println("tripJson: " + tripJson);
		    	
//				tripArray.add(tripJson);
				tripArray.add(tripJson);
//		    	trip.add( new Trip(tripId, tripDate, tripPlace, tripTransportation, tripGeoJson, tripUser) );
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
//	    System.out.println(trip);
	    return tripArray;
	}


	public boolean updateTrip( Trip trip) throws TripNotFoundException {
		logger.debug( "updateTrip: " + trip.getId() );
		
		PreparedStatement stmt = null;
		
	    try {
	    	// prepare query
	    	StringBuffer query = new StringBuffer();
	    	query.append( "UPDATE TRIP SET " );
	    	
	    	boolean comma = false;
	    	List<Object> prepare = new ArrayList<>();
	    	
	    	if( trip.getId() != null ) {
	    		if( comma ) query.append(",");
	    		query.append( "ID=?" );
	    		prepare.add( trip.getId() );
	    	}
	    	if( trip.getDate() != null ) {
	    		if( comma ) query.append(",");
	    		query.append( "DATE=?" );
	    		prepare.add( trip.getDate() );
	    	}
	    	if( trip.getPlace() != null ) {
	    		if( comma ) query.append(",");
	    		query.append( "PLACE=?" );
	    		prepare.add( trip.getPlace() );
	    	}
	    	if( trip.getTransportation() != null ) {
	    		if( comma ) query.append(",");
	    		query.append( "TRANSPORTATION=?" );
	    		prepare.add( trip.getTransportation() );
	    	}
	    	if( trip.getGeoJson() != null ) {
	    		if( comma ) query.append(",");
	    		query.append( "GEOJSON=?" );
	    		prepare.add( trip.getGeoJson() );
	    	}
	    	if( trip.getUser() != null ) {
	    		if( comma ) query.append(",");
	    		query.append( "USER=?" );
	    		prepare.add( trip.getUser() );
	    	}
	    	
	    	query.append(" WHERE ID=?");
	    	stmt = connection.prepareStatement( query.toString() );
	    	
	    	for( int i = 0; i < prepare.size(); i++ ) {
	    		stmt.setString( i+1, prepare.get(i).toString() );
	    	}
	    	
	    	stmt.setInt( prepare.size() + 1, trip.getId() );
	    	
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