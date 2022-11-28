package Modules.Timestamp;

import Modules.Module;
import Modules.RequireModuleHook;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TimestampModule extends ListenerAdapter implements Module {
	
	private static final HashMap<String, Integer> timezoneOffsets = new HashMap<>() {{
		put("1044331600969740308",   0); // UTC
		put("1044331770272817192", - 5); // PST
		put("1044332040063029300", + 1); // CET
	}};
	
	private static TimestampModule instance;
	private final List<Long> enabledGuilds = new ArrayList<>();
	
	@Override
	public void onMessageReceived(@NotNull MessageReceivedEvent event) {
		if(!event.isFromGuild()) return;
		if(!enabledGuilds.contains(event.getGuild().getIdLong())) return;
		if(event.getAuthor().isBot()) return;
		String s = event.getMessage().getContentRaw();
		Pattern p = Pattern.compile("(10|11|12|\\d)([;,:.]\\d\\d)?\\s?(am|pm)");
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
				List<Role> authorRoles = event.getMember().getRoles();
				for(Role r: authorRoles) {
					for(String key: timezoneOffsets.keySet()) {
						if(r.getId().equals(key)) {
							hour -= timezoneOffsets.get(key);
							if(hour > 24) hour -= 24;
							if(hour < 0) hour += 24;
							break;
						}
					}
				}
				hits.add("I read " + m.group(0) + " and that should be <t:" + (hour * 3600 + minute * 60) + ":t> for you.");
			} while(m.find());
			event.getChannel().sendMessage(String.join("\n", hits)).queue();
		}
	}
	
	private TimestampModule() {}
	
	public static TimestampModule createModule(long guildId) {
		if(instance == null) instance = new TimestampModule();
		instance.enabledGuilds.add(guildId);
		return instance;
	}
	
	@Override
	public void init(Guild guild) {
		guild.getJDA().addEventListener(instance);
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
