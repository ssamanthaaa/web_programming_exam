package it.units.rest.database.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import it.units.rest.database.dao.UserDAO;
import it.units.rest.exception.UserExistingException;
import it.units.rest.exception.UserNotFoundException;
import it.units.rest.model.User;
import it.units.rest.model.UserSecurity;


public class SqlUserDAO implements UserDAO {
	final static Logger logger = Logger.getLogger( SqlUserDAO.class );
	
	private Connection connection = null;
	
	public SqlUserDAO( it.units.rest.database.connection.Connection connection ) {
		this.connection = (Connection) connection.get();
	}

	@Override
	public boolean createUser( UserSecurity user ) throws UserExistingException {
		logger.debug( "createUser: " + user.getUsername() );
		
		PreparedStatement stmt = null;
		
	    try {
	    	
			// check if user already registered
			try {
				if( getUserIdByUsername( user.getUsername() ) != null ) {
					throw new UserExistingException( user.getUsername() );
				}
			}
			// continue if no user found
			catch( UserNotFoundException e) {}
	    	
	    	stmt = connection.prepareStatement( "INSERT INTO USER(" + 
	    									    "USERNAME,EMAIL, PASSWORD,ROLE) VALUES" +
	    									    "(?,?,?,?)" );
	    	stmt.setString( 1, user.getUsername() );
	    	stmt.setString( 2, user.getEmail() );
	    	stmt.setString( 3, user.getPassword() );
	    	stmt.setString( 4, user.getRole() );
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

	
	@Override
	public String getUserIdByUsername(String username) throws UserNotFoundException {
		logger.debug( "getUserIdByEmail: " + username );
		
		String id = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
	    try {
	    	stmt = connection.prepareStatement( "SELECT ID FROM USER WHERE USERNAME=?;" );
	    	stmt.setString(1, username);
		    rs = stmt.executeQuery();
		    
		    if( rs.next() ) {
		    	id = String.valueOf( rs.getInt("ID") );
		    }
		    else {
		    	throw new UserNotFoundException( username );
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
	    
	    return id;
	}

	@Override
	public User getUser(String id) throws UserNotFoundException {
		logger.debug( "getUser: " + id );
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		User user = null;
		
	    try {
	    	stmt = connection.prepareStatement( "SELECT ID, USERNAME, EMAIL FROM USER WHERE ID=?;" );
	    	stmt.setString(1, id);
		    rs = stmt.executeQuery();
		    
		    if( rs.next() ) {
		    	String userId = String.valueOf( rs.getInt("ID") );
		    	String username = rs.getString("USERNAME");
		    	String email = rs.getString("EMAIL");
		    	
		    	user = new User(userId, username, email);
		    }
		    else {
		    	throw new UserNotFoundException( id );
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
	    
	    return user;
	}

	@Override
	public List<User> getAllUsers() {
		logger.debug( "getAllUsers" );
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<User> user = new ArrayList<User>();
		
	    try {
	    	stmt = connection.prepareStatement( "SELECT ID, USERNAME, EMAIL FROM USER;" );
		    rs = stmt.executeQuery();
		    
		    while( rs.next() ) {
		    	String userId = String.valueOf( rs.getInt("ID") );
		    	String username = rs.getString("USERNAME");
		    	String email = rs.getString("EMAIL");
		    	
		    	user.add( new User( userId, username, email ) );
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
	    
	    return user;
	}

	@Override
	public UserSecurity getUserAuthentication( String id ) throws UserNotFoundException {
		logger.debug( "getUserAuthentication: " + id );
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		UserSecurity userSecurity = null;
		
	    try {
	    	stmt = connection.prepareStatement( "SELECT USERNAME, EMAIL, PASSWORD, TOKEN, ROLE FROM USER WHERE ID=?;" );
	    	stmt.setString(1, id);
		    rs = stmt.executeQuery();
		    
		    if( rs.next() ) {
		    	String username = rs.getString("USERNAME");
		    	String email = rs.getString("EMAIL");
		    	String password = rs.getString("PASSWORD");
		    	String token = rs.getString("TOKEN");
		    	String role = rs.getString("ROLE");
		    	
		    	userSecurity = new UserSecurity( username, email, password, token, role );
		    }
		    else {
		    	throw new UserNotFoundException( id );
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
	    
	    return userSecurity;
	}

	@Override
	public boolean setUserAuthentication( UserSecurity user ) throws UserNotFoundException {
		logger.debug( "setUserAuthentication: " + user.getId() );
		
		PreparedStatement stmt = null;
		
	    try {
	    	// prepare query
	    	StringBuffer query = new StringBuffer();
	    	query.append( "UPDATE USER SET " );
	    	
	    	boolean comma = false;
	    	List<String> prepare = new ArrayList<String>();
	    	if( user.getPassword() != null ) {
	    		query.append( "PASSSWORD=?" );
	    		comma = true;
	    		prepare.add( user.getPassword() );
	    	}
	    	
	    	if( user.getToken() != null ) {
	    		if( comma ) query.append(",");
	    		query.append( "TOKEN=?" );
	    		comma = true;
	    		prepare.add( user.getToken() );
	    	}
	    	
	    	if( user.getRole() != null ) {
	    		if( comma ) query.append(",");
	    		query.append( "ROLE=?" );
	    		prepare.add( user.getRole() );
	    	}
	    	
	    	query.append(" WHERE ID=?");
	    	stmt = connection.prepareStatement( query.toString() );
	    	
	    	for( int i = 0; i < prepare.size(); i++ ) {
	    		stmt.setString( i+1, prepare.get(i) );
	    	}
	    	
	    	stmt.setInt( prepare.size() + 1, Integer.parseInt( user.getId() ) );
	    	
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

	@Override
	public boolean updateUser( User user ) throws UserNotFoundException {
		logger.debug( "updateUser: " + user.getId() );
		
		PreparedStatement stmt = null;
		
	    try {
	    	// prepare query
	    	StringBuffer query = new StringBuffer();
	    	query.append( "UPDATE USER SET " );
	    	
	    	boolean comma = false;
	    	List<String> prepare = new ArrayList<String>();
	    	
	    	if( user.getUsername() != null ) {
	    		if( comma ) query.append(",");
	    		query.append( "USERNAME=?" );
	    		prepare.add( user.getUsername() );
	    	}
	    	if( user.getEmail() != null ) {
	    		if( comma ) query.append(",");
	    		query.append( "EMAIL=?" );
	    		prepare.add( user.getEmail() );
	    	}
	    	
	    	query.append(" WHERE ID=?");
	    	stmt = connection.prepareStatement( query.toString() );
	    	
	    	for( int i = 0; i < prepare.size(); i++ ) {
	    		stmt.setString( i+1, prepare.get(i) );
	    	}
	    	
	    	stmt.setInt( prepare.size() + 1, Integer.parseInt( user.getId() ) );
	    	
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

	@Override
	public boolean deleteUser( String id ) throws UserNotFoundException {
		logger.debug( "deleteUser: " + id );
		
		PreparedStatement stmt = null;
		
	    try {
	    	
	    	stmt = connection.prepareStatement( "DELETE FROM USER WHERE ID=?" );
	    	stmt.setString( 1, id );
	    	
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