package Head;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PrivateChatHandler extends ListenerAdapter {
	
	@Override
	public void onMessageReceived(@NotNull MessageReceivedEvent event) {
		if(event.getAuthor().isBot()) return;
		int sum = 0;
		try {
			Matcher m = Pattern.compile("\\d+[dD]\\d+").matcher(event.getMessage().getContentDisplay());
			if(m.find()) {
				String s = event.getMessage().getContentDisplay().substring(m.start(),m.end());
				String[] ss = s.split("[dD]");
				if(ss.length != 2) throw new Exception("Tell Becher about this.");
				StringBuilder results = new StringBuilder("You rolled a [").append(s).append("]```");
				int maxValue = Integer.parseInt(ss[1]);
				int dices = Integer.parseInt(ss[0]);
				if(dices < 1 || maxValue < 2) {
					event.getChannel().sendMessage("[" + s + "] makes no sense.").queue();
					return;
				}
				int[] values = new int[dices];
				for(int i = 0; i < dices; i++) {
					values[i] = (int) (Math.random() * (maxValue)) + 1;
					sum += values[i];
					if(i % 8 == 0) results.append("\n").append(values[i]);
					else results.append(", ").append(values[i]);
				}
				results.append("```");
				results.insert(results.indexOf(s) + s.length() + 1," {Sum: " + sum + "}");
				event.getChannel().sendMessage(results).queue();
			}
		} catch(Exception e) {
			if(e.getMessage().equals("Provided text for message must be less than 2000 characters in length")) {
				event.getChannel().sendMessage("Your Sum is " + sum).queue();
			} else {
				e.printStackTrace();
			}
		}
	}
	
}
