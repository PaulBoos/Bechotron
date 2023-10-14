package Modules.Ticket;

import net.dv8tion.jda.api.entities.channel.concrete.ForumChannel;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;
import org.jetbrains.annotations.NotNull;

public class TicketButtons extends ListenerAdapter {
	
	@Override
	public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
		if(event.getUser().isBot()) return;
		if(event.getButton().getId().startsWith("openticket")) {
			ForumChannel channel = event.getGuild().getForumChannelById(1047724679252693033L);
			channel.createForumPost("Ticket #asfgsdg", MessageCreateData.fromContent("Ticket opened by " + event.getUser().getAsMention())).queue();
		}
	}
	
}
