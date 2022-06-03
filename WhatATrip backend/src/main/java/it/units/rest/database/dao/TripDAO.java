package it.units.rest.database.dao;


import java.sql.SQLException;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import it.units.rest.exception.TripNotFoundException;
import it.units.rest.model.Trip;

public interface TripDAO {
	
	public boolean createTrip( Trip trip ) throws SQLException;
	
	public JsonObject getTrip(Integer userId, Integer tripId) throws TripNotFoundException;
	
	public JsonArray getAllUserTrips(Integer userId);
	
	
	public boolean updateTrip( Trip trip) throws TripNotFoundException;
	public boolean deleteTrip( Integer id ) throws TripNotFoundException;

}
