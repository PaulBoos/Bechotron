package Modules.Steam;

import Head.BotInstance;
import Modules.Module;
import Modules.RequireModuleHook;
import Utils.Security.Tokens;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Request;
import org.apache.http.util.EntityUtils;

import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.List;

public class SteamModule implements Module {
	
	private static final RequireModuleHook HOOK = new RequireModuleHook();
	
	public final BotInstance botInstance;
	private String token;
	
	public SteamModule(BotInstance botInstance) {
		this.botInstance = botInstance;
	}
	
	public void prettySteamProfileEmbed(long textChannelid, long steamUserId) {
		TextChannel channel = botInstance.jda.getTextChannelById(textChannelid);
		try {
			SteamUser user = requestSteamUser(steamUserId);
			EmbedBuilder builder = new EmbedBuilder();
			builder.setTitle("Steam Profile of " + user.username);
			builder.setThumbnail(user.avatarfull);
			{
				StringBuilder description = new StringBuilder();
				switch(user.onlineState) {
					case 0 -> description.append("Online Status: \u26AA Offline\n");
					case 1 -> description.append("Online Status: \uD83D\uDFE2 Online\n");
					case 2 -> description.append("Online Status: \uD83D\uDD34 Do not Disturb\n");
					case 3 -> description.append("Online Status: \uD83D\uDFE1 AFK / Idle\n");
					case 4 -> description.append("Online Status: \uD83D\uDCA4 Snooze");
					case 5 -> description.append("Online Status: \uD83D\uDCA0 Looking to trade");
					case 6 -> description.append("Online Status: \uD83C\uDFAE Looking to play");
				}
				builder.setDescription(description);
			}
			builder.setFooter("Steam64ID: " + user.steamid);
			channel.sendMessageEmbeds(builder.build()).queue();
		} catch(IOException e) {
			e.printStackTrace();
			channel.sendMessage("Sorry, couldn't fulfill your request.");
		}
		
	}
	
	public SteamUser requestSteamUser(long steamid) throws IOException {
		Request request = null;
		try {
			request = Request.Get(String.format(
				"http://api.steampowered.com/ISteamUser/GetPlayerSummaries/v0002/?key=%s&steamids=%s&format=xml",
				getToken(), steamid
			));
		} catch(Tokens.TokenFileNotFoundException e) {
			e.printStackTrace();
		}
		HttpResponse httpResponse = request.execute().returnResponse();
		if (httpResponse.getEntity() != null) {
			String xml = EntityUtils.toString(httpResponse.getEntity());
			SteamUser[] result = new XmlMapper().readValue(xml, SteamXMLResponse.PlayerRequestResponse.class).players;
			return result[0];
		}
		return null;
	}
	
	public static void main(String[] args) throws IOException, UnsupportedFlavorException, InterruptedException {
		new Thread(() -> {
			try {
				BotInstance.main(null);
			} catch(IOException | UnsupportedFlavorException e) {
				throw new RuntimeException(e);
			}
		}).start();
		Thread.sleep(10000);
		BotInstance.steamModule.prettySteamProfileEmbed(775070563109568512L, 76561198320395703L);
		// channel 758778066725371994L
		// user     76561197960435530L
	}
	
	public String getToken() throws Tokens.TokenFileNotFoundException {
		if(token == null) {
			token = Tokens.readToken("steam");
			return token;
		} else return token;
	}
	
	
	@Override
	public void init(Guild guild) {
	
	}
	
	@Override
	public String getDescription() {
		return "This Module allows to get some information off steam.";
	}
	
	@Override
	public String getName() {
		return "Steam Module";
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
