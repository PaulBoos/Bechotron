package Modules.Games;

import Head.BotInstance;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.PrivateChannel;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.TimeUnit;

public class SkullGame {
	
	// Class facts
	private static final short
			PLAYER_NUMBER = 4,
			BLOSSOM_NUMBER = 4,
			SKULL_NUMBER = 1,
			MINIMUM_TO_BID = 4,
			MAXIMUM_TO_BID = 12,
			POINTS_TO_WIN = 2;
	
	private static final String
			blossomEmote     = "\uD83C\uDF38",   // cherry blossom
			skullEmote       = "\uD83D\uDC80",   //
			
			joinEmoteDefault = "\u2705",         // ✔
			deleteGameEmote  = "\u274c",         // ❌
	
	
			genericDot       = "\uD83D\uDCBF",   // 💿
			blackDot         = "\u26AB",         // ⚫
			
			redDot           = "\uD83D\uDD34",   //
			yellowDot        = "\uD83D\uDFE1",   //
			blueDot          = "\uD83D\uDD35",   //
			whiteDot         = "\u26AA",         // ⚪
	
	
			emote_one    = "\u0030\uFE0F\u20E3", // 1️⃣
			emote_two    = "\u0031\uFE0F\u20E3", // 2️⃣
			emote_three  = "\u0032\uFE0F\u20E3", // 3️⃣
			emote_four   = "\u0033\uFE0F\u20E3", // 4️⃣
			emote_five   = "\u0034\uFE0F\u20E3", // 5️⃣
			emote_six    = "\u0035\uFE0F\u20E3", // 6️⃣
			emote_seven  = "\u0036\uFE0F\u20E3", // 7️⃣
			emote_eight  = "\u0037\uFE0F\u20E3", // 8️⃣
			emote_nine   = "\u0038\uFE0F\u20E3", // 9️⃣
			emote_ten    = "\uD839\uDD1F",       // 🔟
			emote_eleven = "893456635706605568", // (custom)
			emote_twelve = "893456652618072065", // (custom)
			emote_plus   = "\u2795",             // ➕
			emote_hash   = "\u0023\uFE0F\u20E3", // #️⃣
			emote_skip   = "\u23E9";             // ⏭
	
	private static final String[] number_emotes = new String[] {
			emote_one, emote_two, emote_three, emote_four, emote_five, emote_six, emote_seven, emote_eight, emote_nine, emote_ten, emote_eleven, emote_twelve};
	
	private static final Color[] colors = new Color[] {Color.RED, Color.YELLOW, Color.BLUE, Color.WHITE};
	
	
	// Object Facts
	BotInstance bot;
	JDA jda;
	
	// Overall vars
	STATE state;
	Member[] players;
	
	// Initiation vars
	Member host;
	Message initMessage;
	SkullEventListener eventListener;
	Queue<Member> toJoinQueue;
	
	// Initiate Game
	public SkullGame(BotInstance bot, MessageReceivedEvent event) {
		state = STATE.OPENING;
		this.bot = bot;
		jda = event.getJDA();
		host = event.getMember();
		initMessage = event.getChannel().sendMessage(host.getEffectiveName() + " wants to play Skull! " + skullEmote + "\nReact with " + joinEmoteDefault + " to join. When " + PLAYER_NUMBER + " Players are ready, I will start the game!").complete();
		initMessage.addReaction(Emoji.fromUnicode(joinEmoteDefault)).complete();
		initMessage.addReaction(Emoji.fromUnicode(deleteGameEmote)).complete();
		eventListener = new SkullEventListener();
		toJoinQueue = new ArrayDeque<>();
		registerEventListener();
		state = STATE.WAITING_FOR_PLAYERS;
	}
	
	// Game Startup update (called by onMessageReactionAdd / onMessageReactionRemove)
	public synchronized void checkPlayers() {
		if(state != STATE.WAITING_FOR_PLAYERS
				|| toJoinQueue.size() < PLAYER_NUMBER) return;
		players = new Member[PLAYER_NUMBER];
		for(short i = PLAYER_NUMBER - 1; i > -1; i--) {
			players[i] = toJoinQueue.poll();
		}
		for(short i = 0; i < PLAYER_NUMBER; i++) {
			if(players[i] == null) {// make sure that all four Players are set
				throw new NullPointerException("For some Reason, one Player is not set.");
			}
			for(short j = 0; j < PLAYER_NUMBER; j++) { // and none of these Players
				if(players[i] == players[j] && i != j)
					throw new NullPointerException("For some Reason, one Player is placed twice.");
			}
		}
		state = null;
		startGame();
	}
	
	private void startGame() {
		state = STATE.IN_PROGRESS;
		
		// Edit original message to show all players
		StringBuilder ps = new StringBuilder("");
		for(Member m: players) ps.append(m.getEffectiveName()).append(", ");
		initMessage.editMessage("Players: " + ps.substring(0,ps.length()-3)).complete();
		
		playerChannels = new PrivateChannel[PLAYER_NUMBER];
		cardsMax = new short[PLAYER_NUMBER][2];
		cardsPlayed = new short[PLAYER_NUMBER][2];
		victoryPoints = new short[PLAYER_NUMBER];
		for(short i = 0; i < PLAYER_NUMBER; i++) {
			PrivateChannel p = bot.jda.openPrivateChannelById(players[i].getId()).complete();
			StringBuilder otherPlayers = new StringBuilder();
			for(short j = 0; j < PLAYER_NUMBER; j++) {
				if(j != i) otherPlayers.append(players[j]).append(", ");
			}
			p.sendMessageEmbeds(new EmbedBuilder()
					.setTitle(skullEmote + " Hello " + players[i].getEffectiveName() + "!" + skullEmote)
					.setDescription("You signed up for a game of Skull! Your fellow players are: " + otherPlayers.substring(0,otherPlayers.length()-3))
					.setColor(colors[i])
					.build()).queue();
			playerChannels[i] = p;
			cardsMax[i] = new short[] {BLOSSOM_NUMBER, SKULL_NUMBER};
			cardsPlayed[i] = new short[BLOSSOM_NUMBER + SKULL_NUMBER];
		}
		playerAtPlay = 0;
		currentBid = 0;
		callToAction();
	}
	
	// Game loop variables
	private short playerAtPlay;
	private short currentBid;
	private short currentBidder;
	private PrivateChannel[] playerChannels;
	private Message interactWith; // The message object the current player is to interact with.
	private short[][] cardsMax;      // Save amount of Cards, which are not yet discarded in runtime, for every Player.   First Order: Player (0-3), Second Order: Blossom (0) or Skull (1)
	
	// First Order: Player (0-3), Second Order: 0 Nothing | 1 Blossom | 2 Skull | 3 Visible Blossom | 4 Visible Skull
	private short[][] cardsPlayed;   // Save amount of Cards, which the players played this round.
	
	private short[]   victoryPoints; // Save the successful bids of every player towards victory.
	private boolean[] hasSkipped = new boolean[PLAYER_NUMBER];
	
	private ActionEnum callToAction() {
		try {
			interactWith = sendMessage(playerAtPlay, currentBid > 0, colors[0]);
			return null;
		} catch(OnlyActionException ex) {
			return ex.actionEnum;
		}
	}
	
	private void cardPlayLoop(Action actionToPerform) {
		if(state != STATE.IN_PROGRESS) return;
		if(actionToPerform instanceof ActionEnum) {
			// Is this a special action?
			if(actionToPerform == ActionEnum.PLUS) {
				currentBid++;
				currentBidder = playerAtPlay;
			} else if(actionToPerform == ActionEnum.SKIP) {
				hasSkipped[playerAtPlay] = true;
				playerChannels[playerAtPlay].sendMessage(emote_skip + "Skipping... ").queue();
			} else if(actionToPerform == ActionEnum.BLOSSOM) {
				for(int i = 0; i < BLOSSOM_NUMBER + SKULL_NUMBER; i++) {
					if(cardsPlayed[playerAtPlay][i] == 0) {
						cardsPlayed[playerAtPlay][i] = 1;
						break;
					}
				}
			} else if(actionToPerform == ActionEnum.SKULL) {
				for(int i = 0; i < BLOSSOM_NUMBER + SKULL_NUMBER; i++) {
					if(cardsPlayed[playerAtPlay][i] == 0) {
						cardsPlayed[playerAtPlay][i] = 2;
						break;
					}
				}
			}
		} else {
			// Or is this bidding?
			// TODO bidding
		}
		
		
		for(short p = 0; p < PLAYER_NUMBER; p++) {
			if(victoryPoints[p] >= POINTS_TO_WIN) win(p);
		}
		shareUpdate();  // Update all Players of the new board
		playerAtPlay++; // Next Player!
		actionToPerform = callToAction(); // Initiate next turn, if the next player has only one Option - execute that action recursively.
		if(actionToPerform != null) {
			cardPlayLoop(actionToPerform);
		}
	}
	private void shareUpdate() {
		StringBuilder sb = new StringBuilder();
		for(int p = 0; p < PLAYER_NUMBER; p++) {
			for(int i = 0; i < BLOSSOM_NUMBER + SKULL_NUMBER; i++) {
				switch(cardsPlayed[p][i]) {
					case 0 -> sb.append(blackDot);
					case 1, 2 -> {
						switch(p) {
							case 0 -> sb.append(redDot);
							case 1 -> sb.append(yellowDot);
							case 2 -> sb.append(blueDot);
							case 3 -> sb.append(whiteDot);
							default -> sb.append(genericDot);
						}
					}
					case 3 -> sb.append(blossomEmote);
					case 4 -> sb.append(skullEmote);
				}
			}
		}
		initMessage.getChannel().sendMessage(sb).queue(); // Update publicly
		for(PrivateChannel ch: playerChannels)            // Update all players privately
			ch.sendMessage(sb).queue();
	}
	
	private Message sendMessage(short player, boolean withCards, Color color) throws OnlyActionException {
		String message = buildMessage(player, withCards);
		Message sentMessage = playerChannels[player].sendMessageEmbeds(
				new EmbedBuilder()
						.setTitle("Your turn.")
						.setDescription(message)
						.setColor(color).build()).complete();
		boolean[] options = new boolean[4]; // PLUS SKIP BLOSSOM SKULL
		if(message.contains(emote_hash)) {
			for(int i = MINIMUM_TO_BID; i < Math.min(cardsPlayed(), MAXIMUM_TO_BID); i++) { // Start with the minimum.
				sentMessage.addReaction(Emoji.fromUnicode(number_emotes[i - 1])).queue();                      // Add all bidding options from MINIMUM_TO_BID up to MAXIMUM_TO_BID
			}
			if(message.contains(emote_plus)) {
				sentMessage.addReaction(Emoji.fromUnicode(emote_plus)).queue();
			}
			if(message.contains(emote_skip)) {
				sentMessage.addReaction(Emoji.fromUnicode(emote_skip)).queue();
			}
			if(message.contains(blossomEmote)) {
				sentMessage.addReaction(Emoji.fromUnicode(blossomEmote)).queue();
			}
			if(message.contains(skullEmote)) {
				sentMessage.addReaction(Emoji.fromUnicode(skullEmote)).queue();
			}
		} else {
			if(message.contains(emote_plus)) {
				sentMessage.addReaction(Emoji.fromUnicode(emote_plus)).queue();
				options[0] = true;
			}
			if(message.contains(emote_skip)) {
				sentMessage.addReaction(Emoji.fromUnicode(emote_skip)).queue();
				options[1] = true;
			}
			if(message.contains(blossomEmote)) {
				sentMessage.addReaction(Emoji.fromUnicode(blossomEmote)).queue();
				options[2] = true;
			}
			if(message.contains(skullEmote)) {
				sentMessage.addReaction(Emoji.fromUnicode(skullEmote)).queue();
				options[3] = true;
			}
			
			short count = 0;
			for(boolean b: options) if(b) count++;
			if(count < 2)
				for(short i = 0; i < 4; i++)
					if(options[i]) throw new OnlyActionException(
							i == 0 ? (ActionEnum) ActionEnum.PLUS :
							i == 1 ? (ActionEnum) ActionEnum.SKIP :
							i == 2 ? (ActionEnum) ActionEnum.BLOSSOM :
									(ActionEnum) ActionEnum.SKULL);
		}
		
		return sentMessage;
	}
	private String buildMessage(short player, boolean withCards) {
		if(withCards) {
			if(canBid()) {
				if(blossomsLeft(player) > 0) {
					if(skullsLeft(player) > 0) {
						return "You can either play a Card " + genericDot + ", or Bid.\n" +
								"To play a Blossom, react with " + blossomEmote + " or with " + skullEmote + " to play a Skull. You can Bid by typing any number or selecting it > " + emote_hash;
					} else {
						return "You can either play a Blossom " + blossomEmote + ", or Bid." + emote_hash + ".";
					}
				} else {
					if(skullsLeft(player) > 0) {
						return "You can either play a Skull " + skullEmote + ", or Bid. " + emote_hash + ".";
					} else {
						if(hasSkipped[player]) {
							return "You skipped " + emote_skip + ".";
						} else {
							return "You can only Bid " + emote_hash + ", or Skip " + emote_skip + ".";
						}
					}
				}
			} else {
				if(blossomsLeft(player) > 0) {
					if(skullsLeft(player) > 0)
						return "You can only play a Card " + genericDot + ".\n" +
								"To play a Blossom, react with " + blossomEmote + " or with " + skullEmote + " to play a Skull. You can Bid by typing any number or selecting it > " + emote_hash;
					else
						return "You can only play a Blossom " + blossomEmote + ".";
				} else {
					if(skullsLeft(player) > 0)
						return "You can only play a Skull " + skullEmote + ".";
					else
						return "You can only skip " + emote_skip + ".";
				}
			}
		} else {
			if(hasSkipped[player]) {
				return "You skipped " + emote_skip + ".";
			} else if(MAXIMUM_TO_BID - currentBid > 1)
				return "You have to Bid or Skip.\n" +
						"To Bid, hit " + emote_hash + " or " + emote_plus + " to up the bid by one. To Skip, hit " + emote_skip + ".";
			else if(canBid())
				return "You have to Bid or Skip.\n" +
						"To up the bid by one, hit " + emote_plus + ". Hit " + emote_skip + " to skip.";
			else
				return "You can not bid. This should not be possible, Becher fucked up. Tell him please. " + emote_skip;
		}
	}
	
	private void selectionLoop(short playerChosen) {
		if(state != STATE.SELECTION) return;
		int returnedCard = 0;
		for(int i = BLOSSOM_NUMBER + SKULL_NUMBER -1; i > 0; i--) {
			if(cardsPlayed[playerChosen][i] == 1 || cardsPlayed[playerChosen][i] == 2) { // Where is hidden Blossom / Skull?
				returnedCard = cardsPlayed[playerChosen][i];                             // Save Blossom or Skull
				cardsPlayed[playerChosen][i] += 2;                                       // unhide Blossom or Skull
				break;
			}
		}
		if(returnedCard == 2) {
			shareUpdate();
			initMessage.getChannel().sendMessage("Player " + playerAtPlay + " got a skull and loses one of his cards.").queue();
			if(Math.random() * (BLOSSOM_NUMBER+SKULL_NUMBER) > BLOSSOM_NUMBER)
				cardsMax[playerAtPlay][1]--;
			else
				cardsMax[playerAtPlay][0]--;
		} else {
		
		}
	}
	
	private static class OnlyActionException extends Exception {
		ActionEnum actionEnum;
		OnlyActionException(ActionEnum action) {
			this.actionEnum = action;
		}
	}
	private static class Action {
		short bid;
		Action() {}
		Action(short bid) {
			this.bid = bid;
		}
	}
	private static class ActionEnum extends Action {
		private final static Action
				PLUS = new Action(),
				SKIP = new Action(),
				BLOSSOM = new Action(),
				SKULL = new Action();
	}
	
	private short blossomsLeft(short player) {
		short blsms = 0;
		for(int i = 0; i < BLOSSOM_NUMBER + SKULL_NUMBER; i++)
			if(cardsPlayed[player][i] == 1) blsms++;
		return (short) (cardsMax[player][0] - blsms);
	}
	private short skullsLeft(short player) {
		short skls = 0;
		for(int i = 0; i < BLOSSOM_NUMBER + SKULL_NUMBER; i++)
			if(cardsPlayed[player][i] == 1) skls++;
		return (short) (cardsMax[player][1] - skls);
	}
	
	private boolean canBid() {
		return cardsPlayed() >= MINIMUM_TO_BID;
	}
	private short cardsPlayed() {
		short i = 0;
		for(short[] arr: cardsPlayed) {
			for(short var: arr) i += (var != 0 ? 1 : 0);
		}
		return i;
	}
	
	private void killGame() {
		state = STATE.DYING;
		deregisterEventListener();
		initMessage.getChannel().sendMessage("Closing game.").complete().delete().queueAfter(10, TimeUnit.SECONDS);
		initMessage.delete().queue();
		toJoinQueue.clear();
		//TODO kill the game
	}
	private void win(short player) {
		state = STATE.ENDING;
		initMessage.getChannel().sendMessage("The game is decided! The winner is: " + players[player].getEffectiveName()).queue();
		// TODO win
	}
	
	// Register EventListener only while game is initiating
	private void registerEventListener() {
		jda.addEventListener(eventListener);
	}
	private void deregisterEventListener() {
		jda.removeEventListener(eventListener);
	}
	
	
	
	
	private class SkullEventListener extends ListenerAdapter {
		
		@Override
		public void onMessageReactionAdd(@NotNull MessageReactionAddEvent event) {
			if(event.getUser().isBot()) return;                 // Do not process Bots
			if(event.isFromGuild()) {
				
				// GUILD RECEIVER
				switch(event.getEmoji().toString()) {
					case joinEmoteDefault -> {
						if(state == STATE.WAITING_FOR_PLAYERS)              // Is game in startup phase?
							if(!toJoinQueue.contains(event.getMember())) {  // Add member, if not already added
								toJoinQueue.offer(event.getMember());
								checkPlayers();
							}
							else
								event.getReaction().removeReaction(event.getUser()).queue();   // Game is not in startup phase, reject (=remove) the reaction.
					}
					case deleteGameEmote -> {
						if(event.getMember() == host) {
							killGame();
						} else event.getReaction().removeReaction(event.getUser()).queue();   // Member ("Author") is not the host, reject (=remove) the reaction.
					}
				}
				
			} else {
				
				// PRIVATE RECEIVER
				if(!event.getMessageId().equals(interactWith.getId())) return; // Do not process unexpected Reactions.
				if(event.getReaction().getCount() == 1) {
					event.getReaction().removeReaction().queue();
					event.getChannel().sendMessage("Please only react with your current options.").complete().delete().completeAfter(10, TimeUnit.SECONDS);
				} else if(event.getReaction().getCount() == 2) {
					switch(event.getReaction().getEmoji().toString()) {
						case blossomEmote -> cardPlayLoop(ActionEnum.BLOSSOM);
						case skullEmote ->   cardPlayLoop(ActionEnum.SKULL);
						case emote_skip ->   cardPlayLoop(ActionEnum.SKIP);
						case emote_plus ->   cardPlayLoop(ActionEnum.PLUS);
						case redDot ->       selectionLoop((short) 0);
						case yellowDot ->    selectionLoop((short) 1);
						case blueDot ->      selectionLoop((short) 2);
						case whiteDot ->     selectionLoop((short) 3);
						default -> event.getReaction().removeReaction().queue();
					}
				} else {
					event.getChannel().sendMessage("event.getReaction().getCount() returned " + event.getReaction().getCount() + " : please forward this to Becher.").queue();
				}
				
			}
		}
		
		@Override
		public void onMessageReactionRemove(@NotNull MessageReactionRemoveEvent event) {
			if(event.getUser().isBot()) return;                 // Do not process Bots
			if(state == STATE.WAITING_FOR_PLAYERS)              // Is game in startup phase?
				toJoinQueue.remove(event.getMember());          // Remove member
			else
				event.getReaction().removeReaction().queue();   // Game is not in startup phase, reject (=remove) the reaction.
		}
		
		@Override // PRIVATE RECEIVER
		public void onMessageReceived(@NotNull MessageReceivedEvent event) {
			if(event.isFromGuild())
				return;
			if(!event.getChannel().getId().equals(interactWith.getChannel().getId())) return; // Do not process unexpected Messages.
			short bid;
			try {
				bid = Short.parseShort(event.getMessage().getContentStripped());
				if(bid > MAXIMUM_TO_BID ||bid < MINIMUM_TO_BID) throw new NumberFormatException();
			} catch(NumberFormatException e) {
				event.getChannel().sendMessage("Please send a number between " + MINIMUM_TO_BID + " and " + MAXIMUM_TO_BID).complete().delete().queueAfter(10, TimeUnit.SECONDS);
				event.getMessage().delete().queue();
				return;
			}
			cardPlayLoop(new Action(bid));
		}
		
	}
	
	private enum STATE {
		OPENING, WAITING_FOR_PLAYERS, IN_PROGRESS, SELECTION, ENDING, DYING
	}
	
}