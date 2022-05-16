package it.units;

import java.util.Date;
import java.util.List;


public class Trip {
	private int ID;
	private Date DATE;
	private String PLACE;
	
	private int TRANSPORTATION;
	private List<Path> PATH;
	private List<MainStage> MAIN_STAGES;
	private int USER;
	
	public Trip(Date DATE, String PLACE, int TRANSPORTATION, List<Path> PATH, List<MainStage> MAIN_STAGES, int USER) {
		this.DATE = DATE;
		this.PLACE = PLACE;
		this.TRANSPORTATION = TRANSPORTATION;
		this.PATH = PATH;
		this.MAIN_STAGES = MAIN_STAGES;
		this.USER = USER;
	}
	
	
	public int getId() {
		return ID;
	}
	
	public Date getDate() {
		return DATE;
	}
	
	public String getPlace() {
		return PLACE;
	}
	

	public int getTransportation() {
		return TRANSPORTATION;
	}
	
	public List<Path> getPath() {
		return PATH;
	}
	
	public List<MainStage> getMainStages() {
		return MAIN_STAGES;
	}
	
	public int getUser() {
		return USER;
	}
	
	public void setId(int ID) {
		this.ID = ID;
	}
	
	public void setDate(Date DATE) {
		this.DATE = DATE;
	}
	
	public void setPlace(String PLACE) {
		this.PLACE = PLACE;
	}
	
	public void setTransportation(int TRANSPORTATION) {
		this.TRANSPORTATION = TRANSPORTATION;
	}
	
	public void setPath(List<Path> PATH) {
		this.PATH = PATH;
	}
	
	public void setMainStages(List<MainStage> MAIN_STAGES) {
		this.MAIN_STAGES = MAIN_STAGES;
	}	
	
	public void setUser(int USER) {
		this.USER = USER;
	}
	
	@Override
	public String toString() {
		return "Trip: [ ID: " + ID + ", DATE: " + DATE + ". PLACE: " + PLACE + "TRANSPORTATION: " + TRANSPORTATION + "PATH: " + PATH.toString() 
			+ "MAIN_STAGES: " + MAIN_STAGES.toString() + "]";
	}
}