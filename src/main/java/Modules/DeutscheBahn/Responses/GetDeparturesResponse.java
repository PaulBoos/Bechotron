package Modules.DeutscheBahn.Responses;

import Modules.DeutscheBahn.Objects.Departure;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@JacksonXmlRootElement(localName = "DepartureBoard")
public class GetDeparturesResponse {
	
	@JacksonXmlProperty(localName = "Departure")
	@JacksonXmlElementWrapper(useWrapping = false)
	private List<Departure> departures;
	
	public List<Departure> getDepartures() {
		return departures;
	}
	
	@Override
	public String toString() {
		return "GetDeparturesResponse{" +
				"departures=" + departures +
				'}';
	}
}
