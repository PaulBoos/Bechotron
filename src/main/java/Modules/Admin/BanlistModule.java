package Modules.Admin;

import Modules.Module;
import Modules.RequireModuleHook;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.List;

public class BanlistModule extends ListenerAdapter implements Module {
	
	private RequireModuleHook HOOK = new RequireModuleHook();
	
	@Override
	public String getDescription() {
		return "This module bans accounts present on this server's banlist. WIP";
	}
	
	@Override
	public String getName() {
		return "Banlist Module";
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
