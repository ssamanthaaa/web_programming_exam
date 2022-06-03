package it.units;

public class MainStage {

	private int ID;
	private String STAGE_NAME;
	private String LAT;
	private String LONG;
	
	public MainStage(String STAGE_NAME, String LAT, String LONG) {
		this.STAGE_NAME = STAGE_NAME;
		this.LAT = LAT;
		this.LONG = LONG;
	}
	
	public int getId() {
		return ID;
	}
	
	public String getStageName() {
		return STAGE_NAME;
	}
	
	public String getLat() {
		return LAT;
	}
	
	public String getLong() {
		return LONG;
	}
	
	public void setStageName(String STAGE_NAME) {
		this.STAGE_NAME = STAGE_NAME;
	}
	
	public void setLat(String LAT) {
		this.LAT = LAT;
	}
	
	public void setLong(String LONG) {
		this.LONG = LONG;
	}
	
	@Override
	public String toString() {
		return "MainStages: [ID: " + ID + "STAGE_NAME: " + STAGE_NAME + ", LAT: " + LAT + ", LONG: " + LONG + "]";
	}
}
