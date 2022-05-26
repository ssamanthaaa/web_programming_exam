package it.units.rest.exception;

public class TripNotFoundException extends RuntimeException {

	private static final long serialVersionUID = -8078059893066017473L;
	
	public TripNotFoundException( Integer trip ) {
		super("Trip not found: " + trip );
	}
	
}