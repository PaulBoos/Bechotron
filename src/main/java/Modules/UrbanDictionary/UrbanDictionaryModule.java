package Modules.UrbanDictionary;

import Modules.Module;
import Modules.RequireModuleHook;
import Modules.SlashCommands.Command;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.List;

public class UrbanDictionaryModule extends ListenerAdapter implements Module {
	
	private static final RequireModuleHook HOOK = new RequireModuleHook();
	
	private static final String
		BASE_URL = "http://api.urbandictionary.com/v0/define?term=",
		BASE_RAN = "http://api.urbandictionary.com/v0/random",
		BASE_TAG = "http://api.urbandictionary.com/v0/random?tag=";
	
	private static final ObjectMapper mapper = new ObjectMapper();
	public static final Command URBAN = new Command(
			"urban",
			"Search the famous URBAN DICTIONARY",
			event -> {
				try {
					String searchQuery = event.getOption("query").getAsString();
					int resultsRequested = event.getOption("results") != null ? Math.min(event.getOption("results").getAsInt(), 5) : 1;
					List<UDEntry> entries = requestUrbanEntries(searchQuery);
					
					for(int i = 0; i < resultsRequested; i++) {
						UDEntry pointer = entries.get(i);
						EmbedBuilder eb = new EmbedBuilder()
								.setTitle(searchQuery.toUpperCase())
								.setDescription(
										pointer.definition.replaceAll("[\\[\\]_]", "")
												+ "\n\n_"
												+ pointer.example.replaceAll("[\\[\\]_]", "") + "_")
								.setTimestamp(pointer.timestamp)
								.setFooter("\uD83D\uDC4D " + pointer.thumbs_up + " | " + pointer.thumbs_down + " \uD83D\uDC4E") // 👍👎
								.setColor(new Color(78,124,160))
								.setAuthor("\"" + pointer.author + "\" in The Urban Dictionary", pointer.permalink)
								.setImage("https://external-content.duckduckgo.com/iu/?u=http%3A%2F%2Flegalinsurrection.com%2Fwp-content%2Fuploads%2F2013%2F10%2FUrban-Dictionary-Logo.jpg");
						
						if(i == 0) event.replyEmbeds(eb.build()).complete();
						else event.getChannel().sendMessageEmbeds(eb.build()).queue();
					}
				} catch(IOException e) {
					event.reply("Your search could not be completed.").queue();
					e.printStackTrace();
				}
			},
			new OptionData(OptionType.STRING, "query", "Put the query term here.", true),
			new OptionData(OptionType.INTEGER, "results", "How many results you want to see (Max 10).", false));
	
	public UrbanDictionaryModule(JDA jda) {
		jda.addEventListener(this);
	}
	
	public static List<UDEntry> requestUrbanEntries(String searchQuery) throws IOException {
		return mapper.readValue(new URL((BASE_URL + searchQuery).replace(" ", "%20")), entryList.class).list;
	}
	
	@Override
	public void init(Guild guild) {
	
	}
	
	@Override
	public String getDescription() {
		return "This module allows to look up terms in the Urban Dictionary!";
	}
	
	@Override
	public String getName() {
		return "Urban Dictionary Module";
	}
	
	
	@Override
	public List<RequireModuleHook> requireModules() {
		return null;
	}
	
	@Override
	public RequireModuleHook getMyRequireModuleHook() {
		return HOOK;
	}
	
	public static class entryList {
		
		@JsonProperty
		List<UDEntry> list;
		
	}
	
}
