package Modules.Games;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.Queue;

public class Skull {
	
	JDA jda;
	SkullEventListener eventListener;
	
	private static final String
			joinEmoteDefault = "\u2705",
			skullEmote = "\uD83D\uDC80";
	
	Member host;
	Message initMessage;
	Member[] players;
	
	Queue<Member> addQueue;
	Queue<Member> removeQueue;
	
	public Skull(GuildMessageReceivedEvent event) {
		jda = event.getJDA();
		host = event.getMember();
		initMessage = event.getChannel().sendMessage(host.getEffectiveName() + " wants to play Skull! " + skullEmote + "\nReact with " + joinEmoteDefault + " to join. When 4 Players are ready, I will start the game!").complete();
		initMessage.addReaction(joinEmoteDefault).complete();
		registerEventListener();
	}
	
	private void registerEventListener() {
		jda.addEventListener();
	}
	
	private void deregisterEventListener() {
		jda.removeEventListener();
	}
	
	private class SkullEventListener extends ListenerAdapter {
		
		@Override
		public void onMessageReactionAdd(@NotNull MessageReactionAddEvent event) {
			addQueue.offer(event.getMember());
		}
		
		@Override
		public void onMessageReactionRemove(@NotNull MessageReactionRemoveEvent event) {
			addQueue.remove(event.getMember());
			
		}
		
	}
	
}
