package Modules.Timestamp;

import Head.BotInstance;
import Head.GuildModule;
import Modules.RequireModuleHook;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TimestampModule extends ListenerAdapter implements GuildModule {
	
	@Override
	public void onMessageReceived(@NotNull MessageReceivedEvent event) {
		if(!event.isFromGuild()) return;
		if(event.getGuild().getIdLong() != 1037783202430976131L) return;
		if(event.getAuthor().isBot()) return;
		String s = event.getMessage().getContentRaw();
		Pattern p = Pattern.compile("/(10|11|12|\\d)([;,:.]\\d\\d)?\\s?(am|pm)/gi");
		Matcher m = p.matcher(s);
		if(m.find()) {
			List<String> hits = new ArrayList<>();
			do {
				int hour   = Integer.parseInt(m.group(1));
				int minute = 0;
				try {
					minute = Integer.parseInt(m.group(2).substring(1));
				} catch(NullPointerException ignored) {}
				if(m.group(3).equals("pm")) hour += 12;
				switch(event.getAuthor().getId()) {
					case "223168912173301761" -> hour += 5; // Honda
					case "282551955975307264" -> hour -= 1; // Becher
					case "506144155538292746" -> hour -= 1; // lefi
				}
				hits.add("I read " + m.group(0) + " and that should be <t:" + (hour * 3600 + minute * 60) + ":t> for you.");
			} while(m.find());
			event.getChannel().sendMessage(String.join("\n", hits)).queue();
		}
	}
	
	@Override
	public void init(Guild guild) {
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
	public void init() {
	
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
