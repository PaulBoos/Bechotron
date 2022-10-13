package Modules.Tycoon;

public class Facility {
	
	private final int id;
	private String name;
	private FacilityType type;
	private float locX, locY;
	private Landlord landlord;
	
	public Facility(int id, String name, FacilityType type, float locX, float locY) {
		this.id = id;
		this.name = name;
		this.type = type;
		this.locX = locX;
		this.locY = locY;
	}
	
	public Facility rename(String newName) {
		// TODO
		return this;
	}
	
	public void setLandlord(Landlord landlord) {
		this.landlord = landlord;
	}
	
	public FacilityType getType() {
		return type;
	}
	
	public int getId() {
		return id;
	}
	
	public Landlord getLandlord() {
		return landlord;
	}
	
	@Override
	public String toString() {
		return "Facility" +
				" #" + id +
				" " + type +
				" '" + name + '\'' +
				" owned by " + landlord.getUser().getName() + " [" + landlord.getUser().getId() +
				"] located at " + locX +
				"/" + locY;
	}
	
	public int getID() {
		return id;
	}
	
	public enum FacilityType {
		
		SMALL_FARM, SMALL_MARKET_DISTRICT
		
	}
	
}
