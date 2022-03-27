package Modules.DeutscheBahn;

import Modules.DeutscheBahn.Objects.Arrival;
import Modules.DeutscheBahn.Objects.Departure;
import Modules.DeutscheBahn.Objects.Station;
import Modules.DeutscheBahn.Responses.GetArrivalsResponse;
import Modules.DeutscheBahn.Responses.GetDeparturesResponse;
import Modules.DeutscheBahn.Responses.GetStationsResponse;
import Modules.DeutscheBahn.Responses.Journey;
import Modules.Module;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Request;
import org.apache.http.util.EntityUtils;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.time.Instant;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DBModule extends ListenerAdapter implements Module {
	
	private final JDA jda;
	
	private static final char S = '║', W = '═', LO = '╔', RO = '╗', LU = '╚', RU = '╝';
	
	public DBModule(JDA jda) {
		this.jda = jda;
		jda.addEventListener(this);
	}
	
	@Override
	public void onMessageReceived(@NotNull MessageReceivedEvent event) {
		if(event.getAuthor().isBot())
			return;
		String messageContent = event.getMessage().getContentRaw();
		String[] args = messageContent.split(" ");
		if(args.length > 0) {
			if(args[0].equalsIgnoreCase("station")) {
				if(args.length >= 2) {
					try {
						StringBuilder sb = new StringBuilder("These are the search hits I got:");
						for(Station s: requestStation("en", messageContent.substring(8)).getStations()) {
							sb.append("\n> `#").append(s.getId()).append("`: ").append(s.getName());
						}
						event.getChannel().sendMessage(sb.toString()).queue();
					} catch(IOException e) {
						event.getChannel().sendMessage("❌ Oh no! Seems like something broke. Maybe try again later?").queue();
					} catch(NullPointerException e) {
						event.getChannel().sendMessage("❔ Oops! Seems like I found nothing.").queue();
					}
				} else {
					event.getChannel().sendMessage("\uD83D\uDD0D To search for stations in the **Deutsche Bahn** Database, please provide a search term.").queue();
				}
			} else if(args[0].equalsIgnoreCase("departures")) {
				if(args.length >= 2) {
					long foundId = 0L;
					String stationName;
					String unixDay = null;
					String unixTime = null;
					Matcher matcher;
					
					matcher = Pattern.compile("\\d{1,2}:\\d{1,2}").matcher(messageContent.substring(11));
					if(matcher.find()) {
						unixTime = matcher.group();
						messageContent = messageContent.replace(matcher.group(), "");
					}
					matcher = Pattern.compile("\\d{1,2}\\.\\d{1,2}\\.?").matcher(messageContent.substring(11));
					if(matcher.find()) {
						unixDay = matcher.group().substring(3, 5) + '/' + matcher.group().substring(0, 2);
						messageContent = messageContent.replace(matcher.group(), "");
					}
					matcher = Pattern.compile("\\d{1,2}/\\d{1,2}").matcher(messageContent.substring(11));
					if(matcher.find()) {
						unixDay = matcher.group();
						messageContent = messageContent.replace(matcher.group(), "");
					}
					matcher = Pattern.compile("#?\\d{3,8}").matcher(messageContent);
					if(matcher.find())
						foundId = Long.parseLong(matcher.group());
					else {
						try {
							foundId = requestStation("en", messageContent.substring(11)).getStations().get(0).getId();
						} catch(IOException | NullPointerException ignored) {}
					}
					
					try {
						stationName = requestStation("en", String.valueOf(foundId)).getStations().get(0).getName();
						StringBuilder sb = new StringBuilder(
								unixTime != null ?
										unixDay != null ?
												String.format(
														"These are the Departures for Station `#%7d` \"%s\" on %s at %s",
														foundId, stationName, unixDay, unixTime
												):
												String.format(
														"These are the Departures for Station `#%7d` \"%s\" at %s",
														foundId, stationName, unixTime
												):
										unixDay != null ?
												String.format(
														"These are the Departures for Station `#%7d` \"%s\" on %s",
														foundId, stationName, unixDay
												):
												String.format(
														"These are the Departures for Station `#%7d` \"%s\"",
														foundId, stationName
												)
						);
						for(Departure d: requestDepartures("en", foundId,
								Instant.parse(
										(unixDay == null ?
												Instant.now().toString().substring(0,11) :
												((Instant.now().getEpochSecond() / 31536000) + 1970) + "-" + unixDay + "T"
										).replace('/', '-') +
										(unixTime == null ?
												Instant.now().toString().substring(11) : unixTime + ":00Z"
										).replace('/', '-')
								)
						).getDepartures()) {
							sb.append("\n> **").append(d.getName()).append("** departs at **<t:")
									.append(d.getInstant().getEpochSecond()).append(":t>** in Direction **")
									.append(d.getDirection()).append("** on Track ").append(d.getTrack()).append(".");
						}
						event.getChannel().sendMessage(sb.toString()).queue();
					} catch(IOException e) {
						event.getChannel().sendMessage("❌ Oh no! Seems like something broke. Maybe try again later?").queue();
					} catch(NullPointerException e) {
						event.getChannel().sendMessage("❔ Oops! Seems like I found nothing.").queue();
					}
				} else {
					event.getChannel().sendMessage("\uD83D\uDD0D To search for departures from a station in the **Deutsche Bahn** Database, please provide an id or (precise) search term.").queue();
				}
			} else if(args[0].equalsIgnoreCase("arrivals")) {
				if(args.length >= 2) {
					long foundId = 0L;
					String stationName;
					String unixDay = null;
					String unixTime = null;
					Matcher matcher;
					
					matcher = Pattern.compile("\\d{1,2}:\\d{1,2}").matcher(messageContent.substring(11));
					if(matcher.find()) {
						unixTime = matcher.group();
						messageContent = messageContent.replace(matcher.group(), "");
					}
					matcher = Pattern.compile("\\d{1,2}\\.\\d{1,2}\\.?").matcher(messageContent.substring(11));
					if(matcher.find()) {
						unixDay = matcher.group().substring(3, 5) + '/' + matcher.group().substring(0, 2);
						messageContent = messageContent.replace(matcher.group(), "");
					}
					matcher = Pattern.compile("\\d{1,2}/\\d{1,2}").matcher(messageContent.substring(11));
					if(matcher.find()) {
						unixDay = matcher.group();
						messageContent = messageContent.replace(matcher.group(), "");
					}
					matcher = Pattern.compile("#?\\d{3,8}").matcher(messageContent);
					if(matcher.find())
						foundId = Long.parseLong(matcher.group());
					else {
						try {
							foundId = requestStation("en", messageContent.substring(11)).getStations().get(0).getId();
						} catch(IOException | NullPointerException ignored) {}
					}
					
					try {
						stationName = requestStation("en", String.valueOf(foundId)).getStations().get(0).getName();
						StringBuilder sb = new StringBuilder(
								unixTime != null ?
										unixDay != null ?
												String.format(
														"These are the Arrivals for Station `#%7d` \"%s\" on %s at %s",
														foundId, stationName, unixDay, unixTime
												):
												String.format(
														"These are the Arrivals for Station `#%7d` \"%s\" at %s",
														foundId, stationName, unixTime
												):
										unixDay != null ?
												String.format(
														"These are the Arrivals for Station `#%7d` \"%s\" on %s",
														foundId, stationName, unixDay
												):
												String.format(
														"These are the Arrivals for Station `#%7d` \"%s\"",
														foundId, stationName
												)
						);
						for(Arrival a: requestArrivals("en", foundId,
								Instant.parse(
										(unixDay == null ?
												Instant.now().toString().substring(0,11) :
												((Instant.now().getEpochSecond() / 31536000) + 1970) + "-" + unixDay + "T"
										).replace('/', '-') +
												(unixTime == null ?
														Instant.now().toString().substring(11) : unixTime + ":00Z"
												).replace('/', '-')
								)
						).getArrivals()) {
							sb.append("\n> **").append(a.getName()).append("** departs at **<t:")
									.append(a.getInstant().getEpochSecond()).append(":t>** in Direction **")
									.append(a.getOrigin()).append("** on Track ").append(a.getTrack()).append(".");
						}
						event.getChannel().sendMessage(sb.toString()).queue();
					} catch(IOException e) {
						event.getChannel().sendMessage("❌ Oh no! Seems like something broke. Maybe try again later?").queue();
					} catch(NullPointerException e) {
						event.getChannel().sendMessage("❔ Oops! Seems like I found nothing.").queue();
					}
				} else {
					event.getChannel().sendMessage("\uD83D\uDD0D To search for arrivals from a station in the **Deutsche Bahn** Database, please provide an id or (precise) search term.").queue();
				}
			} else if(args[0].equalsIgnoreCase("board")) {
			
			}
		}
	}
	
	private static final Pattern stationPattern = Pattern.compile("#?\\d{3,8}");
	public static long recognizeStationId(String input) {
	
	}
	private static final Pattern[] timePatterns = Pattern.compile("\\d{1,2}:\\d{1,2}");
	public static String recognizeTime(String input) {
	
	}
	private static final Pattern[] datePatterns = Pattern.compile("\\d{1,2}[./]\\d{1,2}\\.?");
	public static String recognizeDate(String input) {
	
	}
	
	private static final String authKey = "DBhackFrankfurt0316";
	public static GetStationsResponse requestStation(String lang, String station) throws IOException {
		Request request = Request.Get(String.format(
				"https://open-api.bahn.de/bin/rest.exe/location.name?format=xml&lang=%s&authKey=%s&input=%s",
				lang, authKey, station
		).replace(" ", "%20"));
		HttpResponse httpResponse = request.execute().returnResponse();
		if (httpResponse.getEntity() != null) {
			String xml = EntityUtils.toString(httpResponse.getEntity());
			return new XmlMapper().readValue(xml, GetStationsResponse.class);
		}
		return null;
	}
	public static GetDeparturesResponse requestDepartures(String lang, long id, Instant instant) throws IOException {
		Request request = Request.Get(String.format(
				"https://open-api.bahn.de/bin/rest.exe/departureBoard?format=xml&lang=%s&authKey=%s&id=%d&date=%s&time=%s",
				lang, authKey, id, instant.toString().substring(0,10), instant.toString().substring(11,16)
		).replace(" ", "%20"));
		HttpResponse httpResponse = request.execute().returnResponse();
		if (httpResponse.getEntity() != null) {
			String xml = EntityUtils.toString(httpResponse.getEntity());
			return new XmlMapper().readValue(xml, GetDeparturesResponse.class);
		}
		return null;
	}
	public static GetArrivalsResponse requestArrivals(String lang, long id, Instant instant) throws IOException {
		Request request = Request.Get(String.format(
				"https://open-api.bahn.de/bin/rest.exe/arrivalBoard?format=xml&lang=%s&authKey=%s&id=%d&date=%s&time=%s",
				lang, authKey, id, instant.toString().substring(0,10), instant.toString().substring(11,16)
		).replace(" ", "%20"));
		HttpResponse httpResponse = request.execute().returnResponse();
		if (httpResponse.getEntity() != null) {
			String xml = EntityUtils.toString(httpResponse.getEntity());
			return new XmlMapper().readValue(xml, GetArrivalsResponse.class);
		}
		return null;
	}
	public static Journey requestJourney(String url) throws IOException {
		Request request = Request.Get(url);
		HttpResponse httpResponse = request.execute().returnResponse();
		if (httpResponse.getEntity() != null) {
			String xml = EntityUtils.toString(httpResponse.getEntity());
			return new XmlMapper().readValue(xml, Journey.class);
		}
		return null;
	}
	
	public static String fixHTMLCharacters(String input) {
		return input
				.replace("&#x0028;", "(")
				.replace("&#x0029;", ")");
	}
	
}
