package Modules.Timestamp;

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

public class TimestampListener extends ListenerAdapter {
	
	private static final HashMap<String, Integer> timezoneOffsets = new HashMap<>() {{
		put("1044331600969740308",     0); // UTC
		put("1044332040063029300", +  60); // CET
		put("1045788491163906159", + 120); // EET
		put("1148389505892634684", - 240); // EDT
		put("1044331770272817192", - 300); // PST
	}};
	private static final Pattern[] messagePatterns = new Pattern[] {
			Pattern.compile("(20|21|22|23|24|1\\d|\\d)([;,:.]\\d\\d|\\sh|h)"), // Sensible Notation
			Pattern.compile("(10|11|12|\\d)([;,:.]\\d\\d)?\\s?(am|pm)")  // American Notation
	};
	final List<Long> enabledGuilds = new ArrayList<>();
	
	@Override
	public void onMessageReceived(@NotNull MessageReceivedEvent event) {
		if(!event.isFromGuild()) return;
		if(!enabledGuilds.contains(event.getGuild().getIdLong())) return;
		if(event.getAuthor().isBot()) return;
		String s = event.getMessage().getContentStripped();
		{ // Sensible Notation
			Matcher m = messagePatterns[0].matcher(s);
			if(m.find()) {
				List<String> hits = new ArrayList<>();
				do {
					int minute = Integer.parseInt(m.group(1)) * 60;
					try {
						minute += Integer.parseInt(m.group(2).substring(1));
					} catch(NullPointerException | NumberFormatException ignored) {}
					hits.add(renderHit(event, m, minute));
				} while(m.find());
				event.getChannel().sendMessage(String.join("\n", hits)).queue();
				if(!hits.isEmpty()) return;
			}
		} { // American Notation
			Matcher m = messagePatterns[1].matcher(s);
			if(m.find()) {
				List<String> hits = new ArrayList<>();
				do {
					int minutes = (Integer.parseInt(m.group(1)) + (m.group(3).equals("pm") ? 12 : 0)) * 60;
					try {
						minutes += Integer.parseInt(m.group(2));
					} catch(NullPointerException | NumberFormatException ignored) {}
					hits.add(renderHit(event, m, minutes));
				} while(m.find());
				event.getChannel().sendMessage(String.join("\n", hits)).queue();
				if(!hits.isEmpty()) return;
			}
		}
	}
	
	/**
	 * Renders a message for the user to receive.
	 * @return the message to send to the user.
	 */
	private String renderHit(@NotNull MessageReceivedEvent event, Matcher m, int minutes) {
		assert event.getMember() != null;
		int offset = -getMembersTimezoneOffset(event.getMember());
		int hour = offset / 60;
		int mem = minutes + offset % 60;
		hour %= 24;
		return "I read %s and that should be <t:%d:t> for you.".formatted(m.group(0), hour * 3600 + mem * 60);
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
	
}
