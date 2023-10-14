package Modules;

import Modules.Music.MusicModule;
import Modules.SlashCommands.Command;
import net.dv8tion.jda.api.entities.Guild;

import java.util.List;

public class TrumpOrBidenTTV implements Module {
	
	public static final Command LISTEN = new Command(
			"listen",
			"Listen to the legendary AI battle between Trump and Biden!",
			event -> {
				if(event.getMember().getUser().isBot()) return;
				if(event.getMember().getVoiceState().inAudioChannel()) event.reply("You are in no voice channel!");
				MusicModule.manager.loadAndPlay(event.getChannel().asTextChannel(), event.getMember().getVoiceState().getChannel(), "https://www.twitch.tv/trumporbiden2024");
				event.reply("Listening to Trump versus Biden on Twitch!").setEphemeral(true).queue();
			}
	);
	
	
	@Override
	public Module load(Guild guild) {
		return Module.super.load(guild);
	}
	
	@Override
	public Module unload(Guild guild) {
		return Module.super.unload(guild);
	}
	
	@Override
	public String getName() {
		return "Trump versus Biden on Twitch";
	}
	
	@Override
	public String getDescription() {
		return "Listen to the legendary AI battle between Trump and Biden!";
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
