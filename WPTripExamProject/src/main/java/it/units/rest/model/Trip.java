package it.units.rest.model;

//import java.util.Date;
import java.sql.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

//import org.bson.Document;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

@Entity
@Table(name = "TRIP")
public class Trip implements JsonSerializable {
	private Integer ID;
	private Date DATE;
	private String PLACE;
	
	private String TRANSPORTATION;
	private Object GEOJSON;
	private Integer USER;
	

	public Trip(Integer ID, Date DATE, String PLACE, String TRANSPORTATION, Object GEOJSON, Integer USER) {
		this.ID = ID;
		this.DATE = DATE;
		this.PLACE = PLACE;
		this.TRANSPORTATION = TRANSPORTATION;
		this.GEOJSON = GEOJSON;
		this.USER = USER;
	}
	
	public Trip(Date DATE, String PLACE, String TRANSPORTATION, Object GEOJSON, Integer USER) {
		this.DATE = DATE;
		this.PLACE = PLACE;
		this.TRANSPORTATION = TRANSPORTATION;
		this.GEOJSON = GEOJSON;
		this.USER = USER;
	}
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getId() {
		return ID;
	}
	
	public Date getDate() {
		return DATE;
	}
	
	public String getPlace() {
		return PLACE;
	}
	

	public String getTransportation() {
		return TRANSPORTATION;
	}
	
	public Object getGeoJson() {
		return GEOJSON;
	}
	
	
	public Integer getUser() {
		return USER;
	}
	
	public void setId(int ID) {
		this.ID = ID;
	}
	
	public void setDate(Date DATE) {
		this.DATE = DATE;
	}
	
	public void setPlace(String PLACE) {
		this.PLACE = PLACE;
	}
	
	public void setTransportation(String TRANSPORTATION) {
		this.TRANSPORTATION = TRANSPORTATION;
	}
	
	 public void setGeoJson(Object GEOJSON) {
		 this.GEOJSON = GEOJSON;
	 }
	
	
	public void setUser(Integer USER) {
		this.USER = USER;
	}
	
	@Override
	public String toString() {
		return "Trip: [ ID: " + ID + ", DATE: " + DATE + ". PLACE: " + PLACE + ", TRANSPORTATION: " + TRANSPORTATION + ",\n GEOJOSN: " + GEOJSON + ",\n USER= "+ USER + " ]"; 
	}
	
	@Override
	public JSONObject toJson() throws JSONException {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put( "tripId", this.ID );
		return jsonObject;
	}
}