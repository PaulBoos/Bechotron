package Modules.DeutscheBahn.Objects;

import Modules.DeutscheBahn.DBModule;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.beans.ConstructorProperties;
import java.time.Instant;

public class Departure {
	
	@JacksonXmlProperty(isAttribute = true)
	private final String name, type, stop, direction, track;
	private final String journeyDetailReference;
	@JacksonXmlProperty(isAttribute = true)
	private final long stopid;
	private final Instant instant;
	
	@ConstructorProperties({"name", "type", "stop", "direction", "JourneyDetailRef", "stopid", "track", "time", "date"})
	private Departure(String name, String type, String stop, String direction, JourneyDetailReference journeyDetailReference, long stopid, String track, String time, String date) {
		this.name = name;
		this.type = type;
		this.stop = DBModule.fixHTMLCharacters(stop);
		this.direction = DBModule.fixHTMLCharacters(direction);
		this.journeyDetailReference = journeyDetailReference.ref;
		this.stopid = stopid;
		this.track = track;
		this.instant = Instant.parse(date + "T" + time + ":00.000Z");
	}
	
	public String getName() {
		return name;
	}
	public String getStop() {
		return stop;
	}
	public long getStopid() {
		return stopid;
	}
	public String getDirection() {
		return direction;
	}
	public Instant getInstant() {
		return instant;
	}
	public String getJourneyDetailReference() {
		return journeyDetailReference;
	}
	public String getTrack() {
		return track;
	}
	public String getType() {
		return type;
	}
	
	@Override
	public String toString() {
		return "Departure{" +
				"name='\"" + name + "\", " +
				"type=\"" + type + "\", " +
				"stop=\"" + stop + "\", " +
				"direction=\"" + direction + "\", " +
				"journeyDetailReference=\"" + journeyDetailReference + "\", " +
				"stopid=" + stopid + ", " +
				"track=" + track + ", " +
				"instant=" + instant +
				"}";
	}
	
	@JacksonXmlRootElement(localName = "JourneyDetailRef")
	private static class JourneyDetailReference {
		@JacksonXmlProperty(localName = "ref", isAttribute = true)
		private final String ref;
		@ConstructorProperties("ref")
		public JourneyDetailReference(String ref) {
			this.ref = ref;
		}
	}
}
