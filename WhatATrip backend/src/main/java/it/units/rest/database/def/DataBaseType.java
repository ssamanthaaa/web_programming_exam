package it.units.rest.database.def;


public enum DataBaseType {
	SQL("SQL");
	
    private final String val;       

    private DataBaseType( String s ) {
    	val = s;
    }
    
    public String toString() {
       return this.val;
    }
}