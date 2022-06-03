package it.units.rest.model;


import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class User implements JsonSerializable {
	
	// global unique identifier
	private Integer id = null;
	private String username = null;
	private String email = null;
	private String password;
	
	public User() {}
	
	public User( String username ) {			
		this.username = username;
	}
	
	public User( Integer id, String username, String email ) {	
		this.id = id;
		this.username = username;
		this.email = email;
	}
	public User( Integer id, String username, String email, String password ) {	
		this.id = id;
		this.username = username;
		this.email = email;
		this.password = password;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}
	
	public void setUsername(String email) {
		this.username = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}


	@Override
	public String toString() {
		return "User [id=" + id + ", username=" + username + ", email= " + email +  "]";
	}

	@Override
	public JSONObject toJson() throws JSONException {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put( "username", this.username );
		return jsonObject;
	}
	
}