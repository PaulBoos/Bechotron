package Modules.Plex;

import Modules.Module;
import Modules.Plex.Library.PlexAlbum;
import Modules.Plex.Library.PlexArtist;
import Modules.Plex.Library.PlexLibrary;
import Modules.Plex.Library.PlexTrack;
import Modules.RequireModuleHook;
import net.dv8tion.jda.api.entities.Guild;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PlexServerModule implements Module {
	
	static private PlexLibrary plexLibrary = new PlexLibrary();
	
	public static Collection<PlexArtist> getArtists() {
		return plexLibrary.getArtists().values();
	}
	
	public static Collection<PlexAlbum> getAlbums() {
		return plexLibrary.getAlbums().values();
	}
	
	public static Collection<PlexTrack> getSongs() {
		return plexLibrary.getTracks().values();
	}
	
	static Collection<Searchable> searchables;
	
	static {
		searchables = new ArrayList<>();
		searchables.addAll(getArtists());
		searchables.addAll(getAlbums());
		searchables.addAll(getSongs());
	}
	
	public static Collection<Searchable> getSearchable() {
		return searchables;
	}
	
	@Override
	public Module load(Guild guild) {
		if(plexLibrary == null) {
			plexLibrary = new PlexLibrary();
		}
		return this;
	}
	
	@Override
	public Module unload(Guild guild) {
		return Module.super.unload(guild);
	}
	
	@Override
	public String getName() {
		return "Plex Server Module";
	}
	
	@Override
	public String getDescription() {
		return "";
	}
	
	@Override
	public List<RequireModuleHook> requireModules() {
		return null;
	}
	
	@Override
	public RequireModuleHook getMyRequireModuleHook() {
		return null;
	}
}
