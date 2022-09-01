package Modules.CharacterSheet;

import Head.GuildInstance;
import Databases.dbAccess;

public class CharacterDB extends dbAccess {
	
	public CharacterDB(GuildInstance guildInstance) {
		super("jdbc:sqlite:data/guild/" + guildInstance.guild + "/char.db");
	}
	
	
	
}
