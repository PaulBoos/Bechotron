package Modules.DeutscheBahn.Objects;

import Modules.DeutscheBahn.DBModule;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.beans.ConstructorProperties;

@JacksonXmlRootElement(localName = "StopLocation")
public class Station {
	
	@JacksonXmlProperty(isAttribute = true)
	private final String name;
	@JacksonXmlProperty(isAttribute = true)
	private final float lon, lat;
	@JacksonXmlProperty(isAttribute = true)
	private final long id;
	
	@ConstructorProperties({"name", "lon", "lat", "id"})
	private Station(String name, float lon, float lat, long id) {
		this.id = id;
		this.lon = lon;
		this.lat = lat;
		this.name = DBModule.fixHTMLCharacters(name);
	}
	
	public long getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	public float getLat() {
		return lat;
	}
	public float getLon() {
		return lon;
	}
	
	@Override
	public String toString() {
		return "\n  " + name + " {#" + id + ", " + "lon=" + lon + ", " + "lat=" + lat + "}";
	}
}
