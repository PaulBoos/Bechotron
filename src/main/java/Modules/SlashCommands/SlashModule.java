package Modules.SlashCommands;

import Head.GuildInstance;
import Modules.Module;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SlashModule extends ListenerAdapter implements Module {
	
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
		
		//Get Hook
		//CommandHook hook = event.getHook();
		//hook.setEphemeral(true);
		
		for(Command c: commands) {
			if(c.getCommand().equals(event.getName())) {
				c.execute(event);
			}
		}
	}
	
	public void updateSlashCommands(boolean updateDiscord) {
		//addCommands();//TODO add Commands to be activated
		
		if(instance.guild == 730778264880152698L) {
			//addCommands(Command.ping,Command.say);
		}
		if(instance.guild == 739513862449266729L) { //BTS
			addCommands(Command.memberinfo, Command.delete);
		}
		if(instance.guild == 652967667946225667L) { //J4F
			addCommands(Command.memberinfo, Command.avatar, Command.music, Command.vip);
		}
		if(instance.guild == 795777477627609153L) { //Garry
			//addCommands(Command.timeout);
		}
		if(instance.guild == 328874791669071882L) {
			addCommands(Command.memberinfo);
		}
		if(updateDiscord) {
			CommandListUpdateAction updater = instance.bot.jda.getGuildById(instance.guild).updateCommands();
			
			System.out.println(instance.guild);
			if(!commandData.isEmpty()) updater.addCommands(commandData).complete();
		}
		
		//System.out.println("Updated Commands. (" + guild + ")");
	}
	private void addCommands(Command... command) {
		for(Command c: command) {
			commands.add(c);
			commandData.add(c.init());
		}
	}
	
}
