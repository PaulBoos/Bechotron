package Modules.Fun;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PrivateChannels {
	
	/*
	 * Returns a
	 */
	public static List<Member> fixPrivateChannels(JDA jda, long guildid) {
		Guild guild = jda.getGuildById(guildid);
		Category category;
		try {
			category = guild.getCategoriesByName("private channels", true).get(0);
		} catch(IndexOutOfBoundsException e) {
			category = guild.createCategory("private channels").complete();
		}
		List<TextChannel> channels = category.getTextChannels();
		List<Member> membersWithAChannel = new ArrayList<>();
		Pattern pattern = Pattern.compile("#\\d{18}");
		for(TextChannel tc: channels) {
			try {
				String topic = tc.getTopic();
				if(topic == null || topic.equals(""))
					tc.delete().queue();
				Matcher matcher = pattern.matcher(topic);
				if(!matcher.find())
					tc.delete().queue();
				Member cache = guild.getMemberById(matcher.group().substring(1));
				membersWithAChannel.add(cache);
			} catch(IllegalStateException e) {
				tc.delete().queue();
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		List<Member> membersWithoutAChannel = new ArrayList<>(guild.getMembers());
		membersWithoutAChannel.removeAll(membersWithAChannel);
		return null;
	}
	
}
