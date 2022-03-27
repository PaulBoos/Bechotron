package Modules.DeutscheBahn.Responses;

import Modules.DeutscheBahn.Objects.Station;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@JacksonXmlRootElement(localName = "LocationList")
public class GetStationsResponse {
	
	@JacksonXmlProperty(localName = "StopLocation")
	@JacksonXmlElementWrapper(useWrapping = false)
	private List<Station> stations;
	
	public List<Station> getStations() {
		return stations;
	}
	
	@Override
	public String toString() {
		return "GetStationsResponse{" +
				"stations=" + stations +
				'}';
	}
}
