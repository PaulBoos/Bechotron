package Head;

import Modules.Admin.GlobalBanlist;
import Modules.DeutscheBahn.DBModule;
import Modules.Fun.ShipperModule;
import Modules.Music.MusicModule;
import Modules.Nday.NdayGame;
import Modules.PrunClanTrade.PrunClanTradeModule;
import Modules.Steam.SteamModule;
import Modules.TestModule.Test;
import Modules.Timestamp.TimestampModule;
import Modules.Trivia.TriviaModule;
import Modules.Trivia.TriviaQuestion;
import Modules.UrbanDictionary.UrbanDictionaryModule;
import Modules.VoteModule.VotingModule;
import Utils.Security.Tokens;
import ch.qos.logback.classic.Logger;
import com.fasterxml.jackson.databind.json.JsonMapper;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.slf4j.LoggerFactory;

import javax.security.auth.login.LoginException;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BotInstance {
	
	public final List<GuildInstance> guildInstances = new ArrayList<>();
	public JDA jda;
	private static Logger logger;
	public static SteamModule steamModule;
	public static TriviaModule triviaModule;
	
	public static void main(String[] args) throws IOException, UnsupportedFlavorException {
		logger = setupLogger();
		try {
			new BotInstance(Tokens.readToken("bot"));
		} catch(Tokens.TokenFileNotFoundException e) {
			logger.info("No Token file found, trying to use Clipboard token.");
			if(Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor).toString().contains(Integer.toString(0x0a))) {
				logger.error("Clipboard token invalid! (Contains linebreak)");
			}
			Thread t = new Thread(() -> {
				try {
					new BotInstance(Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor).toString());
				} catch(Exception ex) {
					logger.error("Clipboard token login failed!");
					ex.printStackTrace();
				}
			});
			t.start();
		} catch(LoginException | InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	BotInstance(String token) throws LoginException, InterruptedException {
		jda = JDABuilder.create(
				token,
				Arrays.asList(GatewayIntent.values())
		).enableCache(
				Arrays.asList(CacheFlag.values())
		).setMemberCachePolicy(
				MemberCachePolicy.ALL
		).build();
		jda.awaitReady();
		setPresence();
		new GlobalBanlist(this);
		new Test(this);
		//createGuilds(false);
		new DBModule(jda);
		new MusicModule(jda);
		jda.addEventListener(new PrivateChatHandler());
		new ShipperModule(jda);
		new UrbanDictionaryModule(jda);
		steamModule = new SteamModule(this);
		new TimestampModule(739513862449266729L, this);
		new TimestampModule(1037783202430976131L, this);
		new VotingModule(this, 739513862449266729L, 282551955975307264L, 529637080431853589L);
		//new TicketModule(this, 1047724679252693033L);
		
//		new PrunClanTradeModule(jda, 1047724679252693033L, 1058214669027909684L);
		
		GuildInstance testServer = new GuildInstance(this, 739513862449266729L);
		//GuildInstance nmxb = new GuildInstance(this, 1058214669027909684L);
		GuildInstance J4F = new GuildInstance(this, 652967667946225667L);
		GuildInstance Garry = new GuildInstance(this, 795777477627609153L);
		GuildInstance salz = new GuildInstance(this, 607323699065913345L);
		GuildInstance patron = new GuildInstance(this, 1092947249434218538L);
		GuildInstance candyvan = new GuildInstance(this, 1037783202430976131L);
		
		triviaModule = new TriviaModule(this);
		triviaModule.init();
		
		/*
		File f = new File("./sources/nday_config.json");
		NdayGame nday;
		if(f.exists()) {
			try {
				nday = new JsonMapper().readValue(f, NdayGame.class);
			} catch(IOException e) {
				throw new RuntimeException(e);
			}
		} else {
			nday = new NdayGame();
		}
		nday.setBot(this);
		nday.init();
		GuildInstance ndayguild = new GuildInstance(this, 1157782174846697502L);
		ndayguild.slash.addCommands(nday.commands);
		ndayguild.slash.updateSlashCommands(true);
		*/
		/*
		JsonMapper mapper = new JsonMapper();
		
		System.out.println("Loading Planet Population...");
		List<Planet> planets;
		StringAccumulator errorAccumulator = new StringAccumulator();
		try {
			planets = Planet.loadAllPlanets();
		} catch(IOException e) {
			throw new RuntimeException(e);
		}
		int totalPopulation = 0,
				totalPioneerPopulation = 0,
				totalSettlerPopulation = 0,
				totalTechnicianPopulation = 0,
				totalEngineerPopulation = 0,
				totalScientistPopulation = 0;
		int planetCounter = 0,
				totalPlanets = planets.size(),
				failCounter = 0;
		for(Planet planet : planets) {
			Infrastructure inf;
			planetCounter++;
			try {
				inf = mapper.readValue(FioInterface.loadInfrastructure(planet.naturalId), Infrastructure.class);
			} catch(IOException e) {
				failCounter++;
				System.out.printf("Fail:    %04d/%04d Planets - %d fails. %s\n", planetCounter, totalPlanets, failCounter, e.getMessage());
				errorAccumulator.addString(e.getMessage().split("\\n")[0]);
				continue;
			}
			if(inf.InfrastructureReports.length == 0) {
				System.out.printf("Omitted: %04d/%04d Planets - %d fails.\n", planetCounter, totalPlanets, failCounter);
				errorAccumulator.addString("Reports empty");
				continue;
			}
			totalPopulation += inf.InfrastructureReports[0].NextPopulationSettler
					+ inf.InfrastructureReports[0].NextPopulationPioneer
					+ inf.InfrastructureReports[0].NextPopulationTechnician
					+ inf.InfrastructureReports[0].NextPopulationEngineer
					+ inf.InfrastructureReports[0].NextPopulationScientist;
			totalPioneerPopulation += inf.InfrastructureReports[0].NextPopulationPioneer;
			totalSettlerPopulation += inf.InfrastructureReports[0].NextPopulationSettler;
			totalTechnicianPopulation += inf.InfrastructureReports[0].NextPopulationTechnician;
			totalEngineerPopulation += inf.InfrastructureReports[0].NextPopulationEngineer;
			totalScientistPopulation += inf.InfrastructureReports[0].NextPopulationScientist;
			System.out.printf("Success: %04d/%04d Planets - %d fails.\n", planetCounter, totalPlanets, failCounter);
		}
		System.out.printf("""
						|===============================================
						| Total Population:            %d
						| ----------------------------------------------
						| Total Pioneer Population:    %d
						| Total Settler Population:    %d
						| Total Technician Population: %d
						| Total Engineer Population:   %d
						| Total Scientist Population:  %d
						|===============================================
						""",
				totalPopulation,
				totalPioneerPopulation,
				totalSettlerPopulation,
				totalTechnicianPopulation,
				totalEngineerPopulation,
				totalScientistPopulation);
		System.out.println("Errors:");
		System.out.println(errorAccumulator);
		*/
		
		/*
		MaterialOffer offer1 = new MaterialOffer(
				Material.getMaterial("RAT"),
				"A LOT",
				new Price(100, Currency.CIS),
				"Katoa or smth",
				true,
				true,
				"[Notes]",
				282551955975307264L
		);
		MaterialOffer offer2 = new MaterialOffer(
				Material.getMaterial("DW"),
				"A LOT",
				new Price(100, Currency.CIS),
				"Katoa or smth",
				false,
				true,
				"[Notes]",
				282551955975307264L
		);
		MaterialOffer offer3 = new MaterialOffer(
				Material.getMaterial("FIM"),
				"A LOT",
				new Price(100, Currency.CIS),
				"Katoa or smth",
				true,
				true,
				"[Notes]",
				282551955975307264L
		);
		MaterialOffer offer4 = new MaterialOffer(
				Material.getMaterial("H2O"),
				"A LOT",
				new Price(100, Currency.CIS),
				"Katoa or smth",
				false,
				true,
				"[Notes]",
				282551955975307264L
		);
		jda.getGuildById(nmxb.guild).getTextChannelById(1058214669925503008L).sendMessageEmbeds(
				new EmbedBuilder()
						.addField(offer1.getAsEmbedField(jda))
						.addField(offer2.getAsEmbedField(jda))
						.addField(offer3.getAsEmbedField(jda))
						.addField(offer4.getAsEmbedField(jda))
						.build())
				.queue();*/
		
//		DBIPModule dbipModule = new DBIPModule();
//		jda.addEventListener(dbipModule);

//		try {
//			Webhooker webhooker = new Webhooker("https://discord.com/api/webhooks/1057476406185046106/D8WFfAiHf0NHTuCOEOS_pHgsnHwZIdfCfiZzChqAXmqAJhcnjNNzZXNythUGlNt_QsQ0");
//			webhooker.addEmbed(new Webhooker.EmbedObject()
//					.setTitle("BACCUS MOVE 1921")
//					.setDescription(
//							("""
//									Total Economy: 16 | Unit Upkeep: 0 | Bank Balance: 9
//
//									> BANK   6E
//									> BRIBE 10E
//									> How much Eco is in _Mongolian Separatists_ Eco Bank?
//									> How much did _Mongolian Separatists_ Warlord spend as bribe last turn?
//									> How much Eco is in _Manchurian PRC_ Eco Bank?
//
//									New Bank Balance: 15"""
//							).replace("\n", "\\n")
//					)
//					.setColor(Color.RED)
//					.setThumbnail("https://cdn.discordapp.com/attachments/1051536766580043939/1052224261676347473/Warlord_Baccus_Flag.png")
//			);
//			webhooker.setAvatarUrl("https://cdn.discordapp.com/attachments/1051536766580043939/1052224261676347473/Warlord_Baccus_Flag.png");
//			webhooker.setTts(false);
//			webhooker.setUsername("Baccus Turn Hook");
//			webhooker.execute();
//		} catch(IOException e) {
//			throw new RuntimeException(e);
//		}

//		ThreadChannel channel = jda.getForumChannelById(1047724679252693033L).getThreadChannels().get(0);
//		System.out.println(channel.getType());
//		System.out.println("Forum Post name: " + channel.getName());
//		System.out.println("Is Post pinned1: " + channel.isPinned());
//		channel.getManager().setPinned(true).complete();
//		System.out.println("Is Post pinned2: " + channel.isPinned());
//		System.out.println("Is Post archived1: " + channel.isArchived());
//		channel.getManager().setArchived(true).complete();
//		System.out.println("Is Post archived2: " + channel.isArchived());
	
	}
	
	private static Logger setupLogger() {
		return ((ch.qos.logback.classic.LoggerContext) LoggerFactory.getILoggerFactory()).getLogger(BotInstance.class);
	}
	
	public static final String[] randomStatuses = new String[] {
			"with your feelings 💕",
			"with your feelings 💕",
			"Factorio with your mom.",
			"Rimworld (It's good).",
			"hide and seek with my sanity.",
			"hot potato with my responsibilities."
	};
	
	public void setPresence() {
		jda.getPresence().setPresence(OnlineStatus.ONLINE, Activity.of(Activity.ActivityType.PLAYING, randomStatuses[(int) (Math.random() * randomStatuses.length)]));
	}
	
	public void createGuilds(boolean log) {
		if(log) {
//			logger.info("Creating Guild-Instances...");
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
			logger.info("Creating Guild-Instances...");
			for(Guild g: jda.getGuilds()) {
				guildInstances.add(new GuildInstance(this, g.getIdLong()));
			}
			logger.info("Created " + guildInstances.size() + " Guild-Instances.");
		}
	}
	
}
