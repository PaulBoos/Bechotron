package Modules.Plex.Library;

import Utils.Security.Tokens;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;

public class PlexLibrary {
	
	static final String BASE_URL = "http://127.0.0.1:32400";
	static final String TOKEN_EXTENSION = "?X-Plex-Token=" + readToken();
	static final int MUSIC_DIRECTORY_KEY = 3;
	static final String MUSIC_DIRECTORY_URL = BASE_URL + "/library/sections/" + MUSIC_DIRECTORY_KEY + "/all" + TOKEN_EXTENSION;
	static final XmlMapper xmlMapper = new XmlMapper();
	
	private HashMap<Integer, PlexArtist> artists;
	private HashMap<Integer, PlexAlbum> albums;
	private HashMap<Integer, PlexTrack> tracks;
	
	public PlexLibrary() {
		this.artists = new HashMap<>();
		this.albums = new HashMap<>();
		this.tracks = new HashMap<>();
		try {
			PreArtist[] preArtists = xmlMapper.readValue(new URL(MUSIC_DIRECTORY_URL), PreArtist[].class);
			FullArtist[] fullArtists = new FullArtist[preArtists.length];
			for(int i = 1; i < preArtists.length; i++) {
				fullArtists[i] = FullArtist.create(preArtists[i]);
				PlexArtist plexArtist = new PlexArtist(fullArtists[i]);
				for(int j = 0; j < plexArtist.getAlbums().length; j++) {
					albums.put(plexArtist.getAlbums()[j].getId(), plexArtist.getAlbums()[j]);
					for(PlexTrack track : plexArtist.getAlbums()[j].getTracks())
						tracks.put(track.getId(), track);
				}
				artists.put(fullArtists[i].id(), plexArtist);
			}
		} catch(IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	private static String readToken() {
		try {
			return Tokens.readToken("plex");
		} catch(Tokens.TokenFileNotFoundException e) {
			throw new RuntimeException(e);
		}
	}
	
	@JsonIgnoreProperties(ignoreUnknown = true)
	@JacksonXmlRootElement(localName = "Directory")
	record PreArtist(
			
			@JacksonXmlProperty(isAttribute = true, localName = "ratingKey")
			int id,
			
			@JacksonXmlProperty(isAttribute = true, localName = "key")
			String reference,
			
			@JacksonXmlProperty(isAttribute = true, localName = "title")
			String name
			
	) {
		
		@Override
		public String toString() {
			return "PreArtist{" +
					"id=" + id +
					", thumbnailLocation='" + reference + '\'' +
					", name='" + name + '\'' +
					'}';
		}
		
	}
	
	public HashMap<Integer, PlexArtist> getArtists() {
		return artists;
	}
	
	public HashMap<Integer, PlexAlbum> getAlbums() {
		return albums;
	}
	
	public HashMap<Integer, PlexTrack> getTracks() {
		return tracks;
	}
}
