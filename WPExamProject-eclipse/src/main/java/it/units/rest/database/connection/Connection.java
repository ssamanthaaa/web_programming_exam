package it.units.rest.database.connection;


public interface Connection {
	public Object get();
	public boolean open();
	public boolean close();
}