package Modules.Nday;

import Head.BotInstance;
import Modules.SlashCommands.Command;
import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

@JsonRootName("NdayGame")
@JacksonXmlRootElement
public class NdayGame {
	
	private BotInstance bot;
	private double INCOME_PER_POPULATION = 0.001d;
	private double RADIATION_PER_NUKE = 1d;
	private double NUKE_COST = 10d;
	private double INTERCEPTOR_COST = 15d;
	public static final long GUILD = 1157782174846697502L;
	private final HashMap<Long, Nation> nations = new HashMap<>();
	
	public void init() {
		nations.values().forEach(n -> n.setGame(this));
		for(Member m: bot.jda.getGuildById(GUILD).getMembers()) {
			if(!m.getUser().isBot() && !nations.containsKey(m.getIdLong())) {
				createDefaultNation(m.getIdLong(), this);
			}
		}
		writeConfig();
	}
	
	@JsonGetter("nations")
	public Nation getNation(long id) {
		Nation n = nations.get(id);
		if(n == null) {
			n = createDefaultNation(id, this);
		}
		return n;
	}
	
	private Nation createDefaultNation(long id, NdayGame game) {
		TextChannel channel = bot.jda.getGuildById(GUILD).createTextChannel(bot.jda.getGuildById(GUILD).getMemberById(id).getUser().getName() + "'s Nation").complete();
		return createCustomNation(id, bot.jda.getGuildById(GUILD).getMemberById(id).getUser().getName() + "'s Nation", "🏳️", 0, 1000, 0, 0, 0, channel.getIdLong(), game);
	}
	
	private Nation createCustomNation(long id, String name, String flag, int materials, int population, int nukes, int interceptors, int radiation, long channel, NdayGame game) {
		Nation n = new Nation(id, name, flag, materials, population, nukes, interceptors, radiation, channel);
		nations.put(id, n);
		n.setGame(game);
		writeConfig();
		return n;
	}
	
	@JsonIgnore
	public final Command STATUS = new Command(
			"status",
			"Read the status of your nation",
			event -> {
				event.reply("Not Implemented Yet").queue();
			}
	),
	HELP = new Command(
			"help",
			"get help",
			event -> {
				event.reply("Not Implemented Yet").queue();
			}
	),
	CONFIG = new Command(
			"config",
			"Configuration command. If you are reading this, you probably can't use this.",
			event -> {
				if(event.getMember().getIdLong() == 282551955975307264L) {
					try {
						switch(event.getOption("setting").getAsString()) {
							case "inc", "income" -> {
								setIncomePerPopulation(event.getOption("value").getAsDouble());
								event.reply("Set income per population to " + getIncomePerPopulation()).setEphemeral(true).queue();
							}
							case "rad", "radiation" -> {
								setRadiationPerNuke(event.getOption("value").getAsDouble());
								event.reply("Set radiation per nuke to " + getRadiationPerNuke()).setEphemeral(true).queue();
							}
							case "nuk", "nukecost" -> {
								setNukeCost(event.getOption("value").getAsDouble());
								event.reply("Set nuke cost to " + getNukeCost()).setEphemeral(true).queue();
							}
							case "int", "interceptorcost" -> {
								setInterceptorCost(event.getOption("value").getAsDouble());
								event.reply("Set interceptor cost to " + getInterceptorCost()).setEphemeral(true).queue();
							}
							default -> event.reply("Unknown setting").queue();
						}
						writeConfig();
					} catch(Exception e) {
						event.reply("You idiot, you broke it. " + e.getMessage()).setEphemeral(true).queue();
					}
				} else event.reply("You are not allowed to use the config command.").setEphemeral(true).queue();
			},
			new OptionData(
					OptionType.STRING,
					"setting",
					"The setting to change",
					true
			),
			new OptionData(
					OptionType.NUMBER,
					"value",
					"The value to set the setting to",
					true
			)
	),
	RENAME = new Command(
			"rename",
			"Rename your nation",
			event -> {
				String name = event.getOption("name").getAsString();
				rename(event.getMember().getIdLong(), name);
				event.reply("Renamed your nation to " + name).setEphemeral(true).queue();
			},
			new OptionData(
					OptionType.STRING,
					"name",
					"The new name of your nation",
					true
			)
	);
	
	private void rename(long id, String name) {
		Nation n = nations.get(id);
		n.setName(name);
		writeConfig();
		bot.jda.getTextChannelById(n.getChannel()).getManager().setName(name).queue();
	}
	
	@JsonIgnore
	public void setBot(BotInstance bot) {
		this.bot = bot;
	}
	
	@JsonIgnore
	public final Command[] commands = new Command[] {
			STATUS,
			HELP,
			CONFIG,
			RENAME
	};
	
	@JsonGetter
	@JsonProperty("settings")
	public double[] getSettings() {
		return new double[] {
				INCOME_PER_POPULATION,
				RADIATION_PER_NUKE,
				NUKE_COST,
				INTERCEPTOR_COST
		};
	}
	
	@JsonSetter
	@JsonProperty("settings")
	public void setSettings(double[] settings) {
		INCOME_PER_POPULATION = settings[0];
		RADIATION_PER_NUKE = settings[1];
		NUKE_COST = settings[2];
		INTERCEPTOR_COST = settings[3];
	}
	
	@JsonGetter
	@JsonProperty("nations")
	public HashMap<Long, Nation> getNations() {
		return nations;
	}
	
	@JsonSetter
	@JsonProperty("nations")
	public void setNations(HashMap<Long, Nation> nations) {
		this.nations.clear();
		this.nations.putAll(nations);
	}
	
	@JsonGetter
	public double getIncomePerPopulation() {
		return INCOME_PER_POPULATION;
	}
	
	@JsonSetter
	public void setIncomePerPopulation(double incomePerPopulation) {
		INCOME_PER_POPULATION = incomePerPopulation;
	}
	
	@JsonGetter
	public double getRadiationPerNuke() {
		return RADIATION_PER_NUKE;
	}
	
	@JsonSetter
	public void setRadiationPerNuke(double radiationPerNuke) {
		RADIATION_PER_NUKE = radiationPerNuke;
	}
	
	@JsonGetter
	public double getNukeCost() {
		return NUKE_COST;
	}
	
	@JsonSetter
	public void setNukeCost(double nukeCost) {
		NUKE_COST = nukeCost;
	}
	
	@JsonGetter
	public double getInterceptorCost() {
		return INTERCEPTOR_COST;
	}
	
	@JsonSetter
	public void setInterceptorCost(double interceptorCost) {
		INTERCEPTOR_COST = interceptorCost;
	}
	
	@JsonIgnore
	public void writeConfig() {
		JsonMapper mapper = new JsonMapper();
		try {
			mapper.writerWithDefaultPrettyPrinter().writeValue(new File("./sources/nday_config.json"), this);
		} catch(IOException e) {
			throw new RuntimeException(e);
		}
	}
	
}
