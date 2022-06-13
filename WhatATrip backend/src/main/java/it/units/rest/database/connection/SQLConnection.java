package it.units.rest.database.connection;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;
//import org.sqlite.SQLiteConfig;
//import org.sqlite.SQLiteOpenMode;

import it.units.rest.config.DbConfig;
import it.units.rest.security.PasswordSecurity;


public class SQLConnection implements it.units.rest.database.connection.Connection {

	final static Logger logger = Logger.getLogger( SQLConnection.class );
	
	Connection connection = null;
	
	@Override
	public Object get() {
		return connection;
	}

	@Override
	public boolean open() {
		connection = null;
		String path = null;
	    try {
	      Class.forName("com.mysql.cj.jdbc.Driver");

	      
	      if( DbConfig.getDbPath() != null ) {	 
	    	  path = DbConfig.getDbPath();
	      }
	      else if( DbConfig.getDbName() != null ) {
	    	  path = "jdbc:mysql://localhost:3306/" + DbConfig.getDbName();
	      }
	      else {
	    	  path = "jdbc:mysql://localhost:3306/TRIPS";
	      }
	      String password = "Cioccolato123";
	      System.out.println("getPath = " + path + ", user= " + DbConfig.getDbUser() + ", password= " + password);
	      connection = DriverManager.getConnection( path, DbConfig.getDbUser(), password );
	      
	    } catch ( Exception e ) {
	      logger.debug( e.getClass().getName() + ": " + e.getMessage() );
	      System.out.println("!--------- path: " + path + "-----------");
	      System.out.println("Exception!!");
	      System.exit(0);
	    }
	    logger.debug("Opened mysql database successfully");
	    
	    // database openend - init database schema
		    createUserSchema();
	    // create admin user
		    createAdminUser();
	    
	    return true;
	}

	@Override
	public boolean close() {
		if( connection != null ) {
			try {
				connection.close();
			} catch ( SQLException e ) {
				logger.debug( "Databank not closed correctly: " + e.getMessage() );
			}
			return true;
		}
		return false;
	}
	
	private boolean checkForUserSchema() {
		try {
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery( "SELECT USERNAME FROM USER;" ); //WHERE type='table' AND name='USER'
			if (rs.next()) {
				return true;
			}
			rs.close();
			stmt.close();
		} catch ( Exception e ) {
			logger.debug( e.getClass().getName() + ": " + e.getMessage() );
		}
		return false;
	}
	
	private void createUserSchema() {
		if( checkForUserSchema() == true ) return;
		
		try {
			Statement stmt = connection.createStatement();
			String sql = "CREATE TABLE USER ( " +
					     " ID INTEGER PRIMARY KEY AUTO_INCREMENT NOT NULL," +
					     " USERNAME 	  VARCHAR(45) UNIQUE NOT NULL, " +
					     " EMAIL    	  VARCHAR(45) UNIQUE NOT NULL, " + 
					     " PASSWORD		  VARCHAR(200) NOT NULL, " + 
					     " TOKEN          TEXT, " + 
					     " ROLE           VARCHAR(20)"; 
			stmt.executeUpdate( sql );
			stmt.close();
		} catch ( Exception e ) {
			logger.debug( e.getClass().getName() + ": " + e.getMessage() );
		}
	}
	
	private boolean checkForAdminUser() {
		try {
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery( "SELECT USERNAME FROM USER  WHERE USERNAME='';" );
			if (rs.next()) {
				return true;
			}
			rs.close();
			stmt.close();
		} catch ( Exception e ) {
			logger.debug( e.getClass().getName() + ": " + e.getMessage() );
		}
		return false;
	}
	
	private void createAdminUser() {
		if( checkForAdminUser() ) return;
		
		PreparedStatement stmt = null;
		
		try {
			stmt = connection.prepareStatement(
	    	 "INSERT INTO USER(" + 
			 "USERNAME, EMAIL, PASSWORD,ROLE) VALUES" +
			 "(?,?,?,?)" );
			
			stmt.setString( 1, "admin" );
			stmt.setString( 2, "admin@gmail.com" );
			stmt.setString( 3, PasswordSecurity.generateHash( "secret" ) );
			stmt.setString( 4, "admin" );
			stmt.executeUpdate();
	    } catch ( SQLException e ) {
	    	logger.debug( e.getClass().getName() + ": " + e.getMessage() );
	    } catch (NoSuchAlgorithmException e) {
	    	logger.debug( e.getClass().getName() + ": " + e.getMessage() );
		} catch (InvalidKeySpecException e) {
			logger.debug( e.getClass().getName() + ": " + e.getMessage() );
		}
	    finally {
	    	try {
				stmt.close();
			} catch ( SQLException e ) {
				logger.warn( e.getMessage() );
			}
	    }
	}
}