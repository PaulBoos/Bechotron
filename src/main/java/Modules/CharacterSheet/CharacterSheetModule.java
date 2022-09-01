package Modules.CharacterSheet;

import Head.GuildInstance;
import Modules.Module;
import Modules.RequireModuleHook;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.List;

public class CharacterSheetModule extends ListenerAdapter implements Module {
	
	private RequireModuleHook HOOK = new RequireModuleHook();
	
	private CharacterDB database;
	
	
	public CharacterSheetModule(GuildInstance guildInstance) {
		database = new CharacterDB(guildInstance);
		
	}
	
	@Override
	public String getDescription() {
		return "This module allows you to create and share your own character sheets!";
	}
	
	@Override
	public String getName() {
		return "Character Sheet Module";
	}
	
	@Override
	public void init() {
	
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
