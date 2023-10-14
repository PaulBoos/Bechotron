package Modules.Timestamp;

import Head.BotInstance;
import Modules.Module;
import Modules.RequireModuleHook;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import java.util.List;

public class TimestampModule extends ListenerAdapter implements Module {
	
	private static TimestampListener listener;
	private final long guildId;
	private final BotInstance bot;
	
	public TimestampModule(long guildId, BotInstance bot) {
		this.guildId = guildId;
		this.bot = bot;
		init();
	}
	
	private TimestampModule init() {
		if(listener == null) {
			listener = new TimestampListener();
			bot.jda.addEventListener(listener);
		}
		listener.enabledGuilds.add(guildId);
		return this;
	}
	
	@Override
	public Module unload(Guild guild) {
		listener.enabledGuilds.remove(guild.getIdLong());
		if(listener.enabledGuilds.size() == 0) listener = null;
		return this;
	}
	
	@Override
	public String getDescription() {
		return "This module adds dynamic timestamps to peoples messages based on their time zone.";
	}
	
	@Override
	public String getName() {
		return "Timestamp Module";
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
