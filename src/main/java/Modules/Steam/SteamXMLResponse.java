package Modules.Steam;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.beans.ConstructorProperties;

public class SteamXMLResponse {
	
	@JacksonXmlRootElement(localName = "request")
	public static class PlayerRequestResponse {
		
		@JacksonXmlProperty(localName = "players")
		public final SteamUser[] players;
		
		@ConstructorProperties({"players"})
		PlayerRequestResponse(SteamUser[] players) {
			this.players = players;
		}
		
	}
	
}
