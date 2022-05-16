package it.units;

public class Path {

	private int ID;
	private String LAT;
	private String LONG;
	
	public int getId() {
		return ID;
	}
	
	public Path(String LAT, String LONG) {
		this.LAT = LAT;
		this.LONG = LONG;
	}
	
	public String getLat() {
		return LAT;
	}
	
	public String getLong() {
		return LONG;
	}
	
	public void setLat(String LAT) {
		this.LAT = LAT;
	}
	
	public void setLong(String LONG) {
		this.LONG = LONG;
	}
	
	@Override
	public String toString() {
		return "Path: [ID: " + ID + ", LAT: " + LAT + ", LONG: " + LONG + "]";
	}
}
