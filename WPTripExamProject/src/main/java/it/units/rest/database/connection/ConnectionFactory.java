package it.units.rest.database.connection;


import it.units.rest.config.DbConfig;
//import com.tutorialacademy.rest.database.connection.orientdb.OrientDbConnection;
import it.units.rest.database.connection.SQLiteConnection;
import it.units.rest.database.connection.SQLConnection;

public class ConnectionFactory {
	
	private static Connection connection = null;
	
	public static Connection getConnection() {
		if( connection != null ) return connection;
		
//		connection = new SQLiteConnection();
		
//		switch( DbConfig.getDbType() ) {
//			case SQL:
//				connection = new SQLConnection();
//			case SQLITE:
//				connection = new SQLiteConnection();
//			default:
//				break;
//		}
		connection = new SQLConnection();
		// open connection
		connection.open();
		
		return connection;
	}
}