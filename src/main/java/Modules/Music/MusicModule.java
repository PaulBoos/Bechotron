package Modules.Music;

import Modules.Module;
import Modules.RequireModuleHook;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.List;

public class MusicModule extends ListenerAdapter implements Module {
	
	private static final RequireModuleHook HOOK = new RequireModuleHook();
	
	public static MusicManager manager;
	
	public MusicModule(JDA jda) {
		jda.addEventListener(this);
		manager = new MusicManager();
	}
	
	@Override
	public String getDescription() {
		return "This module plays back music";
	}
	
	@Override
	public String getName() {
		return "Music Module";
	}
	
	@Override
	public List<RequireModuleHook> requireModules() {
		return null;
	}
	
	@Override
	public RequireModuleHook getMyRequireModuleHook() {
		return HOOK;
	}
	
}
