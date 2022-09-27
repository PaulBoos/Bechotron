package Modules.Tycoon;

public class Facility {
	
	private final int id;
	private String name;
	private FacilityType type;
	private Long owner;
	private float locX, locY;
	
	public Facility(int id, String name, FacilityType type, Long owner, float locX, float locY) {
		this.id = id;
		this.name = name;
		this.type = type;
		this.owner = owner;
		this.locX = locX;
		this.locY = locY;
	}
	
	public enum FacilityType {
		
		SMALL_FARM, SMALL_MARKET_DISTRICT
		
	}
	
}
