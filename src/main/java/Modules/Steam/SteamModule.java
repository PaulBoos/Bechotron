package Modules.Steam;

import Head.BotInstance;
import Modules.Module;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Request;
import org.apache.http.util.EntityUtils;

import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class SteamModule implements Module {
	
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
			channel.sendMessage("Sorry, couldn't fulfill your request.");
		}
		
	}
	
	public SteamUser requestSteamUser(long steamid) throws IOException {
		Request request = Request.Get(String.format(
			"http://api.steampowered.com/ISteamUser/GetPlayerSummaries/v0002/?key=%s&steamids=%s&format=xml",
			getToken(), steamid
		));
		HttpResponse httpResponse = request.execute().returnResponse();
		if (httpResponse.getEntity() != null) {
			String xml = EntityUtils.toString(httpResponse.getEntity());
			return new XmlMapper().readValue(
				xml // TODO Fix this heresy
					.replace("<response>", "")
					.replace("</response>", "")
					.replace("<players>","")
					.replace("</players>","")
				, SteamUser.class);
		}
		return null;
	}
	
	public static void main(String[] args) throws IOException, UnsupportedFlavorException, InterruptedException {
		BotInstance.main(null);
		Thread.sleep(5000);
		BotInstance.steamModule.prettySteamProfileEmbed(758778066725371994L, 76561198320395703L);
		// channel 758778066725371994L
		// user     76561197960435530L
	}
	
	public String getToken() {
		if(token == null) {
			try {
				DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get("./tokens/"), path -> path.toFile().isFile());
				for(Path path: stream) {
					if(path.getFileName().toString().equals("steam")) {
						List<String> lines = Files.readAllLines(path);
						token = lines.get(0);
						return token;
					}
				}
			} catch(IOException e) {
				e.printStackTrace();
			}
		} else return token;
		return null;
	}
	
	
	@Override
	public String getDescription() {
		return "This Module allows to get some information off steam.";
	}
}
