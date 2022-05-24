package Modules.DeutscheBahn.Objects;

import Modules.DeutscheBahn.DBModule;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.beans.ConstructorProperties;
import java.time.Instant;
import java.time.format.DateTimeParseException;

@JacksonXmlRootElement(localName = "Stop")
public class JourneyStop {
	
	@JacksonXmlProperty(isAttribute = true)
	private final String name, track;
	@JacksonXmlProperty(isAttribute = true)
	private final float lon, lat;
	@JacksonXmlProperty(isAttribute = true)
	private final long id;
	@JacksonXmlProperty(isAttribute = true, localName = "routeIdx")
	private final int routeIndex;
	private Instant arrInstant, depInstant;
	
	@ConstructorProperties({"name", "lon", "lat", "id", "routeIdx", "track", "arrDate", "arrTime", "depDate", "depTime"})
	public JourneyStop(String name, float lon, float lat, long id, int routeIndex, String track,
	                   String arrDate, String arrTime, String depDate, String depTime) {
		this.name = DBModule.fixHTMLCharacters(name);
		this.lon = lon;
		this.lat = lat;
		this.id = id;
		this.routeIndex = routeIndex;
		this.track = DBModule.fixHTMLCharacters(track);
		try {this.arrInstant = Instant.parse(arrDate + "T" + arrTime + ":00.000Z");} catch(DateTimeParseException ignored) {}
		try {this.depInstant = Instant.parse(depDate + "T" + depTime + ":00.000Z");} catch(DateTimeParseException ignored) {}
	}
	
	public String getName() {
		return name;
	}
	
	public float getLon() {
		return lon;
	}
	
	public float getLat() {
		return lat;
	}
	
	public String getTrack() {
		return track;
	}
	
	public Instant getArrInstant() {
		return arrInstant;
	}
	
	public Instant getDepInstant() {
		return depInstant;
	}
	
	public int getRouteIndex() {
		return routeIndex;
	}
	
	public long getId() {
		return id;
	}
	
	@Override
	public String toString() {
		return "JourneyStop{" +
				"name='" + name + '\'' +
				", track='" + track + '\'' +
				", lon=" + lon +
				", lat=" + lat +
				", id=" + id +
				", routeIndex=" + routeIndex +
				", arrInstant=" + arrInstant +
				", depInstant=" + depInstant +
				'}';
	}
}

// FasterXML > Jackson
// JSON, XML