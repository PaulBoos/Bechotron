package Head;

import Modules.Admin.GlobalBanlist;
import Modules.DeutscheBahn.DBModule;
import Modules.Fun.ShipperModule;
import Modules.Music.MusicModule;
import Modules.Steam.SteamModule;
import Modules.TestModule.Test;
import Modules.Timestamp.TimestampModule;
import Modules.UrbanDictionary.UrbanDictionaryModule;
import Modules.VoteModule.VotingModule;
import Utils.Security.Tokens;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
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
	public static SteamModule steamModule;
	
	public static void main(String[] args) throws IOException, UnsupportedFlavorException {
		try {
			new BotInstance(Tokens.readToken("bot"));
		} catch(Tokens.TokenFileNotFoundException e) {
			e.printStackTrace();
			if(Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor).toString().contains(Integer.toString(0x0a))) {
				System.out.println("LOGIN FAILED - CLIPBOARD TOKEN INVALID");
			}
			Thread t = new Thread(() -> {
				try {
					new BotInstance(Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor).toString());
				} catch(Exception ex) {
					System.out.println("\nvvv CLIPBOARD LOGIN FAILED vvv");
					ex.printStackTrace();
				}
			});
			t.start();
		} catch(LoginException | InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	BotInstance(String token) throws LoginException, InterruptedException, IOException {
		jda = JDABuilder.create(
				token,
				GatewayIntent.GUILD_MEMBERS,
				GatewayIntent.GUILD_BANS,
				GatewayIntent.GUILD_EMOJIS_AND_STICKERS,
				GatewayIntent.GUILD_VOICE_STATES,
				GatewayIntent.GUILD_MESSAGES,
				GatewayIntent.GUILD_MESSAGE_REACTIONS,
				GatewayIntent.GUILD_PRESENCES,
				GatewayIntent.DIRECT_MESSAGES,
				GatewayIntent.DIRECT_MESSAGE_REACTIONS,
				GatewayIntent.MESSAGE_CONTENT
		).enableCache(
				CacheFlag.ACTIVITY,
				CacheFlag.CLIENT_STATUS
		).setMemberCachePolicy(
				MemberCachePolicy.ALL
		).build();
		jda.awaitReady();
		setPresence();
		new GlobalBanlist(this);
		new Test(this);
		createGuilds(false);
		new DBModule(jda);
		new MusicModule(jda);
		jda.addEventListener(new PrivateChatHandler());
		new ShipperModule(jda);
		new UrbanDictionaryModule(jda);
		steamModule = new SteamModule(this);
		TimestampModule.createModule(739513862449266729L);
		TimestampModule.createModule(1037783202430976131L);
		VotingModule vm = new VotingModule(this, 739513862449266729L, 282551955975307264L, 529637080431853589L);
		
		//DBIPModule dbipModule = new DBIPModule();
		//jda.addEventListener(dbipModule);
		
		
	}
	
	public void setPresence() {
		jda.getPresence().setPresence(OnlineStatus.ONLINE, Activity.of(Activity.ActivityType.PLAYING, " Factorio with your mom"));
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
