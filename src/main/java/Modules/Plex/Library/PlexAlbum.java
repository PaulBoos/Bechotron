package Modules.Plex.Library;

import Modules.Plex.Searchable;

import java.util.Arrays;

public class PlexAlbum implements Searchable {
	
	private final int id;
	private final String name;
	private String reference;
	private PlexTrack[] tracks;
	private String artist;
	
	PlexAlbum(FullAlbum fullAlbum) {
		this.id = fullAlbum.id();
		this.name = fullAlbum.name();
		this.reference = fullAlbum.thumbnailLocation();
		this.tracks = new PlexTrack[fullAlbum.tracks().length];
		this.artist = fullAlbum.artistName();
		for (int i = 0; i < fullAlbum.tracks().length; i++) {
			this.tracks[i] = new PlexTrack(fullAlbum.tracks()[i]);
		}
	}
	
	@Override
	public String toString() {
		return "PlexAlbum{" +
				"id=" + id +
				", name='" + name + '\'' +
				", artist='" + artist + '\'' +
				", reference='" + reference + '\'' +
				", tracks=" + Arrays.toString(tracks) +
				'}';
	}
	
	public int getId() {
		return id;
	}
	
	public String getTitle() {
		return name;
	}
	
	public String getReference() {
		return reference;
	}
	
	public PlexTrack[] getTracks() {
		return tracks;
	}
	
	@Override
	public String getSearchable() {
		return name;
	}
	
	@Override
	public String getParentTree() {
		return artist + ">" + name;
	}
	
	@Override
	public int getID() {
		return id;
	}
}
