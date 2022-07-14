package Modules.CharacterSheet;

import Head.GuildInstance;
import Modules.Module;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class CharacterSheetModule extends ListenerAdapter implements Module {
	
	private CharacterDB database;
	
	
	public CharacterSheetModule(GuildInstance guildInstance) {
		database = new CharacterDB(guildInstance);
		
	}
	
	@Override
	public String getDescription() {
		return "This module allows you to create and share your own character sheets!";
	}
	
}
