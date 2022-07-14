package Modules.Steam;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.beans.ConstructorProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
@JacksonXmlRootElement(localName = "player")
public class SteamUser {
	
	public final long steamid;
	public final String username, profileurl, avatarsmall, avatarmedium, avatarfull;
	public final int onlineState;
	
	@ConstructorProperties({"steamid", "personaname", "profileurl", "avatar", "avatarmedium", "avatarfull", "personastate"})
	public SteamUser(long steamid, String username, String profileurl, String avatarsmall, String avatarmedium, String avatarfull, String onlineState) {
		this.steamid = steamid;
		this.username = username;
		this.profileurl = profileurl;
		this.avatarsmall = avatarsmall;
		this.avatarmedium = avatarmedium;
		this.avatarfull = avatarfull;
		if(onlineState == null) onlineState = "0";
		this.onlineState = Integer.parseInt(onlineState);
	}
	
}
