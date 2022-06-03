package it.units.rest.database.def;

//import org.sqlite.SQLiteConfig;

public enum DataBaseType {
//	ORIENTDB("ORIENTDB"),
	SQLITE("SQLITE");
//	SQL("SQL");
	
    private final String val;       

    private DataBaseType( String s ) {
    	val = s;
    }
    
    public String toString() {
       return this.val;
    }
}