package Modules.Fun;

import Modules.Module;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.List;

public class ShipperModule implements Module {
	
	public ShipperModule(@NotNull JDA jda) {
		jda.addEventListener(new ShipMessageReceiver());
	}
	
	private class ShipMessageReceiver extends ListenerAdapter {
		
		//💕
		
		private static final String
				LOVE_CHAR = "\uD83D\uDC95",     // 💕 Hearts
				HATE_CHAR = "\uD83D\uDC94",     // 💔 Broken Heart
				POSITIVE_CHAR = "\uD83D\uDC96", // 💖 Sparkling Heart
				NEGATIVE_CHAR = "\uD83D\uDC94", // 💔 Broken Heart
				NEUTRAL_CHAR = "⬛";            // ⬛ Box.
		
		
		@Override // GUILD RECEIVER
		public void onMessageReceived(@NotNull MessageReceivedEvent event) {
			if(event.getAuthor().isBot())
				return;
			if(!event.isFromGuild())
				return;
			if(event.getMessage().getContentRaw().startsWith("ship ")) {
				List<User> mentions = event.getMessage().getMentionedUsers();
				if(mentions.size() == 2) {
					float compatibility = calculateCompatibility(mentions.get(0).getIdLong(), mentions.get(1).getIdLong());
					event.getChannel().sendMessageEmbeds(new EmbedBuilder()
							.setDescription(mentions.get(0).getAsMention() + " " + (compatibility > 0 ? LOVE_CHAR : HATE_CHAR) +  " " + mentions.get(1).getAsMention() + " = " + displayCompatibility(compatibility) +
									"\n\n[" + colorCodedBar(POSITIVE_CHAR, NEGATIVE_CHAR, NEUTRAL_CHAR, compatibility, 10) + "]" + (compatibility > 90 ? " :heart_on_fire:" : ""))
							.setColor(Color.RED)
							.build()
					).queue();
				} else {
					String[] arguments = event.getMessage().getContentStripped().split(" ");
					if(arguments.length == 3) {
						long firstUser = 0;
						long secondUser = 0;
						try {
							firstUser = Long.parseLong(arguments[1]);
							secondUser = Long.parseLong(arguments[2]);
							float compatibility = calculateCompatibility(firstUser, secondUser);
							event.getChannel().sendMessageEmbeds(new EmbedBuilder()
									.setDescription(event.getJDA().getUserById(secondUser).getAsMention() + " " + (compatibility > 0 ? LOVE_CHAR : HATE_CHAR) +  " " + event.getJDA().getUserById(firstUser).getAsMention() + " = " + displayCompatibility(compatibility) +
											"\n\n[" + colorCodedBar(POSITIVE_CHAR, NEGATIVE_CHAR, NEUTRAL_CHAR, compatibility, 10) + "]" + (compatibility > 90 ? " :heart_on_fire:" : ""))
									.setColor(Color.RED)
									.build()
							).queue();
						} catch(NumberFormatException exception) {
							event.getChannel().sendMessage("You did not provide two user ids.").queue();
						} catch(NullPointerException exception) {
							if(firstUser != 0 && secondUser != 0) {
								float compatibility = calculateCompatibility(firstUser, secondUser);
								event.getChannel().sendMessage("I probably don't know these people.\nI'll calculate anyways: " + (compatibility > 0 ? LOVE_CHAR : HATE_CHAR) + " = " + displayCompatibility(compatibility) +
										"\n\n[" + colorCodedBar(POSITIVE_CHAR, NEGATIVE_CHAR, NEUTRAL_CHAR, compatibility, 10) + "]" + (compatibility > 90 ? " :heart_on_fire:" : "")).queue();
							} else
								event.getChannel().sendMessage("The given ids were probably not recognized.").queue();
						}
					} else {
						event.getChannel().sendMessage("Either mention exactly two (2) users or provide two (2) user ids.").queue();
					}
				}
			}
			else if(event.getMessage().getContentRaw().equalsIgnoreCase("who loves me?")) {
				List<Member> allMembers = event.getGuild().getMembers();
				Member top = null;
				float value = -1;
				for(Member m: allMembers) {
					if(m.getUser().isBot())
						continue;
					float v = calculateCompatibility(event.getAuthor().getIdLong(), m.getIdLong());
					if(v > value) {
						value = v;
						top = m;
					}
				}
				event.getChannel().sendMessage(String.format("Your best lover **in this Guild**: %s with a %s-factor of %.2f%%", top.getAsMention(), value > 0 ? LOVE_CHAR : HATE_CHAR, value)).queue();
			}
			else if(event.getMessage().getContentRaw().equalsIgnoreCase("who hates me?")) {
				List<Member> allMembers = event.getGuild().getMembers();
				Member top = null;
				float value = 1;
				for(Member m: allMembers) {
					if(m.getUser().isBot())
						continue;
					float v = calculateCompatibility(event.getAuthor().getIdLong(), m.getIdLong());
					if(v < value) {
						value = v;
						top = m;
					}
				}
				event.getChannel().sendMessage(String.format("Your greatest hater **in this Guild**: %s with a %s-factor of %.2f%%", top.getAsMention(), value > 0 ? LOVE_CHAR : HATE_CHAR, value)).queue();
			}
			else if(event.getMessage().getContentRaw().equalsIgnoreCase("am I loved?")) {
				List<Member> allMembers = event.getGuild().getMembers();
				float value = 0;
				for(Member m: allMembers) {
					if(m.getUser().isBot() || m.getUser().getIdLong() == event.getAuthor().getIdLong())
						continue;
					value += calculateCompatibility(event.getAuthor().getIdLong(), m.getIdLong());
				}
				event.getChannel().sendMessage(String.format("Your average \uD83D\uDC95-factor is %.2f%%. %s", value / allMembers.size(), value < 0 ? " People don't generally like you. \uD83C\uDDEB" : " You are truly appreciated. \uD83D\uDC96")).queue();
			}
			else if(event.getMessage().getContentRaw().equalsIgnoreCase("The Love-INATOR")) {
				List<Member> allMembers = event.getGuild().getMembers();
				
				//HIGHEST LOVE-SCORE
				Member highScoreA = null;
				Member highScoreB = null;
				float highScoreValue = -1;
				//LOWEST LOVE-SCORE
				Member lowScoreA = null;
				Member lowScoreB = null;
				float lowScoreValue = 1;
				//CALCULATE MIN/MAX
				for(Member z: allMembers) {
					for(Member y: allMembers) {
						if(z.getUser().isBot() || y.getUser().isBot()) continue;
						else {
							float v = calculateCompatibility(z.getIdLong(), y.getIdLong());
							if(v > highScoreValue) {
								highScoreValue = v;
								highScoreA = z;
								highScoreB = y;
							} else if(v < lowScoreValue) {
								lowScoreValue = v;
								lowScoreA = z;
								lowScoreB = y;
							}
						}
					}
				}
				
				//HIGHEST AVG-SCORE
				Member highAvg = null;
				float highAvgValue = -100;
				//LOWEST AVG-SCORE
				Member lowAvg = null;
				float lowAvgValue = 100;
				//CALCULATE AVG
				for(Member z: allMembers) {
					if(z.getUser().isBot())
						continue;
					float v = 0;
					for(Member y: allMembers) {
						if(y.getUser().isBot())
							continue;
						else {
							if(z.getUser().getIdLong() == y.getIdLong()) continue;
							v += calculateCompatibility(z.getIdLong(), y.getIdLong());
						}
					}
					if(v > highAvgValue) {
						highAvgValue = v / allMembers.size();
						highAvg = z;
					} else if(v < lowAvgValue) {
						lowAvgValue = v / allMembers.size();
						lowAvg = z;
					}
				}
				
				//SEND MESSAGE
				event.getChannel().sendMessage(
						String.format(
						"""
								**This Guild**'s greatest lovers are %s %s %s with a love-factor of %.2f%%
								**This Guild**'s antimatter-matter non-lovers are %s %s %s with a love-factor of %.2f%%
								
								**This Guild**'s most likeable member is %s with an average %s-factor of %.2f%%
								**This Guild**'s most charmless member is %s with an average %s-factor of %.2f%%
								""",
						highScoreA.getAsMention(), highScoreValue > 0 ? LOVE_CHAR : HATE_CHAR, highScoreB.getAsMention(), highScoreValue,
						lowScoreA.getAsMention(), lowScoreValue > 0 ? LOVE_CHAR : HATE_CHAR, lowScoreB.getAsMention(), lowScoreValue,
						highAvg.getAsMention(), highAvgValue > 0 ? LOVE_CHAR : HATE_CHAR, highAvgValue,
						lowAvg.getAsMention(), lowAvgValue > 0 ? LOVE_CHAR : HATE_CHAR, lowAvgValue)
				).queue();
			}
		}
		
		private static String displayCompatibility(float compatibility) {
			return String.format("%.2f%%", compatibility);
		}
		private static String colorCodedBar(String positiveChar, String negativeChar, String neutralChar, float fraction, int units) {
			fraction /=100;
			StringBuilder sb = new StringBuilder();
			for(float i = 0f; i < units; i++) {
				sb.append(i/units < ((Math.abs(fraction))/*-(1f/(2f*units))*/) ? (fraction > 0 ? positiveChar : negativeChar) : neutralChar);
				// -(1f/(2f*units)) auskommentiert. Wiederherstellen, zum korrekten runden, aktuell wird stets aufgerundet.
			}
			return sb.toString();
		}
		private static float calculateCompatibility(long userA, long userB) {
			return (calculatePersonalValue(Math.max(userA, userB)) - calculatePersonalValue(Math.min(userA, userB)));
		}
		private static float calculatePersonalValue(long user) {
			return (float) ((user >> (user % 10))%10000)/100;
		}
		
	}
	
}
