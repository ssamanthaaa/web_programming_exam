package it.units.rest.restapi;


import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.security.DeclareRoles;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.servlet.annotation.WebServlet;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.server.ResourceConfig;

import com.google.gson.JsonObject;

import it.units.rest.database.dao.UserDAO;
import it.units.rest.database.dao.UserDAOFactory;
import it.units.rest.exception.UserExistingException;
import it.units.rest.exception.UserNotFoundException;
import it.units.rest.filter.AuthenticationFilter;
import it.units.rest.model.Credentials;
import it.units.rest.model.JsonSerializable;
import it.units.rest.model.User;
import it.units.rest.model.UserSecurity;
import it.units.rest.security.PasswordSecurity;
import it.units.rest.security.TokenSecurity;

@DeclareRoles({"admin", "user", "guest"})
@Path("/user")
public class UserRestService extends ResourceConfig {
	
	@POST
	@Path("/create")
	@PermitAll
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createUser( UserSecurity userSecurity ) {
		UserDAO userDao = UserDAOFactory.getUserDAO();
		
		try {
			try {
				// check if user no registered already
//				Integer idByUsername = 
				 userDao.getUserIdByUsername( userSecurity.getUsername() );
//				 userDao.getUserIdByEmail( userSecurity.getEmail());
	//				System.out.println(idByUsername);
					throw new UserExistingException( userSecurity.getUsername() );
				
			}
			
			catch( UserNotFoundException e ) {
				System.out.println("email e username ok, continuo");
				// standard user role
				userSecurity.setRole("user");
				// store plain password
				String plainPassword = userSecurity.getPassword();
				// generate password
				userSecurity.setPassword( PasswordSecurity.generateHash( userSecurity.getPassword() ) );
				// create user
				userDao.createUser( userSecurity );
				// authenticate user
				return authenticate( new Credentials( userSecurity.getUsername(), plainPassword ) );
				
			}
		}
		catch ( UserExistingException e ) {
			System.out.println("(userRestService) UserExistingException");
			return ResponseBuilder.createResponse( Response.Status.CONFLICT, e.getMessage() );
		} 
		catch ( Exception e ) {
			System.out.println("Exception");
			return ResponseBuilder.createResponse( Response.Status.INTERNAL_SERVER_ERROR );
		}
	}
	
	@POST
	@Path("/authenticate")
	@PermitAll
	@Produces("application/json")
	@Consumes("application/json")
	public Response authenticate( Credentials credentials ) {
		UserDAO userDao = UserDAOFactory.getUserDAO();
		
		try {
			Integer id = userDao.getUserIdByUsername( credentials.getUsername() );
			UserSecurity userSecurity = userDao.getUserAuthentication( id );
			
			if( PasswordSecurity.validatePassword( credentials.getPassword(), userSecurity.getPassword() ) == false ) {
				return ResponseBuilder.createResponse( Response.Status.UNAUTHORIZED );
			}

			// generate a token for the user
			String token = TokenSecurity.generateJwtToken( id );
			
			// write the token to the database
			UserSecurity sec = new UserSecurity( null, token );
			sec.setId( id );
			userDao.setUserAuthentication( sec );
			
			Map<String,Object> map = new HashMap<String,Object>();
			map.put( AuthenticationFilter.AUTHORIZATION_PROPERTY, token );
			
			// Return the token on the response
			
//			return ResponseBuilder.createResponse( Response.Status.OK, map );
			JsonObject responJson = new JsonObject();
			responJson.addProperty("USERNAME", userSecurity.getUsername());
			responJson.addProperty("EMAIL", userSecurity.getEmail());
			responJson.addProperty("ID", id);
			responJson.addProperty("x-access-token", token);
//			String tokenJson = "{\"x-access-token\": \""+token+"\"}";
			return Response.status(Response.Status.OK).entity(responJson.toString()).type(MediaType.APPLICATION_JSON).build();
		}
		catch( UserNotFoundException e ) {
			return ResponseBuilder.createResponse( Response.Status.NOT_FOUND, e.getMessage() );
		}
		catch( Exception e ) {
			return ResponseBuilder.createResponse( Response.Status.UNAUTHORIZED );
		}
		
	}
	
	@GET
	@Path("/get")
	@RolesAllowed({"admin","user"})
	@Produces("application/json")
	public Response get( @Context HttpHeaders headers ) {
		UserDAO userDao = UserDAOFactory.getUserDAO();
		
		try {
			Integer id = getId( headers );
			
			// use decoded email from jwt in header
			User user = userDao.getUser( id );
			
			// Return the user on the response
			return ResponseBuilder.createResponse( Response.Status.OK, user );
		}
		catch( UserNotFoundException e ) {
			return ResponseBuilder.createResponse( Response.Status.NOT_FOUND, e.getMessage() );
		}
		catch ( Exception e ) {
			return ResponseBuilder.createResponse( Response.Status.UNAUTHORIZED );
		}
	}
	
	@GET
	@Path("/getAll")
	@RolesAllowed({"admin"}) // only an admin user should be allowed to request all users
	@Produces("application/json")
	public Response getAll( @Context HttpHeaders headers ) {
		UserDAO userDao = UserDAOFactory.getUserDAO();
		
		try {
			List<JsonSerializable> usersJson = new ArrayList<JsonSerializable>();
			usersJson.addAll( (Collection<? extends JsonSerializable>) userDao.getAllUsers() );
			
			// Return the users on the response
			return ResponseBuilder.createResponse( Response.Status.OK, usersJson );
		}
		catch( UserNotFoundException e ) {
			return ResponseBuilder.createResponse( Response.Status.NOT_FOUND, e.getMessage() );
		}
		catch ( Exception e ) {
			return ResponseBuilder.createResponse( Response.Status.UNAUTHORIZED );
		}
		
	}
	
	@POST
	@Path("/update")
	@RolesAllowed({"admin","user"}) // only an admin user should be allowed to request all users
	@Produces("application/json")
	public Response update( @Context HttpHeaders headers, User user ) {
		UserDAO userDao = UserDAOFactory.getUserDAO();
		
		try {
			Integer id = getId( headers );
			
			user.setId( id );
			userDao.updateUser( user );
			
			// Return the token on the response
			return ResponseBuilder.createResponse( Response.Status.OK, "User updated" );
		}
		catch( UserNotFoundException e ) {
			return ResponseBuilder.createResponse( Response.Status.NOT_FOUND, e.getMessage() );
		}
		catch ( Exception e ) {
			return ResponseBuilder.createResponse( Response.Status.UNAUTHORIZED );
		}
		
	}
	
	@DELETE
	@Path("/delete")
	@RolesAllowed({"admin","user"}) 
	@Produces("application/json")
	public Response delete( @Context HttpHeaders headers ) {
		UserDAO userDao = UserDAOFactory.getUserDAO();
		
		try {
			Integer id = getId( headers );
			
			userDao.deleteUser( id );
			
			// Return the response
			return ResponseBuilder.createResponse( Response.Status.OK, "User deleted" );
		}
		catch( UserNotFoundException e ) {
			return ResponseBuilder.createResponse( Response.Status.NOT_FOUND, e.getMessage() );
		}
		catch ( Exception e ) {
			return ResponseBuilder.createResponse( Response.Status.UNAUTHORIZED );
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


