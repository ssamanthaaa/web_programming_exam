package it.units.rest.model;


public class UserSecurity extends User {
	
	private String password = null;
	private String token = null;
	private String role = null;
	
	public UserSecurity() {}

	
	public UserSecurity( String password, String token ) {
		this.password = password;
		this.token = token;
		toString();
	}
	
	public UserSecurity( String password, String token, String role ) {
		this.password = password;
		this.token = token;
		this.role = role;
		toString();
	}
	
	
	public UserSecurity( String username, String email, String password, String token, String role ) {
		this.setUsername( username );
		this.setEmail(email);
		this.password = password;
		this.token = token;
		this.role = role;
		toString();
	}

	
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
	
	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	@Override
	public String toString() {
		return "UserSecurity [id="+ this.getId() + ", username=" + this.getUsername() + ", email= " + this.getEmail() + ", role=" + role + ", password=" + password + ", token=" + token + "]";
	}

}