package Modules.Timestamp;

import Modules.Module;
import Modules.RequireModuleHook;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
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
		put("1044331600969740308",     0); // UTC
		put("1044331770272817192", - 300); // PST
		put("1044332040063029300", +  60); // CET
		put("1045788491163906159", + 120); // EET
	}};
	private static final Pattern[] messagePatterns = new Pattern[] {
			Pattern.compile("(20|21|22|23|24|1\\d|\\d)([;,:.]\\d\\d)?"), // Sensible Notation
			Pattern.compile("(10|11|12|\\d)([;,:.]\\d\\d)?\\s?(am|pm)")  // American Notation
	};
//	private static final
	
	private static TimestampModule instance;
	private final List<Long> enabledGuilds = new ArrayList<>();
	
	@Override
	public void onMessageReceived(@NotNull MessageReceivedEvent event) {
		if(!event.isFromGuild()) return;
		if(!enabledGuilds.contains(event.getGuild().getIdLong())) return;
		if(event.getAuthor().isBot()) return;
		String s = event.getMessage().getContentRaw();
		{ // Sensible Notation
			Matcher m = messagePatterns[0].matcher(s);
			if(m.find()) {
				List<String> hits = new ArrayList<>();
				do {
					int hour = Integer.parseInt(m.group(1));
					int minute = hour * 60;
					try {
						minute += Integer.parseInt(m.group(2).substring(1));
					} catch(NullPointerException ignored) {}
					assert event.getMember() != null;
					int offset = getMembersTimezoneOffset(event.getMember());
					hour += offset / 60;
					minute += offset % 60;
					if(hour > 24) hour -= 24;
					if(hour < 0) hour += 24;
					hits.add("I read " + m.group(0) + " and that should be <t:" + (hour * 3600 + minute * 60) + ":t> for you.");
				} while(m.find());
				event.getChannel().sendMessage(String.join("\n", hits)).queue();
			}
		} { // American Notation
			Matcher m = messagePatterns[1].matcher(s);
			if(m.find()) {
				List<String> hits = new ArrayList<>();
				do {
					int hour = Integer.parseInt(m.group(1) + (m.group(3).equals("pm") ? 12 : 0));
					int minute = hour * 60;
					try {
						minute += Integer.parseInt(m.group(2).substring(1));
					} catch(NullPointerException ignored) {}
					assert event.getMember() != null;
					int offset = getMembersTimezoneOffset(event.getMember());
					hour += offset / 60;
					minute += offset % 60;
					if(hour > 24) hour -= 24;
					if(hour < 0) hour += 24;
					hits.add("I read " + m.group(0) + " and that should be <t:" + (hour * 3600 + minute * 60) + ":t> for you.");
				} while(m.find());
				event.getChannel().sendMessage(String.join("\n", hits)).queue();
			}
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
	
	/**
	 * Get the timezone offset of a given member
	 * @param member The member to get the timezone offset of
	 * @return minutes of offset from UTC
	 */
	private static int getMembersTimezoneOffset(Member member) {
		for(Role role : member.getRoles()) {
			if(timezoneOffsets.containsKey(role.getId())) {
				return timezoneOffsets.get(role.getId());
			}
		}
		return 0;
	}
	
	/**
	 * Find all timestamps in a string.
	 * The matcher must be created from a regex Pattern and the String to search.
	 * Make sure that group #1 is the hour and group #2 is the minute.
	 * @param m the regex Matcher.
	 * @return a list of timestamps.
	 */
	private static List<Integer> findTimestamps(Matcher m) {
		List<Integer> hits = new ArrayList<>();
		do {
			int hour = Integer.parseInt(m.group(1));
			int minute = hour * 60;
			try {
				minute += Integer.parseInt(m.group(2).substring(1));
			} catch(NullPointerException ignored) {}
			hits.add(hour * 3600 + minute * 60);
		} while(m.find());
		return hits;
	}
	
}

class Hit {

}