package it.units.rest.model;


import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class User implements JsonSerializable {
	
	// global unique identifier
	private String id = null;
	private String username = null;
	public User() {}
	
	public User( String username ) {			
		this.username = username;
	}
	
	public User( String id, String username ) {	
		this.id = id;
		this.username = username;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String email) {
		this.username = email;
	}


	@Override
	public String toString() {
		return "User [id=" + id + ", username=" + username +  "]";
	}

	@Override
	public JSONObject toJson() throws JSONException {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put( "username", this.username );
		return jsonObject;
	}
	
}