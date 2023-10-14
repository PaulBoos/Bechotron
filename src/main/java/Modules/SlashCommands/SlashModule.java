package Modules.SlashCommands;

import Head.GuildInstance;
import Modules.Fun.ShipperModule;
import Modules.Module;
import Modules.Nday.NdayGame;
import Modules.PrunClanTrade.PrunClanTradeModule;
import Modules.RequireModuleHook;
import Modules.Trivia.TriviaModule;
import Modules.TrumpOrBidenTTV;
import Modules.UrbanDictionary.UrbanDictionaryModule;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.ErrorResponseException;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SlashModule extends ListenerAdapter implements Module {
	
	private static final RequireModuleHook HOOK = new RequireModuleHook();
	private static final Logger LOGGER = LoggerFactory.getLogger(SlashModule.class);
	
	public final GuildInstance instance;
	public final CommandDB db;
	public final List<Command> commands;
	public final List<CommandData> commandData;

	public SlashModule(GuildInstance guildInstance) {
		instance = guildInstance;
		this.db = new CommandDB(this);

		this.commands = new ArrayList<>();
		this.commandData = new ArrayList<>();
	}
	
	@Override
	public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
		//Only This Guild's slash commands
		if(event.getGuild().getIdLong() != instance.guild) return;
		LOGGER.info("Slash Command Received! " + event.getName());
		
		for(Command c: commands) {
			if(c.getCommand() != null && c.getCommand().equals(event.getName())) {
				c.execute(event);
				return;
			}
		}
		LOGGER.error("Slash Command Not Found! " + event.getName());
	}
	
	@Override
	public void onMessageReceived(@NotNull MessageReceivedEvent event) {
		//Only This Guild's slash commands
		if(!event.isFromGuild()) return;
		if(event.getGuild().getIdLong() != instance.guild) return;
		
		for(Command c: commands) {
			if(c.getTextCommand() != null && event.getMessage().getContentStripped().startsWith(c.getTextCommand())) {
				c.execute(event);
				LOGGER.info("Text Command Received! " + c.getTextCommand());
			}
		}
	}
	
	public void updateSlashCommands(boolean updateDiscord) {
		//addCommands(UrbanDictionaryModule.URBAN);
		if(instance.guild == 1001947716554850417L) { // FINN
			addCommands(Command.GLOBALBANREQUEST);
		}
		if(instance.guild == 607323699065913345L) { // KdS
			addCommands(Command.MEMBERINFO, Command.AVATAR, Command.VIP, Command.PLEX, Command.MUSIC, Command.DELETE, Command.TIMEOUT, Command.PING, TriviaModule.TRIVIA, TrumpOrBidenTTV.LISTEN);
		}
		if(instance.guild == 739513862449266729L) { // BTS
			addCommands(Command.MEMBERINFO, Command.DELETE);
		}
		if(instance.guild == 1058214669027909684L) { // NMxB
			addCommands(Command.MEMBERINFO, PrunClanTradeModule.SEARCH);
		}
		if(instance.guild == 652967667946225667L) { // J4F
			addCommands(Command.PING, Command.MUSIC, Command.DELETE, Command.TIMEOUT, Command.MEMBERINFO, Command.AVATAR, Command.VIP);
		}
		if(instance.guild == 795777477627609153L) { // Garry
			addCommands(Command.MUSIC);
		}
		if(instance.guild == 328874791669071882L) {
			addCommands(Command.MEMBERINFO);
		}
		if(instance.guild == 1037783202430976131L) { // Free Candy Van
			addCommands(Command.MEMBERINFO, Command.AVATAR, Command.VIP, TrumpOrBidenTTV.LISTEN);
		}
		if(instance.guild == 1092947249434218538L) { // Patron
			addCommands(Command.MEMBERINFO, Command.AVATAR, Command.VIP, Command.MUSIC, Command.DELETE, Command.PLEX);
		}
		if(updateDiscord) {
			CommandListUpdateAction updater = instance.bot.jda.getGuildById(instance.guild).updateCommands();
			
			try {
				if(!commandData.isEmpty()) updater.addCommands(commandData).complete();
			} catch(ErrorResponseException | IllegalArgumentException ignored) {
				LOGGER.warn("Failed to update slash commands. Set debug level to debug to see more.");
				LOGGER.debug(ignored.getMessage());
			}
		}
	}
	public void addCommands(Command... command) {
		for(Command c: command) {
			commands.add(c);
			commandData.add(c.init());
		}
	}
	
	@Override
	public String getDescription() {
		return null;
	}
	
	@Override
	public String getName() {
		return "Slash Command Module";
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
