package Modules.Music;

import Modules.Module;
import Modules.Music.MusicManager;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class MusicModule extends ListenerAdapter implements Module {
	
	public static MusicManager manager;
	
	public MusicModule(JDA jda) {
		jda.addEventListener(this);
		manager = new MusicManager();
	}
	
	@Override
	public String getDescription() {
		return "This module plays back music";
	}
}
