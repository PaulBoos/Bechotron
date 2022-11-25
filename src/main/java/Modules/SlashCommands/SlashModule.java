package Modules.SlashCommands;

import Head.GuildInstance;
import Modules.Module;
import Modules.RequireModuleHook;
import Modules.UrbanDictionary.UrbanDictionaryModule;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.ErrorResponseException;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SlashModule extends ListenerAdapter implements Module {
	
	private static final RequireModuleHook HOOK = new RequireModuleHook();
	
	public final GuildInstance instance;
	public final CommandDB db;
	public final List<Command> commands;
	public final Collection<CommandData> commandData;

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
		System.out.println("Slash Command Received! " + event.getName());
		
		for(Command c: commands) {
			if(c.getCommand() != null && c.getCommand().equals(event.getName())) {
				c.execute(event);
			}
		}
	}
	
	@Override
	public void onMessageReceived(@NotNull MessageReceivedEvent event) {
		//Only This Guild's slash commands
		if(event.getGuild().getIdLong() != instance.guild) return;
		
		for(Command c: commands) {
			if(c.getTextCommand() != null && event.getMessage().getContentStripped().startsWith(c.getTextCommand())) {
				c.execute(event);
				System.out.println("Text Command Received! " + c.getTextCommand());
			}
		}
	}
	
	public void updateSlashCommands(boolean updateDiscord) {
		addCommands(UrbanDictionaryModule.URBAN);
		if(instance.guild == 1001947716554850417L) { // FINN
			addCommands(Command.GLOBALBANREQUEST);
		}
		if(instance.guild == 607323699065913345L) { //KdS
			addCommands(Command.MEMBERINFO, Command.AVATAR, Command.VIP);
		}
		if(instance.guild == 739513862449266729L) { //BTS
			addCommands(Command.MEMBERINFO, Command.DELETE, Command.GLOBALBANREQUEST);
		}
		if(instance.guild == 652967667946225667L) { //J4F
			addCommands(Command.PING, Command.MUSIC, Command.DELETE, Command.TIMEOUT, Command.MEMBERINFO, Command.AVATAR, Command.VIP);
		}
		if(instance.guild == 795777477627609153L) { //Garry
			//addCommands(Command.timeout);
		}
		if(instance.guild == 328874791669071882L) {
			addCommands(Command.MEMBERINFO);
		}
		if(updateDiscord) {
			CommandListUpdateAction updater = instance.bot.jda.getGuildById(instance.guild).updateCommands();
			
			System.out.println(instance.guild);
			try {
				if(!commandData.isEmpty()) updater.addCommands(commandData).complete();
			} catch(ErrorResponseException ignored) {}
		}
		
		//System.out.println("Updated Commands. (" + guild + ")");
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
