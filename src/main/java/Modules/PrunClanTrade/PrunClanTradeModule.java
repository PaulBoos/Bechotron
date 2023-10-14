package Modules.PrunClanTrade;

import Modules.Module;
import Modules.PrunClanTrade.PrunWorldConcept.Currency;
import Modules.PrunClanTrade.PrunWorldConcept.Faction;
import Modules.PrunClanTrade.PrunWorldObject.Material;
import Modules.PrunClanTrade.Trade.Offer;
import Modules.PrunClanTrade.PrunWorldObject.Planet;
import Modules.PrunClanTrade.PrunWorldObject.PrunPlanet.Resource;
import Modules.RequireModuleHook;
import Modules.SlashCommands.Command;
import Utils.StringHelper;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.List;

import static net.dv8tion.jda.api.interactions.commands.OptionType.STRING;

public class PrunClanTradeModule extends ListenerAdapter implements Module {
	
	//🌌☀
	public static final String GAS_GIANT_SYMBOL = "\uD83E\uDE90"; // 🪐
	public static final String PLANET_SYMBOL = "\uD83C\uDF0D"; // 🌍
	public static final String ATMOSPHERE_SYMBOL = "☁";
	public static final String LIQUID_SYMBOL = "\uD83D\uDCA7"; // 💧
	public static final String MINERAL_SYMBOL = "⛰";
	public static final String FERTILITY_SYMBOL = "\uD83C\uDF31"; // 🌱
	
	
	public static FioInterface fio = new FioInterface();
	
	public static final Command SEARCH = new Command(
			"search",
			"Request Prun Game Object Information",
			event -> {
				OptionMapping commandOption = event.getOption("query");
				String searchString =  commandOption != null ? commandOption.getAsString() : "";
				Planet planet = fio.getPlanet(searchString);
				Material material = Material.getMaterial(searchString);
				Faction faction = Faction.getFaction(searchString);
				Currency currency = Currency.getCurrency(searchString);
				if(planet != null) {
					String dynamicName = planet.name.equals(planet.naturalId) ? planet.name : planet.name + " (" + planet.naturalId + ")";
					String dashName = planet.name.equals(planet.naturalId) ? "--" : planet.name;
					EmbedBuilder eb = new EmbedBuilder();
					eb.setTitle((planet.surface ? PLANET_SYMBOL : GAS_GIANT_SYMBOL) + " __Planet Information for " + dynamicName + "__");
					eb.addField("", """
							**__Catalog ID:__**
							**__Name:__**
							*Named by:*
							*Faction affinity:*
							*Governor:*
							*Governing entity:*
							**__Type:__**
							*Radius:*
							""", true);
					eb.addField("", String.format("""
									**%s**
									**%s**
									*%s*
									*%s*
									*%s*
									*%s*
									*%s*
									*%.02f km*
									""",
							planet.naturalId,
							dashName,
							localUtils.doubleDashIfNull(planet.namer),
							localUtils.doubleDashIfNull(planet.factionName),
							localUtils.doubleDashIfNull(planet.governorUserName),
							localUtils.doubleDashIfNull(planet.governorCorporationName),
							planet.surface ? "**Rocky** (requires MCG)" : "**Gaseous** (requires AEF)",
							planet.radius / 1000
					), true);
					eb.addBlankField(false);
					eb.addField("Environment", String.format("""
							🌐 Gravity: **%.2f**
							🌡 Temperature: **%.2f**
							⏲ Pressure: **%.2f**
							""", planet.gravity, planet.temperature, planet.pressure
					), true);
					eb.addField(
							String.format(FERTILITY_SYMBOL + " Soil fertility: %d%%", (int) (planet.fertility * 100)),
							planet.fertility == -1 ? "(Infertile)" : "`-[" + StringHelper.Bar.formatBarWithMarkerRealNumber("=", "|", "0", 20, Math.round(planet.fertility * 10)) + "]+`",
							true);
					if(planet.resources.size() > 0) eb.addBlankField(false);
					else eb.addField("Resources:", "Planet does not provide any Resources.", false);
					int rescount = (planet.resources.size() == 4) ? 2 : 5;
					for(Resource r : planet.resources) {
						if(rescount == 0)
							eb.addBlankField(false);
						rescount--;
						Material mat = Material.getMaterial(r.materialid);
						assert mat != null;
						eb.addField(
								String.format("%s %s (%s): %.02f%%", MaterialEmoji.getEmoji(event.getJDA(), Material.getMaterial(r.materialid)), StringHelper.capitalizeFirstLetter(mat.name), switch(r.resourceType) {
									case MINERAL -> MINERAL_SYMBOL;
									case LIQUID -> LIQUID_SYMBOL;
									case GASEOUS -> ATMOSPHERE_SYMBOL;
									case UNKNOWN -> " Unknown ";
								}, r.factor * 100),
								"`[" + StringHelper.Bar.formatBarWithMarkerTwoSided("█", "", " ", 20, Math.round(r.factor * 20)) + "]`",
								true);
					}
					event.replyEmbeds(eb.build()).queue();
				} else if(material != null) {
					EmbedBuilder eb = new EmbedBuilder();
					eb.setTitle(MaterialEmoji.getEmoji(event.getJDA(), material).getFormatted() + " Material Information for Ticker " + material.ticker + " (" + StringHelper.capitalizeFirstLetter(material.name) + ")");
					eb.setDescription(String.format("Category: **%32s**\nWeight: *~%1.3ft*\nVolume: *~%1.3fm³*", StringHelper.capitalizeWords(material.category.name), material.weight, material.volume));
					eb.setThumbnail(String.format("https://raw.githubusercontent.com/GelberBecher/PrunFiles/master/materials/images/%s.png", material.ticker));
					event.replyEmbeds(eb.build()).queue();
				} else if(faction != null) {
					try {
						List<String> factionInfo = Files.readAllLines(Path.of("./sources/prun/factions/" + faction.name() + ".txt"));
						EmbedBuilder eb = new EmbedBuilder();
						eb.setTitle(faction.displayName + " [" + faction.name() + "]", factionInfo.get(1));
						eb.setDescription("Currency: " + faction.currency + "\n\n>>> " + factionInfo.get(0));
						eb.setThumbnail(factionInfo.get(2));
						event.replyEmbeds(eb.build()).queue();
					} catch(IOException e) {
						throw new RuntimeException(e);
					}
				} else if(currency != null) {
					Faction f = Faction.getFaction(currency.faction);
					EmbedBuilder eb = new EmbedBuilder();
					eb.setTitle("Currency: " + currency.displayName + " [" + currency.name() + "]");
					eb.setDescription("Faction: " + f.displayName + " [" + f.name() + "]");
					event.replyEmbeds(eb.build()).queue();
				} else {
					event.reply("""
							No information found for "%32s".
							You can search for:
							> Materials (Ticker, Name, ID)
							> Planets (Name, ID)
							> Factions (Name, Code)
							> Currencies (Name, Code)
							""".formatted(searchString.replace("\n", ""))).setEphemeral(true).queue();
				}
			},
			new OptionData(
					STRING,
					"query",
					"Search query. You can use the planet's name, ticker or id, material's ticker, "
			).setRequired(true)
	);
	
	PrunModuleDatabase db;
	JDA jda;
	long[] guildIds;
	
	public PrunClanTradeModule(JDA jda, long... guildIds) {
		this.jda = jda;
		this.guildIds = guildIds;
		try {
			db = new PrunModuleDatabase();
		} catch(SQLException e) {
			throw new RuntimeException(e);
		}
		// new Thread(MaterialEmoji::getEmojiMap).start();
	}
	
	protected List<Offer> findOffers(String filters) {
		return null; // TODO
	}
	
	
	@Override
	public Module load(Guild guild) {
		return null;
	}
	
	@Override
	public Module unload(Guild guild) {
		return null;
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
	
	private static class localUtils {
		
		private static String doubleDashIfNull(String s) {
			return s == null ? "--" : s;
		}
	}
	
}
