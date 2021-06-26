package Head;

import Files.ConfigReader;
import Modules.Music.MusicModule;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import javax.security.auth.login.LoginException;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BotInstance {
	
	public final List<GuildInstance> guildInstances = new ArrayList<>();
	public JDA jda;
	
	public static void main(String[] args) throws IOException, UnsupportedFlavorException {
		if(ConfigReader.readTokenFile()) {
			Thread t = new Thread(() -> {
				try {
					new BotInstance(ConfigReader.firstLine);
				} catch(Exception e) {
					System.out.println("\nvvv LOGIN FAILED vvv");
					e.printStackTrace();
				}
			});
			t.start();
		} else {
			if(Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor).toString().contains(Integer.toString(0x0a))) {
				System.out.println("LOGIN FAILED - CLIPBOARD TOKEN INVALID");
			}
			Thread t = new Thread(() -> {
				try {
					new BotInstance(Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor).toString());
				} catch(Exception e) {
					System.out.println("\nvvv LOGIN FAILED vvv");
					e.printStackTrace();
				}
			});
			t.start();
		}
	}
	
	BotInstance(String token) throws LoginException, InterruptedException {
		jda = JDABuilder.create(
				token,
				GatewayIntent.GUILD_MEMBERS,
				GatewayIntent.GUILD_BANS,
				GatewayIntent.GUILD_EMOJIS,
				GatewayIntent.GUILD_VOICE_STATES,
				GatewayIntent.GUILD_MESSAGES,
				GatewayIntent.GUILD_MESSAGE_REACTIONS,
				GatewayIntent.GUILD_PRESENCES,
				GatewayIntent.DIRECT_MESSAGES,
				GatewayIntent.DIRECT_MESSAGE_REACTIONS
		).enableCache(
				CacheFlag.ACTIVITY,
				CacheFlag.CLIENT_STATUS
		).setMemberCachePolicy(
				MemberCachePolicy.ALL
		).build();
		System.out.println("Login Successful");
		jda.awaitReady();
		setPresence();
		createGuilds(false);
		new MusicModule();
		jda.addEventListener(new PrivateChatHandler());
	}
	
	public void setPresence() {
//		jda.getPresence().setPresence(OnlineStatus.ONLINE, Activity.of(Activity.ActivityType.LISTENING, "\"help!\" or \"about!\""));
		jda.getPresence().setPresence(OnlineStatus.ONLINE, Activity.of(Activity.ActivityType.DEFAULT, "Now with SLASH COMMANDS!"));
		System.out.println("Presence Set.");
	}
	
	public void createGuilds(boolean log) {
		if(log) {
//			System.out.println("\nCreating Guild-Instances:");
//			int length = 0;
//			for(Guild g: jda.getGuilds()) {
//				guildInstances.add(new GuildInstance(this, g));
//				if(g.getName().length() > length) length = g.getName().length();
//			}
//			for(int i = 0; i < guildInstances.size(); i++) {
//				StringBuilder s = new StringBuilder("\"" + guildInstances.get(i).getGuild().getName() + "\"");
//				for(int x = length - guildInstances.get(i).getGuild().getName().length(); x > 0; x--) s.append(" ");
//				System.out.println(s + "  as " + guildInstances.get(i));
//			}
		} else {
			System.out.println("\n[BotInstance] Creating Guild-Instances...");
			for(Guild g: jda.getGuilds()) {
				guildInstances.add(new GuildInstance(this, g.getIdLong()));
			}
			System.out.println("[BotInstance] Created!");
		}
	}
	
}
