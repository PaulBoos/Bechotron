package Modules.Tycoon;

import java.sql.SQLException;
import java.util.List;

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
	
	@Override
	public String toString() {
		return "Facility" +
				" #" + id +
				" [" + type +
				"] '" + name + '\'' +
				" owned by " + owner +
				" located at " + locX +
				"/" + locY;
	}
	
	public enum FacilityType {
		
		SMALL_FARM, SMALL_MARKET_DISTRICT
		
	}
	
}
