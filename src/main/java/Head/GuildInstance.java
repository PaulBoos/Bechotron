package Head;

import Modules.Music.MusicModule;
import Modules.SlashCommands.Command;
import Modules.SlashCommands.SlashModule;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class GuildInstance {
	
	public final BotInstance bot;
	public final long guild;
	public MusicModule music;
	public SlashModule slash;

	
	GuildInstance(BotInstance botInstance, long guild) {
		this.bot = botInstance;
		this.guild = guild;

		music = new MusicModule();
		slash = new SlashModule(this);
		slash.updateSlashCommands(true);
		botInstance.jda.addEventListener(slash);
	}

	/*
	@Override
	public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
		//Only This Guild's text commands
		if(!(event.getGuild().getIdLong() == guildInstance.guild)) return;
		if(event.getAuthor().isBot()) return;
		System.out.println("Message Received! " + event.getMessage().getContentRaw());
		String com = event.getMessage().getContentRaw().split(" ")[0];
		for(Command c: guildInstance.slash.commands) {
			if(c.getTextCommand().equals(com)) {
				c.execute(event);
			}
		}
	}
	 */

}
