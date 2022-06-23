package Head;

import Modules.Games.SkullGame;
import Modules.Music.MusicModule;
import Modules.SlashCommands.SlashModule;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class GuildInstance {
	
	public final BotInstance bot;
	public final long guild;
	public MusicModule music;
	public SlashModule slash;
	public TempMessageReceiver messageReceiver;
	
	GuildInstance(BotInstance botInstance, long guild) {
		this.bot = botInstance;
		this.guild = guild;

		music = new MusicModule(bot.jda);
		slash = new SlashModule(this);
		slash.updateSlashCommands(true);
		botInstance.jda.addEventListener(slash);
		messageReceiver = new TempMessageReceiver(this);
		botInstance.jda.addEventListener(messageReceiver);
	}
	
	private class TempMessageReceiver extends ListenerAdapter {
		
		private GuildInstance guildInstance;
		
		TempMessageReceiver(GuildInstance guildInstance) {
			this.guildInstance = guildInstance;
		}
		/*TODO
		@Override
		public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
			// Only This Guild's text commands
			if(!(event.getGuild().getIdLong() == guildInstance.guild)) return;
			// Only execute human commands
			if(event.getAuthor().isBot()) return;
			System.out.println("Message Received! " + event.getMessage().getContentRaw());
			
			if(event.getChannel().getIdLong() == 910234972193951744L) {
				if(event.getMessage().getContentRaw().equals("playskull"))
					new SkullGame(bot, event);
			}
			
		}
		*/
	}
	
}
