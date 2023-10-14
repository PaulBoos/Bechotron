package Modules.Ticket;

import Head.BotInstance;
import Modules.Module;
import Modules.RequireModuleHook;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.ThreadChannel;
import net.dv8tion.jda.api.entities.channel.forums.ForumPost;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;

import java.util.List;

public class TicketModule implements Module {
	
	long guildid;
	TicketButtons ticketButtons;
	
	public TicketModule(BotInstance botInstance, long guildid) {
		this.guildid = guildid;
		ticketButtons = new TicketButtons();
		botInstance.jda.addEventListener(ticketButtons);
		ForumPost fp = botInstance.jda.getForumChannelById(1047724679252693033L).createForumPost(
				"Open Tickets Here",
				new MessageCreateBuilder().addContent(
						"Press the Button below to open a ticket"
				).addActionRow(
						Button.primary("openticket", "Open Ticket")
				).build()
		).complete();
		System.out.println("Sent");
		ThreadChannel tc = fp.getThreadChannel();
		tc.getManager().setPinned(true).complete();
	}
	
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
		return null;
	}
	
	@Override
	public String getDescription() {
		return null;
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
