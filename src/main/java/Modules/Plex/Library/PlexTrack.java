package Modules.Plex.Library;

import Modules.Plex.Searchable;

import java.util.Arrays;

public class PlexTrack implements Searchable {
	
	private int id;
	private String location;
	private String title;
	private Media[] media;
	private String artist, album;
	
	PlexTrack(FullTrack fullTrack) {
		this.id = fullTrack.id();
		this.location = fullTrack.location();
		this.title = fullTrack.title();
		this.album = fullTrack.albumName();
		this.artist = fullTrack.artistName();
		this.media = fullTrack.media();
	}
	
	@Override
	public String toString() {
		return "Plex Track: \"" + title + "\" #" + id + " @" + location + " Media: " + Arrays.toString(media);
	}
	
	@Override
	public String getSearchable() {
		return title;
	}
	
	@Override
	public String getParentTree() {
		return artist + ">" + album + ">" + title;
	}
	
	@Override
	public int getID() {
		return id;
	}
	
	public int getId() {
		return id;
	}
	
	public String getLocation() {
		return location;
	}
	
	public String getTitle() {
		return title;
	}
	
	public Media[] getMedia() {
		return media.clone();
	}
	
	public String getArtist() {
		return artist;
	}
	
	public String getAlbum() {
		return album;
	}
	
}
