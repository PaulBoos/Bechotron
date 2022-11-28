package Modules.BanVote;

import Head.BotInstance;
import Modules.Module;
import Modules.RequireModuleHook;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.PrivateChannel;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class BanVoteModule extends ListenerAdapter implements Module {
	private static final RequireModuleHook HOOK = new RequireModuleHook();
	
	BotInstance bot;
	long[] bans;
	
	public BanVoteModule(BotInstance botInstance) {
		bot = botInstance;
		bot.jda.addEventListener(this);
		for(Guild g: bot.jda.getGuilds()) {
			onGuildReady(new GuildReadyEvent(bot.jda, 0L, g));
		}
	}
	
	private void ban(long userid, long guildid, String reason, String appealInstructions, String publicReason, int deletionTimeframe) {
		PrivateChannel privateChannel = bot.jda.getUserById(userid).openPrivateChannel().complete();
		privateChannel.sendMessageEmbeds(new EmbedBuilder()
				.setTitle("You were banned from a server.")
				.setDescription("You were banned from " + bot.jda.getGuildById(guildid).getName() + "."
						+ (publicReason != null ? "\nThe reason given was: " + publicReason : "\nThe server did not give a reason.")
						+ (appealInstructions != null ? "\nTo appeal this decision, " + appealInstructions : "\nThe server does not allow appealing."))
				.setThumbnail(bot.jda.getGuildById(guildid).getIconUrl())
				.build()).complete();
		bot.jda.getGuildById(guildid).getMemberById(userid).ban(deletionTimeframe, TimeUnit.SECONDS)
				.reason(reason != null ? "This account was banned in a vote for the following reason: " + reason : "This account was banned in a vote, no reason given.")
				.complete();
	}
	
	private void timedban(long userid, long guildid, String reason, String appealInstructions, String publicReason, int deletionTimeframe, Instant unbanTime) {
		ban(userid, guildid, reason, appealInstructions, publicReason, deletionTimeframe);
		// TODO unban users again
	}
	
	
	@Override
	public void init(Guild guild) {
	
	}
	
	@Override
	public String getDescription() {
		return "This module allows you to vote on who to ban.";
	}
	
	@Override
	public String getName() {
		return "BanVote Module";
	}
	
	
	@Override
	public List<RequireModuleHook> requireModules() {
		return null;
	}
	
	@Override
	public RequireModuleHook getMyRequireModuleHook() {
		return HOOK;
	}
}
