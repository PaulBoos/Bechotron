package Modules.Admin;

import Head.BotInstance;
import Modules.Module;
import Modules.RequireModuleHook;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.PrivateChannel;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class GlobalBanlist extends ListenerAdapter implements Module {
	
	private RequireModuleHook HOOK = new RequireModuleHook();
	
	BotInstance bot;
	long[] bans;
	
	public GlobalBanlist(BotInstance botInstance) {
		bot = botInstance;
		rereadBanlist();
		bot.jda.addEventListener(this);
		for(Guild g: bot.jda.getGuilds()) {
			onGuildReady(new GuildReadyEvent(bot.jda, 0L, g));
		}
	}
	
	private void ban(long userid, long guildid) {
		PrivateChannel privateChannel = bot.jda.getUserById(userid).openPrivateChannel().complete();
		privateChannel.sendMessage("Hello! You were just banned from " + bot.jda.getGuildById(guildid).getName() +
				", because you are listed on my global banlist. " +
				"To appeal this decision, send an email to ban-appeal@salt.faith").complete();
		bot.jda.getGuildById(guildid).getMemberById(userid).ban(0, TimeUnit.SECONDS)
				.reason("This account is listed on the global banlist.")
				.complete();
	}
	
	@Override
	public void onGuildReady(@NotNull GuildReadyEvent event) {/*
		for(Member m: event.getGuild().getMembers())
			for(long b: bans)
				if(m.getIdLong() == b)
					ban(b, event.getGuild().getIdLong());*/ // TODO onGuildReady
	}
	
	@Override
	public void onGuildMemberJoin(@NotNull GuildMemberJoinEvent event) {
		for(long b: bans)
			if(b == event.getUser().getIdLong())
				ban(b, event.getGuild().getIdLong());
	}
	
	@Override
	public void onMessageReceived(@NotNull MessageReceivedEvent event) {
		for(long l: bans) if(l == event.getAuthor().getIdLong()) {
			bot.jda.getTextChannelById(973916305285595147L).sendMessage(
					String.format("User %s (%d) tries to appeal his global ban:\n%s",
							event.getAuthor().getName(),
							event.getAuthor().getIdLong(),
							event.getMessage().getContentStripped())
			).queue();
		}
	}
	
	public void rereadBanlist() {
		try {
			DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get("."), path -> path.toFile().isFile());
			for(Path path: stream) {
				if(path.getFileName().toString().equals("globalbans.txt")) {
					List<String> lines = Files.readAllLines(path);
					List<Long> banlist = new ArrayList<>();
					for(String line: lines) {
						line = line.replace("#", " #");
						String[] split = line.split(" ");
						for(String s: split) {
							if(s.startsWith("#")) break;
							try {
								long i = Long.parseLong(s);
								banlist.add(i);
							} catch(NumberFormatException ignored) {}
						}
					}
					bans = new long[banlist.size()];
					for(int i = 0; i < banlist.size(); i++) {
						bans[i] = banlist.get(i);
					}
				}
			}
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public String getDescription() {
		return "This module bans accounts on my public global banlist.";
	}
	
	@Override
	public String getName() {
		return "GlobalBanlist";
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
		return HOOK;
	}
	
}
