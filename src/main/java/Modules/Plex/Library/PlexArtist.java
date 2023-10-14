package Modules.Plex.Library;

import Modules.Plex.Searchable;

import java.util.Arrays;

public class PlexArtist implements Searchable {
	
	private final int id;
	private final String name;
	private final PlexAlbum[] albums;
	
	PlexArtist(FullArtist fullArtist) {
		this.id = fullArtist.id();
		this.name = fullArtist.name();
		albums = new PlexAlbum[fullArtist.albums().length];
		for(int j = 0; j < albums.length; j++) {
			albums[j] = new PlexAlbum(FullAlbum.create(fullArtist.albums()[j]));
		}
	}
	
	@Override
	public String toString() {
		return "PlexArtist{" +
				"id=" + id +
				", name='" + name + '\'' +
				", albums=" + Arrays.toString(albums) +
				'}';
	}
	
	public int getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public PlexAlbum[] getAlbums() {
		return albums;
	}
	
	@Override
	public String getSearchable() {
		return name;
	}
	
	@Override
	public String getParentTree() {
		return name;
	}
	
	@Override
	public int getID() {
		return id;
	}
}
