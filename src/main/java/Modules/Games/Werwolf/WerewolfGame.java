package Modules.Games.Werwolf;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class WerewolfGame {
	
	protected final JDA jda;
	protected List<WerewolfPlayer> players;
	protected GameState gameState = GameState.SETUP;
	protected long werewolfCategory;
	protected long werewolfChannel;
	protected long gameChannel;
	protected long voiceChannel;
	protected WerewolfEventListener listener;
	
	public WerewolfGame(JDA jda, long guild) {
		this.players = new ArrayList<>();
		this.jda = jda;
		jda.addEventListener(listener = new WerewolfEventListener());
		Guild guildObject = jda.getGuildById(guild);
		
	}
	
	private static class WerewolfEventListener extends ListenerAdapter {
		
		@Override
		public void onMessageReceived(@NotNull MessageReceivedEvent event) {
			super.onMessageReceived(event);
		}
	}
	
}