package Modules.DeutscheBahn.Responses;

import Modules.DeutscheBahn.Objects.Arrival;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@JacksonXmlRootElement(localName = "ArrivalBoard")
public class GetArrivalsResponse {
	
	@JacksonXmlProperty(localName = "Arrival")
	@JacksonXmlElementWrapper(useWrapping = false)
	private List<Arrival> arrivals;
	
	public List<Arrival> getArrivals() {
		return arrivals;
	}
	
	@Override
	public String toString() {
		return "GetArrivalsResponse{" +
				"arrivals=" + arrivals +
				'}';
	}
}
