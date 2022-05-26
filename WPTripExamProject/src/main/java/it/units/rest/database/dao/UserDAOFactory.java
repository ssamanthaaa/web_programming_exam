package it.units.rest.database.dao;


import it.units.rest.config.DbConfig;
import it.units.rest.database.connection.Connection;
import it.units.rest.database.connection.ConnectionFactory;
//import it.units.rest.database.dao.gremlin.GremlinUserDAO;
import it.units.rest.database.dao.SqlUserDAO;

public class UserDAOFactory {
	
	public static UserDAO getUserDAO() {
		// get connection
		Connection connection = ConnectionFactory.getConnection();
		
		// use driver specified according to database
//        switch( DbConfig.getDbType() ) {
//            case SQL:
//                return new SqlUserDAO( connection );
//            case SQLITE:
//                return new SqlUserDAO( connection );
//            default:
//                // should not happen: we test for correct input in DbConfig.java
//                return null;
//        }
		return new SqlUserDAO( connection );
	}
}