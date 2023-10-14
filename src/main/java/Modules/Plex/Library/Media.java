package Modules.Plex.Library;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.util.Arrays;

import static Modules.Plex.Library.PlexLibrary.BASE_URL;

@JsonIgnoreProperties(ignoreUnknown = true)
@JacksonXmlRootElement(localName = "Media")
public record Media(
		
		@JacksonXmlElementWrapper(useWrapping = false)
		@JacksonXmlProperty(isAttribute = true, localName = "Part")
		Part[] parts

) {
	
	@Override
	public String toString() {
		return "Media #" + Arrays.toString(parts);
	}
	
	public String[] getPlayableURLs() {
		// return array of playable URLs in form BASE_URL + location + PlexLibrary.TOKEN_EXTENSION
		return Arrays.stream(parts).map(part -> BASE_URL + part.location() + PlexLibrary.TOKEN_EXTENSION).toArray(String[]::new);
	}
	
}
