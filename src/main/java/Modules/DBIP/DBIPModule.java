package Modules.DBIP;

import Modules.Module;
import Modules.RequireModuleHook;
import in.ankushs.dbip.api.DbIpClient;
import in.ankushs.dbip.api.GeoEntity;
import in.ankushs.dbip.exceptions.InvalidIPException;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.net.InetAddress;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DBIPModule extends ListenerAdapter implements Module {
	
	private static final RequireModuleHook HOOK = new RequireModuleHook();
	
	final Pattern // IGNORE THIS. DO NOT LOOK INTO THE REGEX' EYES. YOU MIGHT TURN INTO STONE.
			ipv4pattern = Pattern.compile("(\\b25[0-5]|\\b2[0-4][0-9]|\\b[01]?[0-9][0-9]?)(\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)){3}"),
			ipv6pattern = Pattern.compile("(([0-9a-fA-F]{1,4}:){7,7}[0-9a-fA-F]{1,4}|([0-9a-fA-F]{1,4}:){1,7}:|([0-9a-fA-F]{1,4}:){1,6}:[0-9a-fA-F]{1,4}|([0-9a-fA-F]{1,4}:){1,5}(:[0-9a-fA-F]{1,4}){1,2}|([0-9a-fA-F]{1,4}:){1,4}(:[0-9a-fA-F]{1,4}){1,3}|([0-9a-fA-F]{1,4}:){1,3}(:[0-9a-fA-F]{1,4}){1,4}|([0-9a-fA-F]{1,4}:){1,2}(:[0-9a-fA-F]{1,4}){1,5}|[0-9a-fA-F]{1,4}:((:[0-9a-fA-F]{1,4}){1,6})|:((:[0-9a-fA-F]{1,4}){1,7}|:)|fe80:(:[0-9a-fA-F]{0,4}){0,4}%[0-9a-zA-Z]{1,}|::(ffff(:0{1,4}){0,1}:){0,1}((25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])\\.){3,3}(25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])|([0-9a-fA-F]{1,4}:){1,4}:((25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])\\.){3,3}(25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9]))");
	
	final File gzipFile = new File("./dbip-city-lite-2022-08.csv.gz");
	final DbIpClient client = new DbIpClient(gzipFile);
	
	@Override
	public void onMessageReceived(@NotNull MessageReceivedEvent event) {
		if(event.getAuthor().isBot()) return;
		String messageContent = event.getMessage().getContentRaw();
		if(messageContent.startsWith("dbip ") || messageContent.startsWith("lookup ")) {
			String argument = messageContent.split(" ")[1];
			try {
				Matcher ipv4matcher = ipv4pattern.matcher(argument);
				Matcher ipv6matcher = ipv6pattern.matcher(argument);
				
				if(ipv4matcher.find()) {        // IPv4
					String hit = ipv4matcher.group();
					event.getChannel().sendMessageEmbeds(buildMessage(lookup(hit), event).build()).queue();
				} else if(ipv6matcher.find()) { // IPv6
					String hit = ipv6matcher.group();
					event.getChannel().sendMessageEmbeds(buildMessage(lookup(hit), event).build()).queue();
				} else {                        // None found.
					event.getChannel().sendMessage("Sorry " + event.getAuthor().getAsMention() + ", I could not complete your query. Please make sure you typed the IPv4 or IPv6 Address correctly.").queue();
				}
			} catch(InvalidIPException e) {
				event.getChannel().sendMessage("Sorry " + event.getAuthor().getAsMention() + ", I could not complete your query. Please make sure you typed the IPv4 or IPv6 Address correctly.").queue();
			}
		}
	}
	
	private EmbedBuilder buildMessage(DBIPPacket dbipPacket, MessageReceivedEvent event) {
		EmbedBuilder builder = new EmbedBuilder();
		builder.setTitle("IP lookup for " + dbipPacket.getAddress());
		builder.setDescription(
				String.format(
						"City: %s\nProvince: %s\nCountry: %s [%s]",
						dbipPacket.getCity(),
						dbipPacket.getProvince(),
						dbipPacket.getCountry(),
						dbipPacket.getCountryCode()
				)
		);
		builder.setFooter(
				String.format(
						"Requested by %s (%s)",
						event.getAuthor().getAsTag(),
						event.getAuthor().getIdLong()
				),
				event.getAuthor().getAvatarUrl()
		);
		return builder;
	}
	
	public DBIPPacket lookup(@NotNull String ip) {
		return new DBIPPacket(ip, client.lookup(ip));
	}
	
	public DBIPPacket lookup(@NotNull InetAddress address) {
		return new DBIPPacket(
				address.toString().substring(
						address.toString().indexOf('/')
				), client.lookup(address));
	}
	
	public static void main(String[] args) {
		final DBIPModule module = new DBIPModule();
	}
	
	@Override
	public void init(Guild guild) {
	
	}
	
	@Override
	public String getDescription() {
		return "Module for looking up IP addresses geographic information.\nIP Geolocation by DB-IP (https://db-ip.com/)";
	}
	
	@Override
	public String getName() {
		return null;
	}
	
	
	@Override
	public List<RequireModuleHook> requireModules() {
		return null;
	}
	
	@Override
	public RequireModuleHook getMyRequireModuleHook() {
		return HOOK;
	}
	
	public class DBIPPacket {
		
		private final String address;
		private final GeoEntity geoEntity;
		
		public DBIPPacket(String address, GeoEntity geoEntity) {
			this.address = address;
			this.geoEntity = geoEntity;
		}
		
		public String getAddress() {
			return address;
		}
		public String getCity() {
			return geoEntity.getCity();
		}
		public String getCountry() {
			return geoEntity.getCountry();
		}
		public String getProvince() {
			return geoEntity.getProvince();
		}
		public String getCountryCode() {
			return geoEntity.getCountryCode();
		}
		
	}
	
}
