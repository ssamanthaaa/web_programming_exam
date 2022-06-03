package it.units.rest.database.dao;


//import it.units.rest.config.DbConfig;
import it.units.rest.database.connection.Connection;
import it.units.rest.database.connection.ConnectionFactory;
//import it.units.rest.database.dao.gremlin.GremlinUserDAO;
//import it.units.rest.database.dao.SqlUserDAO;

public class UserDAOFactory {
	
	public static UserDAO getUserDAO() {
		// get connection
		Connection connection = ConnectionFactory.getConnection();
		
		return new SqlUserDAO( connection );
	}
}